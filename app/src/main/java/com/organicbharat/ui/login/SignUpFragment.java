package com.organicbharat.ui.login;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.organicbharat.R;
import com.organicbharat.api_model.SendOtpModel;
import com.organicbharat.databinding.FragmentSignUpBinding;
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
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    FragmentSignUpBinding binding;
    CompositeDisposable disposable=new CompositeDisposable();
    String phone="",from;
    private ArrayList<String> listKey;
    public static SignUpFragment newInstance(Bundle args) {
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

      //  init();

        listKey=new AppSignatureHelper(getActivity()).getAppSignatures();

        binding.btnSubmit.setOnClickListener(this);
        if(!phone.equalsIgnoreCase("")){
            binding.etPhone.setText(""+phone);
        }
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            phone = getArguments().getString("phone","");
            from = getArguments().getString("from","");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:{
                if(!isValid()){
                    return;
                }
                SendOtpModel.SendOtpreq sendOtpreq = new SendOtpModel.SendOtpreq();
                sendOtpreq.setPhone(binding.etPhone.getText().toString());
                sendOtpreq.setEmail(binding.etEmail.getText().toString());
                sendOtpreq.setType("validation");
                if(listKey!=null && listKey.size()>0){
                    sendOtpreq.setKey(listKey.get(0));
                }else {
                    sendOtpreq.setKey("");
                }
                SendOTP(sendOtpreq);
                break;
            }
        }
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(binding.etPhone.getText().toString())){
            showSnake(binding.rlMain,"Please Enter Phone Number");
            return false;
        }
        if(TextUtils.isEmpty(binding.etName.getText().toString())){
            showSnake(binding.etName,"Please Enter Name");
            return false;
        }
        if(TextUtils.isEmpty(binding.etEmail.getText().toString())){
            showSnake(binding.rlMain,"Please Enter Email");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()){
            showSnake(binding.rlMain,"Please Enter Valid Email");
            return false;
        }
        if(TextUtils.isEmpty(binding.etPassword.getText().toString())){
            showSnake(binding.rlMain,"Please Enter Password");
            return false;
        }
        return true;
    }

    public void SendOTP(final SendOtpModel.SendOtpreq sendOtpreq)
    {

        if(!isOnline())
        {
            showSnake(binding.rlMain,"Internet Not Available , Please Try Again");
            return;
        }

        AppLog.e(TAG,"SendOTPReq : "+sendOtpreq);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    public void onSendOTPRes(SendOtpModel.SendOtpRes sendOtpres) {
        if(sendOtpres.isStatus()){
            appPref.set(AppPref.OTP,sendOtpres.getOTP());
            Bundle bundle = new Bundle();
            bundle.putString("from","signupscreen");
            bundle.putString("phone",binding.etPhone.getText().toString());
            bundle.putString("name",binding.etName.getText().toString());
            bundle.putString("email",binding.etEmail.getText().toString());
            bundle.putString("password",binding.etPassword.getText().toString());
            ((Base)getActivity()).changeFrag(VerificationFragment.newInstance(bundle),true,false);
        }else {
            if(sendOtpres.getMsg().equalsIgnoreCase("User not registered!")){
                ((Base)getActivity()).changeFrag(SignUpFragment.newInstance(null),true,false);
            }
            showToast(sendOtpres.getMsg());
        }

    }

}
