package com.organicbharat.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class SubscriptionProductDetail extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_product_detail);

        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_product_detail));
        enableBack(true);
        changeFrag(SubscritionProductFragment.newInstance(null),false,false);
    }
}
