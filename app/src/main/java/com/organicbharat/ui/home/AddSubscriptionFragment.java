package com.organicbharat.ui.home;

import android.app.DatePickerDialog;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.CreateSummeryModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentAddSubscriptionBinding;
import com.organicbharat.image_utils.ImageBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.cart.SubscriptionSummery;
import com.organicbharat.ui.product.SubscriptionProductDetail;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.DateTimeHelper;
import com.organicbharat.views.NumberPicker;
import com.organicbharat.views.NumberPickerDaily;
import com.organicbharat.views.NumberPickerVertical;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AddSubscriptionFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "AddSubscriptionFragment";
    FragmentAddSubscriptionBinding binding;
    int count = 1;
    Calendar startDate = Calendar.getInstance();
    int prodid;
    Calendar endDate = Calendar.getInstance();
    String startdate, enddate;
    CreateSummeryModel.CreateSummeryReq createSummeryReq;
    NumberPicker numberPickerodd;
    NumberPicker numberPickereven;
    NumberPickerDaily numberPickerdaily;
    NumberPickerVertical numberPickerVertical1;
    NumberPickerVertical numberPickerVertical2;
    NumberPickerVertical numberPickerVertical3;
    NumberPickerVertical numberPickerVertical4;
    NumberPickerVertical numberPickerVertical5;
    NumberPickerVertical numberPickerVertical6;
    NumberPickerVertical numberPickerVertical7;
    Date startDateValue, endDateValue;
    ArrayList<CreateSummeryModel.Item> itemlist;
    CreateSummeryModel.Item item;
    CreateSummeryModel.Days days;
    ArrayList<ProductModel.Slot> slots = new ArrayList<>();
    long differenceDates;

    public static AddSubscriptionFragment newInstance(Bundle args) {
        AddSubscriptionFragment fragment = new AddSubscriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_subscription, container, false);
        init();
        listener();
        return binding.getRoot();
    }

    private void listener() {
        binding.llStartDate.setOnClickListener(this);
        binding.llEndDate.setOnClickListener(this);
        binding.btnproddetails.setOnClickListener(this);
        binding.btdone.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            prodid = bundle.getInt("prodid");
            Type type = new TypeToken<ArrayList<ProductModel.Slot>>() {
            }.getType();

            // slots = new Gson().fromJson(bundle.getString("slots"),type);
        }

        getProductDetail(prodid);
    }

    //Show Date Picker User select Start and End Date
    private void showDatePicker(final boolean isStart) {


        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                if (isStart) {

                    Date cDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate = df.format(cDate);

                    Log.e(TAG, "START DATE IS.." + startDate);
                    Log.e(TAG, "Current Date is.." + formattedDate);
                    startDate.set(Calendar.YEAR, year);
                    startDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    //if start date after 30 days not valid
                    Calendar cexp = Calendar.getInstance();
                    cexp.add(Calendar.DATE, 30);
                    Date expDate = cexp.getTime();

                    /*if (startDate.getTime().after(expDate)) {
                        // Your time expired do your logic here.
                        AppLog.e(TAG, " date not valid ");
                        showToast("you can not select date after 30days");
                    } else {*/


                        String date = DateTimeHelper.convertFormat(startDate.getTime(), "MM/dd/yyyy");//"dd-MM-yyyy"

                        if (date.equalsIgnoreCase(formattedDate)) {
                            startDate.add(Calendar.DATE, 1);

                            showSnake(binding.llMain,"Enter valid date");
                        } else {

                            Calendar calenderenddefault = Calendar.getInstance();
                            calenderenddefault.set(Calendar.MONTH, month);
                            calenderenddefault.set(Calendar.DAY_OF_MONTH, calenderenddefault.getActualMaximum(Calendar.DAY_OF_MONTH));

                            Date date1 = startDate.getTime();
                            Date date2 = calenderenddefault.getTime();


                            Log.e(TAG, "DATE --1 IS..." + date1);
                            Log.e(TAG, "DATE --2 IS" + date2);

                            long difference = Math.abs(date1.getTime() - date2.getTime());
                            Log.e(TAG, "DIFFRENCE CALCULATION IS.." + difference);
                            long differenceDates = difference / (24 * 60 * 60 * 1000);


                            Log.e(TAG, "DIFFRENCE BETWEEN TWO DATES" + differenceDates);

                            if (differenceDates >= 5) {

                                binding.tvStartDate.setText(DateTimeHelper.convertFormat(startDate.getTime(), "dd-MM-yyyy"));

                                //    binding.tvEndDate.setText(DateTimeHelper.convertFormat(endDate.getTime(), "dd-MM-yyyy"));
                            } else {
                                ///   binding.0.setText(DateTimeHelper.convertFormat(startDate.getTime(), "dd-MM-yyyy"));


                                showSnake(binding.llMain,"You can not order for this month . Minimum 5 Days needed.");


                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                            Log.e(TAG, "FOMATED DATE ISS...." + date);

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


                            binding.tvEndDate.setText((DateTimeHelper.convertFormat(c.getTime(), "dd-MM-yyyy")));

                        }
                  //  }

                } else {
                    endDate.set(Calendar.YEAR, year);
                    endDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    if (endDate.before(startDate)) {
                        AppLog.e(TAG, " date not valid " + endDate);

                        showSnake(binding.llMain,"Enter valid end date");


                    } else {


                        Date date1 = startDate.getTime();
                        Date date2 = endDate.getTime();

                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        Log.e(TAG, "DIFFRENCE CALCULATION IS.." + difference);
                        differenceDates = difference / (24 * 60 * 60 * 1000);


                        Log.e(TAG, "DIFFRENCE BETWEEN TWO DATES" + differenceDates);
                        if (differenceDates>=30){
                            showSnake(binding.llMain,"you can not select date after 30days");

                        }else if (differenceDates >= 4) {

                            binding.tvEndDate.setText(DateTimeHelper.convertFormat(endDate.getTime(), "dd-MM-yyyy"));

                        } else  {

                            showSnake(binding.llMain,"Set Minimum Five Days!");
                        }


                    }

                }
            }
        }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

        if (isStart) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }else{
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        }

        Log.e(TAG, "isStart : " + isStart);
      /*  if (!isStart) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, startDate.get(Calendar.MONTH));
            datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());

            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        }*/
        datePickerDialog.show();

    }

    private void init() {
        startDate.add(Calendar.DATE, 1);

        createSummeryReq = new CreateSummeryModel.CreateSummeryReq();
        item = new CreateSummeryModel.Item();
        days = new CreateSummeryModel.Days();
        itemlist = new ArrayList<>();

        // numberPickerodd = binding.getRoot().findViewById(R.id.pickerOdd);
        numberPickerodd = (com.organicbharat.views.NumberPicker) getChildFragmentManager().findFragmentById(R.id.pickerOdd);
        numberPickereven = (com.organicbharat.views.NumberPicker) getChildFragmentManager().findFragmentById(R.id.pickerEven);
        numberPickerdaily = (com.organicbharat.views.NumberPickerDaily) getChildFragmentManager().findFragmentById(R.id.pickerDaily);
        numberPickerVertical1 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np1);
        numberPickerVertical2 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np2);
        numberPickerVertical3 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np3);
        numberPickerVertical4 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np4);
        numberPickerVertical5 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np5);
        numberPickerVertical6 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np6);
        numberPickerVertical7 = (NumberPickerVertical) getChildFragmentManager().findFragmentById(R.id.np7);

        binding.rbtDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llDailySelection.setVisibility(View.VISIBLE);
                    binding.llAltSelection.setVisibility(View.GONE);
                    binding.llChks1.setVisibility(View.GONE);
                }
            }
        });
        binding.rbtAlter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llDailySelection.setVisibility(View.GONE);
                    binding.llAltSelection.setVisibility(View.VISIBLE);
                    binding.llChks1.setVisibility(View.GONE);
                }
            }
        });
        binding.rbtSelectDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.llDailySelection.setVisibility(View.GONE);
                    binding.llAltSelection.setVisibility(View.GONE);
                    binding.llChks1.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.tvStartDate.setText(DateTimeHelper.convertFormat(startDate.getTime(), "dd-MM-yyyy"));

        Calendar calenderenddefault = Calendar.getInstance();
        calenderenddefault.setTime(startDate.getTime());
        calenderenddefault.set(Calendar.DAY_OF_MONTH, calenderenddefault.getActualMaximum(Calendar.DAY_OF_MONTH));


        binding.tvEndDate.setText(DateTimeHelper.convertFormat(calenderenddefault.getTime(), "dd-MM-yyyy"));


        binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llStartDate:
                showDatePicker(true);
                break;
            case R.id.llEndDate:
                showDatePicker(false);
                break;
            case R.id.btdone:
                getdata();
                break;
            case R.id.btnproddetails:
                Bundle b1 = new Bundle();
                b1.putInt("prodid", prodid);
                gotoActivity(SubscriptionProductDetail.class, b1, false);
                break;
        }
    }

    private void getdata() {
        createSummeryReq.setUser_id(appPref.getUserId());
        createSummeryReq.setStart_date(DateTimeHelper.convertFormat(binding.tvStartDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
        createSummeryReq.setEnd_date(DateTimeHelper.convertFormat(binding.tvEndDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
        item.setProd_id(prodid);
        if (binding.rbtDaily.isChecked()) {
            createSummeryReq.setRequirement("Daily");
            item.setQty(numberPickerdaily.getNumber());
            itemlist.clear();
            itemlist.add(item);
            createSummeryReq.setItemlist(itemlist);

            Bundle b = new Bundle();
            b.putString("createordersummery", new Gson().toJson(createSummeryReq));
            b.putString("prodname", binding.tvProdname.getText().toString());
            // b.putString("slots",new Gson().toJson(slots));
            gotoActivity(SubscriptionSummery.class, b, false);
        } else if (binding.rbtAlter.isChecked()) {

            if (numberPickerodd.getNumber() > 0 || numberPickereven.getNumber() > 0) {

                createSummeryReq.setRequirement("Alternate");
                createSummeryReq.setOdd_day(numberPickerodd.getNumber());
                createSummeryReq.setEven_day(numberPickereven.getNumber());
                itemlist.clear();
                itemlist.add(item);
                createSummeryReq.setItemlist(itemlist);

                Bundle b = new Bundle();
                b.putString("createordersummery", new Gson().toJson(createSummeryReq));
                b.putString("prodname", binding.tvProdname.getText().toString());
                // b.putString("slots",new Gson().toJson(slots));
                gotoActivity(SubscriptionSummery.class, b, false);
            } else {


                showSnake(binding.llMain,"Please add item first");


            }


        } else if (binding.rbtSelectDays.isChecked()) {

            if (numberPickerVertical1.getNumber() > 0 || numberPickerVertical1.getNumber() > 0 || numberPickerVertical2.getNumber() > 0
                    || numberPickerVertical3.getNumber() > 0 || numberPickerVertical4.getNumber() > 0 || numberPickerVertical5.getNumber() > 0
                    || numberPickerVertical6.getNumber() > 0 || numberPickerVertical7.getNumber() > 0) {


                createSummeryReq.setRequirement("Selected");
                days.setSun(numberPickerVertical1.getNumber());
                days.setMon(numberPickerVertical2.getNumber());
                days.setTue(numberPickerVertical3.getNumber());
                days.setWed(numberPickerVertical4.getNumber());
                days.setThur(numberPickerVertical5.getNumber());
                days.setFri(numberPickerVertical6.getNumber());
                days.setSat(numberPickerVertical7.getNumber());
                createSummeryReq.setDays(days);
                itemlist.clear();
                itemlist.add(item);
                createSummeryReq.setItemlist(itemlist);

                Bundle b = new Bundle();
                b.putString("createordersummery", new Gson().toJson(createSummeryReq));
                b.putString("prodname", binding.tvProdname.getText().toString());
                // b.putString("slots",new Gson().toJson(slots));
                gotoActivity(SubscriptionSummery.class, b, false);

            } else {
             showSnake(binding.llMain,"Please add item first!");



            }

        }
    }

    private void getProductDetail(final int prodid) {
        if (!isOnline()) {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }


        apiService.getProductDetail(appPref.getUserId(), prodid)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ProductModel.ProductDetailsRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ProductModel.ProductDetailsRes> productListResRes) {
                        if (isSuccess(productListResRes)) {
                            setProductDetail(productListResRes.body());
                        } else {
                            if (productListResRes.code()==401){
                                logout();
                            }else{
                                showSnake(binding.llMain,productListResRes.body().getMsg());



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

    private void setProductDetail(ProductModel.ProductDetailsRes productDetailsRes) {
        AppLog.e(TAG, "ProductDetailsRes : " + productDetailsRes);
        if (productDetailsRes.isStatus()) {
            ImageBinding.loadImage(binding.imgSubscription, productDetailsRes.getProduct_image());
            binding.tvProdname.setText(productDetailsRes.getProduct());
            binding.tvOldPrice.setText("Price: ₹ " + productDetailsRes.getRate());
            binding.tvnewPrice.setText("" + productDetailsRes.getDiscounted_rate());
        /*    binding.tvQty.setText(""+productDetailsRes.getQty());
            binding.tvdeliverytime.setText(""+ productDetailsRes.getDelivery_time());
            binding.tvdestext.setText(""+productDetailsRes.getDescription());*/
        }
    }
}
