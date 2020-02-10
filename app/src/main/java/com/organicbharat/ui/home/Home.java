package com.organicbharat.ui.home;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.databinding.ActivityHomeBinding;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.address.DeliveryAddress;
import com.organicbharat.ui.cart.Cart;
import com.organicbharat.ui.faq.Faq;
import com.organicbharat.ui.login.UserProfile;
import com.organicbharat.ui.my_orders.MyOrders;
import com.organicbharat.utils.AppDialog;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.AppPref;

import java.util.Date;

public class Home extends Base {
    ActivityHomeBinding binding;
    int[] tabIcon = new int[4];
    String[] tabLabels = new String[4];
    private static final String TAG = "Home";
    public TextView cartcount;

    boolean isBackPressedOnce;
    Handler handler;  // For back button Press timer
    Runnable backRunnable; // runnable to close app on back pressed within 3 seconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        //  Crashlytics.getInstance().crash();
        init();

        setToolbar();

        initDrawer();

        setTabLayout();

        changeTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu, menu);
        MenuItem menuCart = menu.findItem(R.id.menu_cart);
        View view = LayoutInflater.from(this).inflate(R.layout.cart_layout, null);
        cartcount = view.findViewById(R.id.tvCount);
        if (appPref.getInt(AppPref.ITEMINCART) > 0) {
            cartcount.setVisibility(View.VISIBLE);
            cartcount.setText("" + appPref.getInt(AppPref.ITEMINCART));
            AppLog.e(TAG, "ITEMINCART: " + appPref.getInt(AppPref.ITEMINCART));
        } else {
            cartcount.setVisibility(View.GONE);
        }
        menuCart.setActionView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(Cart.class, null, false);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        if (isBackPressedOnce){
            isBackPressedOnce=false;
            binding.tabLayout.getTabAt(0).select();
            //this.finish();
        } else {
            isBackPressedOnce = true;

            AppDialog.showConfirmDialog(this, "Press back again to close the app", new AppDialog.AppDialogListener() {
                @Override
                public void onOkClick(DialogInterface dialog) {
                    Home.this.finish();
                    dialog.dismiss();
                }
            });


            handler.postDelayed(backRunnable, 5000);
        }

    }

    private void init() {
        tabIcon[0] = R.drawable.home;
        tabIcon[1] = R.drawable.subsciption;
        tabIcon[2] = R.drawable.category;
        tabIcon[3] = R.drawable.search;

        tabLabels[0] = getString(R.string.lbl_home);
        tabLabels[1] = getString(R.string.lbl_subscription);
        tabLabels[2] = getString(R.string.lbl_categories);
        tabLabels[3] = getString(R.string.lbl_explore);

        AppLog.e(TAG, "key : " + appPref.getString(AppPref.API_KEY));
        AppLog.e(TAG, "User id : " + appPref.getUserId());

        handler = new Handler();
        backRunnable = new Runnable() {
            @Override
            public void run() {
                isBackPressedOnce = false;
            }
        };
    }

    private void setTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab());
        binding.tabLayout.addTab(binding.tabLayout.newTab());
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

    public void selecttab(int pos) {
        binding.tabLayout.getTabAt(pos).select();
    }

    public void changeTab(int position) {
        clearFragment();
        switch (position) {
            case 0:// Home fragment
                changeFrag(HomeFragment.newInstance(null), false, false);
                break;
            case 1:// Subscription Fragment
                changeFrag(SubscriptionFragment.newInstance(null), false, false);
                break;
            case 2:// Category Fragment
                changeFrag(CategoryFragment.newInstance(null), false, false);
                break;
            case 3://Explore Fragment
                changeFrag(ExploreFragment.newInstance(null), false, false);
                break;
        }
    }


    private void createTabs() {
        for (int i = 0; i < tabIcon.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.tab_layout, null);
            ImageView tabImg = view.findViewById(R.id.tabImg);
            TextView tabLabel = view.findViewById(R.id.tabLabel);
            tabImg.setImageResource(tabIcon[i]);
            tabLabel.setText(tabLabels[i]);
            binding.tabLayout.getTabAt(i).setCustomView(view);
        }
    }

    private void initDrawer() {
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawer.openDrawer(Gravity.LEFT);
            }
        });

        View headerView = getLayoutInflater().inflate(R.layout.drawer_header, null);
        TextView tvname = headerView.findViewById(R.id.tvname);
        tvname.setText("" + appPref.getString(AppPref.NAME) + ",");

        TextView tvphone = headerView.findViewById(R.id.tvphone);
        tvphone.setText(appPref.getString(AppPref.PHONE));

        RelativeLayout rl = headerView.findViewById(R.id.navTitle);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

        binding.navView.addHeaderView(headerView);

        AppLog.e(TAG, " " + appPref.getString(AppPref.NAME) + " : " + appPref.getString(AppPref.PHONE));
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                binding.drawer.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()) {
                    default:
                        return true;
                    case R.id.nav_view_home:
                        binding.tabLayout.getTabAt(0).select();
                        return true;
                    case R.id.nav_view_all_categories:
                        binding.tabLayout.getTabAt(2).select();
                        return true;
                    case R.id.nav_view_subscriptions:
                        binding.tabLayout.getTabAt(1).select();
                        return true;
                    case R.id.nav_view_my_orders:
                        gotoActivity(MyOrders.class, null, false);
                        return true;
                    case R.id.nav_view_my_address:
                        AppPref.cart_address = "Home";
                        gotoActivity(DeliveryAddress.class, null, false);
                        return true;
                    case R.id.nav_view_profile:
                        gotoActivity(UserProfile.class, null, false);
                        return true;
                    case R.id.nav_view_faq:
                        gotoActivity(Faq.class, null, false);
                        return true;
                    case R.id.nav_view_logout:
                        showConfirmDialog();
                       // logout();
                        return true;
                }
            }
        });
    }

    private void showConfirmDialog() {

        final Dialog alertDialog=new Dialog(this,android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.setContentView(R.layout.dialog_delete);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtMsg=alertDialog.findViewById(R.id.tvItemName);
        txtMsg.setText("Are you sure want to logout?");
        Button btOk=alertDialog.findViewById(R.id.btnOk);
        Button btCancel=alertDialog.findViewById(R.id.btnCancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                alertDialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void updateCount(int sendValue) {
        AppLog.e(TAG + "TEST123", "Value Is " + sendValue);

        if (cartcount != null) {
            if (sendValue == 0) {
                cartcount.setVisibility(View.GONE);
                cartcount.setText(sendValue + "");
                //  binding.topView.flCart.setVisibility(View.INVISIBLE);

                //    binding.topView.lblNavCart.setText("");

            } else {

                cartcount.setVisibility(View.VISIBLE);
                cartcount.setText(sendValue + "");
            }
        }
    }
}
