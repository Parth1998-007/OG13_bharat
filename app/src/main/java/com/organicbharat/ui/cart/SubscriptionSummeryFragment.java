package com.organicbharat.ui.cart;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.api_model.BaseRes;
import com.organicbharat.api_model.CreateSubscriptionOrderModel;
import com.organicbharat.api_model.CreateSummeryModel;
import com.organicbharat.api_model.ProductAvailabilityModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentSubscriptionSummeryBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.address.DeliveryAddress;
import com.organicbharat.ui.my_orders.MyOrders;
import com.organicbharat.utils.AppConstants;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import com.organicbharat.utils.DateTimeHelper;
import com.paytm.pgsdk.Log;
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


/**
 * A simple {@link Fragment} subclass.
 */

public class SubscriptionSummeryFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SubscriptionSummeryFragment";
    FragmentSubscriptionSummeryBinding binding;
    CreateSummeryModel.CreateSummeryReq createSummeryReq;
    String startdate="",enddate="";
    int oddday=0,evenday=0,sun=0,mon=0,tue=0,wed=0,thu=0,fri=0,sat=0;
    String requirement="",prodname="";
    int prodid,qty=0, addressId, cityId;
    CreateSummeryModel.Days days;
    ArrayList<CreateSummeryModel.Item> itemlist;
    CreateSummeryModel.Item item;
    CreateSubscriptionOrderModel.Days daysOrder;
    ArrayList<CreateSubscriptionOrderModel.Item> itemOrderlist;
    CreateSubscriptionOrderModel.Item itemorder;
    ArrayList<ProductModel.Slot> slots= new ArrayList<>();
    String city, locality, area, flatNo, street, landmark, phone, pincode, email;

    public static SubscriptionSummeryFragment newInstance(Bundle bundle) {
        SubscriptionSummeryFragment fragment = new SubscriptionSummeryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_subscription_summery, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription_summery,container,false);

        init();

        //callAvailabilityApi();

        listener();

        return binding.getRoot();
    }

/*    private void callAvailabilityApi() {

        ProductAvailabilityModel.ProductAvailabilityReq productAvailabilityReq =new ProductAvailabilityModel.ProductAvailabilityReq();
        productAvailabilityReq.setProductlist(itemlist);
        productAvailabilityReq.setUser_id(appPref.getUserId());

    }*/

    private void listener() {
        binding.btnPay.setOnClickListener(this);
        binding.btneditAddress.setOnClickListener(this);
        binding.btnAddAddress.setOnClickListener(this);
    }

    private void init() {
        Bundle b = getActivity().getIntent().getExtras();

        Type type = new TypeToken<CreateSummeryModel.CreateSummeryReq>(){}.getType();
        createSummeryReq=new Gson().fromJson(b.getString("createordersummery"),type);
        Type typeslot = new TypeToken<ArrayList<ProductModel.Slot>>(){}.getType();
        //slots = new Gson().fromJson(b.getString("slots"),typeslot);
        slots = new Gson().fromJson(appPref.getString(AppPref.SLOTS),typeslot);
        prodname = b.getString("prodname");
        days = new CreateSummeryModel.Days();
        itemlist = new ArrayList<>();
        itemorder = new CreateSubscriptionOrderModel.Item();
        itemOrderlist = new ArrayList<>();
        daysOrder = new CreateSubscriptionOrderModel.Days();


        Log.e(TAG,"DATA IS..."+itemlist);

        Log.e(TAG,"iTEM ORDER LIST IUS..."+itemOrderlist);

        Log.e(TAG,"iTEM ORDER IS...."+itemorder);

        setspinnetslot();

        setdata(createSummeryReq);

        getsummerydata(createSummeryReq);

    }

    private void setspinnetslot() {
        ArrayList<String> slotdata = new ArrayList<>();
        // String[] slotdata = {"Select Type of ID", "Licence","Voter ID","Adhar Card","ID4","ID5"};

        for (int i=0;i<slots.size();i++){
            slotdata.add(slots.get(i).getTime_slots());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinshiftAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spin_item,  slotdata);

        // Drop down layout style - list view with radio button
        spinshiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        binding.spinnerslot.setAdapter(spinshiftAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        getAddressList();

    }

    private void getAddressList()
    {
        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        apiService.getAddressList(appPref.getUserId())//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AddressModel.AddressListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<AddressModel.AddressListRes> addressListRes) {
                        if (isSuccess(addressListRes)) {
                            AppLog.e(TAG,"addresslisrres: "+addressListRes.body());
                            if(addressListRes.body().isStatus()) {
                                if(addressListRes.body().getDataList().size()>0){
                                    for(int i=0;i<addressListRes.body().getDataList().size();i++){
                                        if(addressListRes.body().getDataList().get(i).getIsdefault() == 1){
                                            binding.btneditAddress.setVisibility(View.VISIBLE);
                                            binding.btnAddAddress.setVisibility(View.GONE);


                                            addressId = addressListRes.body().getDataList().get(i).getAddress_id();
                                            city = addressListRes.body().getDataList().get(i).getCity();
                                            locality = addressListRes.body().getDataList().get(i).getAddress();
                                            area = addressListRes.body().getDataList().get(i).getArea();
                                            flatNo = addressListRes.body().getDataList().get(i).getFlat_no();
                                            street = addressListRes.body().getDataList().get(i).getStreet();
                                            landmark = addressListRes.body().getDataList().get(i).getLandmark();
                                            phone = addressListRes.body().getDataList().get(i).getPhone();
                                            pincode = addressListRes.body().getDataList().get(i).getPincode();
                                            email = addressListRes.body().getDataList().get(i).getEmail();
                                            cityId=addressListRes.body().getDataList().get(i).getCity_id();



                                            AppLog.e(TAG,"DEFAULTADDRESS: "+addressListRes.body().getDataList().get(i).getAddress_id());
                                            appPref.set(AppPref.ADDRESSID,addressListRes.body().getDataList().get(i).getAddress_id());
                                            setaddress(addressListRes.body().getDataList().get(i));
                                            break;
                                        }else{
                                            binding.btnAddAddress.setVisibility(View.VISIBLE);
                                            binding.btneditAddress.setVisibility(View.GONE);
                                        }
                                    }
                                }else {
                                    binding.btnAddAddress.setVisibility(View.VISIBLE);
                                    binding.btneditAddress.setVisibility(View.GONE);
                                    binding.tvAddress.setVisibility(View.GONE);
                                }
                            }else {

                            }
                        }else {

                            if (addressListRes.code()==401){
                                logout();
                            }else{
                                binding.btnAddAddress.setVisibility(View.VISIBLE);
                                binding.btneditAddress.setVisibility(View.GONE);
                                binding.tvAddress.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(TAG,"here "+e.getMessage());
                        onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });
    }

    private void setaddress(AddressModel.Data data) {
        String address = data.getFlat_no()+", "+data.getAddress()+", "+data.getCity()+", "+data.getCity();

        binding.tvAddress.setText(address);
        binding.btnAddAddress.setVisibility(View.GONE);
        binding.btneditAddress.setVisibility(View.VISIBLE);
        binding.tvAddress.setVisibility(View.VISIBLE);
    }

    private void setdata(CreateSummeryModel.CreateSummeryReq createSummeryReq) {
        startdate = createSummeryReq.getStart_date();
        enddate = createSummeryReq.getEnd_date();
        requirement = createSummeryReq.getRequirement();
        itemlist = createSummeryReq.getItemlist();
        prodid = itemlist.get(0).getProd_id();

        if(requirement.equalsIgnoreCase("Daily")){
            binding.llqty.setVisibility(View.VISIBLE);
            binding.llodddayqty.setVisibility(View.GONE);
            binding.llevendayqty.setVisibility(View.GONE);
            binding.llselecteddayqty.setVisibility(View.GONE);
            qty = itemlist.get(0).getQty();
        }else if(requirement.equalsIgnoreCase("Alternate")){
            binding.llodddayqty.setVisibility(View.VISIBLE);
            binding.llevendayqty.setVisibility(View.VISIBLE);
            binding.llqty.setVisibility(View.GONE);
            binding.llselecteddayqty.setVisibility(View.GONE);
            oddday = createSummeryReq.getOdd_day();
            evenday = createSummeryReq.getEven_day();
        }else if(requirement.equalsIgnoreCase("Selected")){
            binding.llselecteddayqty.setVisibility(View.VISIBLE);
            binding.llqty.setVisibility(View.GONE);
            binding.llodddayqty.setVisibility(View.GONE);
            binding.llevendayqty.setVisibility(View.GONE);
            days = createSummeryReq.getDays();
            days = createSummeryReq.getDays();
            AppLog.e(TAG,"days "+createSummeryReq.getDays());
            sun = days.getSun();
            mon = days.getMon();
            tue = days.getTue();
            wed = days.getWed();
            thu = days.getThur();
            fri = days.getFri();
            sat = days.getSat();
        }

        binding.tvProductname.setText(""+prodname);
        binding.tvQty.setText(""+qty);
        binding.tvOrdertype.setText(""+requirement);
        binding.tvOdddayqty.setText(""+oddday);
        binding.tvEvendayqty.setText(""+evenday);
        binding.tvOrderfrom.setText(""+ DateTimeHelper.convertFormat(startdate,"yyyy-mm-dd","dd-mm-yyyy"));
        binding.tvOrderto.setText(""+ DateTimeHelper.convertFormat(enddate,"yyyy-mm-dd","dd-mm-yyyy"));
        binding.tvSunqty.setText(""+sun);
        binding.tvMonqty.setText(""+mon);
        binding.tvTueqty.setText(""+tue);
        binding.tvWedqty.setText(""+wed);
        binding.tvThurqty.setText(""+thu);
        binding.tvFriqty.setText(""+fri);
        binding.tvSatqty.setText(""+sat);

    }

    private void getsummerydata(CreateSummeryModel.CreateSummeryReq createSummeryReq) {

        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        createSummeryReq.setAddress_id(appPref.getInt(AppPref.ADDRESSID));//static now
        AppLog.e(TAG,"createSummeryReq: "+createSummeryReq);
        apiService.createsummery(createSummeryReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CreateSummeryModel.CreateSummeryRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CreateSummeryModel.CreateSummeryRes> createSummeryRes) {
                        if(isSuccess(createSummeryRes)){
                            setsummeryRes(createSummeryRes.body());
                        }else {

                            if (createSummeryRes.code()==401){
                                logout();
                            }else{
                                showSnake(binding.llMain,createSummeryRes.body().getMsg());
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

    private void setsummeryRes(CreateSummeryModel.CreateSummeryRes createSummeryRes) {
        AppLog.e(TAG,"createSummeryRes: "+createSummeryRes);
        if(createSummeryRes.isStatus()){
            AppLog.e(TAG,createSummeryRes.getPayable_amount()+"");
            binding.tvPayable.setText(""+createSummeryRes.getPayable_amount());
            binding.tvDelivery.setText(""+createSummeryRes.getDelivery_charge());
            binding.tvGst.setText(""+createSummeryRes.getGst());
            binding.tvSubtotal.setText(""+createSummeryRes.getSubtotal());
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPay:{
                if(appPref.getInt(AppPref.ADDRESSID) == 0){

                    AppDialog.showConfirmDialog(getActivity(), "Add address first", new AppDialog.AppDialogListener() {
                        @Override
                        public void onOkClick(DialogInterface dialog) {
                            AppPref.cart_address="CartFragment";
                            gotoActivity(DeliveryAddress.class,null,false);
                            dialog.dismiss();
                        }
                    });

                }else {
                    PayViaPaytm(Float.parseFloat(binding.tvPayable.getText().toString()));
                    // createSubscriptionOrder();

                }
                break;
            }
            case R.id.btnAddAddress:
                AppPref.cart_address="CartFragment";
                gotoActivity(DeliveryAddress.class,null,false);
                break;
            case R.id.btneditAddress:
                AppPref.cart_address="CartFragment1";
                Bundle bundleEdit = new Bundle();
                bundleEdit.putBoolean("Cart_Edit",true);
                bundleEdit.putInt("addressId",addressId);
                bundleEdit.putString("locality",locality);
                bundleEdit.putString("flatNo", flatNo);
                bundleEdit.putString("street", street);
                bundleEdit.putString("city", city);
                bundleEdit.putString("landmark", landmark);
                bundleEdit.putString("pincode", pincode);
                bundleEdit.putString("phone",phone);
                bundleEdit.putString("email", email);
                bundleEdit.putInt("cityId",cityId);
                gotoActivity(DeliveryAddress.class, bundleEdit, false);
                break;
        }
    }

    //call create subscripiton order
    private void createSubscriptionOrder(String txnID)
    {

        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        CreateSubscriptionOrderModel.CreateSubscriptionOrderReq createSubscriptionOrderReq = new CreateSubscriptionOrderModel.CreateSubscriptionOrderReq();
        createSubscriptionOrderReq.setAddress_id(appPref.getInt(AppPref.ADDRESSID));//static
        createSubscriptionOrderReq.setUser_id(appPref.getUserId());
        createSubscriptionOrderReq.setRequirement(requirement);
        createSubscriptionOrderReq.setStart_date(startdate);
        createSubscriptionOrderReq.setEnd_date(enddate);
        createSubscriptionOrderReq.setTxnID(txnID);
        createSubscriptionOrderReq.setTotal(binding.tvSubtotal.getText().toString());
        createSubscriptionOrderReq.setDelivery_charge(binding.tvDelivery.getText().toString());
        createSubscriptionOrderReq.setGst(binding.tvGst.getText().toString());
        createSubscriptionOrderReq.setDiscount(binding.tvDiscount.getText().toString());
        createSubscriptionOrderReq.setPromocode(binding.etPromoCode.getText().toString());
        createSubscriptionOrderReq.setPayable_amount(binding.tvPayable.getText().toString());

        createSubscriptionOrderReq.setTime_slot(binding.spinnerslot.getSelectedItem().toString());

        itemorder.setProd_id(prodid);

        if(requirement.equalsIgnoreCase("Daily")){
            itemorder.setQty(qty);
            itemOrderlist.clear();
            itemOrderlist.add(itemorder);
            createSubscriptionOrderReq.setItemlist(itemOrderlist);


        }else if(requirement.equalsIgnoreCase("Alternate")){
            createSubscriptionOrderReq.setOdd_day(oddday);
            createSubscriptionOrderReq.setEven_day(evenday);
            itemOrderlist.clear();
            itemOrderlist.add(itemorder);
            createSubscriptionOrderReq.setItemlist(itemOrderlist);

        }else if(requirement.equalsIgnoreCase("Selected")){
            daysOrder.setSun(sun);
            daysOrder.setMon(mon);
            daysOrder.setTue(tue);
            daysOrder.setWed(wed);
            daysOrder.setThur(thu);
            daysOrder.setFri(fri);
            daysOrder.setSat(sat);
            createSubscriptionOrderReq.setDays(daysOrder);
            itemOrderlist.clear();
            itemOrderlist.add(itemorder);
            createSubscriptionOrderReq.setItemlist(itemOrderlist);

        }

        AppLog.e(TAG,"createSubscriptionOrderReq: "+createSubscriptionOrderReq);
        apiService.createSubscriptionOrder(createSubscriptionOrderReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<BaseRes> baseRes) {
                        if(isSuccess(baseRes)){
                            onCreateSubscriptionOrder(baseRes);
                            //setsummeryRes(baseRes.body());
                        }else {
                         if (baseRes.code()==401){
                             logout();
                         }else{
                             showSnake(binding.llMain,baseRes.body().getMsg());
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

    private void onCreateSubscriptionOrder(Response<BaseRes> baseRes) {

        showSnake(binding.llMain,baseRes.body().getMsg());
        AppLog.e(TAG,"createsubscriptionOrderRES: "+baseRes.body().getMsg());
        sub_script=1;
        gotoActivity(MyOrders.class,null,true);
    }



    //Paytm gateway start
    public void PayViaPaytm(float amount)
    {
        generateCheckSum(amount);
    }


    //Generate checksumdata
    private void generateCheckSum(float amount)
    {
        String url = AppConstants.URL_FOR_PAYTM ;
        new GenerateChecksum(amount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
    }


    //Paytm Payment Gateway set data generate payments
    public class GenerateChecksum extends AsyncTask<String, Void, String>
    {

        GenerateChecksum(float amount)
        {
            this.amount = amount;
        }

        String server_response;
        private String orderId = "";
        private float amount ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            AppLog.e(TAG, "onPreExecute called");
            //showLoading();
        }

        //Background processes
        @Override
        protected String doInBackground(String... strings) {
            AppLog.e("CHECK_ON_ACT_RES", "doInBackground called");
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                AppLog.e(TAG,"url : "+strings[0]);
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
                    obj.put("CUST_ID", appPref.getInt(AppPref.USER_ID)+"");
                    obj.put("CHANNEL_ID", "WAP");
                    obj.put("INDUSTRY_TYPE_ID", "Retail");
                    obj.put("WEBSITE", AppConstants.PAYTM_WEBSITE);
                    obj.put("CALLBACK_URL", AppConstants.PAYTM_CALLBACK+  orderId);
                    //obj.put("CALLBACK_URL", "http://frenzinsoftwares.in/foodapp/Paytm_v1/verifyChecksum.php");
                    obj.put("TXN_AMOUNT", String.valueOf(amount));
                    //obj.put("PAYMENT_TYPE_ID", "DC");

                    wr.writeBytes(obj.toString());
                    AppLog.e(TAG,"send Data : "+obj);
                    wr.flush();
                    wr.close();
                }
                catch (JSONException ex)
                {
                    ex.printStackTrace();
                }
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
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
            AppLog.e(TAG,"Checksum Response" + server_response);

            // hideLoading();
            String checksumhash = "";

            if (server_response != null) {
                try {

                    JSONObject jsonObject = new JSONObject(server_response);
                    try {
                        checksumhash = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                        AppLog.e("CheckSum result >>", checksumhash);
                        callPaytm(orderId , amount , checksumhash);
                    } catch (JSONException e) {
                        AppLog.e(TAG,""+e.toString());
                    }

                } catch (Exception e) {
                    AppLog.e(TAG,""+e.toString());
                }
            }  else {
                showSnake(binding.llMain,"Problem Occurred , Please Try Again!");
            }
        }
    }

    //After paytm configuration Call Paytm method that is set necessory data
    private void callPaytm(final String orderId , float amount , String checksumhash) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        //PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put("MID", AppConstants.PAYTM_MID);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", appPref.getInt(AppPref.USER_ID)+"");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", AppConstants.PAYTM_WEBSITE);
        paramMap.put("CALLBACK_URL", AppConstants.PAYTM_CALLBACK+orderId);
        //paramMap.put("CALLBACK_URL", "http://frenzinsoftwares.in/foodapp/Paytm_v1/verifyChecksum.php");
        paramMap.put("TXN_AMOUNT", String.valueOf(amount));
        //paramMap.put("PAYMENT_TYPE_ID", "DC");
        paramMap.put("CHECKSUMHASH", checksumhash);

        AppLog.e(TAG,"send Data Paytm : "+new Gson().toJson(paramMap));

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        Service.enableLog(getActivity());

        Service.startPaymentTransaction(getActivity(), true, false, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
                AppLog.e(TAG,"onTransactionResponse : "+inResponse.toString());

                if(inResponse.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS"))
                {
                    String TXN_ORDERID = orderId;
                    String TXNID = inResponse.getString("TXNID");
                    String BANKTXNID = inResponse.containsKey("BANKTXNID")?inResponse.getString("BANKTXNID"):"";

                    createSubscriptionOrder(TXNID);

                }
                else
                {
                    showSnake(binding.llMain,"Payment Failed , Please Try Again");

                }
            }

            @Override
            public void networkNotAvailable() {
                AppLog.e(TAG,"networkNotAvailable : ");
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                AppLog.e(TAG,"clientAuthenticationFailed : "+inErrorMessage);
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                AppLog.e(TAG,"someUIErrorOccurred : "+inErrorMessage);
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                AppLog.e(TAG,"onErrorLoadingWebPage : "+inErrorMessage);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                AppLog.e(TAG,"onBackPressedCancelTransaction : ");
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                AppLog.e(TAG,"onTransactionCancel : "+inResponse.toString());
            }
        });
    }


}
