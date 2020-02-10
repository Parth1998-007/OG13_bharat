package com.organicbharat.ui.home;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.CategoryModel;
import com.organicbharat.databinding.FragmentCategoryBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.product.Product;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CategoryFragment extends BaseFragment implements CategoryHomeAdapter.CategoryClick {
    private static final String TAG = "CategoryFragment";
    FragmentCategoryBinding binding;
    CategoryHomeAdapter categoryHomeAdapter;
    int totalPages= 1,currentPage=1;
    boolean mIsLoading=false;

    public static CategoryFragment newInstance(Bundle args) {
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category,container,false);
        init();
        return binding.getRoot();
    }

    private void getCategory()
    {
        if(!isOnline())
        {
            showToast("Internet Not Available , Please Try Again");
            return;
        }

        apiService.getCategory(appPref.getUserId(),0,currentPage)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CategoryModel.CategoryRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mIsLoading=true;
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CategoryModel.CategoryRes> categoryRes) {


                        if(isSuccess(categoryRes)){
                            setCategory(categoryRes.body());
                        }else if (categoryRes.code()==401){
                            logout();
                        }else if(categoryRes.body().getCategoryList().size()==0){

                            showSnake(binding.llMain,"Data not found");

                        }else {
                            //showToast(categoryRes.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });
    }


    private void setCategory(CategoryModel.CategoryRes categoryRes) {
        mIsLoading=false;
        AppLog.e(TAG,"CategorylistRes : "+categoryRes);
        if(categoryRes.isStatus()){
            appPref.set(AppPref.MINAMOUNT,categoryRes.getConfigData().getMin_amount());
            appPref.set(AppPref.DELIVERYCHARGE,categoryRes.getConfigData().getDelivery_charge());
            categoryHomeAdapter.clear();
            categoryHomeAdapter.addCategory(categoryRes.getCategoryList());
        }
        else {
            showSnake(binding.llMain,categoryRes.getMsg());


        }

    }

    private void init() {
        currentPage =1;
        getCategory();
        categoryHomeAdapter = new CategoryHomeAdapter();
        categoryHomeAdapter.setCategoryClick(this);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.rvCategory.setLayoutManager(layoutManager);
        binding.rvCategory.setAdapter(categoryHomeAdapter);

        binding.rvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && totalPages > currentPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                    ) {
                        currentPage = currentPage + 1;
                        AppLog.e(TAG, "onScrollCalled " + currentPage);

                        getCategory();
                    }
                }
            }
        });


    }

  /*  private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

        @NonNull
        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int position) {

        }

        @Override
        public int getItemCount() {
            return 7;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCategoryClick();
                    }
                });
            }
        }
    }*/
    public void onCategoryClick(int catid) {
        Bundle b = new Bundle();
        b.putInt("catid",catid);
        b.putInt("subsid",0);
        gotoActivity(Product.class,b,false);
       // ((Base)getActivity()).changeFrag(ProductFragment.newInstance(null),true,false);
    }
}
