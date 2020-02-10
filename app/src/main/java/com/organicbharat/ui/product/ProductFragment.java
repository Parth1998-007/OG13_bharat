package com.organicbharat.ui.product;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.organicbharat.R;
import com.organicbharat.api_model.CartModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.databinding.FragmentProductBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.cart.Cart;
import com.organicbharat.ui.home.ProductAdapter;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProductFragment extends BaseFragment implements ProductAdapter.ProductClick, View.OnClickListener {
    private static final String TAG = "ProductFragment";
    ProductAdapter productAdapter;
    FragmentProductBinding binding;
    int catid, subsid;
    int currentPageprod = 1;
    boolean isdataavailable = true;
    boolean mIsLoadingprod = false;
    ArrayList<ProductModel.Slot> slots = new ArrayList<>();

    public static ProductFragment newInstance(Bundle args) {
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            catid = bundle.getInt("catid");
            subsid = bundle.getInt("subsid");
            AppLog.e(TAG, "catid: " + catid + " :subsid: " + subsid);
        }
    }

    private void init() {

        productAdapter = new ProductAdapter();
        productAdapter.setProductClick(this);
        @SuppressLint("WrongConstant") final LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvProducts.setLayoutManager(linearlayoutManager);
        binding.rvProducts.setAdapter(productAdapter);
        binding.btgotocart.setOnClickListener(this);

        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = linearlayoutManager.getItemCount();
                int firstVisibleItemPosition = linearlayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearlayoutManager.findLastVisibleItemPosition();

                if (!mIsLoadingprod && isdataavailable) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                    ) {
                        currentPageprod = currentPageprod + 1;
                        AppLog.e(TAG, "onScrollCalled " + currentPageprod);

                        getProductList(subsid, catid);
                    }
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        currentPageprod = 1;
        getProductList(subsid, catid);
        // getMyCartItemList();

    }

    private void getProductList(int subsid, int catid) {

        if (!isOnline()) {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }

        apiService.getProductList(appPref.getUserId(), subsid, catid, currentPageprod)//appPref.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ProductModel.ProductListRes>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mIsLoadingprod = true;
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<ProductModel.ProductListRes> productListResRes) {
                        AppLog.e(TAG, "getProductList()" + productListResRes);
                        if (isSuccess(productListResRes)) {
                            setProduct(productListResRes.body());
                        } else {
                            if (productListResRes.code() == 401) {
                                logout();
                            } else {
                                if (currentPageprod == 1)
                                    showSnake(binding.rlMain,productListResRes.body().getMsg());
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

    private void setProduct(ProductModel.ProductListRes productListRes) {
        mIsLoadingprod = false;
        AppLog.e(TAG, "ProductlistRes : " + productListRes);
        if (productListRes.isStatus()) {
            if (productListRes.getProduct_list().size() > 0) {
                isdataavailable = true;
                slots = productListRes.getSlots();
                appPref.set(AppPref.SLOTS, new Gson().toJson(slots));

                if (currentPageprod == 1)
                    productAdapter.clear();

                productAdapter.addCategory(productListRes.getProduct_list());
            } else {
                isdataavailable = false;
            }

            Product activity = (Product) getActivity();

            activity.updateCount(appPref.getInt(AppPref.ITEMINCART));
        } else {
            if (currentPageprod == 1)
            {
                showSnake(binding.rlMain, productListRes.getMsg());
            }
        }
    }

    @Override
    public void onProductClick(int prodid, int item_in_cart) {
        AppLog.e(TAG, "Prodeuct List : " + appPref.getInt(AppPref.ITEMINCART));
        Bundle b = new Bundle();
        b.putInt("prodid", prodid);
        b.putInt("itemincart", item_in_cart);

        gotoActivity(ProductDetails.class, b, false);
    }

    @Override
    public void onProductQtyInc(ProductModel.ProductData productData) {
        AppLog.e(TAG, "Prodid : " + productData.getProduct_id());
        AppLog.e(TAG, "Product_Adapter_QTY : " + productData.getProduct_id());
        addtCartApi(productData.getProduct_id(), productData);

    }

    private void addtCartApi(final int prodid, final ProductModel.ProductData productData) {

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
                                currentPageprod = 1;
                                getProductList(subsid, catid);
                                appPref.set(AppPref.ITEMINCART, addtoCartResResponse.body().getItems_in_cart());
                                Product activity = (Product) getActivity();
                                activity.updateCount(addtoCartResResponse.body().getItems_in_cart());

                            }
                            //setProduct(productListResRes.body());
                        } else {
                            if (addtoCartResResponse.code() == 401) {
                                logout();
                            } else {

                                showSnake(binding.rlMain, addtoCartResResponse.body().getMsg());

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


    @Override
    public void onProductQtyDec(ProductModel.ProductData productData) {
        AppLog.e(TAG, "Prodid : " + productData.getProduct_id());

        removeFromCartApi(productData.getProduct_id(), productData);

    }

    private void removeFromCartApi(final int prodid, final ProductModel.ProductData productData) {


        if (!isOnline()) {
            showSnake(binding.rlMain,"Internet Not Availavle , Please Try Again");
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
                            getProductList(subsid, catid);
                            appPref.set(AppPref.ITEMINCART, removefromcartres.body().getItems_in_cart());

                            Product activity = (Product) getActivity();

                            activity.updateCount(removefromcartres.body().getItems_in_cart());
                            //setProduct(productListResRes.body());
                        } else {
                            if (removefromcartres.code() == 401) {
                                logout();
                            } else {
                                showSnake(binding.rlMain, removefromcartres.body().getMsg());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btgotocart:
                Bundle b = new Bundle();
                //  b.putString("slots",new Gson().toJson(slots));
                gotoActivity(Cart.class, b, false);
                break;
        }
    }
}
