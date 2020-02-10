package com.organicbharat.ui.product;


import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.organicbharat.R;
import com.organicbharat.api_model.CartModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentSubscritionProductBinding;
import com.organicbharat.image_utils.ImageBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscritionProductFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SubscritionProductFragment";
    FragmentSubscritionProductBinding binding;
    int prodid;
    ProductModel.ProductDetailsRes productDetailsResponse;
    boolean checkOrderStatus = false;

    public static SubscritionProductFragment newInstance(Bundle args) {
        SubscritionProductFragment fragment = new SubscritionProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscrition_product, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            prodid = bundle.getInt("prodid");
            AppLog.e(TAG, "prodid: " + prodid + " : ");
        }


        binding.btadd.setOnClickListener(this);
        binding.ivDec.setOnClickListener(this);
        binding.ivInc.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getProductDetail(prodid);
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
                            if (productListResRes.code() == 401) {
                                logout();
                            } else {
                                showSnake(binding.llMain, productListResRes.body().getMsg());
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

        productDetailsResponse = productDetailsRes;
        if (productDetailsRes.isStatus()) {
            ImageBinding.loadImage(binding.ivProdimg, productDetailsRes.getProduct_image());
            binding.tvprodname.setText(productDetailsRes.getProduct());
            binding.tvPrice.setText("Price: ₹ " + productDetailsRes.getRate());
            binding.tvSize.setText("Size: " + productDetailsRes.getSize());
            binding.tvdeliverytime.setText("" + productDetailsRes.getDelivery_time());
            binding.tvdestext.setText("" + productDetailsRes.getDescription());

            if (productDetailsRes.getItem_in_cart() > 0) {
                binding.tvqty.setText("" + productDetailsRes.getItem_in_cart());
                binding.lladd.setVisibility(View.VISIBLE);
                binding.btadd.setVisibility(View.GONE);
            } else {
                binding.lladd.setVisibility(View.GONE);
                binding.btadd.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btadd: {
                if (productDetailsResponse.getAvailibility() == 1) {
                    addtCartApi(prodid);
                    binding.lladd.setVisibility(View.VISIBLE);
                    binding.btadd.setVisibility(View.GONE);
                } else {
                    binding.lladd.setVisibility(View.GONE);
                    binding.btadd.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.iv_inc:
                if (productDetailsResponse.getAvailibility() == 1) {
                    addtCartApi(prodid);
                } else {
                    binding.lladd.setVisibility(View.GONE);
                    binding.btadd.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_dec:
                if (productDetailsResponse.getAvailibility() == 1) {
                    removeFromCartApi(prodid);
                } else {
                    binding.lladd.setVisibility(View.GONE);
                    binding.btadd.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addtCartApi(final int prodid) {

        if (!isOnline()) {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
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
                                getProductDetail(prodid);
                                //  getProductList(subsid, catid);
                                appPref.set(AppPref.ITEMINCART, addtoCartResResponse.body().getItems_in_cart());
                                ProductDetails activity = (ProductDetails) getActivity();

                                activity.updateCount(addtoCartResResponse.body().getItems_in_cart());

                            }
                            //setProduct(productListResRes.body());
                        } else {
                            if (addtoCartResResponse.code()==401){
                                logout();
                            }else{
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

    private void removeFromCartApi(final int prodid) {

        if (!isOnline()) {
            showSnake(binding.llMain, "Internet Not Available , Please Try Again");
            return;
        }

        final CartModel.RemoveFromCartReq removeFromCartReq = new CartModel.RemoveFromCartReq();
        removeFromCartReq.setUser_id(appPref.getUserId());
        removeFromCartReq.setProduct_id(prodid);
        removeFromCartReq.setQty(1);

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

                            if (removefromcartres.body().isStatus()) {
                                getProductDetail(prodid);
                                appPref.set(AppPref.ITEMINCART, removefromcartres.body().getItems_in_cart());

                                ProductDetails activity = (ProductDetails) getActivity();

                                activity.updateCount(removefromcartres.body().getItems_in_cart());
                            }
                            //setProduct(productListResRes.body());
                        } else {
                            if (removefromcartres.code()==401){
                                logout();
                            }else {
                                showSnake(binding.llMain, removefromcartres.body().getMsg());
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

}
