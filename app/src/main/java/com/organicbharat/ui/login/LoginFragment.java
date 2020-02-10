package com.organicbharat.ui.login;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.SendOtpModel;
import com.organicbharat.databinding.FragmentLoginBinding;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import com.organicbharat.utils.AppSignatureHelper;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = LoginFragment.class.getSimpleName();
    FragmentLoginBinding binding;
    CompositeDisposable disposable=new CompositeDisposable();
    private ArrayList<String> listKey;
    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        init();

        listKey=new AppSignatureHelper(getActivity()).getAppSignatures();

        return binding.getRoot();
    }

    private void init() {
        binding.btnSubmit.setOnClickListener(this);

        binding.etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                AppLog.e(TAG,"click...1  : "+actionId);
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.btnSubmit.performClick();
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Sendotp requset send to method
            case R.id.btnSubmit: {
                if(isValid()){

                    SendOtpModel.SendOtpreq sendOtpreq = new SendOtpModel.SendOtpreq();
                    sendOtpreq.setPhone(binding.etPhone.getText().toString());
                    sendOtpreq.setType("");
                    if(listKey!=null && listKey.size()>0){
                        sendOtpreq.setKey(listKey.get(0));
                    }else {
                        sendOtpreq.setKey("");
                    }
                    SendOTP(sendOtpreq);

                   /* Bundle bundle = new Bundle();
                    bundle.putString("phone",binding.etPhone.getText().toString());
                    ((Base)getActivity()).changeFrag(VerificationFragment.newInstance(bundle),true,false);*/
                }

              /*  if (isValid()) {
                    LoginModel.LoginReq loginReq = new LoginModel.LoginReq();
                    loginReq.setEmail(binding.etEmail.getText().toString());
                    loginReq.setPassword(binding.etOtp.getText().toString());
                    loginReq.setLogin_type(AppConstants.LOGIN_TYPE_NORMAL);
                    loginReq.setToken("");
                    login(loginReq);
                }*/
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    //Call send otp api
    public void SendOTP(final SendOtpModel.SendOtpreq sendOtpreq)
    {
        AppLog.e(TAG,"SendOTPReq : "+sendOtpreq);

        if(!isOnline())
        {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }

        showLoading();
        apiService.sendOTP(sendOtpreq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SendOtpModel.SendOtpRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        showLoading();
                    }

                    @Override
                    public void onNext(Response<SendOtpModel.SendOtpRes> sendOtpResResponse) {
                        AppLog.e(TAG,"sendOtpRes :"+sendOtpResResponse.body());
                        onSendOTPRes(sendOtpResResponse.body());
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

    //IF response success then redirect verification Fragment or
    //Else Redirect SignUpFragment
    public void onSendOTPRes(SendOtpModel.SendOtpRes sendOtpres) {
        if(sendOtpres.isStatus()){
            appPref.set(AppPref.OTP,sendOtpres.getOTP());

            Bundle bundle = new Bundle();
            bundle.putString("phone",binding.etPhone.getText().toString());
            bundle.putString("from","logincreen");
            ((Base)getActivity()).changeFrag(VerificationFragment.newInstance(bundle),true,false);
        }else {
            if(sendOtpres.getMsg().equalsIgnoreCase("User not registered!")){
                Bundle bundle = new Bundle();
                bundle.putString("phone",binding.etPhone.getText().toString());
                bundle.putString("from","logincreen");
                ((Base)getActivity()).changeFrag(SignUpFragment.newInstance(bundle),true,false);
            }
            showSnake(binding.rlMain,sendOtpres.getMsg());
        }

    }
    private boolean isValid() {
        if (isEmpty(binding.etPhone, R.string.hint_phone)) {
            return false;
        }
        if (binding.etPhone.getText().toString().length()!=10) {
            showSnake(binding.rlMain,getResources().getString(R.string.valid_phone));
            return false;
        }

        return true;
    }

}
