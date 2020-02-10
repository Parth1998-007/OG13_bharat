package com.organicbharat.ui.address;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.ui.Base;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppPref;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DeliveryAddress extends Base {

    private static final String TAG = "DeliveryAddress";
    DeliveryAddress binding;
    public static int addAddress;
    boolean from_cart;
    String city, locality, area, flatNo, street, landmark, phone, pin, email;
    int cityId, addressId;
    int addressListSize;
    public static View view;
    public static LinearLayout llmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_delivery_address));
        enableBack(true);
        init();
        llmain=findViewById(R.id.llmain);
        getAddressList();

        cartAddressBundle();
        view = LayoutInflater.from(this).inflate(R.layout.add_address_layout, null);

    }

    public void getAddressList() {

        apiService.getAddressList(appPref.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AddressModel.AddressListRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<AddressModel.AddressListRes> addressListResResponse) {
                        if (isSuccess(addressListResResponse)){
                            addressListSize=addressListResResponse.body().getDataList().size();
                            Log.e(TAG, "onNext: "+addressListSize );
                            Log.e(TAG, "OnResponse:DELVIERY ADRESS LIST "+addressListResResponse.body() );
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


    void init() {

        if (AppPref.cart_address.equalsIgnoreCase("CartFragment")) {
            addAddress = 1;
            changeFrag(DeliveryAddressListFragment.newInstance(null), false, false);

            //changeFrag(DeliveryShowAddressFragment.newInstance(null), false, false);
        } else if (AppPref.cart_address.equalsIgnoreCase("CartFragment1")){
            addAddress=0;
            cartAddressBundle();
        }else{
            changeFrag(DeliveryAddressListFragment.newInstance(null), false, false);

        }

    }


    void cartAddressBundle() {
        Bundle bundleEdit = getIntent().getExtras();
        if (bundleEdit != null) {
            if (bundleEdit.getBoolean("Cart_Edit")) {
                addressId=bundleEdit.getInt("addressId");
                locality = bundleEdit.getString("locality");
                flatNo = bundleEdit.getString("flatNo");
                street = bundleEdit.getString("street");
                city = bundleEdit.getString("city");
                landmark = bundleEdit.getString("landmark");
                pin = bundleEdit.getString("pincode");
                phone = bundleEdit.getString("phone");
                email = bundleEdit.getString("email");
                from_cart = bundleEdit.getBoolean("Cart_Edit");
                cityId=bundleEdit.getInt("cityId");
                changeFrag(DeliveryShowAddressFragment.newInstance(bundleEdit), false, false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //Add new Address
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_address_menu, menu);
        MenuItem menuAdd = menu.findItem(R.id.menu_add_Address);
        menuAdd.setActionView(view);

           if (addressListSize<=2){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAddress = 1;
                        AppPref.cart_address="DeliveryAddress";
                        Bundle b=new Bundle();

                        //send bundle for Condition for searchActivity open or not
                        b.putBoolean("NewAddress",true);

                        if (addressListSize==2){
                            showSnake(llmain,"You can not add more than two Address!");
                           /* AppDialog.showConfirmDialog(DeliveryAddress.this, "You can not add more than two Address!", new AppDialog.AppDialogListener() {
                                @Override
                                public void onOkClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });*/
                        }else{
                            changeFrag(DeliveryShowAddressFragment.newInstance(b), true, false);
                        }


                    }
                });
            }




        return super.onCreateOptionsMenu(menu);
    }

}
