package com.organicbharat.ui.home;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.organicbharat.R;
import com.organicbharat.api_model.CartModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.api_model.SearchModel;
import com.organicbharat.databinding.FragmentExploreBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.cart.Cart;
import com.organicbharat.ui.product.Product;
import com.organicbharat.ui.product.ProductDetails;
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

public class ExploreFragment extends BaseFragment implements ProductAdapter.ProductClick, View.OnClickListener {
    private static final String TAG = "ExploreFragment";
    ProductAdapter productAdapter;
    FragmentExploreBinding binding;
    ArrayList<ProductModel.Slot> slots= new ArrayList<>();
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    public static ExploreFragment newInstance(Bundle args) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore,container,false);
        init();

        //getSearchList();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!TextUtils.isEmpty(binding.etSearch.getText().toString())){
            getSearchList();
        }
    }

    private void getSearchList()
    {

        if(!isOnline())
        {
            showToast("Internet Not Available , Please Try Again");
            return;
        }

        SearchModel.SearchReq searchReq = new SearchModel.SearchReq();
        searchReq.setSearch(""+binding.etSearch.getText().toString());
        searchReq.setUser_id(appPref.getUserId());

        Log.e(TAG,"SEARCH REQUEST..."+searchReq);

        apiService.searchlist(searchReq)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ProductModel.ProductListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ProductModel.ProductListRes> productListResRes) {
                        if (isSuccess(productListResRes)) {
                            setProduct(productListResRes.body());
                        }else if (productListResRes.code()==401){
                            logout();
                        }else if(productListResRes.body().getProduct_list().size()==0){

                            productAdapter.clear();
                            binding.tvnodata.setVisibility(View.VISIBLE);
                           // showToast("Product not found");
                        }else {
                            showSnake(binding.llMain,productListResRes.body().getMsg());
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

    private void setProduct(ProductModel.ProductListRes productListRes) {
        AppLog.e(TAG, "ProductlistRes : " + productListRes);
        if (productListRes.isStatus()) {
            if (productListRes.getProduct_list().size() > 0) {
                slots = productListRes.getSlots();
                appPref.set(AppPref.SLOTS,new Gson().toJson(slots));
                Type typeslot = new TypeToken<ArrayList<ProductModel.Slot>>(){}.getType();
                AppLog.e(TAG," dataataaaa: "+new Gson().fromJson(appPref.getString(AppPref.SLOTS),typeslot));
                productAdapter.clear();
                productAdapter.addCategory(productListRes.getProduct_list());
                binding.tvnodata.setVisibility(View.GONE);
            }else{
                binding.tvnodata.setVisibility(View.VISIBLE);
            }

            /*Product activity = (Product) getActivity();

            activity.updateCount(appPref.getInt(AppPref.ITEMINCART));*/
        } else {
            showSnake(binding.llMain,productListRes.getMsg());
        }
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                // ............
                // ............
                getSearchList();
            }
        }
    };

    private void init() {


        getSearchList();

        productAdapter = new ProductAdapter();
        productAdapter.setProductClick(this);
        binding.rvProducts.setAdapter(productAdapter);

       // binding.imgSearch.setOnClickListener(this);
        binding.imgClose.setOnClickListener(this);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                AppLog.e(TAG," beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //AppLog.e(TAG," onTextChanged");
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppLog.e(TAG," afterTextChanged");
                if (s.length() >= 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);

                }

                if(s.length() == 0){

                }

                /*if(s.length() == 0){
                    getCategory(isfilter);
                }*/
            }

        });

        // binding.btgotocart.setOnClickListener(this);
    }

    @Override
    public void onProductClick(int prodid, int item_in_cart) {
        Bundle b = new Bundle();
        b.putInt("prodid", prodid);
        b.putInt("itemincart", item_in_cart);
        gotoActivity(ProductDetails.class,b,false);
    }

    @Override
    public void onProductQtyInc(ProductModel.ProductData productData) {
        AppLog.e(TAG, "Prodid : " + productData.getProduct_id());

        addtCartApi(productData.getProduct_id(),productData);

    }
    @Override
    public void onProductQtyDec(ProductModel.ProductData productData)
    {
        AppLog.e(TAG, "Prodid : " + productData.getProduct_id());

        removeFromCartApi(productData.getProduct_id(),productData);

    }

    private void addtCartApi(final int prodid, final ProductModel.ProductData productData) {

        if(!isOnline())
        {
            showToast("Internet Not Available , Please Try Again");
            return;
        }

        final CartModel.AddtoCartReq addtoCartReq = new CartModel.AddtoCartReq();
        addtoCartReq.setUser_id(appPref.getUserId());
        addtoCartReq.setProduct_id(prodid);

        AppLog.e(TAG," "+addtoCartReq);

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

                                getSearchList();
                                appPref.set(AppPref.ITEMINCART, addtoCartResResponse.body().getItems_in_cart());
                                Home activity = (Home) getActivity();

                                activity.updateCount(addtoCartResResponse.body().getItems_in_cart());

                            }
                            //setProduct(productListResRes.body());
                        }else {
                            if (addtoCartResResponse.code()==401){
                                logout();
                            }else {
                               showSnake(binding.llMain,addtoCartResResponse.body().getMsg());
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




    private void removeFromCartApi(final int prodid, final ProductModel.ProductData productData) {

        if(!isOnline())
        {
            showToast("Internet Not Available , Please Try Again");
            return;
        }

        final CartModel.RemoveFromCartReq removeFromCartReq = new CartModel.RemoveFromCartReq();
        removeFromCartReq.setUser_id(appPref.getUserId());
        removeFromCartReq.setProduct_id(prodid);
        removeFromCartReq.setQty(1);

        AppLog.e(TAG,"RemoveFromCartReq: "+removeFromCartReq);

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
                            getSearchList();
                            appPref.set(AppPref.ITEMINCART, removefromcartres.body().getItems_in_cart());
                            Home activity = (Home) getActivity();

                            activity.updateCount(removefromcartres.body().getItems_in_cart());

                           /* Product activity = (Product) getActivity();
*//*
                            activity.updateCount(removefromcartres.body().getItems_in_cart());
  */                          //setProduct(productListResRes.body());
                        }else {
                            if (removefromcartres.code()==401){

                            }else{
                                showSnake(binding.llMain,removefromcartres.body().getMsg());

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailure(e);
                        showSnake(binding.llMain,e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        onDone();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSearch:{
                if(!TextUtils.isEmpty(binding.etSearch.getText().toString())){
                    getSearchList();
                }else {
                    showSnake(binding.llMain,"Enter what you want to search");
                }
                break;
            }
            case R.id.btgotocart: {
                Bundle b = new Bundle();
               // b.putString("slots", new Gson().toJson(slots));
                gotoActivity(Cart.class, b, false);
                break;
            }
            case R.id.imgClose:{
                binding.etSearch.setText("");
                break;
            }
        }
    }
}
