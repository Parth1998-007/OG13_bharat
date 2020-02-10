package com.organicbharat.ui.cart;

import android.os.Bundle;
import android.view.Menu;

import com.organicbharat.R;
import com.organicbharat.ui.Base;
import com.organicbharat.ui.my_orders.OrdersDetails;

public class OrderSummery extends Base {

    String action="";
    int orderid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);
        setToolbar();
        setToolbarTitle(getResources().getString(R.string.title_order_summery));
        enableBack(true);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            action = b.getString("action");
            orderid = b.getInt("orderid");
        }

        changeFrag(SummeryFragment.newInstance(null),false,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.home_option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if("repeatOrder".equalsIgnoreCase(action)){
            Bundle b = new Bundle();
            b.putString("action", "clearcart");
            b.putInt("orderid",orderid);
            gotoActivity(OrdersDetails.class,b,true);
        }else {
            super.onBackPressed();

        }

    }
}
