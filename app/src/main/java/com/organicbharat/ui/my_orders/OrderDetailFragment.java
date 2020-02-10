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
import com.organicbharat.api_model.ClearCartModel;
import com.organicbharat.api_model.MyOrderModel;
import com.organicbharat.databinding.FragmentOrderDetailsBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.cart.OrderSummery;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.DateTimeHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "OrderDetailFragment";
    public static FragmentOrderDetailsBinding binding;
    OrderDetailsItemAdapter myOrdersAdapter;
    int orderid;
    String action = "";
    public String pdf_path = "";

    public static OrderDetailFragment newInstance(Bundle bundle) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            orderid = bundle.getInt("orderid");
            action = bundle.getString("action", "");
            AppLog.e(TAG, "orderid: " + orderid);
        }

        init();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myOrdersAdapter = new OrderDetailsItemAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        getOrderDetais();
    }

    private void getOrderDetais() {
        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }


        apiService.getOrderDetails(appPref.getUserId(), orderid)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MyOrderModel.OrderDetailRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<MyOrderModel.OrderDetailRes> orderDetailRes) {
                        if (isSuccess(orderDetailRes)) {
                            setOrderDetails(orderDetailRes.body());
                        } else {
                            if (orderDetailRes.code() == 401) {
                                logout();
                            } else {

                                showSnake(binding.rlMain,orderDetailRes.body().getMsg());
                                //showToast(orderDetailRes.body().getMsg());
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

    private void setOrderDetails(MyOrderModel.OrderDetailRes orderDetailRes) {
        AppLog.e(TAG, "orderDetailRes: " + orderDetailRes);

        if (orderDetailRes.isStatus()) {
            pdf_path = orderDetailRes.getPdf();
            binding.tvOrderid.setText("" + orderDetailRes.getUnique_order_id());
            binding.tvOrderdate.setText("" + DateTimeHelper.convertFormat(orderDetailRes.getCreated_date(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy hh:mm a"));//DateTimeHelper.convertFormat(orderDetailRes.getCreated_date(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")
            binding.tvSubtotal.setText(" " + orderDetailRes.getTotal_amount());
            binding.tvDeliverycharge.setText(" " + orderDetailRes.getDelivery_charge());
            binding.tvGst.setText(" " + orderDetailRes.getGst());
            binding.tvDiscount.setText(" " + orderDetailRes.getDiscount());
            binding.tvAmount.setText(" " + orderDetailRes.getPayable_amount());
            binding.tvOrderStatus.setText(" " + orderDetailRes.getOrder_status());
            binding.tvDeliverydate.setText(orderDetailRes.getOrder_status() + ": " + DateTimeHelper.convertFormat(orderDetailRes.getDelivery_date(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy hh:mm a"));//orderDetailRes.getDelivery_date()
            binding.tvdeliverytime.setText(""+orderDetailRes.getDelivery_time());
            if (orderDetailRes.getOrderitemlist().size() > 0) {
                myOrdersAdapter.clear();
                myOrdersAdapter.addCartItem(orderDetailRes.getOrderitemlist());
            }

        } else {
            showSnake(binding.rlMain,orderDetailRes.getMsg());
        }
    }

    private void init() {
        binding.rvOrderItem.setAdapter(myOrdersAdapter);
        binding.btrepeatOrder.setOnClickListener(this);
        binding.btGanratePDF.setOnClickListener(this);

        if (action.equalsIgnoreCase("clearcart")) {
            callClearCartAPI();
        }
    }

    private void callClearCartAPI() {

        if (!isOnline()) {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }

        ClearCartModel.ClearCartReq clearCartReq = new ClearCartModel.ClearCartReq();
        clearCartReq.setUser_id(appPref.getUserId());

        apiService.clearcart(clearCartReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ClearCartModel.ClearCartRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ClearCartModel.ClearCartRes> clearCartRes) {
                        if (isSuccess(clearCartRes)) {

                        } else {
                            if (clearCartRes.code()==401){
                                logout();
                            }else{
                                showSnake(binding.rlMain,clearCartRes.body().getMsg());

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btrepeatOrder: {
                Bundle b = new Bundle();
                b.putString("action", "repeatOrder");
                b.putInt("orderid", orderid);
                gotoActivity(OrderSummery.class, b, false);
                break;
            }
            case R.id.bt_ganrate_PDF: {
                Bundle bundle = new Bundle();
                bundle.putString("pdf_path", pdf_path);
                ((OrdersDetails) getActivity()).changeFrag(PDFViewFragment.newInstance(bundle), true, false);
                break;
            }
        }
    }


}
