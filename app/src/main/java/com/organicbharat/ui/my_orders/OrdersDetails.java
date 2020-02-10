package com.organicbharat.ui.my_orders;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class OrdersDetails extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_details);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_order_details));
        enableBack(true);
        changeFrag(OrderDetailFragment.newInstance(null),false,false);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0){
            super.onBackPressed();
        }
        else {
            gotoActivity(MyOrders.class,null,true);
            finish();
        }
    }
}
