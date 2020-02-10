package com.organicbharat.ui.my_orders;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import com.organicbharat.R;

import com.organicbharat.databinding.ActivityMyOrdersBinding;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.BaseFragment;
import com.organicbharat.ui.home.Home;

public class MyOrders extends Base {
    ActivityMyOrdersBinding binding;
    String[] tabLabels=new String[2];
    int value;
    private static final String TAG = "MyOrders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
       // setContentView(R.layout.activity_my_orders);


        init();

        setToolbar();

        setToolbarTitle(getResources().getString(R.string.title_my_orders));

        enableBack(true);

        setTabLayout();
        if(BaseFragment.sub_script==1){
            BaseFragment.sub_script=0;
//            changeTab(1);
            binding.tabLayout.getTabAt(1).select();
        }else {
            changeTab(0);
        }
       // changeFrag(MyOrdersFragment.newInstance(null),false,false);
    }


    private void init() {


        tabLabels[0]=getString(R.string.lbl_order);
        tabLabels[1]=getString(R.string.lbl_subscription_order);

    }



    private void changeTab(int position) {
        clearFragment();
        switch (position){
            case 0:// MyOrder fragment
                changeFrag(MyOrdersFragment.newInstance(null),false,false);
                break;
            case 1:// Subscription Fragment
                changeFrag(SubscriptionOrderFragment.newInstance(null),false,false);
                break;
        }
    }

    private void setTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab());
        binding.tabLayout.addTab(binding.tabLayout.newTab());

        createTabs();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createTabs() {
        for (int i=0;i<tabLabels.length;i++){

            View view = getLayoutInflater().inflate(R.layout.tab_layout_order,null);
            TextView tabLabel=view.findViewById(R.id.tabLabel);
            tabLabel.setText(tabLabels[i]);
            binding.tabLayout.getTabAt(i).setCustomView(view);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.home_option_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
