package com.organicbharat.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.organicbharat.R;
import com.organicbharat.api_model.LoginModel;
import com.organicbharat.api_model.SignUpModel;
import com.organicbharat.databinding.FragmentVerificationBinding;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.home.Home;
import com.organicbharat.utils.AppConstants;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VerificationFragment extends BaseFragment {
    private static final String TAG = VerificationFragment.class.getSimpleName();
    TextView[] tvNumbers=new TextView[10];
    FragmentVerificationBinding binding;
    String phone,from,name,email,password;
    CompositeDisposable disposable=new CompositeDisposable();
    IncomingSms incomingSms;
    public static VerificationFragment newInstance(Bundle args) {
        VerificationFragment fragment = new VerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            phone = getArguments().getString("phone","");
            from = getArguments().getString("from","");
            email = getArguments().getString("email","");
            name = getArguments().getString("name","");
            password = getArguments().getString("password","");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification,container,false);
        init();
        setData();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        smsListener();

        return binding.getRoot();
    }

    private void smsListener() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity() /* context */);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AppLog.e("smsListener","onSuccess");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AppLog.e("smsListener","onFailure  :"+e.toString());
            }
        });
    }


    private void setData() {
        binding.tvMsg.setText("Please type the verification code sent to +91"+phone);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

            disposable.dispose();

    }

    private void init() {
        tvNumbers[0]=binding.tv0;
        tvNumbers[1]=binding.tv1;
        tvNumbers[2]=binding.tv2;
        tvNumbers[3]=binding.tv3;
        tvNumbers[4]=binding.tv4;
        tvNumbers[5]=binding.tv5;
        tvNumbers[6]=binding.tv6;
        tvNumbers[7]=binding.tv7;
        tvNumbers[8]=binding.tv8;
        tvNumbers[9]=binding.tv9;

        for (int i=0;i<tvNumbers.length;i++){
            final int finalI = i;
            tvNumbers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumber(finalI +"",true);
                }
            });
        }

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNumber("",false);
            }
        });

        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==4){
                   // callApi(s.toString());
                    if (isValid(s.toString())) {
                        if(from.equalsIgnoreCase("logincreen")){
                            LoginModel.LoginReq loginReq = new LoginModel.LoginReq();
                            loginReq.setPhone(phone);
                            loginReq.setLogin_type(AppConstants.LOGIN_TYPE_NORMAL);
                            loginReq.setToken(appPref.getString(AppPref.FCM_TOKEN));
                            login(loginReq);
                        }else {
                            SignUpModel.SignUpReq signUpReq = new SignUpModel.SignUpReq();
                            signUpReq.setName(name);
                            signUpReq.setDevice_type("android");
                            signUpReq.setPassword(password);
                            signUpReq.setPhone(phone);
                            signUpReq.setToken(appPref.getString(AppPref.FCM_TOKEN));
                            signUpReq.setEmail(email);

                            signup(signUpReq);
                        }

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Call Signup api
    public void signup(final SignUpModel.SignUpReq signUpReq)
    {

        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        AppLog.e(TAG,"signupReq : "+signUpReq);
        showLoading();
        apiService.signUp(signUpReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SignUpModel.SignUpRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                        disposable.add(d);

                    }

                    @Override
                    public void onNext(Response<SignUpModel.SignUpRes> signUpRes) {
                        AppLog.e(TAG,"signUpRes :"+signUpRes.body());
                        onsignupRes(signUpRes.body());
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

    //Save Preferences data
    private void onsignupRes(SignUpModel.SignUpRes signUpRes) {
        if(signUpRes.isStatus()){
            appPref.set(AppPref.USER_ID,signUpRes.getProfile().getUserId());
            appPref.set(AppPref.API_KEY,signUpRes.getProfile().getApi_key());
            appPref.set(AppPref.NAME,signUpRes.getProfile().getUsername());
            appPref.set(AppPref.PHONE,signUpRes.getProfile().getPhone());
            appPref.set(AppPref.EMAIL,signUpRes.getProfile().getEmail());
            appPref.set(AppPref.IS_LOGIN,true);

            gotoActivity(Home.class, null, true);
            getActivity().finish();
        }
        else {
            showSnake(binding.llMain,signUpRes.getMsg());
            hideLoading();
            getActivity().onBackPressed();
        }
    }


    private boolean isValid(String pin) {
        if (pin.length()!=4) {
            return false;
        }
        AppLog.e(TAG,"pin: "+pin+" : otp: "+appPref.getString(AppPref.OTP));
        if (!pin.equalsIgnoreCase(appPref.getString(AppPref.OTP))) {
            showToast(getResources().getString(R.string.valid_otp));
            return false;
        }

        return true;
    }

    //Call Login api
    public void login(final LoginModel.LoginReq loginReq)
    {

        if(!isOnline())
        {
            showSnake(binding.llMain,"Internet Not Available , Please Try Again");
            return;
        }

        AppLog.e(TAG,"loginReq : "+loginReq);
        showLoading();
        apiService.login(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginModel.LoginRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        showLoading();
                    }

                    @Override
                    public void onNext(LoginModel.LoginRes loginRes) {
                        AppLog.e(TAG,"LoginRes :"+loginRes);
                        onLoginRes(loginRes);
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

    public void onLoginRes(LoginModel.LoginRes loginRes) {
        if(loginRes.isStatus()){
            appPref.set(AppPref.USER_ID,loginRes.getProfile().getUserId());
            appPref.set(AppPref.API_KEY,loginRes.getProfile().getApi_key());
            appPref.set(AppPref.NAME,loginRes.getProfile().getUsername());
            appPref.set(AppPref.PHONE,loginRes.getProfile().getPhone());
            appPref.set(AppPref.EMAIL,loginRes.getProfile().getEmail());
            appPref.set(AppPref.IS_LOGIN,true);

            gotoActivity(Home.class, null, true);
            getActivity().finish();
        }
        else {
            showToast(loginRes.getMsg());
        }
    }

    private void callApi(String pin) {
        appPref.set(AppPref.IS_LOGIN,true);
        AppLog.e(TAG,"callApi : "+pin);

        gotoActivity(Home.class, null, true);
        getActivity().finish();
    }

    public void setNumber(String ch1,boolean isAdd){
        int len = binding.firstPinView.getText().toString().length();
        if(isAdd){
            if(len!=4){
                binding.firstPinView.setText(binding.firstPinView.getText().toString()+ch1);
            }
        }
        else {
            if(binding.firstPinView.getText().toString().length()>0){
                binding.firstPinView.setText(binding.firstPinView.getText().toString().substring(0,len-1));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        incomingSms = new IncomingSms();
        getActivity().registerReceiver(incomingSms,new IntentFilter("com.google.android.gms.auth.api.phone.SMS_RETRIEVED"));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(incomingSms!=null)
            getActivity().unregisterReceiver(incomingSms);
    }

    //For Fetching sms data
    public class IncomingSms extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e(TAG, "Incoming sms: ......." );

            AppLog.e("BROADCAST", "onReceive");

            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                assert status != null;
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        AppLog.e("BROADCAST", "message : " + message);
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        decode(message);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        AppLog.e("BROADCAST", "TIMEOUT : ");
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }

        private void decode(String message) {
            try {
                String code = parseCode(message);
                binding.firstPinView.setText(code);
                Log.e(TAG, "SMS FETCHING NOW ..decode: "+code );
            } catch (Exception e) {
                AppLog.e("BROADCAST", "Catch1\n" + e.getMessage() + "\n" + e.toString());
            }
        }
        public String parseCode(String message) {
            Pattern p = Pattern.compile("\\b\\d{4}\\b");
            Matcher m = p.matcher(message);
            String code = "";
            while (m.find()) {
                code = m.group(0);
            }
            return code;
        }
    }
}
