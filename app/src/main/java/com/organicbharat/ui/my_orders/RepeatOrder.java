package com.organicbharat.ui.my_orders;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class RepeatOrder extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_order);

        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_add_subscription));
        enableBack(true);
        changeFrag(RepeatOrderFragment.newInstance(null),false,false);
    }
}
