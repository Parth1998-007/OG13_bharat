package com.organicbharat.ui.address;

import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.api_model.BaseRes;
import com.organicbharat.api_model.DeleteAddressReq;
import com.organicbharat.databinding.FragmentAddressListBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DeliveryAddressListFragment extends BaseFragment implements DeliveryAddressListAdapter.AddressItemClick
{
    private FragmentAddressListBinding binding;
    private DeliveryAddressListAdapter deliveryAddressListAdapter;
    private String TAG="DeliveryAddressListFragment";
    public boolean oneSelect;

    public static DeliveryAddressListFragment newInstance(Bundle bundle) {
         DeliveryAddressListFragment fragment = new DeliveryAddressListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAddressItemClick(AddressModel.Data addressdata) {
    //        DeliveryAddress.addAddress=0;
    //        Bundle b = new Bundle();
    //        b.putString("list_address", new Gson().toJson(addressmodel));
    //        ((DeliveryAddress)getActivity()).changeFrag(DeliveryShowAddressFragment.newInstance(b),true,false);


        updateaddressApi(addressdata);
    }

    //Delete address Interface method and click and open dialog
    @Override
    public void onAddressDeleteClick(final AddressModel.Data data) {
        AppDialog.showConfirmDialog(context, "Are you sure you want to delete?", new AppDialog.AppDialogListener() {
            @Override
            public void onOkClick(DialogInterface dialog) {
                dialog.dismiss();
                DeleteAddressReq deleteAddressReq = new DeleteAddressReq();
                deleteAddressReq.setAddress_id(data.getAddress_id()+"");
                deleteAddressReq.setUser_id(appPref.getUserId()+"");
                apiService.deleteAddress(deleteAddressReq)//appPref.getUserId()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseRes>>() {

                            @Override
                            public void onSubscribe(Disposable d) {
                                subscribe(d);
                            }

                            @Override
                            public void onNext(Response<BaseRes> editaddressRes) {
                                AppLog.e(TAG,"onNext : "+editaddressRes);
                                if (isSuccess(editaddressRes)) {

                                    //call addresslist api in activity

                                    ((DeliveryAddress)getActivity()).getAddressList();


                                    appPref.set(AppPref.ADDRESSID,0);
                                    AppLog.e(TAG, "deleteAddress: " + editaddressRes.body());
                                    getAddresslist();

                                }else {
                                    if (editaddressRes.code()==401){
                                        logout();
                                    }else{
                                        showSnake(binding.rlMain,"Erro in deleting address");

                                      /*  AppDialog.showConfirmDialog(getActivity(), "Error in deleting address!", new AppDialog.AppDialogListener() {
                                            @Override
                                            public void onOkClick(DialogInterface dialog) {
                                               dialog.dismiss();
                                            }
                                        });*/
                                        //showToast("Error in deleting address!");
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
        });

    }

    //Update address
    private void updateaddressApifor1(AddressModel.Data addressdata) {
        AddressModel.EditAddressReq editAddressReq = new AddressModel.EditAddressReq();
        editAddressReq.setUser_id(appPref.getUserId());
        editAddressReq.setAddress_id(String.valueOf(addressdata.getAddress_id()));
        editAddressReq.setCity(addressdata.getCity_id());//binding.etCity.getText().toString()
        editAddressReq.setPhone(addressdata.getPhone());
        editAddressReq.setArea(addressdata.getArea());
        editAddressReq.setStreet(addressdata.getStreet());
        editAddressReq.setLandmark(addressdata.getLandmark());
        editAddressReq.setEmail(addressdata.getEmail());
        editAddressReq.setFlat_no(addressdata.getFlat_no());
        editAddressReq.setLat(addressdata.getLat());
        editAddressReq.setLng(addressdata.getLng());
        editAddressReq.setZipcode(addressdata.getPincode());
        editAddressReq.setAddress(addressdata.getAddress());
        editAddressReq.setIsdefault(1);

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
                            // showToast(editaddressRes.body().getMsg());
                           // getAddresslist();
                            // setaddress(addressListRes.body());
                        } else {
                            if (editaddressRes.code()==401){
                               logout();
                            }else{

                                showSnake(binding.rlMain,editaddressRes.body().getMsg());

                               /* AppDialog.showConfirmDialog(getActivity(), editaddressRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });*/

                               // showToast(editaddressRes.body().getMsg());
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
//update address
    private void updateaddressApi(AddressModel.Data addressdata) {
        AddressModel.EditAddressReq editAddressReq = new AddressModel.EditAddressReq();
        editAddressReq.setUser_id(appPref.getUserId());
        editAddressReq.setAddress_id(String.valueOf(addressdata.getAddress_id()));
        editAddressReq.setCity(addressdata.getCity_id());//binding.etCity.getText().toString()
        editAddressReq.setPhone(addressdata.getPhone());
        editAddressReq.setArea(addressdata.getArea());
        editAddressReq.setStreet(addressdata.getStreet());
        editAddressReq.setLandmark(addressdata.getLandmark());
        editAddressReq.setEmail(addressdata.getEmail());
        editAddressReq.setFlat_no(addressdata.getFlat_no());
        editAddressReq.setLat(addressdata.getLat());
        editAddressReq.setLng(addressdata.getLng());
        editAddressReq.setZipcode(addressdata.getPincode());
        editAddressReq.setAddress(addressdata.getAddress());
        editAddressReq.setIsdefault(1);

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
                           // showToast(editaddressRes.body().getMsg());
                            getAddresslist();
                           // ((DeliveryAddress)getActivity()).finish();
                            // setaddress(addressListRes.body());
                        } else {
                            if(editaddressRes.code()==401){
                                logout();
                            }else{
                              //  showToast(editaddressRes.body().getMsg());
                                showSnake(binding.rlMain,editaddressRes.body().getMsg());

/*
                                AppDialog.showConfirmDialog(getActivity(), editaddressRes.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });*/
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


    //Set deliver address list adapter data
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deliveryAddressListAdapter=new DeliveryAddressListAdapter();
        deliveryAddressListAdapter.setAddressItemClick(this);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_list,container,false);
        init();

        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        DeliveryAddress.view.setVisibility(View.VISIBLE);
        getAddresslist();
    }

    //get address api call
    private void getAddresslist() {
        apiService.getAddressList(appPref.getUserId())//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AddressModel.AddressListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<AddressModel.AddressListRes> addressListResResponse) {
                        if (isSuccess(addressListResResponse)) {

                            setAddresslist(addressListResResponse.body());
                        }else {

                            if (addressListResResponse.code()==401){
                                logout();
                            }else{
                                binding.llnodata.setVisibility(View.VISIBLE);
                                binding.rvAddress.setVisibility(View.GONE);
                                appPref.set(AppPref.ADDRESSID,0);

                               // showToast(addressListResResponse.body().getMsg());

                                showSnake(binding.rlMain,addressListResResponse.body().getMsg());
                               /* AppDialog.showConfirmDialog(getActivity(), addressListResResponse.body().getMsg(), new AppDialog.AppDialogListener() {
                                    @Override
                                    public void onOkClick(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });*/

                            }

                            //showToast(addressListResResponse.body().getMsg());
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

    //set address list data add into adapter
    private void setAddresslist(AddressModel.AddressListRes myAddressListRes) {
        AppLog.e(TAG, "myAddressListRes : " + myAddressListRes);


        if (myAddressListRes.getDataList().size() > 0) {
            if(myAddressListRes.getDataList().size()==1){
                updateaddressApifor1(myAddressListRes.getDataList().get(0));
            }
            deliveryAddressListAdapter.clear();
            deliveryAddressListAdapter.addAddress(myAddressListRes.getDataList());

            binding.llnodata.setVisibility(View.GONE);
            binding.rvAddress.setVisibility(View.VISIBLE);
        }else {
            binding.llnodata.setVisibility(View.VISIBLE);
            binding.rvAddress.setVisibility(View.GONE);
        }
    }

    private void init() {
        binding.rvAddress.setAdapter(deliveryAddressListAdapter);
    }

}
