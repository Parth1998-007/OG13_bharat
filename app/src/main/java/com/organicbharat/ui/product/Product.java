package com.organicbharat.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.cart.Cart;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

public class Product extends Base {

    private static final String TAG = "Product";
    public TextView cartcount;
    ProductFragment productFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_product));
        enableBack(true);
        changeFrag(ProductFragment.newInstance(null),false,false);
        AppLog.e(TAG,"ITEMINCART: "+appPref.getInt(AppPref.ITEMINCART));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu,menu);
        MenuItem menuCart = menu.findItem(R.id.menu_cart);
        View view = LayoutInflater.from(this).inflate(R.layout.cart_layout, null);
        cartcount = view.findViewById(R.id.tvCount);
        if(appPref.getInt(AppPref.ITEMINCART)>0){
            cartcount.setVisibility(View.VISIBLE);
            cartcount.setText(""+appPref.getInt(AppPref.ITEMINCART));
        }else {
            cartcount.setVisibility(View.GONE);
        }
        menuCart.setActionView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(Cart.class,null,false);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void updateCount(int sendValue) {
        AppLog.e(TAG + "TEST123", "Value Is " + sendValue);
        if(cartcount!=null){
            if (sendValue == 0) {
                cartcount.setVisibility(View.GONE);
                cartcount.setText(sendValue + "");
                //  binding.topView.flCart.setVisibility(View.INVISIBLE);

                //    binding.topView.lblNavCart.setText("");

            } else {
                AppLog.e(TAG + "Update_Cart", "Value Is " + sendValue);

                cartcount.setVisibility(View.VISIBLE);
                cartcount.setText(sendValue+"");
            }
        }

    }

}
