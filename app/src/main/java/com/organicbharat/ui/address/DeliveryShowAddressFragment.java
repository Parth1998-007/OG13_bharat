package com.organicbharat.ui.address;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.api_model.AreaListModel;
import com.organicbharat.api_model.BaseRes;
import com.organicbharat.api_model.CityListModel;
import com.organicbharat.databinding.FragmentDeliveryShowAddressBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.cart.Cart;
import com.organicbharat.ui.cart.SubscriptionSummery;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import com.organicbharat.utils.FindAddress;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class DeliveryShowAddressFragment extends BaseFragment implements View.OnClickListener, FindAddress.FindAddressListener {

    private static final String TAG = "DeliveryShowAddressFragment";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 12;
    String lat, lang;
    FragmentDeliveryShowAddressBinding binding;
    int addressid;
    AddressModel addressModel;
    String city, locality, area, flatNo, street, landmark, phone, pin, email;
    boolean from_cart;
    int cityId;
    FindAddress findAddress;
    Address selAddress;
    ArrayList<CityListModel.CityList> cityLists = new ArrayList<>();
    ArrayList<AreaListModel.AreaList> areaLists = new ArrayList<>();
    int cityid = 0;
    String pincode = "";
    String cityofaddress = "", pinocdeofaddress = "";
    boolean ispincodematch = false;
    boolean iscitymatch = false;
    private AddressModel.Data deleverdAddress;
    //GetLocation getLocation;

    public DeliveryShowAddressFragment() {
        // Required empty public constructor
    }

    public static DeliveryShowAddressFragment newInstance(Bundle bundle) {
        DeliveryShowAddressFragment fragment = new DeliveryShowAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    void cartAddressBundle() {


        Bundle bundleEdit = getArguments();

        if (getArguments() != null) {
            //searchAddress();
            binding.tvetcityeerror.setVisibility(View.GONE);
            binding.tvetPincodeerror.setVisibility(View.GONE);
            iscitymatch = true;
            ispincodematch = true;
            addressid = bundleEdit.getInt("addressId");
            locality = bundleEdit.getString("locality");
            flatNo = bundleEdit.getString("flatNo");
            street = bundleEdit.getString("street");
            city = bundleEdit.getString("city");
            landmark = bundleEdit.getString("landmark");
            pin = bundleEdit.getString("pincode");
            phone = bundleEdit.getString("phone");
            email = bundleEdit.getString("email");
            from_cart = bundleEdit.getBoolean("Cart_Edit");
            cityId = bundleEdit.getInt("cityId");
            Log.e("DELIVERY SHOW ADDRESS", "onClick: " + "locality :" + locality + " flatNo :" + flatNo + " street :" + street + " city :" + city + "landmark :" + landmark
                    + " pincode :" + pincode + " phone :" + pincode + " phone :" + phone + " email :" + email);

            binding.etAddress.setText(locality);
            binding.etSociety.setText(street);
            binding.etCity.setText(city);
            binding.etLandmark.setText(landmark);
            binding.etPincode.setText(pin);
            binding.etContact.setText(phone);
            binding.etemail.setText(email);
        }
    }

    void addressBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            searchAddress();
        }

        Type type = new TypeToken<AddressModel.Data>() {
        }.getType();
        deleverdAddress = new Gson().fromJson(bundle.getString("list_address"), type);
        AppLog.e(TAG, deleverdAddress.getStreet() + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_show_address, container, false);

        init();


        DeliveryAddress.view.setVisibility(View.GONE);
        binding.btnProceed.setOnClickListener(this);
        binding.btEdit.setOnClickListener(this);
        binding.btSave.setOnClickListener(this);
        binding.etAddress.setOnClickListener(this);
        binding.etAddress.requestFocus();
        if (DeliveryAddress.addAddress == 1) {
            binding.etAddress.setEnabled(true);
            binding.etflatNo.setEnabled(true);
            binding.etSociety.setEnabled(true);
            binding.etCity.setEnabled(true);
            binding.etLandmark.setEnabled(true);
            binding.etemail.setEnabled(true);
            binding.etPincode.setEnabled(true);

            binding.btEdit.setVisibility(View.GONE);
            binding.btnProceed.setVisibility(View.GONE);
        } else {

            if (getArguments().getBoolean("Cart_Edit")) {

                cartAddressBundle();
            } else {

                addressBundle();
                getAddressData();
                binding.btEdit.setVisibility(View.GONE);
                binding.btnProceed.setVisibility(View.VISIBLE);
                DeliveryAddress.view.setVisibility(View.GONE);
            }

//            binding.etemail.setText("" + appPref.getString(AppPref.EMAIL));

        }
        if (AppPref.cart_address.equalsIgnoreCase("CartFragment")) {
            DeliveryAddress.view.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }

    private void init() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean("NewAddress")) {
                searchAddress();
            }
        }

        binding.etContact.setText(appPref.getString(AppPref.PHONE));


        binding.etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iscitymatch = false;
                AppLog.e(TAG, "citydata: " + s);


                for (int i = 0; i < cityLists.size(); i++) {
                    if (s.toString().equalsIgnoreCase(cityLists.get(i).getCity())) {
                        cityid = cityLists.get(i).getCity_id();
                        AppLog.e(TAG, "cityid: " + cityid);
                        getArelist(cityid);
                        iscitymatch = true;
                        break;
                        //binding.etPincode.setText(""+pinocdeofaddress);
                        // binding.tvetcityeerror.setVisibility(View.GONE);
                    } else {
                        //binding.tvetcityeerror.setVisibility(View.VISIBLE);
                    }
                }
                if (iscitymatch)//cityid > 0
                {
                    //getArelist(cityid);
                    binding.tvetcityeerror.setVisibility(View.GONE);

                } else {
                    binding.tvetcityeerror.setVisibility(View.VISIBLE);
                    binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AppLog.e(TAG, "pincodedata: " + s);
               /* for (int i = 0; i < cityLists.size(); i++) {
                    if (s.toString().equalsIgnoreCase(cityLists.get(i).getCity())) {
                        cityid = cityLists.get(i).getCity_id();
                        AppLog.e(TAG, "cityid: " + cityid);
                        getArelist(cityid);
                        binding.tvetcityeerror.setVisibility(View.GONE);
                    }else {
                        binding.tvetcityeerror.setVisibility(View.VISIBLE);
                    }
                }*/
                ispincodematch = false;
                AppLog.e(TAG, "pincodearraysize: " + areaLists.size());

                for (int i = 0; i < areaLists.size(); i++) {
                    if (s.toString().equalsIgnoreCase(areaLists.get(i).getPincode())) {
                        ispincodematch = true;
                        pincode = areaLists.get(i).getPincode();
                        AppLog.e(TAG, "pincode: " + pincode + " : " + areaLists.get(i).getPincode());
                        break;
                        //binding.tvetPincodeerror.setVisibility(View.GONE);
                    } else {
                        ispincodematch = false;
                        AppLog.e(TAG, "pincodehere: " + pincode + " : " + areaLists.get(i).getPincode());
                        // binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                    }

                }
                if (ispincodematch)//pincode.length()>0
                {
                    AppLog.e(TAG, "ispincodematch1: " + ispincodematch);
                    binding.tvetPincodeerror.setVisibility(View.GONE);
                } else {
                    AppLog.e(TAG, "ispincodematch2: " + ispincodematch);
                    binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*for (int i = 0; i < areaLists.size(); i++) {
                    if (s.toString().equalsIgnoreCase(areaLists.get(i).getPincode())) {
                        pincode = areaLists.get(i).getPincode();
                        AppLog.e(TAG, "pincode: " + pincode);
                        binding.tvetPincodeerror.setVisibility(View.GONE);
                    }else {
                        AppLog.e(TAG, "pincode: " + pincode);
                        binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                    }

                }*/
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        getCityList();


        //getArelist(cityid);
    }


    //call city list api
    private void getCityList() {
        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }


        apiService.getCityList()//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CityListModel.CityListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CityListModel.CityListRes> cityListRes) {
                        if (isSuccess(cityListRes)) {
                            if (cityListRes.body().isStatus()) {

                                AppLog.e(TAG, "citylistRes: " + cityListRes.body());
                                cityLists = cityListRes.body().getCityList();

                                if (addressid > 0) {
                                    String str = binding.etCity.getText().toString();
                                    binding.etCity.setText("");
                                    binding.etCity.setText(str);
                                }


                                // binding.etCity.setText(""+cityofaddress);
                            }
                        } else {
                            if (cityListRes.code() == 401) {
                                logout();
                            } else {
                                showToast(cityListRes.body().getMsg());
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


    //call area list api
    private void getArelist(int cityid) {
        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }


        apiService.getAreaList(cityid)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AreaListModel.AreaListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<AreaListModel.AreaListRes> areaistRes) {
                        if (isSuccess(areaistRes)) {
                            if (areaistRes.body().isStatus()) {
                                areaLists = areaistRes.body().getAreaList();
                                AppLog.e(TAG, "arealistRes: " + areaistRes.body());

                                if (addressid > 0) {
                                    String str = binding.etPincode.getText().toString();
                                    binding.etPincode.setText("");
                                    binding.etPincode.setText(str);
                                }
                                if (iscitymatch) {
                                    ispincodematch = false;
                                    AppLog.e(TAG, "pincodearraysize: " + areaLists.size());
                                    for (int i = 0; i < areaLists.size(); i++) {
                                        if (binding.etPincode.getText().toString().equalsIgnoreCase(areaLists.get(i).getPincode())) {
                                            ispincodematch = true;
                                            pincode = areaLists.get(i).getPincode();
                                            AppLog.e(TAG, "pincode: " + pincode + " : " + areaLists.get(i).getPincode());
                                            break;
                                            //binding.tvetPincodeerror.setVisibility(View.GONE);
                                        } else {
                                            AppLog.e(TAG, "pincodehere: " + pincode + " : " + areaLists.get(i).getPincode());
                                            // binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    if (ispincodematch)//pincode.length()>0
                                    {
                                        AppLog.e(TAG, "ispincodematch1: " + ispincodematch);
                                        binding.tvetPincodeerror.setVisibility(View.GONE);
                                    } else {
                                        AppLog.e(TAG, "ispincodematch2: " + ispincodematch);
                                        binding.tvetPincodeerror.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else {


                            if (areaistRes.code() == 401) {
                                logout();
                            } else {

                                showSnake(binding.scheduleOrderFragment, areaistRes.body().getMsg());
                                /*AppDialog.showConfirmDialog(getActivity(), areaistRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });*/

                                //  showToast(areaistRes.body().getMsg());
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

//    private void getAddressList() {
//        apiService.getAddressList(appPref.getUserId())//appPref.getUserId()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<AddressModel.AddressListRes>>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        subscribe(d);
//                    }
//
//                    @Override
//                    public void onNext(Response<AddressModel.AddressListRes> addressListRes) {
//                        if (isSuccess(addressListRes)) {
//                            setaddress(addressListRes.body());
//                        } else {
//                            binding.btEdit.setVisibility(View.GONE);
//
//                            binding.etAddress.setEnabled(true);
//                            binding.etflatNo.setEnabled(true);
//                            binding.etSociety.setEnabled(true);
//                            binding.etCity.setEnabled(true);
//                            binding.etLandmark.setEnabled(true);
//                            binding.etPincode.setEnabled(true);
//                            binding.etemail.setEnabled(true);
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        onFailure(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        onDone();
//                    }
//                });
//    }

    //getting address data
    private void getAddressData() {

        pinocdeofaddress = deleverdAddress.getPincode();
        addressid = deleverdAddress.getAddress_id();
        binding.btEdit.setVisibility(View.GONE);
        binding.etContact.setText(deleverdAddress.getPhone());
        binding.etAddress.setText(deleverdAddress.getAddress());
        binding.etflatNo.setText(deleverdAddress.getFlat_no());
        binding.etSociety.setText(deleverdAddress.getStreet());
        binding.etCity.setText(deleverdAddress.getCity());
        binding.etLandmark.setText(deleverdAddress.getLandmark());
        binding.etemail.setText(deleverdAddress.getEmail());
        binding.etPincode.setText(deleverdAddress.getPincode());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProceed: {

                //activity address list api call
                ((DeliveryAddress) getActivity()).getAddressList();

                if (from_cart) {
                    this.getActivity().finish();
                    //gotoActivity(Cart.class,null,true);
                }
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            }
            case R.id.bt_edit: {
                binding.etAddress.setEnabled(true);
                binding.etflatNo.setEnabled(true);
                binding.etSociety.setEnabled(true);
                binding.etCity.setEnabled(true);
                binding.etLandmark.setEnabled(true);
                binding.etemail.setEnabled(true);
                binding.etContact.setEnabled(true);
                binding.etPincode.setEnabled(true);

                break;
            }
            case R.id.bt_save: {

                if (DeliveryAddress.addAddress == 1) {
                    if (iscitymatch && ispincodematch) {

                        if (binding.etContact.getText().toString().length() == 0) {
                            showSnake(binding.scheduleOrderFragment, "Add Contact");
                            //showToast("Add Contact");
                        } else if (binding.etemail.getText().toString().length() == 0) {

                            showSnake(binding.scheduleOrderFragment, "Add Email Address");
                            //   showToast("Add email");
                        } else {
                            addaddressApi();
                        }
                    } else {
                        if (binding.etAddress.getText().toString().equalsIgnoreCase("")) {
                            showSnake(binding.scheduleOrderFragment, "Please fill the Informations.");
                        } else {

                            showSnake(binding.scheduleOrderFragment, "Your city or pincode may be not match!");
                        }
                    }

                } else {
                    if (addressid > 0) {
                        if (iscitymatch && ispincodematch) {
                            if (binding.etContact.getText().toString().length() == 0) {
                                showSnake(binding.scheduleOrderFragment, "Add Contact");
                                //showToast("Add contact");
                            } else if (binding.etemail.getText().toString().length() == 0) {
                                showSnake(binding.etemail, "Add Email Address");
                            } else {
                                updateaddressApi();
                            }
                        } else {
                            showSnake(binding.etemail, "Something went wrong!");
                        }
                    } else {
                        if (iscitymatch && ispincodematch) {
                            if (binding.etemail.getText().toString().length() > 0)
                                addaddressApi();
                            else
                                showSnake(binding.etemail, "Add Email Address");
                        } else {
                            if (binding.etAddress.getText().toString().equalsIgnoreCase("")) {
                                showSnake(binding.scheduleOrderFragment, "Please fill the Information");
                            } else {
                                showSnake(binding.etemail, "Your pincode or City may be not match!");
                            }
                        }

                    }
                }
                break;
            }
            case R.id.etAddress: {
                searchAddress();
                break;
            }
        }
    }

    //call update address api
    private void updateaddressApi() {

        if (!isOnline()) {
            showSnake(binding.etemail, "Internet Not Available , Please Try Again");
            //  showToast("Internet Not Available , Please Try Again");
            return;
        }

        AddressModel.EditAddressReq editAddressReq = new AddressModel.EditAddressReq();
        editAddressReq.setUser_id(appPref.getUserId());

        if (from_cart) {
            editAddressReq.setAddress_id(String.valueOf(addressid));
        } else {
            editAddressReq.setAddress_id(String.valueOf(deleverdAddress.getAddress_id()));
        }

        editAddressReq.setCity(cityid);//binding.etCity.getText().toString()
        editAddressReq.setPhone(binding.etContact.getText().toString());
        editAddressReq.setArea("");
        editAddressReq.setStreet(binding.etSociety.getText().toString());
        editAddressReq.setLandmark(binding.etLandmark.getText().toString());
        editAddressReq.setEmail(binding.etemail.getText().toString());
        editAddressReq.setFlat_no(binding.etflatNo.getText().toString());

        if (lat != null && lang != null) {
            editAddressReq.setLat(lat);
            editAddressReq.setLng(lang);
        } else {
            editAddressReq.setLat(deleverdAddress.getLat());
            editAddressReq.setLng(deleverdAddress.getLng());
        }


        editAddressReq.setZipcode(binding.etPincode.getText().toString());
        editAddressReq.setAddress(binding.etAddress.getText().toString());
        editAddressReq.setIsdefault(1);
        Log.e("TEST", "updateaddressApi:LAT LNG DATA " + lat + lang);

        AppLog.e(TAG, "EditaddressReq: " + editAddressReq);

        apiService.editAddress(editAddressReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<BaseRes> editaddressRes) {
                        if (isSuccess(editaddressRes)) {
                            AppLog.e(TAG, "EditaddressRes: " + editaddressRes.body());
                            binding.btSave.setVisibility(View.GONE);
                            showSnake(binding.scheduleOrderFragment, editaddressRes.body().getMsg());


                            // showToast(editaddressRes.body().getMsg());

                            binding.etAddress.setEnabled(false);
                            binding.etflatNo.setEnabled(false);
                            binding.etSociety.setEnabled(false);
                            binding.etCity.setEnabled(false);
                            binding.etLandmark.setEnabled(false);
                            binding.etemail.setEnabled(false);
                            binding.etPincode.setEnabled(false);

                            // setaddress(addressListRes.body());
                        } else {

                            if (editaddressRes.code() == 401) {
                                logout();
                            } else {

                                AppDialog.showConfirmDialog(getActivity(), editaddressRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });
                                //showToast(editaddressRes.body().getMsg());
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


        //editAddressReq.setAddress_id();
    }

    //call add address
    public void addaddressApi() {

        if (!isOnline()) {
            showSnake(binding.scheduleOrderFragment, "Internet Not Available , Please Try Again");
            return;
        }


        final AddressModel.AddAddressReq addAddressReq = new AddressModel.AddAddressReq();
        addAddressReq.setAddress(binding.etAddress.getText().toString());
        addAddressReq.setUser_id(appPref.getUserId());

        /*int selectedcityid;
        for(int i=0;i<cityLists.size();i++){
            if(binding.etCity.getText().toString().equalsIgnoreCase(cityLists.get(i).getCity())){
                selectedcityid = cityid.get
            }
        }*/
        addAddressReq.setCity(cityid);//binding.etCity.getText().toString()
        addAddressReq.setLng(lang);
        addAddressReq.setZipcode(binding.etPincode.getText().toString());
        addAddressReq.setLandmark(binding.etLandmark.getText().toString());
        addAddressReq.setEmail(binding.etemail.getText().toString());
        addAddressReq.setLat(lat);
        addAddressReq.setPhone(binding.etContact.getText().toString());
        addAddressReq.setFlat_no(binding.etflatNo.getText().toString());
        addAddressReq.setStreet(binding.etSociety.getText().toString());
        addAddressReq.setArea("");

        AppLog.e(TAG, "AddaddressReq: " + addAddressReq);

        apiService.addAddress(addAddressReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<BaseRes> addaddressRes) {
                        if (isSuccess(addaddressRes)) {
                            binding.btSave.setVisibility(View.GONE);
                            AppLog.e(TAG, "AddaddressRes: " + addaddressRes.body());
                            showSnake(binding.scheduleOrderFragment, addaddressRes.body().getMsg());
                            // showToast("Add address Successfully");

                          /*  AppDialog.showConfirmDialog(getActivity(), addaddressRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                @Override
                                public void onOkClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });*/

                            binding.btEdit.setVisibility(View.INVISIBLE);
                            binding.btSave.setVisibility(View.INVISIBLE);
                            binding.btnProceed.setVisibility(View.VISIBLE);
                            binding.etAddress.setEnabled(false);
                            binding.etflatNo.setEnabled(false);
                            binding.etSociety.setEnabled(false);
                            binding.etCity.setEnabled(false);
                            binding.etLandmark.setEnabled(false);
                            binding.etemail.setEnabled(false);
                            binding.etPincode.setEnabled(false);
                            //setaddress(addressListRes.body());
                        } else {
                            if (addaddressRes.code() == 401) {
                                logout();
                            } else {

                                showSnake(binding.scheduleOrderFragment, addaddressRes.body().getMsg());
                                /*AppDialog.showConfirmDialog(getActivity(), addaddressRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });*/

                                // showToast(addaddressRes.body().getMsg());
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


    //call search addresses
    public void searchAddress() {

        iscitymatch = false;
        ispincodematch = false;
        // Initialize Places.
        Places.initialize(getActivity(), getResources().getString(R.string.mapApi));
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.PLUS_CODE);
        // Start the autocomplete intent.

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    //Getting address from google api
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            {
                if (resultCode == Activity.RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);

                        /*pickupLatLng = place.getLatLng();
                        pickupAddress = place.getName();*/
                    AppLog.e(TAG, "place::::::::" + place.toString());
                    AppLog.e(TAG, "place::" + place.getAddress() + "::" + place.getPlusCode());
                    lat = String.valueOf(place.getLatLng().latitude);
                    lang = String.valueOf(place.getLatLng().longitude);
                    ;
                    AppLog.e(TAG, "latlng::::::::" + place.getLatLng());
                    binding.etAddress.setText(place.getName());

                    //getAddress(place.getLatLng().latitude,place.getLatLng().longitude);

                    findAddress = new FindAddress(place.getLatLng().latitude, place.getLatLng().longitude, getContext(), this);
                    findAddress.execute();

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    AppLog.e(TAG, status.getStatusMessage());
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }
    }


    //check and detact location
    @Override
    public void onLocationDetect(Address address) {
        if (address != null) {

            selAddress = address;
            //  edtSearch.setText(selAddress.getAddressLine(0));
            //  edtSearch.clearFocus();

            // getLocation.stopLocationUpdates();

            AppLog.e(TAG, "" + address.getAddressLine(0));

            //  binding.etPincode.setText("" + selAddress.getPostalCode());
            //  binding.etflatNo.setText("" + selAddress.getFeatureName());
            getCityList();

            if (!TextUtils.isEmpty(selAddress.getLocality())) {
                binding.etCity.setText("" + selAddress.getLocality());
                cityofaddress = selAddress.getLocality();
            } else {
                binding.etCity.setText("");
                cityofaddress = "";
            }

            if (!TextUtils.isEmpty(selAddress.getSubAdminArea())) {
                binding.etLandmark.setText("" + selAddress.getSubAdminArea());
            } else {
                binding.etLandmark.setText("");

            }

            if (!TextUtils.isEmpty(selAddress.getPostalCode())) {
                binding.etPincode.setText("" + selAddress.getPostalCode());
                pinocdeofaddress = "" + selAddress.getPostalCode();
            } else {
                binding.etPincode.setText("");
                pinocdeofaddress = "";
            }

            //  sendAddress(address);
        } else {
            AppLog.e(TAG, "Address Null");
            // showToast("Address Not found");
        }
    }
}
