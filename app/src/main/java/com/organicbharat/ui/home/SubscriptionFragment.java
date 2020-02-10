package com.organicbharat.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.organicbharat.R;
import com.organicbharat.api_model.PauseOrderModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.api_model.ResumeOrder;
import com.organicbharat.databinding.FragmentSubscriptionsBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.my_orders.RepeatOrder;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import com.organicbharat.utils.DateTimeHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SubscriptionFragment extends BaseFragment implements SubscriptionProductAdapter.subscriptionproductClick {
    private static final String TAG = "SubscriptionFragment";
    FragmentSubscriptionsBinding binding;
    SubscriptionProductAdapter subscriptionProductAdapter;
    int totalPagesprod = 1, currentPageprod = 1;
    boolean mIsLoadingprod = false;
    ArrayList<ProductModel.Slot> slots = new ArrayList<>();
    String startDatePause_get = "", endDatePause_get = "";
    private String orderfromdate = "", ordertodate;
    private Calendar cal = Calendar.getInstance();
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();

    public static SubscriptionFragment newInstance(Bundle bundle) {
        SubscriptionFragment fragment = new SubscriptionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPageprod = 1;
        getSubscriptions();

        subscriptionProductAdapter = new SubscriptionProductAdapter();
        subscriptionProductAdapter.setSubscriptionClick(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscriptions, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {



        startDate.add(Calendar.DATE, 1);

        @SuppressLint("WrongConstant") final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvSubscription.setLayoutManager(linearlayoutManager);

        binding.rvSubscription.setAdapter(subscriptionProductAdapter);

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
                            && firstVisibleItemPosition >= 0) {
                        currentPageprod = currentPageprod + 1;
                        AppLog.e(TAG, "onScrollCalled " + currentPageprod);
                        getSubscriptions();
                    }
                }
            }
        });
    }


    private void getSubscriptions() {
        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }


        apiService.getProductList(appPref.getUserId(), 1, 0, currentPageprod)
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
                        if (isSuccess(productListRes)) {
                            setSubscription(productListRes.body());
                        } else if (productListRes.code() == 401) {
                            logout();
                        } else if (productListRes.body().getProduct_list().size() == 0) {
                            showSnake(binding.llMain,"Data not found");
                        } else {

                            showSnake(binding.llMain,productListRes.body().getMsg());
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
        AppLog.e(TAG, "setSubscription : " + productListRes);
        if (productListRes.isStatus()) {

            slots = productListRes.getSlots();
            appPref.set(AppPref.SLOTS, new Gson().toJson(slots));
            subscriptionProductAdapter.clear();
            subscriptionProductAdapter.addSubscription(productListRes.getProduct_list());
        }
    }

    @Override
    public void onsubscriptionproductClick(int prodid) {
        Bundle b = new Bundle();
        b.putInt("prodid", prodid);
        // b.putString("slots",new Gson().toJson(slots));
        gotoActivity(AddSubscription.class, b, false);
    }

    @Override
    public void onResumeClick(int orderid, Button btpause, Button btresume) {
        showConfirmationDialog("Are you sure to resume?", 0, orderid, btpause, btresume);
    }

    @Override
    public void onRepeatClick(int prodid) {
        Bundle b = new Bundle();
        b.putInt("prodid", prodid);
        gotoActivity(RepeatOrder.class, b, false);
    }


    @Override
    public void onPauseClick(int orderid, String end_date, Button btpause, Button btresume) {
        ordertodate = DateTimeHelper.convertFormat(end_date, "yyyy-mm-dd", "dd-mm-yyyy");
        callPauseOrderDialog(orderid, btpause, btresume);
    }

    private void callPauseOrderDialog(final int orderid, final Button btpause, final Button btresume) {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pause);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnpause = dialog.findViewById(R.id.btPauseOrder1);
        final ImageView aerrowStart=dialog.findViewById(R.id.aerrowStart);
        final ImageView aerrowEnd=dialog.findViewById(R.id.aerrowEnd);


        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDatePause_get.isEmpty()) {
                    showSnake(binding.llMain,"Select start Date");
                } else if (endDatePause_get.isEmpty()) {
                    showSnake(binding.llMain,"Select End Date");

                } else {
                    dialog.dismiss();
                    showConfirmationDialog("Are you sure to pause?", 1, orderid, btpause, btresume);

                }
            }
        });
        final TextView startDate = dialog.findViewById(R.id.tvStartDate);

        final TextView endDate = dialog.findViewById(R.id.tvEndDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(false, startDate, endDate);

            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(true, startDate, endDate);
            }
        });


        aerrowStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(true,startDate,endDate);
            }
        });

        aerrowEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(false,startDate,endDate);
            }
        });



        dialog.show();
    }

    private void showDatePicker(final boolean isStart, final TextView start_date, final TextView end_date) {
        int todate = Integer.valueOf(ordertodate.substring(0, 2));
        int tomonth = Integer.valueOf(ordertodate.substring(3, 5));
        int toyear = Integer.valueOf(ordertodate.substring(6, 10));
        AppLog.e(TAG, "Date : " + todate);
        AppLog.e(TAG, "Month : " + tomonth);
        AppLog.e(TAG, "Year : " + toyear);
        cal.set(Calendar.YEAR, toyear);
        cal.set(Calendar.MONTH, tomonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, todate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (isStart) {
                    Date cDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate = df.format(cDate);

                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    //if start date after 30 days not valid
                    Calendar cexp = Calendar.getInstance();
                    cexp.add(Calendar.DATE, 30);
                    Date expDate = cexp.getTime();

                    if (startDate.getTime().after(expDate)) {
                        // Your time expired do your logic here.
                        AppLog.e(TAG, " date not valid ");

                        showSnake(binding.llMain,"you can not select date after 30days");

                    } else {

                        String date1 = DateTimeHelper.convertFormat(startDate.getTime(), "MM/dd/yyyy");//"dd-MM-yyyy"

                        if (date1.equalsIgnoreCase(formattedDate)) {
                            startDate.add(Calendar.DATE, 1);

                            showSnake(binding.llMain,"can not select today!");
                        } else {

                            AppLog.e(TAG, ordertodate);

                            AppLog.e(TAG, DateTimeHelper.convertFormat(cal.getTime(), "dd-MM-yyyy"));
                            if (startDate.after(cal)) {
                                showSnake(binding.llMain,"Invalid End Date");
                            } else {
                                start_date.setText(DateTimeHelper.convertFormat(startDate.getTime(), "dd-MM-yyyy"));
                                startDatePause_get = DateTimeHelper.convertFormat(startDate.getTime(), "yyyy-MM-dd");

                                String date = DateTimeHelper.convertFormat(startDate.getTime(), "MM/dd/yyyy");//"dd-MM-yyyy"
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date convertedDate = null;
                                try {
                                    convertedDate = dateFormat.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar c = Calendar.getInstance();
                                c.setTime(convertedDate);
                                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

                                AppLog.e(TAG, "dateofthemonth: " + c.getTime());
                            }
                        }
                    }
                } else {
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (endDate.before(startDate)) {
                        AppLog.e(TAG, " date not valid ");
                        showSnake(binding.llMain,"Enter valid end date");
                    } else {
                        if (endDate.after(cal)) {
                            showSnake(binding.llMain,"Invalid End Date");
                        } else {
                            end_date.setText((DateTimeHelper.convertFormat(endDate.getTime(), "dd-MM-yyyy")));
                            endDatePause_get = DateTimeHelper.convertFormat(endDate.getTime(), "yyyy-MM-dd");
                        }
                    }

                }

            }
        }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showConfirmationDialog(final String msg, final int status, final int orderid, final Button btpause, final Button btresume) {

        final Dialog dialog_con = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog_con.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_con.setContentView(R.layout.dialog_pause_confirm);
        dialog_con.setCancelable(false);

        dialog_con.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView con_msg = dialog_con.findViewById(R.id.msg_con);
        if (status == 1) {
            con_msg.setText(msg);
        } else {
            con_msg.setText(msg);
        }
        Button pauseOrderOkay = dialog_con.findViewById(R.id.btPauseOrderOkay);
        pauseOrderOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_con.dismiss();
                if (status == 1) {
                    pauseOrderAPI(startDatePause_get, endDatePause_get, orderid, btpause, btresume);
                } else {
                    resumeOrderAPI(orderid, btpause, btresume);
                }
            }
        });
        Button pauseOrderCancel = dialog_con.findViewById(R.id.btPauseOrderCancel);
        pauseOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_con.dismiss();
            }
        });

        dialog_con.show();
    }

    private void resumeOrderAPI(int orderid, final Button btpause, final Button btresume) {
        final ResumeOrder.ResumeOrderReq resumeOrderReq = new ResumeOrder.ResumeOrderReq();
        resumeOrderReq.setUser_id(appPref.getUserId());
        resumeOrderReq.setOrder_id(orderid);

        // pauseOrderReq.setEnd_date(enddate);

        apiService.resumeOrderAPI(resumeOrderReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResumeOrder.ResumeOrderRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ResumeOrder.ResumeOrderRes> resumeOrderRes) {
                        if (isSuccess(resumeOrderRes)) {
                            setResumeOrderRes(resumeOrderRes.body());
                            btpause.setVisibility(View.VISIBLE);
                            btresume.setVisibility(View.GONE);
                        } else {
                            if (resumeOrderRes.code() == 401) {
                                logout();
                            } else {
                                showSnake(binding.llMain,resumeOrderRes.body().getMsg());
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
    public void onResume() {
        init();
        super.onResume();
    }

    private void setResumeOrderRes(ResumeOrder.ResumeOrderRes resumeOrderRes) {
        if (resumeOrderRes.isStatus()) {
            showSnake(binding.llMain,resumeOrderRes.getMsg());

            AppLog.e(TAG, "setResumeOrderRes : " + resumeOrderRes.getMsg());
        }
    }


    private void setPauseOrderRes(PauseOrderModel.PauseOrderRes pauseOrderRes) {
        if (pauseOrderRes.isStatus()) {
            AppLog.e(TAG, "setPauseOrderRes : " + pauseOrderRes.getMsg());

            showSnake(binding.llMain,pauseOrderRes.getMsg());

        }
    }

    private void pauseOrderAPI(String tvStartDate, String tvEndDate, int orderid, final Button btpause, final Button btresume) {
        AppLog.e(TAG, "tvStartDate : " + tvStartDate);
        AppLog.e(TAG, "tvEndDate : " + tvEndDate);
        PauseOrderModel.PauseOrderReq pauseOrderReq = new PauseOrderModel.PauseOrderReq();
        pauseOrderReq.setUser_id(appPref.getUserId());
        pauseOrderReq.setOrder_id(orderid);
        pauseOrderReq.setStart_date(tvStartDate);
        pauseOrderReq.setEnd_date(tvEndDate);

        apiService.pauseOrderAPI(pauseOrderReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<PauseOrderModel.PauseOrderRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<PauseOrderModel.PauseOrderRes> pauseOrderRes) {
                        startDatePause_get = "";
                        endDatePause_get = "";
                        AppLog.e(TAG, "pauseOrderRes : " + pauseOrderRes);
                        AppLog.e(TAG, "isSuccess(pauseOrderRes) : " + isSuccess(pauseOrderRes));
                        if (isSuccess(pauseOrderRes)) {
                            setPauseOrderRes(pauseOrderRes.body());
                            btpause.setVisibility(View.GONE);
                            btresume.setVisibility(View.VISIBLE);
                        } else {
                            if (pauseOrderRes.code() == 401) {
                                logout();
                            } else {
                                AppLog.e(TAG, " onNext :" + pauseOrderRes.body().getMsg());

                                showSnake(binding.llMain, pauseOrderRes.body().getMsg());
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

/*
    private class SubscriptionProductAdapter extends RecyclerView.Adapter<SubscriptionProductAdapter.ViewHolder>{

        @NonNull
        @Override
        public SubscriptionProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subscription_product,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SubscriptionProductAdapter.ViewHolder viewHolder, int i) {
            ImageBinding.loadImage(viewHolder.imgSubscription,productlist.get(i).getProd_image());
            viewHolder.tv_prodname.setText("@"+productlist.get(i).getRate()+" Per Ltr");
        }

        @Override
        public int getItemCount() {
            return 12;
        }

        }
*/
}




