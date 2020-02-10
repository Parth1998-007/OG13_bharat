package com.organicbharat.ui.my_orders;

import android.app.DatePickerDialog;
import android.app.Dialog;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.MyOrderModel;
import com.organicbharat.api_model.PauseOrderModel;
import com.organicbharat.api_model.ResumeOrder;
import com.organicbharat.databinding.FragmentSubscriptionOrderDetailBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.DateTimeHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 **/

public class SubscriptionOrderDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SubscriptionOrderDetailFragment";
    FragmentSubscriptionOrderDetailBinding binding;
    int orderid;
    int prodid;
    boolean checkdata = false;
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    String startDatePause_get = "", endDatePause_get = "";
    private String orderfromdate = "", ordertodate;
    private Calendar cal = Calendar.getInstance();
    public String pdf_path = "";


    public static SubscriptionOrderDetailFragment newInstance(Bundle bundle) {
        SubscriptionOrderDetailFragment fragment = new SubscriptionOrderDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_subscription_order_detail, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription_order_detail, container, false);

        binding.btPauseOrder.setVisibility(View.GONE);
        binding.btresumeOrder.setVisibility(View.GONE);


        init();
        getOrderDetail();

        return binding.getRoot();
    }

    private void getOrderDetail() {
        if (!isOnline()) {
            //showSnake(binding.);
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
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
                                showSnake(binding.llMain,orderDetailRes.body().getMsg());

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

        if (orderDetailRes.isStatus()) {
            pdf_path = orderDetailRes.getPdf();
            orderfromdate = DateTimeHelper.convertFormat(orderDetailRes.getStart_date(), "yyyy-mm-dd", "dd-mm-yyyy");
            ordertodate = DateTimeHelper.convertFormat(orderDetailRes.getEnd_date(), "yyyy-mm-dd", "dd-mm-yyyy");
            binding.tvProductname.setText("" + orderDetailRes.getOrderitemlist().get(0).getProduct_name());
            binding.tvOrderfrom.setText("" + DateTimeHelper.convertFormat(orderDetailRes.getStart_date(), "yyyy-mm-dd", "dd-mm-yyyy"));
            binding.tvOrderto.setText("" + DateTimeHelper.convertFormat(orderDetailRes.getEnd_date(), "yyyy-mm-dd", "dd-mm-yyyy"));
            binding.tvSubtotal.setText("" + orderDetailRes.getTotal_amount());
            binding.tvDelivery.setText("" + orderDetailRes.getDelivery_charge());
            binding.tvGst.setText("" + orderDetailRes.getGst());
            binding.tvDiscount.setText("" + orderDetailRes.getDiscount());
            binding.tvPayable.setText("" + orderDetailRes.getPayable_amount());
            binding.tvCompletedorder.setText("" + orderDetailRes.getCompleted_orders());
            binding.tvLeftdorder.setText("" + orderDetailRes.getLeft_orders());
            binding.tvdeliverytime.setText(""+orderDetailRes.getDelivery_time());

            if (orderDetailRes.getSummaryList().get(0).getOrderType().equalsIgnoreCase("daily"))
                binding.tvOrdertype.setText("" + orderDetailRes.getSummaryList().get(0).getOrderType());
            else {
                binding.tvOrdertype.setText("" + orderDetailRes.getSummaryList().get(0).getOrderType() + " Days");
            }

            prodid = orderDetailRes.getOrderitemlist().get(0).getProduct_id();
            Calendar cal = Calendar.getInstance();
            int todate = Integer.valueOf(DateTimeHelper.convertFormat(orderDetailRes.getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(0, 2));
            int tomonth = Integer.valueOf(DateTimeHelper.convertFormat(orderDetailRes.getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(3, 5));
            int toyear = Integer.valueOf(DateTimeHelper.convertFormat(orderDetailRes.getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(6, 10));
            AppLog.e(TAG, "Date : " + todate);
            AppLog.e(TAG, "Month : " + tomonth);
            AppLog.e(TAG, "Year : " + toyear);
            cal.set(Calendar.YEAR, toyear);
            cal.set(Calendar.MONTH, tomonth - 1);
            cal.set(Calendar.DAY_OF_MONTH, todate);
            Calendar calendar = Calendar.getInstance();

            if (cal.getTimeInMillis() <= calendar.getTimeInMillis()) {
                binding.btPauseOrder.setVisibility(View.GONE);
                binding.btresumeOrder.setVisibility(View.GONE);
            } else {
                binding.btPauseOrder.setVisibility(View.VISIBLE);
                binding.btresumeOrder.setVisibility(View.GONE);
            }

        }
    }

    private void init() {

        if (checkdata) {
            binding.btPauseOrder.setVisibility(View.GONE);
            binding.btresumeOrder.setVisibility(View.VISIBLE);
        } else {
            binding.btPauseOrder.setVisibility(View.VISIBLE);
            binding.btresumeOrder.setVisibility(View.GONE);
        }

/*        Log.e("check", "onCreateView: "+appPref.getBoolean("checkOrderStatus") );

        Log.i("checkdata", "init: ");
        if (appPref.getBoolean("checkOrderStatus")) {
            Log.e("check", "IF: " );
            binding.btresumeOrder.setVisibility(View.VISIBLE);
            binding.btPauseOrder.setVisibility(View.GONE);

        }
        else{
            Log.e("check", "ELSE: " );
            binding.btresumeOrder.setVisibility(View.GONE);
            binding.btPauseOrder.setVisibility(View.VISIBLE);
        }
        Log.e("check", "init: "+appPref.getBoolean("checkOrderStatus") );*/
        startDate.add(Calendar.DATE, 1);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            orderid = bundle.getInt("orderid");
            AppLog.e(TAG, "orderid: " + orderid);
        }
        binding.btrepeatOrder.setOnClickListener(this);
        binding.btPauseOrder.setOnClickListener(this);
        binding.btresumeOrder.setOnClickListener(this);
        binding.btGanratePDF.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btrepeatOrder: {
                Bundle b = new Bundle();
                b.putInt("prodid", prodid);
                gotoActivity(RepeatOrder.class, b, false);
                break;
            }
            case R.id.btPauseOrder: {
                callPauseOrderDialog();
                // pauseOrderAPI();
                break;
            }
            case R.id.btresumeOrder: {
                showConfirmationDialog("Are you sure to resume?", 0);

                break;
            }
            case R.id.bt_ganrate_PDF: {
                Bundle bundle = new Bundle();
                bundle.putString("pdf_path", pdf_path);
                ((SubscriptionOrderDetails) getActivity()).changeFrag(PDFViewFragment.newInstance(bundle), true, false);
                break;
            }
        }
    }

    private void callPauseOrderDialog() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pause);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btpause = dialog.findViewById(R.id.btPauseOrder1);


        btpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDatePause_get.isEmpty()) {
                    showSnake(binding.llMain,"Select Start Date");
                } else if (endDatePause_get.isEmpty()) {
                    showSnake(binding.llMain,"Select End Date");
                } else {
                    dialog.dismiss();
                    showConfirmationDialog("Are you sure to pause?", 1);
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
        dialog.show();
    }


    @Override
    public void onResume() {
        Log.i("checkdata", "onResume: ");
        Log.e("check", "onResume: " + appPref.getBoolean("checkOrderStatus"));

        if (appPref.getBoolean("checkOrderStatus")) {
            binding.btPauseOrder.setVisibility(View.GONE);
            binding.btresumeOrder.setVisibility(View.VISIBLE);
        } else {
            binding.btresumeOrder.setVisibility(View.GONE);
            binding.btPauseOrder.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void showConfirmationDialog(final String msg, final int status) {

        final Dialog dialog_con = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog_con.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_con.setContentView(R.layout.dialog_pause_confirm);

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
                    pauseOrderAPI(startDatePause_get, endDatePause_get);
                } else {
                    resumeOrderAPI();
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
            public void onDateSet(DatePicker view, int year, int month, final int dayOfMonth) {


                if (isStart) {

                    Date cDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    String formatedDate = df.format(cDate);

                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    //if start date after 30 days not valid
                    Calendar cexp = Calendar.getInstance();
                    cexp.add(Calendar.DATE, 30);
                    Date expDate = cexp.getTime();

                  /*  if (startDate.getTime().after(expDate)) {
                        // Your time expired do your logic here.
                        AppLog.e(TAG, " date not valid ");
                        showToast("you can not select date after 30days");
                    } else {
*/
                        String sdate = DateTimeHelper.convertFormat(startDate.getTime(), "MM/dd/yyyy");
                        if (sdate.equalsIgnoreCase(formatedDate)) {
                            startDate.add(Calendar.DATE, 1);
                            showSnake(binding.llMain,"You can not select today!");
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
                      //      }
                        }


                    }
                } else {
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (endDate.before(startDate)) {
                        AppLog.e(TAG, " date not valid ");

                        showSnake(binding.llMain,"Enter valid End date");

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


    private void pauseOrderAPI(String tvStartDate, String tvEndDate) {
        if (!isOnline()) {
            showSnake(binding.llMain,"Internet Not Available, Please Try Again");
            return;
        }


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

    private void resumeOrderAPI() {

        if (!isOnline()) {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

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
                        } else {
                            if (resumeOrderRes.code()==401){
                                logout();
                            }else{
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

    private void setResumeOrderRes(ResumeOrder.ResumeOrderRes resumeOrderRes) {
        if (resumeOrderRes.isStatus()) {

            showSnake(binding.llMain,resumeOrderRes.getMsg());

            AppLog.e(TAG, "setResumeOrderRes : " + resumeOrderRes.getMsg());
            binding.btPauseOrder.setVisibility(View.VISIBLE);
            binding.btresumeOrder.setVisibility(View.GONE);
        }
    }


    private void setPauseOrderRes(PauseOrderModel.PauseOrderRes pauseOrderRes) {
        if (pauseOrderRes.isStatus()) {
            AppLog.e(TAG, "setPauseOrderRes : " + pauseOrderRes.getMsg());

            showSnake(binding.llMain,pauseOrderRes.getMsg());

            binding.btresumeOrder.setVisibility(View.VISIBLE);
            binding.btPauseOrder.setVisibility(View.GONE);
        }
    }

}
