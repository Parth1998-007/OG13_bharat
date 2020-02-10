package com.organicbharat.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.organicbharat.R;
import com.organicbharat.api_model.BaseRes;
import com.organicbharat.network.ApiService;
import com.organicbharat.network.RetroClient;
import com.organicbharat.ui.login.Login;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;

public class Base extends AppCompatActivity {

    public Toolbar toolbar;
    protected AppPref appPref;
    protected ApiService apiService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Dialog dialog;
    public static final String TAG="BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPref=AppPref.getInstance(this);
        apiService = RetroClient.getApiService(getHttpClient());
    }

    public void setToolbar()
    {
        toolbar=findViewById(R.id.toolBar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        }
    }
    public void setToolbar(String title)
    {
        toolbar=findViewById(R.id.toolBar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
        }
    }

    public void setToolbarTitle(String title)
    {
        if(toolbar!=null) {
            getSupportActionBar().setTitle(title);
        }
    }
    public void enableBack(boolean isBack)
    {
        if(toolbar!=null)
        {
            if(isBack)
            {
                toolbar.setNavigationIcon(R.drawable.back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
            else
            {
                toolbar.setNavigationIcon(null);
            }

        }
    }



    public boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                return cm.getActiveNetworkInfo().isConnectedOrConnecting();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }



    public  boolean hasPermission( String[] permissions) {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && permissions!=null)
        {
            for(String permission:permissions)
            {
                if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }
    public void gotoActivity(Class className, Bundle bundle, boolean isClearStack)
    {
        Intent intent=new Intent(this,className);

        if(bundle!=null)
            intent.putExtras(bundle);

        if(isClearStack)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
    public void gotoActivity(@NonNull Intent intent, boolean isClearStack)
    {
        if(isClearStack)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Snackbar method
    public void showSnake(View view, String msg) {
        final Snackbar snackbar = Snackbar.make(
                view,
                msg,
                Snackbar.LENGTH_INDEFINITE);
        
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#FFA500"));

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void changeFrag(Fragment fragment, boolean isBackStack, boolean isPopBack)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if(isPopBack)
        {
            fm.popBackStack();

        }
        if(isBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }
    public void clearFragment(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void changeFrag(Fragment fragment, boolean isBackStack, boolean isPopBack, int resourceId)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if(isPopBack)
        {
            fm.popBackStack();

        }
        if(isBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(resourceId,fragment);
        fragmentTransaction.commit();
    }
    public void logout()
    {
        appPref.clearData();
        gotoActivity(Login.class,null,true);
        finish();
    }

    public  void showLoading()
    {
        if(dialog!=null)
            hideLoading();

        if(dialog==null)
        {
            dialog=new Dialog(this);
            if(dialog.getWindow()!=null)
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading_bar);
        }
        if(!dialog.isShowing())
            dialog.show();
    }
    public  void hideLoading()
    {
        if(dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public boolean isSuccess(Response response){
        AppLog.e(TAG,"code :"+response.code());
        if(response.code()==200){

            return true;
        }
        else if(response.code()==401) {
            logout();
            return false;
        }else {
            return false;
        }

    }

    public OkHttpClient getHttpClient() {
        return new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                /*Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic("aUsername", "aPassword"));*/

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",appPref.getString(AppPref.API_KEY));

                Log.e("AUTHORIZATION", "key is " +appPref.getString(AppPref.API_KEY));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
    }

    public void onFailure(Throwable e) {

        AppLog.e(TAG, "onError() : " + e.getMessage());
        showToast(getString(R.string.msg_try_again));
        hideLoading();
    }

    public void onDone() {
        AppLog.e(TAG, "onComplete()");
        hideLoading();
    }

    public void subscribe(Disposable d) {
        compositeDisposable.add(d);
        showLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }


}
