package com.organicbharat.ui.cart;

import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.CreateOrderModel;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.api_model.ProductAvailabilityModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.api_model.RepeatOrder;
import com.organicbharat.databinding.FragmentOrderSummeryBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.home.Home;
import com.organicbharat.ui.home.HomeFragment;
import com.organicbharat.ui.my_orders.MyOrders;
import com.organicbharat.utils.AppConstants;

import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SummeryFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SummeryFragment";
    FragmentOrderSummeryBinding binding;
    OrderSummeryAdapter orderSummeryAdapter;
    OrderSummerErrorAdapter ordererrorSummeryAdapter;
    Calendar calendar;

    ArrayList<ProductAvailabilityModel.Products> productsList = new ArrayList<>();

    String action = "";
    int orderid = 0;
    ArrayList<ProductModel.Slot> slots = new ArrayList<>();

    public static SummeryFragment newInstance(Bundle bundle) {
        SummeryFragment fragment = new SummeryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        int productid = appPref.getInt("productId");
        int prodQty =appPref.getInt("productQty");

        Log.e(TAG, "PRODUCT ID IS "+productid+"..Product qty..."+prodQty );
*/


        calendar = Calendar.getInstance();

        Bundle b = getActivity().getIntent().getExtras();
        if (b != null) {
            action = b.getString("action");
            orderid = b.getInt("orderid");
        }
        Type typeslot = new TypeToken<ArrayList<ProductModel.Slot>>() {
        }.getType();
        //slots = new Gson().fromJson(b.getString("slots"),typeslot);
        slots = new Gson().fromJson(appPref.getString(AppPref.SLOTS), typeslot);

        orderSummeryAdapter = new OrderSummeryAdapter();
        ordererrorSummeryAdapter = new OrderSummerErrorAdapter();
    }

    //Repeat order api
    private void callrepealOrderAPI() {

        Log.e(TAG, "callrepealOrderAPI: ");

        if (!isOnline()) {
            showSnake(binding.rlMain, "Internet Not Available , Please Try Again");
            return;
        }

        RepeatOrder.RepeatOrderReq repeatOrderReq = new RepeatOrder.RepeatOrderReq();
        repeatOrderReq.setOrder_id(orderid);
        repeatOrderReq.setUser_id(appPref.getUserId());

        apiService.repeatOrder(repeatOrderReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<RepeatOrder.RepeatOrderRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<RepeatOrder.RepeatOrderRes> repeatOrderRes) {
                        if (isSuccess(repeatOrderRes)) {
                            Log.e(TAG, "DATA IS..." + repeatOrderRes.body());
                            setrepeatOrderDetails(repeatOrderRes.body());
                        } else {
                            if (repeatOrderRes.code() == 401) {
                                logout();
                            } else {
                                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_alert);

                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                Button btok = dialog.findViewById(R.id.btnOk);
                                TextView tvMsg = dialog.findViewById(R.id.tvItemName);
                                TextView tvTitle = dialog.findViewById(R.id.tvconfirm);

                                tvMsg.setText("Product is Out Of Stock!");
                                tvTitle.setText("Alert");
                                btok.setText("Ok");
                                btok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gotoActivity(Home.class, null, false);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
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

    private void setrepeatOrderDetails(RepeatOrder.RepeatOrderRes repeatOrderRes) {
        if (repeatOrderRes.isStatus()) {
            binding.tvSubtotal.setText("" + repeatOrderRes.getPayable());

            int minamount = Integer.parseInt(appPref.getString(AppPref.MINAMOUNT));
            if (repeatOrderRes.getPayable() >= minamount) {
                binding.tvDelivery.setText("" + 0);
            } else {
                binding.tvDelivery.setText(" " + appPref.getString(AppPref.DELIVERYCHARGE));
            }


            binding.tvGst.setText(" " + repeatOrderRes.getGst());

            Double payamonut = Double.parseDouble(binding.tvSubtotal.getText().toString()) +
                    Double.parseDouble(binding.tvDelivery.getText().toString()) +
                    Double.parseDouble(binding.tvGst.getText().toString()) +
                    Double.parseDouble(binding.tvDicount.getText().toString());

            binding.tvPayable.setText(" " + payamonut);

            if (payamonut == 0) {
                binding.btnPay.setVisibility(View.GONE);
            }


            if (repeatOrderRes.getDatalist().size() > 0) {
                orderSummeryAdapter.clear();
                orderSummeryAdapter.addCartItem(repeatOrderRes.getDatalist());
            }
            if (repeatOrderRes.getErrorItemList().size() > 0) {
                binding.llToperror.setVisibility(View.VISIBLE);
                binding.rverrorSummery.setVisibility(View.VISIBLE);
                ordererrorSummeryAdapter.clear();
                ordererrorSummeryAdapter.addCartItem(repeatOrderRes.getErrorItemList());
            } else {
                binding.llToperror.setVisibility(View.GONE);
                binding.rverrorSummery.setVisibility(View.GONE);
            }
        } else {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summery, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {

        binding.rvSummery.setAdapter(orderSummeryAdapter);
        binding.rverrorSummery.setAdapter(ordererrorSummeryAdapter);


        binding.tvPayable.setVisibility(View.VISIBLE);
        binding.btnPay.setOnClickListener(this);

        Bundle b = getActivity().getIntent().getExtras();
        Type typeslot = new TypeToken<ArrayList<ProductModel.Slot>>() {
        }.getType();
        //slots = new Gson().fromJson(b.getString("slots"),typeslot);

        AppLog.e(TAG, "slotsdata: " + new Gson().fromJson(appPref.getString(AppPref.SLOTS), typeslot));
        slots = new Gson().fromJson(appPref.getString(AppPref.SLOTS), typeslot);
        setspinnetslot();

        if (action != null && action.equalsIgnoreCase("repeatOrder")) {
            binding.rverrorSummery.setVisibility(View.GONE);
            binding.llToperror.setVisibility(View.GONE);
            callrepealOrderAPI();
        } else {
            binding.rverrorSummery.setVisibility(View.GONE);
            binding.llToperror.setVisibility(View.GONE);
            getMyCartItemList();
        }

    }

    //Check Product is having or not
    private void callavailability() {

        final ProductAvailabilityModel.ProductAvailabilityReq productAvailabilityReq = new ProductAvailabilityModel.ProductAvailabilityReq();
        productAvailabilityReq.setProductlist(productsList);
        productAvailabilityReq.setUser_id(appPref.getUserId());

        Log.e(TAG, "Availability Req: " + productAvailabilityReq);


        apiService.checkAvailability(productAvailabilityReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ProductAvailabilityModel.ProductAvailabilityRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ProductAvailabilityModel.ProductAvailabilityRes> productAvailabilityResResponse) {
                        if (isSuccess(productAvailabilityResResponse)) {
                            Log.e(TAG, "Availability Response is: " + productAvailabilityResResponse.body());

                            getProductId(productAvailabilityResResponse.body());
                        } else {
                            if (productAvailabilityResResponse.code() == 401) {
                                logout();
                            } else {
                                productAvailabilityResResponse.body().getMsg();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        showSnake(binding.rlMain, "Error is :" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });

    }

    private void getProductId(ProductAvailabilityModel.ProductAvailabilityRes productAvailabilityRes) {

        Log.e(TAG, "getProductId: " + productAvailabilityRes.getProductsResponseList().size());

        if (productAvailabilityRes.getProductsResponseList().size() > 0) {

            showSnake(binding.rlMain, "Product out of stoke");

        } else {

            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            int year = calendar.get(Calendar.YEAR);

            PayViaPaytm(Float.parseFloat(binding.tvPayable.getText().toString()));
               /* gotoActivity(MyOrders.class,null,true);
                getActivity().finish();*/

        }
    }

    //Time Slots data add
    private void setspinnetslot() {
        ArrayList<String> slotdata = new ArrayList<>();
        // String[] slotdata = {"Select Type of ID", "Licence","Voter ID","Adhar Card","ID4","ID5"};
        //slotdata.add("choose time solt");
        for (int i = 0; i < slots.size(); i++) {
            slotdata.add(slots.get(i).getTime_slots());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinshiftAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spin_item, slotdata);

        // Drop down layout style - list view with radio button
        spinshiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        binding.spinnerslot.setAdapter(spinshiftAdapter);
    }


    public void getMyCartItemList() {
        if (!isOnline()) {
            showSnake(binding.rlMain, "Internet Not Available , Please Try Again");
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
                            setcartItem(myCartListRes.body());
                        } else {
                            if (myCartListRes.code() == 401) {
                                logout();
                            } else {
                                showSnake(binding.rlMain, myCartListRes.body().getMsg());
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

    private void setcartItem(MyCartModel.MyCartModelRes myCartModelRes) {
        AppLog.e(TAG, "MyCartItemlistRes : " + myCartModelRes);
        if (myCartModelRes.isStatus()) {
            binding.tvSubtotal.setText("" + myCartModelRes.getPayable());
            int minamount = Integer.parseInt(appPref.getString(AppPref.MINAMOUNT));
            if (myCartModelRes.getPayable() >= minamount) {
                binding.tvDelivery.setText("" + 0);
            } else {
                binding.tvDelivery.setText(" " + appPref.getString(AppPref.DELIVERYCHARGE));
            }
            binding.tvGst.setText(" " + myCartModelRes.getGst());

            Double payamonut = Double.parseDouble(binding.tvSubtotal.getText().toString()) +
                    Double.parseDouble(binding.tvDelivery.getText().toString()) +
                    Double.parseDouble(binding.tvGst.getText().toString()) +
                    Double.parseDouble(binding.tvDicount.getText().toString());

            binding.tvPayable.setText(" " + payamonut);
            if (myCartModelRes.getDatlist().size() > 0) {
                orderSummeryAdapter.clear();
                orderSummeryAdapter.addCartItem(myCartModelRes.getDatlist());

                for (int i = 0; i < myCartModelRes.getDatlist().size(); i++) {

                    ProductAvailabilityModel.Products products = new ProductAvailabilityModel.Products();
                    products.setId(myCartModelRes.getDatlist().get(i).getProduct_id());
                    products.setQty(myCartModelRes.getDatlist().get(i).getQty());

                    productsList.add(products);
                }

            }
            // callavailability();
        } else {

            showSnake(binding.rlMain, myCartModelRes.getMsg());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPay:
                callavailability();
                break;
        }
    }

    private void createOrder(String date, String txnId) {

        if (!isOnline()) {
            showSnake(binding.rlMain, "Internet Not Available , Please Try Again");
            return;
        }

        final CreateOrderModel.CreateOrderReq createOrderReq = new CreateOrderModel.CreateOrderReq();
        createOrderReq.setUser_id(appPref.getUserId());
        createOrderReq.setAddress_id(appPref.getInt(AppPref.ADDRESSID));
        createOrderReq.setTotal("" + binding.tvSubtotal.getText().toString());
        createOrderReq.setDelivery_charge(binding.tvDelivery.getText().toString());
        createOrderReq.setDiscount("" + binding.tvDicount.getText().toString());
        createOrderReq.setGst("" + binding.tvGst.getText().toString());
        createOrderReq.setPayable_amount("" + binding.tvPayable.getText().toString());
        createOrderReq.setPromocode("");
        createOrderReq.setTxnId(txnId);
        createOrderReq.setTime_slot(binding.spinnerslot.getSelectedItem().toString());

        AppLog.e(TAG, "careteOrderReq: " + createOrderReq);

        apiService.createOrder(createOrderReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CreateOrderModel.CreateOrderRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CreateOrderModel.CreateOrderRes> createOrderRes) {
                        if (isSuccess(createOrderRes)) {
                            onCreateOrderRes(createOrderRes.body());

                        } else {

                            if (createOrderRes.code() == 401) {
                                logout();
                            } else {
                                showSnake(binding.rlMain, createOrderRes.message());
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

    private void onCreateOrderRes(CreateOrderModel.CreateOrderRes createOrderRes) {
        AppLog.e(TAG, "createOrderRes : " + createOrderRes);
        if (createOrderRes.isStatus()) {
            appPref.set(AppPref.ITEMINCART, 0);
            showSnake(binding.rlMain, createOrderRes.getMsg());
            gotoActivity(MyOrders.class, null, true);
        }
    }

/*
    private class OrderSummeryAdapter extends RecyclerView.Adapter<OrderSummeryAdapter.ViewHolder>{

        @NonNull
        @Override
        public OrderSummeryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summery,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderSummeryAdapter.ViewHolder viewHolder, int position) {

        }

        @Override
        public int getItemCount() {
            return 7;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
*/


    //pay via paytm and generates checksum data
    public void PayViaPaytm(float amount) {
        generateCheckSum(amount);
    }


    private void generateCheckSum(float amount) {
        String url = AppConstants.URL_FOR_PAYTM;
        new GenerateChecksum(amount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }


    public class GenerateChecksum extends AsyncTask<String, Void, String> {

        GenerateChecksum(float amount) {
            this.amount = amount;
        }

        String server_response;
        private String orderId = "";
        private float amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AppLog.e(TAG, "onPreExecute called");
            //showLoading();
        }

        //Background process
        @Override
        protected String doInBackground(String... strings) {
            AppLog.e("CHECK_ON_ACT_RES", "doInBackground called");
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                AppLog.e(TAG, "url : " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("Authorization", sessionManager.getApiKey());
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                try {
                    JSONObject obj = new JSONObject();
                    orderId = "ob_" + Calendar.getInstance().getTimeInMillis();
                    // Live account details
                    obj.put("MID", AppConstants.PAYTM_MID);
                    obj.put("ORDER_ID", orderId);
                    obj.put("CUST_ID", appPref.getInt(AppPref.USER_ID) + "");
                    obj.put("CHANNEL_ID", "WAP");
                    obj.put("INDUSTRY_TYPE_ID", "Retail");
                    obj.put("WEBSITE", AppConstants.PAYTM_WEBSITE);
                    obj.put("CALLBACK_URL", AppConstants.PAYTM_CALLBACK + orderId);
                    //obj.put("CALLBACK_URL", "http://frenzinsoftwares.in/foodapp/Paytm_v1/verifyChecksum.php");
                    obj.put("TXN_AMOUNT", String.valueOf(amount));
                    //obj.put("PAYMENT_TYPE_ID", "DC");

                    wr.writeBytes(obj.toString());
                    AppLog.e(TAG, "send Data : " + obj);
                    wr.flush();
                    wr.close();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = AppConstants.readStream(urlConnection.getInputStream());
                    AppLog.v("CatalogClient", server_response);
                    //response = new JSONArray(responseString);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppLog.e(TAG, "onPostExecute called");
            AppLog.e(TAG, "Checksum Response" + server_response);

            // hideLoading();
            String checksumhash = "";

            if (server_response != null) {
                try {

                    JSONObject jsonObject = new JSONObject(server_response);
                    try {
                        checksumhash = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                        AppLog.e("CheckSum result >>", checksumhash);
                        callPaytm(orderId, amount, checksumhash);
                    } catch (JSONException e) {
                        AppLog.e(TAG, "" + e.toString());
                    }

                } catch (Exception e) {
                    AppLog.e(TAG, "" + e.toString());
                }
            } else {
                showSnake(binding.rlMain, "Problem Occurred , Please Try Again!");
            }
        }
    }

    //After Generate checsum call paytm method
    private void callPaytm(final String orderId, float amount, String checksumhash) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        //PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", AppConstants.PAYTM_MID);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", appPref.getInt(AppPref.USER_ID) + "");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", AppConstants.PAYTM_WEBSITE);
        paramMap.put("CALLBACK_URL", AppConstants.PAYTM_CALLBACK + orderId);
        //paramMap.put("CALLBACK_URL", "http://frenzinsoftwares.in/foodapp/Paytm_v1/verifyChecksum.php");
        paramMap.put("TXN_AMOUNT", String.valueOf(amount));
        //paramMap.put("PAYMENT_TYPE_ID", "DC");
        paramMap.put("CHECKSUMHASH", checksumhash);

        AppLog.e(TAG, "send Data Paytm : " + new Gson().toJson(paramMap));

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        Service.enableLog(getActivity());

        Service.startPaymentTransaction(getActivity(), true, false, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
                AppLog.e(TAG, "onTransactionResponse : " + inResponse.toString());

                if (inResponse.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS")) {
                    String TXN_ORDERID = orderId;
                    String TXNID = inResponse.getString("TXNID");
                    String BANKTXNID = inResponse.containsKey("BANKTXNID") ? inResponse.getString("BANKTXNID") : "";

                    int month = calendar.get(Calendar.MONTH) + 1;
                    int date = calendar.get(Calendar.DATE);
                    int year = calendar.get(Calendar.YEAR);

                    createOrder("" + date + "/" + month + "/" + year, TXNID);

                } else {

                    showSnake(binding.rlMain, "Please try again");

                }
            }

            @Override
            public void networkNotAvailable() {
                AppLog.e(TAG, "networkNotAvailable : ");
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                AppLog.e(TAG, "clientAuthenticationFailed : " + inErrorMessage);
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                AppLog.e(TAG, "someUIErrorOccurred : " + inErrorMessage);
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                AppLog.e(TAG, "onErrorLoadingWebPage : " + inErrorMessage);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                AppLog.e(TAG, "onBackPressedCancelTransaction : ");
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                AppLog.e(TAG, "onTransactionCancel : " + inResponse.toString());
            }
        });
    }

}
