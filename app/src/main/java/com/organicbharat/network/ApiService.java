package com.organicbharat.network;

import android.text.style.UpdateAppearance;

import com.organicbharat.api_model.AddressModel;
import com.organicbharat.api_model.AreaListModel;
import com.organicbharat.api_model.BaseReq;
import com.organicbharat.api_model.BaseRes;
import com.organicbharat.api_model.CartModel;
import com.organicbharat.api_model.CategoryModel;
import com.organicbharat.api_model.CityListModel;
import com.organicbharat.api_model.ClearCartModel;
import com.organicbharat.api_model.CreateOrderModel;
import com.organicbharat.api_model.CreateSubscriptionOrderModel;
import com.organicbharat.api_model.CreateSummeryModel;
import com.organicbharat.api_model.DeleteAddressReq;
import com.organicbharat.api_model.LoginModel;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.api_model.MyOrderModel;
import com.organicbharat.api_model.MySubscriptionOrderModel;
import com.organicbharat.api_model.OfferListModel;
import com.organicbharat.api_model.PauseOrderModel;
import com.organicbharat.api_model.ProductAvailabilityModel;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.api_model.RepeatOrder;
import com.organicbharat.api_model.ResumeOrder;
import com.organicbharat.api_model.SearchModel;
import com.organicbharat.api_model.SendOtpModel;
import com.organicbharat.api_model.SignUpModel;
import com.organicbharat.api_model.UpdateProfileModel;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //Login api
    @POST("Login")
    Observable<LoginModel.LoginRes> login(@Body LoginModel.LoginReq loginReq);

    //getting all categoris
    @GET("CategoryList/user_id/{user_id}/subscription/{subscription}/page/{page}")
    Observable<Response<CategoryModel.CategoryRes>> getCategory(@Path("user_id") int userid, @Path("subscription") int subscription, @Path("page") int page);


    //Send otp api
    @Headers({"Content-Type: application/json"})
    @POST("SendOTP")
    Observable<Response<SendOtpModel.SendOtpRes>> sendOTP(@Body SendOtpModel.SendOtpreq sendOtpreq);

    //Product list
    @GET("ProductList/user_id/{user_id}/subscription/{subscription}/category_id/{category_id}/page/{page}")
    Observable<Response<ProductModel.ProductListRes>> getProductList(@Path("user_id") int userid, @Path("subscription") int subscription,@Path("category_id") int category_id, @Path("page") int page);

    //Product Details
    @GET("ProductDetails/user_id/{user_id}/product_id/{product_id}")
    Observable<Response<ProductModel.ProductDetailsRes>> getProductDetail(@Path("user_id") int userId,@Path("product_id") int prodid);

    //add to cart
    @Headers({"Content-Type: application/json"})
    @POST("AddToCart")
    Observable<Response<CartModel.AddtoCartRes>> addtoCart(@Body CartModel.AddtoCartReq addtoCartReq);

    //remove cart api
    @Headers({"Content-Type: application/json"})
    @POST("RemoveFromCart")
    Observable<Response<CartModel.RemoveFromCartRes>> removeFromCart(@Body CartModel.RemoveFromCartReq removeFromCartReq);

    //cart item data
    @GET("MyCart/{user_id}")
    Observable<Response<MyCartModel.MyCartModelRes>> getMyCartItemList(@Path("user_id") int userId);

    //Profile add
    @GET("myProfile/{user_id}")
    Observable<Response<UpdateProfileModel.UpdateProfileRes>> getMyProfile(@Path("user_id") int userId);

    //Update profile
    @Headers({"Content-Type: application/json"})
    @PUT("updateProfile")
    Observable<Response<UpdateProfileModel.UpdateProfileRes>> updateProfile(@Body UpdateProfileModel.UpdateProfileReq updateProfileReq);

    // Sign up user api
    @Headers({"Content-Type: application/json"})
    @POST("Signup")
    Observable<Response<SignUpModel.SignUpRes>> signUp(@Body SignUpModel.SignUpReq signUpReq);

    //Create order summery Fragment
    @Headers({"Content-Type: application/json"})
    @POST("CreateOrder")
    Observable<Response<CreateOrderModel.CreateOrderRes>> createOrder(@Body CreateOrderModel.CreateOrderReq createOrderReq);

    //address list for cart , normal order, subscription order
    @GET("AddressList/{user_id}")
    Observable<Response<AddressModel.AddressListRes>> getAddressList(@Path("user_id") int userId);

    //New Address
    @Headers({"Content-Type: application/json"})
    @POST("AddAddress")
    Observable<Response<BaseRes>> addAddress(@Body AddressModel.AddAddressReq addAddressReq);

    //Edit Address
    @Headers({"Content-Type: application/json"})
    @PUT("EditAddress")
    Observable<Response<BaseRes>> editAddress(@Body AddressModel.EditAddressReq editAddressReq);

    //Normal Order list
    @GET("orderlist/{user_id}")
    Observable<Response<MyOrderModel.MyOrderListRes>> getOrderList(@Path("user_id") int userId);

    //Order Details
    @GET("orderDetails/user_id/{user_id}/order_id/{order_id}")
    Observable<Response<MyOrderModel.OrderDetailRes>> getOrderDetails(@Path("user_id") int userId,@Path("order_id") int order_id);

    @GET("CityList")
    Observable<Response<CityListModel.CityListRes>> getCityList();

    //Get Area list for address
    @GET("AreaList/city_id/{city_id}")
    Observable<Response<AreaListModel.AreaListRes>> getAreaList(@Path("city_id") int city_id);

    //create summery
    @Headers({"Content-Type: application/json"})
    @POST("createSummary")
    Observable<Response<CreateSummeryModel.CreateSummeryRes>> createsummery(@Body CreateSummeryModel.CreateSummeryReq createSummeryReq);

    //create subscription order
    @Headers({"Content-Type: application/json"})
    @POST("CreateSubscriptionOrder")
    Observable<Response<BaseRes>> createSubscriptionOrder(@Body CreateSubscriptionOrderModel.CreateSubscriptionOrderReq createSubscriptionOrderReq);

    //subscription order list
    @GET("subscriptionOrderList/user_id/{user_id}")
    Observable<Response<MySubscriptionOrderModel.MySubscriptionOrderRes>> getSubscriptionOrderList(@Path("user_id") int userId);

    //Home slider
    @GET("OfferList")
    Observable<Response<OfferListModel.OfferListRes>> getOffer();

    //Search product
    @Headers({"Content-Type: application/json"})
    @POST("search")
    Observable<Response<ProductModel.ProductListRes>> searchlist(@Body SearchModel.SearchReq searchReq);

    //Repeat order api
    @Headers({"Content-Type: application/json"})
    @POST("repeatOrder")
    Observable<Response<RepeatOrder.RepeatOrderRes>> repeatOrder(@Body RepeatOrder.RepeatOrderReq repeatOrderReq);

    //Pause order api for Subscription
    @Headers({"Content-Type: application/json"})
    @POST("PauseOrders")
    Observable<Response<PauseOrderModel.PauseOrderRes>> pauseOrderAPI(@Body PauseOrderModel.PauseOrderReq pauseOrderReq);

    //Resume Order api for subscription product
    @Headers({"Content-Type: application/json"})
    @POST("ResumeOrders")
    Observable<Response<ResumeOrder.ResumeOrderRes>> resumeOrderAPI(@Body ResumeOrder.ResumeOrderReq resumeOrderReq);

    //Clear My cart api
    @Headers({"Content-Type: application/json"})
    @POST("ClearMyCart")
    Observable<Response<ClearCartModel.ClearCartRes>> clearcart(@Body ClearCartModel.ClearCartReq clearCartReq);

    //DeleteAddress api
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE",path = "DeleteAddress",hasBody = true)
    Observable<Response<BaseRes>> deleteAddress(@Body DeleteAddressReq clearCartReq);

    //Check product availability at SummeryFragment
    @Headers({"Content-Type: application/json"})
    @POST("checkProductAvailability")
    Observable<Response<ProductAvailabilityModel.ProductAvailabilityRes>> checkAvailability(@Body ProductAvailabilityModel.ProductAvailabilityReq productAvailabilityReq);




 /* @POST("Login")
    Call<LoginModel.LoginRes> login2(@Body LoginModel.LoginReq loginReq);  */

}
