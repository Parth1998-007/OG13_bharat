package com.organicbharat.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.organicbharat.R;
import com.organicbharat.ui.Base;

public class AddSubscription extends Base {

    private static final String TAG = "AddSubscription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_add_subscription));
        enableBack(true);
        changeFrag(AddSubscriptionFragment.newInstance(null),false,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
