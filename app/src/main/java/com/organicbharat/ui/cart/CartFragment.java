package com.organicbharat.ui.cart;

import android.app.Dialog;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.api_model.CartModel;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentCartBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.address.DeliveryAddress;
import com.organicbharat.ui.address.DeliveryShowAddressFragment;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.http.DELETE;

public class CartFragment extends BaseFragment implements View.OnClickListener, CartAdapter.CartItemClick {

    private static final String TAG = "CartFragment";
    FragmentCartBinding binding;
    CartAdapter cartAdapter;
    ArrayList<ProductModel.Slot> slots = new ArrayList<>();
    public String address;
    int addressId;
    int cityId;
    String city, locality, area, flatNo, street, landmark, phone, pincode, email;

    public int availability;

    public static CartFragment newInstance(Bundle bundle) {
        CartFragment fragment = new CartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //cart adapter set
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cartAdapter = new CartAdapter(false);
        cartAdapter.setCartItemClick(this);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            Type type = new TypeToken<ArrayList<ProductModel.Slot>>() {
            }.getType();
            // slots = new Gson().fromJson(bundle.getString("slots"),type);
        }

        //  binding.rvProducts.setAdapter(cartAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getAddressList();
        getMyCartItemList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.rvProducts.setAdapter(cartAdapter);
        binding.btnCont.setOnClickListener(this);
        binding.btnAddAddress.setOnClickListener(this);
        binding.btneditAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCont:
                if (appPref.getInt(AppPref.ADDRESSID) == 0) {

                    AppDialog.showConfirmDialog(getActivity(), "Add address first", new AppDialog.AppDialogListener() {
                        @Override
                        public void onOkClick(DialogInterface dialog) {
                            AppPref.cart_address = "CartFragment";
                            gotoActivity(DeliveryAddress.class, null, false);
                            dialog.dismiss();
                        }
                    });

                    //showToast("Add address first");

                } else if (appPref.getInt(AppPref.ITEMINCART) == 0) {

                    showSnake(binding.rlMain,"Add item in cart first");
                    /*AppDialog.showConfirmDialog(getActivity(), "Add item in cart first", new AppDialog.AppDialogListener() {
                        @Override
                        public void onOkClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });*/

                    // showToast("Add item in cart first");
                } else if (availability == 0) {

                    showSnake(binding.rlMain,"Product out of stoke!");
                    /*AppDialog.showConfirmDialog(getActivity(), "Product out of stoke", new AppDialog.AppDialogListener() {
                        @Override
                        public void onOkClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });*/

                    //  showToast("Product out of stoke");
                } else {

                    Log.e(TAG, "ADDRESS ID....: "+appPref.getInt(AppPref.ADDRESSID ));
                    Bundle b = new Bundle();
                    // b.putString("slots",new Gson().toJson(slots));
                    gotoActivity(OrderSummery.class, b, false);
                }
                break;
            case R.id.btnAddAddress:
                Bundle bundle = new Bundle();
                bundle.putString("edttextCart", "From CartFragment");
                bundle.putString("city", address);
                DeliveryShowAddressFragment fragobj = new DeliveryShowAddressFragment();
                fragobj.setArguments(bundle);

                gotoActivity(DeliveryAddress.class, null, false);
                break;
            case R.id.btneditAddress:
                AppPref.cart_address = "CartFragment1";
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

    //check default address shows
    private void getAddressList() {
        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
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
                            AppLog.e(TAG, "addresslisrres: " + addressListRes.body());
                            if (addressListRes.body().isStatus()) {
                                if (addressListRes.body().getDataList().size() > 0) {

                                    for (int i = 0; i < addressListRes.body().getDataList().size(); i++) {
                                        if (addressListRes.body().getDataList().get(i).getIsdefault() == 1) {
                                            binding.btneditAddress.setVisibility(View.VISIBLE);
                                            binding.btnAddAddress.setVisibility(View.GONE);
                                            AppLog.e(TAG, "DEFAULTADDRESS: " + addressListRes.body().getDataList().get(i).getAddress_id());
                                            appPref.set(AppPref.ADDRESSID, addressListRes.body().getDataList().get(i).getAddress_id());
                                            setaddress(addressListRes.body().getDataList().get(i));
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
                                            break;
                                        }else{
                                            binding.btnAddAddress.setVisibility(View.VISIBLE);
                                            binding.btneditAddress.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    binding.btnAddAddress.setVisibility(View.VISIBLE);
                                    binding.btneditAddress.setVisibility(View.GONE);
                                    binding.tvAddress.setVisibility(View.GONE);
                                }
                            } else {

                            }
                        } else {
                            if (addressListRes.code() == 401) {
                                logout();
                            } else {

                                binding.btnAddAddress.setVisibility(View.VISIBLE);
                                binding.btneditAddress.setVisibility(View.GONE);
                                binding.tvAddress.setVisibility(View.GONE);
                            }
                            //showToast(addressListRes.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(TAG, "here " + e.getMessage());
                        onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });
    }

    //set address which user selet
    private void setaddress(AddressModel.Data data) {
        address = data.getFlat_no() + ", " + data.getAddress() + ", " + ", " + data.getCity();
        binding.tvAddress.setText(address);
        binding.btnAddAddress.setVisibility(View.GONE);
        binding.btneditAddress.setVisibility(View.VISIBLE);
        binding.tvAddress.setVisibility(View.VISIBLE);
    }

    //OnDelete item
    @Override
    public void onCartitemdeleteClick(int prodid, int qty) {
        showdeletedialog(prodid, qty);
    }

    //ON INcrements
    @Override
    public void onCartQtyInc(int prodid, MyCartModel.Data data) {
        addtCartApi(prodid, data);
    }

    //Add to cart
    private void addtCartApi(final int prodid, final MyCartModel.Data productData) {

        if (!isOnline()) {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }

        final CartModel.AddtoCartReq addtoCartReq = new CartModel.AddtoCartReq();
        addtoCartReq.setUser_id(appPref.getUserId());
        addtoCartReq.setProduct_id(prodid);

        AppLog.e(TAG, " " + addtoCartReq);

        apiService.addtoCart(addtoCartReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CartModel.AddtoCartRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CartModel.AddtoCartRes> addtoCartResResponse) {
                        if (isSuccess(addtoCartResResponse)) {
                            AppLog.e(TAG, "AddTocartRes: " + addtoCartResResponse.body());
                            if (addtoCartResResponse.body().isStatus()) {
                                for (int i = 0; i < addtoCartResResponse.body().getDatlist().size(); i++) {
                                    if (addtoCartResResponse.body().getDatlist().get(i).getProduct_id() == prodid) {
                                        if (productData.getQty() <= addtoCartResResponse.body().getDatlist().get(i).getRemaining_qty()) {
                                            productData.isavailable(true);
                                            cartAdapter.notifyDataSetChanged();
                                        } else {
                                            productData.isavailable(false);
                                            cartAdapter.notifyDataSetChanged();
                                        }

                                        getMyCartItemList();
                                       /* Ca activity = (Product) getActivity();
                                        activity.updateCount(addtoCartResResponse.body().getItems_in_cart());*/
                                        appPref.set(AppPref.ITEMINCART, addtoCartResResponse.body().getItems_in_cart());
                                        Cart activity = (Cart) getActivity();
                                        activity.updateCount(addtoCartResResponse.body().getItems_in_cart());

                                    }
                                }
                                appPref.set(AppPref.ITEMINCART, addtoCartResResponse.body().getItems_in_cart());
                            }
                            //setProduct(productListResRes.body());
                        } else {

                            showSnake(binding.rlMain,addtoCartResResponse.body().getMsg());
                           /* AppDialog.showConfirmDialog(getActivity(), addtoCartResResponse.body().getMsg(), new AppDialog.AppDialogListener() {
                                @Override
                                public void onOkClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });*/

                            showToast(addtoCartResResponse.body().getMsg());
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


    //ON Cart Decriment
    @Override
    public void onCartQtyDec(int prodid, MyCartModel.Data data) {
        callRemoveFromCartAPI(prodid, data, 1);
    }

    private void showdeletedialog(final int prodid, final int qty) {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btok = dialog.findViewById(R.id.btnOk);
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("cart_data", myCartModelRes_clear.getDatlist().size() + "");
                if (myCartModelRes_clear.getDatlist().size() == 1) {
                    binding.tvTotal.setText("Total : ₹" + 0);

                }
                callRemoveFromCartAPI(prodid, null, qty);
                dialog.dismiss();
            }
        });

        Button btcancel = dialog.findViewById(R.id.btnCancel);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callRemoveFromCartAPI(int prodid, MyCartModel.Data data, int qty) {

        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }


        final CartModel.RemoveFromCartReq removeFromCartReq = new CartModel.RemoveFromCartReq();
        removeFromCartReq.setUser_id(appPref.getUserId());
        removeFromCartReq.setProduct_id(prodid);
        removeFromCartReq.setQty(qty);

        AppLog.e(TAG, "RemoveFromCartReq: " + removeFromCartReq);

        apiService.removeFromCart(removeFromCartReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CartModel.RemoveFromCartRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<CartModel.RemoveFromCartRes> removefromcartres) {
                        if (isSuccess(removefromcartres)) {
                            AppLog.e(TAG, "RemoveFromcartRes: " + removefromcartres.body());

                            getMyCartItemList();

                            /*for (int i=0;i<removefromcartres.body().getDatlist().size();i++){
                                if(removefromcartres.body().getDatlist().get(i).getProduct_id() == prodid) {
                                    if(productData.getCount()<=removefromcartres.body().getDatlist().get(i).getRemaining_qty()){
                                        productData.isavailable(true);
                                        productAdapter.notifyDataSetChanged();
                                    }else {
                                        productData.isavailable(false);
                                        productAdapter.notifyDataSetChanged();
                                    }
                                }
                            }*/

                            appPref.set(AppPref.ITEMINCART, removefromcartres.body().getItems_in_cart());
                            Cart activity = (Cart) getActivity();

                            activity.updateCount(removefromcartres.body().getItems_in_cart());
                            //setProduct(productListResRes.body());
                        } else {
                            //showToast(removefromcartres.body().getMsg());
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

    public void getMyCartItemList() {

        if (!isOnline()) {
            showToast("Internet Not Available , Please Try Again");
            return;
        }

        apiService.getMyCartItemList(appPref.getUserId())//getting user id
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

                            availability = myCartListRes.body().getAvailablity();

                            appPref.set("availability", myCartListRes.body().getAvailablity());

                            setcartItem(myCartListRes.body());
                        } else {
                            binding.tvTotal.setText("Total :");
                            binding.llnodata.setVisibility(View.VISIBLE);
                            binding.rvProducts.setVisibility(View.GONE);
                            //  showToast(myCartListRes.body().getMsg());
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

    MyCartModel.MyCartModelRes myCartModelRes_clear;

    private void setcartItem(MyCartModel.MyCartModelRes myCartModelRes) {
        myCartModelRes_clear = myCartModelRes;
        AppLog.e(TAG, "MyCartItemlistRes : " + myCartModelRes);
        if (myCartModelRes.isStatus()) {
            binding.tvTotal.setText("Total : ₹" + myCartModelRes.getPayable());
            if (myCartModelRes.getDatlist().size() > 0) {
                cartAdapter.clear();
                cartAdapter.addCartItem(myCartModelRes.getDatlist());
            }
            binding.llnodata.setVisibility(View.GONE);
            binding.rvProducts.setVisibility(View.VISIBLE);
        } else {
            binding.tvTotal.setVisibility(View.GONE);
            binding.btnCont.setVisibility(View.GONE);

            binding.llnodata.setVisibility(View.VISIBLE);
            binding.rvProducts.setVisibility(View.GONE);

            showSnake(binding.rlMain,myCartModelRes.getMsg());
        /*    AppDialog.showConfirmDialog(getActivity(), myCartModelRes.getMsg(), new AppDialog.AppDialogListener() {
                @Override
                public void onOkClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });*/

            //showToast(myCartModelRes.getMsg());
        }
    }

/*
    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

        @NonNull
        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart,viewGroup,false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull CartAdapter.ViewHolder viewHolder, int i) {

        }
        @Override
        public int getItemCount() {
            return 12;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivdelete;
            TextView tvOldprice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                ivdelete = itemView.findViewById(R.id.iv_delete);
                ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  showdeletedialog();
                    }
                });

                tvOldprice = itemView.findViewById(R.id.tvOldPrice);
                tvOldprice.setPaintFlags(tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoActivity(ProductDetails.class,null,false);
                    }
                });

            }
        }

    }
*/


}
