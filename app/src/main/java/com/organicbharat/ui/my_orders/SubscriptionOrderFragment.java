package com.organicbharat.ui.my_orders;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.MySubscriptionOrderModel;
import com.organicbharat.databinding.FragmentSubscriptionOrderBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppLog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionOrderFragment extends BaseFragment implements SubscriptionOrderAdapter.SubscriptionOrderItemClick {

    private static final String TAG = "SubscriptionOrderFragment";
    FragmentSubscriptionOrderBinding binding;
    SubscriptionOrderAdapter subscriptionOrderAdapter;

    public static SubscriptionOrderFragment newInstance(Bundle bundle) {
        SubscriptionOrderFragment fragment = new SubscriptionOrderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_subscription_order, container, false);

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription_order, container, false);
            init();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        getSubscriptionOrderlist();
    }

    private void getSubscriptionOrderlist() {
        if (!isOnline()) {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }


        apiService.getSubscriptionOrderList(appPref.getUserId())//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MySubscriptionOrderModel.MySubscriptionOrderRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<MySubscriptionOrderModel.MySubscriptionOrderRes> subscriptionOrderRes) {
                        if (isSuccess(subscriptionOrderRes)) {
                            setSubscriptionOrderlist(subscriptionOrderRes.body());
                        } else {
                           /* if (subscriptionOrderRes.code() == 401) {
                                logout();
                            } else {*/
                                binding.llnodata.setVisibility(View.VISIBLE);
                                binding.rvSubscriptionOrders.setVisibility(View.GONE);
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

    private void setSubscriptionOrderlist(MySubscriptionOrderModel.MySubscriptionOrderRes subscriptionOrderRes) {
        AppLog.e(TAG, "subscriptionOrderRes : " + subscriptionOrderRes);
        // if (myOrderListRes.isStatus()) {
        if (subscriptionOrderRes.getOrderlist().size() > 0) {
            subscriptionOrderAdapter.clear();
            subscriptionOrderAdapter.addCartItem(subscriptionOrderRes.getOrderlist());

        }
        binding.llnodata.setVisibility(View.GONE);
        binding.rvSubscriptionOrders.setVisibility(View.VISIBLE);
        /*} else {
            binding.llnodata.setVisibility(View.VISIBLE);
            binding.rvMyOrders.setVisibility(View.GONE);
            showToast(myOrderListRes.getMsg());
        }*/
    }

    private void init() {
        binding.rvSubscriptionOrders.setAdapter(subscriptionOrderAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscriptionOrderAdapter = new SubscriptionOrderAdapter();
        subscriptionOrderAdapter.setOrderItemClick(this);
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
        b.putInt("orderid", orderid);
        gotoActivity(SubscriptionOrderDetails.class, b, false);
    }
}
