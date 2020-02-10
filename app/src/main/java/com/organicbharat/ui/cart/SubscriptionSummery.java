package com.organicbharat.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class SubscriptionSummery extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_summery);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_order_summery));
        enableBack(true);
        changeFrag(SubscriptionSummeryFragment.newInstance(null),false,false);
    }


}
