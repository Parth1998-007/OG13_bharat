package com.organicbharat.ui.login;

import android.os.Bundle;

import com.organicbharat.R;
import com.organicbharat.ui.Base;


public class Login extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeFrag(LoginFragment.newInstance(null),false,false);
    }
}
