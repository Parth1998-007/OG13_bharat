package com.organicbharat.ui.login;

import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;


import com.organicbharat.R;
import com.organicbharat.api_model.UpdateProfileModel;
import com.organicbharat.databinding.ActivityUserProfileBinding;
import com.organicbharat.network.ApiService;
import com.organicbharat.network.RetroClient;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.home.Home;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class UserProfile extends Base implements View.OnClickListener{
    ActivityUserProfileBinding binding;
    public static String TAG = "UserProfile";

    protected ApiService apiService;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        init();
        setToolbar("My Profile");
        enableBack(true);
    }

    void init()
    {
        binding.etUserName.setText(appPref.getString(AppPref.NAME));
        binding.etEmail.setText(appPref.getString(AppPref.EMAIL));
        binding.tvNumber.setText(appPref.getString(AppPref.MOBILE));
        binding.tvNumber.setOnClickListener(this);

        binding.btnUpdateProfile.setOnClickListener(this);

        apiService = RetroClient.getApiService(getHttpClient());

       getProfileData();
    }


    private void getProfileData()
    {
        apiService.getMyProfile(appPref.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UpdateProfileModel.UpdateProfileRes>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(Response<UpdateProfileModel.UpdateProfileRes> updateProfileRes)
                    {
                        hideLoading();
                        if (updateProfileRes.isSuccessful())
                        {

                            onProfileUpdated(updateProfileRes.body());
                        }
                        else
                        {
                            showSnake(binding.llMain,updateProfileRes.message());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
           // gotoActivity(HomeCourier.class, null, true);
            finish();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_updateProfile :
            {
                if(isValid())
                callUpdateProfileAPI();
            }
                break;

        }
    }

    private boolean isValid()
    {

        if(binding.etEmail.getText().toString().equalsIgnoreCase(""))
        {
            showSnake(binding.llMain,"Please enter valid Email");
            return false;
        }

        if(binding.etUserName.getText().toString().equalsIgnoreCase(""))
        {
            showSnake(binding.llMain,"Please enter valid User Name");
            return false;
        }


        return true;
    }

    private void callUpdateProfileAPI()
    {
        showLoading();

        UpdateProfileModel.UpdateProfileReq request = new UpdateProfileModel.UpdateProfileReq();
        request.setUserId(appPref.getUserId());
        request.setEmail(binding.etEmail.getText().toString());
        request.setUsername(binding.etUserName.getText().toString());
        request.setMobile(binding.tvNumber.getText().toString());
        AppLog.e(TAG , "request : "+request.toString());

        apiService.updateProfile(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UpdateProfileModel.UpdateProfileRes>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        compositeDisposable.add(d);
                        showLoading();

                    }

                    @Override
                    public void onNext(Response<UpdateProfileModel.UpdateProfileRes> updateProfileRes)
                    {
                        AppLog.e(TAG , "OnNext called : "+updateProfileRes.message());
                        hideLoading();
                        if (updateProfileRes.isSuccessful())
                        {
                            //onProfileUpdated(updateProfileRes.body());
                            getProfileData();

                            showSnake(binding.llMain,"Profile Updated Successfully");


                            gotoActivity(Home.class,null,false);
                        }
                        else
                        {
                            showSnake(binding.llMain,updateProfileRes.message());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(TAG , "OnError called : "+e.getLocalizedMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }

                });

    }

    private void onProfileUpdated(UpdateProfileModel.UpdateProfileRes updateProfileRes)
    {
        appPref.set(AppPref.NAME, updateProfileRes.getUser_name());
        appPref.set(AppPref.EMAIL, updateProfileRes.getEmail());

        appPref.set(AppPref.MOBILE, updateProfileRes.getMobile());

        binding.tvNumber.setText(updateProfileRes.getMobile());
        binding.etEmail.setText(updateProfileRes.getEmail());
        binding.etUserName.setText(updateProfileRes.getUser_name());

    }

   /* private void showChangePasswordDialog(final String msg, String btn_cancel, String btn_okay)
    {

        final Dialog dialog_con = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog_con.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_con.setContentView(R.layout.dialog_password);

        dialog_con.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView con_msg = dialog_con.findViewById(R.id.msg_con);
        con_msg.setText(msg);
        Button pauseOrderOkay = dialog_con.findViewById(R.id.btPauseOrderOkay);
        pauseOrderOkay.setText(btn_okay);

        final EditText etNewPassword = dialog_con.findViewById(R.id.etPassword);
        final EditText etRePassword = dialog_con.findViewById(R.id.etRePassword);

        pauseOrderOkay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!etNewPassword.getText().toString().equalsIgnoreCase(""))
                    if(etNewPassword.getText().toString().equals(etRePassword.getText().toString()))
                    callChangePasswordAPI(etNewPassword.getText().toString() , dialog_con);
                    else
                    showToast("Password doesn't match , Please correct it.");
            }
        });
        Button pauseOrderCancel = dialog_con.findViewById(R.id.btPauseOrderCancel);
        pauseOrderCancel.setText(btn_cancel);
        pauseOrderCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog_con.dismiss();
            }
        });

        dialog_con.show();
    }

    private void callChangePasswordAPI(String password , final Dialog dialog)
    {
        PasswordChangeModel.ChangePassword changeModel = new PasswordChangeModel.ChangePassword();
        changeModel.setNewPassword(password);
        changeModel.setMobile(appPref.getString(AppPref.MOBILE));

        if(appPref.getString(AppPref.USER_TYPE).equalsIgnoreCase("customer"))
            changeModel.setUserType(1);
        else
            changeModel.setUserType(2);


        showLoading();
        apiService.changePassword(changeModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseRes>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<BaseRes> changePassRes) {
                        AppLog.e(TAG, "changepassword :" + changePassRes.body());
                        dialog.dismiss();
                        hideLoading();
                        if (isSuccess(changePassRes, changePassRes.body()))
                        {
                            showToast("Password Changed Successfully !");
                        }
                        else
                        {
                            showToast(""+changePassRes.message());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });


    }*/

   /* private void sendOTP(SendOtpModel.SendOtpreq sendOtpreq) {

        AppLog.e(TAG, "sendOtpreq : " + sendOtpreq);
        showLoading();
        apiService.sendOtpreq(sendOtpreq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SendOtpModel.SendOtpRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        subscribe(d);
                    }

                    @Override
                    public void onNext(Response<SendOtpModel.SendOtpRes> sendOtpreqRes) {
                        AppLog.e(TAG, "sendOTPRes :" + sendOtpreqRes.body());
                        if (isSuccess(sendOtpreqRes, sendOtpreqRes.body()))
                        {
                            OnsendOtpRes(sendOtpreqRes.body());
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });


    }

    private void OnsendOtpRes(SendOtpModel.SendOtpRes sendOtpRes)
    {
        if (sendOtpRes.isStatus())
        {
            showToast(sendOtpRes.getMsg());
            appPref.set(AppPref.OTP, sendOtpRes.getOTP());
        }
    }
*/

}
