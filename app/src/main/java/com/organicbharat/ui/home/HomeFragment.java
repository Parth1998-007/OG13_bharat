package com.organicbharat.ui.home;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.organicbharat.R;
import com.organicbharat.api_model.CategoryModel;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.api_model.OfferListModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentHomeBinding;
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

public class HomeFragment extends BaseFragment implements View.OnClickListener, CategoryHomeAdapter.CategoryClick, SubscriptionAdapter.subscriptionClick {
    private static final String TAG = HomeFragment.class.getSimpleName();
    FragmentHomeBinding binding;
    OfferAdapter offerAdapter;
    SubscriptionAdapter subscriptionAdapter;
    CategoryHomeAdapter categoryHomeAdapter;

    int totalPages= 1,currentPage=1;
    boolean mIsLoading=false;
    int currentPageprod=1;
    boolean mIsLoadingprod=false;
    private Runnable runnable;
    final Handler handler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        init();

        return binding.getRoot();
    }

   //getting cart item lists
    public void getMyCartItemList()
    {
        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        apiService.getMyCartItemList(appPref.getUserId())//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MyCartModel.MyCartModelRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<MyCartModel.MyCartModelRes> myCartListRes) {
                        if (isSuccess(myCartListRes)) {
                            AppLog.e(TAG,"MyCartRes: "+myCartListRes.body());
                            if(myCartListRes.body().isStatus()){
                                appPref.set(AppPref.ITEMINCART,myCartListRes.body().getItems_incart());
                                Home activity = (Home) getActivity();
                                activity.updateCount(myCartListRes.body().getItems_incart());
                            }else if(myCartListRes.body().getMsg().equalsIgnoreCase( "Your cart is empty!")){
                                appPref.set(AppPref.ITEMINCART,0);
                                Home activity = (Home) getActivity();
                                activity.updateCount(0);
                            }

                        }else {
                            if (myCartListRes.code()==401){
                                logout();
                            }else{
                                if(myCartListRes.body().getMsg().equalsIgnoreCase( "Your cart is empty!")){
                                    appPref.set(AppPref.ITEMINCART,0);
                                    Home activity = (Home) getActivity();
                                    activity.updateCount(0);
                                }
                            }


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

    @Override
    public void onStart() {
        super.onStart();

        AppLog.e(TAG,"start called......");
        getOffer();
        currentPage =1;
        getCategory();
        currentPageprod =1;
        getSubscriptions();
        getMyCartItemList();
        //temp
        //appPref.set(AppPref.USER_ID,2);

        //getSubscriptions();
    }

   //offer slider in home screen
    private void getOffer()
    {

        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");

            return;
        }

        apiService.getOffer()//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<OfferListModel.OfferListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mIsLoading=true;
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<OfferListModel.OfferListRes> offerListRes) {

                        if(isSuccess(offerListRes)){
                            setOnOfferRes(offerListRes.body());
                        }else if (offerListRes.code()==401){
                            logout();
                        } else if(offerListRes.body().getOfferlist()!= null && offerListRes.body().getOfferlist().size()==0){
                            showSnake(binding.llMain,"Data not found");
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

    private void setOnOfferRes(OfferListModel.OfferListRes offerlistRes) {
        if(offerlistRes.isStatus()){
            offerAdapter.clear();
            offerAdapter.addOffers(offerlistRes.getOfferlist());
            if(offerlistRes.getOfferlist().size()>0){
                autoScroll();
        }
    }
    }

  //auto scroll method
    private void autoScroll() {
            if (runnable == null) {
                final int speedScroll = 5000;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        AppLog.e(TAG,"run()");
                        if (binding.rvOffers == null) {
                            handler.removeCallbacks(this);
                            AppLog.e(TAG,"rvSlider() null");
                        } else {
                            AppLog.e(TAG,"rvSlider() else");
                            if (((LinearLayoutManager)binding.rvOffers.getLayoutManager()).findFirstVisibleItemPosition() < offerAdapter.getItemCount()) {
                                binding.rvOffers.smoothScrollToPosition(((LinearLayoutManager)binding.rvOffers.getLayoutManager()).findFirstCompletelyVisibleItemPosition() + 1);
                                handler.postDelayed(this, speedScroll);
                            }
                        }
                    }
                };
                handler.postDelayed(runnable, speedScroll);
            }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(runnable!=null){
            handler.removeCallbacks(runnable);
        }
    }


    //get category list
  private void getCategory()
    {
        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
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
                        }else if(categoryRes.body().getCategoryList()!= null && categoryRes.body().getCategoryList().size()==0){
                            showSnake(binding.llMain,"Data not found");
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });
    }

    //set category home adapter
    private void setCategory(CategoryModel.CategoryRes categoryRes) {
        mIsLoading=false;
        AppLog.e(TAG,"CategorylistRes : "+categoryRes);
        Log.e(TAG, "setCategory: ......"+categoryRes.isStatus());
        if(categoryRes.isStatus()){
            appPref.set(AppPref.MINAMOUNT,categoryRes.getConfigData().getMin_amount());
            appPref.set(AppPref.DELIVERYCHARGE,categoryRes.getConfigData().getDelivery_charge());
            categoryHomeAdapter.clear();
            categoryHomeAdapter.addCategory(categoryRes.getCategoryList());
        }else if (categoryRes==null){
            showSnake(binding.llMain,"Please login again");
        }
    }

    //subscription products
    
    private void getSubscriptions()
    {
        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }


        apiService.getProductList(appPref.getUserId(),1,0,currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ProductModel.ProductListRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mIsLoadingprod = true;
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ProductModel.ProductListRes> productListRes) {
                        Log.e(TAG, "productlist code: "+productListRes.code());

                        if(isSuccess(productListRes)){
                            setSubscription(productListRes.body());
                        }else if (productListRes.code()==401){
                            logout();
                        }else if(productListRes.body().getProduct_list().size()==0){
                            showSnake(binding.llMain,"Data not found");
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

    private void setSubscription(ProductModel.ProductListRes productListRes) {
        mIsLoadingprod = false;
        AppLog.e(TAG,"setSubscription : "+productListRes);
        if(productListRes.isStatus()){
            subscriptionAdapter.clear();
            subscriptionAdapter.addSubscription(productListRes.getProduct_list());
        }
    }

    private void setOffers() {
       /* ArrayList<String> offers=new ArrayList<>();
        offers.add("https://picsum.photos/360/240/?random");
        offers.add("https://picsum.photos/360/240/?random");
        offers.add("https://picsum.photos/360/240/?random");*/
        binding.rvOffers.setAdapter(offerAdapter);

        //offerAdapter.addOffers(offers);
    }

    private void init() {
        offerAdapter = new OfferAdapter();

        final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
      //  binding.rvSubscription.setLayoutManager(linearlayoutManager);
        subscriptionAdapter = new SubscriptionAdapter();
        subscriptionAdapter.setSubscriptionClick(this);
      //  binding.rvSubscription.setAdapter(subscriptionAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.rvCategories.setLayoutManager(layoutManager);
        categoryHomeAdapter = new CategoryHomeAdapter();
        binding.rvCategories.setAdapter(categoryHomeAdapter);

        setOffers();

        //bottom scroll listener
        binding.rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
/*
        binding.rvSubscription.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = linearlayoutManager.getItemCount();
                int firstVisibleItemPosition = linearlayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearlayoutManager.findLastVisibleItemPosition();

                if (!mIsLoadingprod && totalPagesprod > currentPageprod) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                    ) {
                        currentPageprod = currentPageprod + 1;
                        AppLog.e(TAG, "onScrollCalled " + currentPageprod);

                        getSubscriptions();
                    }
                }
            }
        });
*/

        categoryHomeAdapter.setCategoryClick(this);
        binding.llsubscription.setOnClickListener(this);
    }

    //on click method of subcription items
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llsubscription:{
                ((Home)getActivity()).selecttab(1);
                //((Home)getActivity()).changeFrag(SubscriptionFragment.newInstance(null),false,false);
                break;
            }
        }
    }

    //Interface of category clicks
    @Override
    public void onCategoryClick(int catid) {
        Bundle b = new Bundle();
        b.putInt("catid",catid);
        b.putInt("subsid",0);
        gotoActivity(Product.class,b,false);
       // ((Base)getActivity()).changeFrag(ProductFragment.newInstance(null),true,false);
    }

    //Interface of subscription click
    @Override
    public void onsubscriptionClick(int prodid) {
        Bundle b = new Bundle();
        b.putInt("prodid",prodid);
        gotoActivity(AddSubscription.class,b,false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
