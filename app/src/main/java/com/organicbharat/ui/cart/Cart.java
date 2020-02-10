package com.organicbharat.ui.cart;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.home.Home;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

public class Cart extends Base {

    private static final String TAG = "Cart";
    public TextView cartcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_my_cart));
        enableBack(true);
        changeFrag(CartFragment.newInstance(null),false,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu,menu);
        MenuItem menuCart = menu.findItem(R.id.menu_cart);
        View view = LayoutInflater.from(this).inflate(R.layout.cart_layout, null);
        cartcount = view.findViewById(R.id.tvCount);
        menuCart.setVisible(false);
        if(appPref.getInt(AppPref.ITEMINCART)>0){
            cartcount.setVisibility(View.VISIBLE);
            cartcount.setText(""+appPref.getInt(AppPref.ITEMINCART));
        }else {
            cartcount.setVisibility(View.GONE);
        }
        menuCart.setActionView(view);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(Cart.class,null,false);
            }
        });*/
        return super.onCreateOptionsMenu(menu);
    }
    //counts update
    public void updateCount(int sendValue) {
        AppLog.e(TAG + "TEST123", "Value Is " + sendValue);

        if (sendValue == 0) {
            cartcount.setVisibility(View.GONE);
            cartcount.setText(sendValue + "");
            //  binding.topView.flCart.setVisibility(View.INVISIBLE);

            //    binding.topView.lblNavCart.setText("");

        } else {

            cartcount.setVisibility(View.VISIBLE);

            cartcount.setVisibility(View.VISIBLE);
            cartcount.setText(sendValue + "");
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0){
            super.onBackPressed();
        }
        else {
            gotoActivity(Home.class,null,true);
            finish();
        }

    }
}
