package com.organicbharat.ui.my_orders;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class SubscriptionOrderDetails extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_order_details);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_order_details));
        enableBack(true);
        changeFrag(SubscriptionOrderDetailFragment.newInstance(null),false,false);
    }
}
