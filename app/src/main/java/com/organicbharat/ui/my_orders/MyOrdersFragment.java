package com.organicbharat.ui.my_orders;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.MyOrderModel;
import com.organicbharat.databinding.FragmentMyOrdersBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MyOrdersFragment extends BaseFragment implements MyOrdersAdapter.OrderItemClick {
    private static final String TAG = "MyOrdersFragment";
    FragmentMyOrdersBinding binding;
    MyOrdersAdapter myOrdersAdapter;
    public static MyOrdersFragment newInstance(Bundle bundle) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myOrdersAdapter=new MyOrdersAdapter();
        myOrdersAdapter.setOrderItemClick(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // int strtext = getArguments().getInt("key");
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders,container,false);
            init();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getOrderlist();
    }

    private void getOrderlist()
    {

        if(!isOnline())
        {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }



        apiService.getOrderList(appPref.getUserId())//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MyOrderModel.MyOrderListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<MyOrderModel.MyOrderListRes> myOrderListRes) {
                        if (isSuccess(myOrderListRes)) {
                            setOrderlist(myOrderListRes.body());
                        }else {
                          /*  if (myOrderListRes.code()==401){
                                logout();
                            }else{*/
                                binding.llnodata.setVisibility(View.VISIBLE);
                                binding.rvMyOrders.setVisibility(View.GONE);

                                showSnake(binding.rlMain,myOrderListRes.body().getMsg());


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

    private void setOrderlist(MyOrderModel.MyOrderListRes myOrderListRes) {
        AppLog.e(TAG, "myOrderListRes : " + myOrderListRes);
       // if (myOrderListRes.isStatus()) {

            if (myOrderListRes.getOrderlist().size() > 0) {
                myOrdersAdapter.clear();
                myOrdersAdapter.addCartItem(myOrderListRes.getOrderlist());
                binding.llnodata.setVisibility(View.GONE);
                binding.rvMyOrders.setVisibility(View.VISIBLE);
            }else {
                binding.llnodata.setVisibility(View.VISIBLE);
                binding.rvMyOrders.setVisibility(View.GONE);
//                showToast(myOrderListRes.getMsg());

            }

        /*} else {
            binding.llnodata.setVisibility(View.VISIBLE);
            binding.rvMyOrders.setVisibility(View.GONE);
            showToast(myOrderListRes.getMsg());
        }*/
    }

    private void init() {
        binding.rvMyOrders.setAdapter(myOrdersAdapter);
    }

/*
    private class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>{

        @NonNull
        @Override
        public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder viewHolder, int position) {

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
                        onItemClick();
                    }
                });
            }
        }
    }
*/

    @Override
    public void onOrderitemClick(int orderid) {
        Bundle b = new Bundle();
        b.putInt("orderid",orderid);
        gotoActivity(OrdersDetails.class,b,false);
    }
}
