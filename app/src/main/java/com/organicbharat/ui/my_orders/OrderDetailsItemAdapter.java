package com.organicbharat.ui.my_orders;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.MyOrderModel;

import java.util.ArrayList;

public class OrderDetailsItemAdapter extends RecyclerView.Adapter<OrderDetailsItemAdapter.ViewHolder> {

    private static final String TAG = "OrderDetailsItemAdapter";
    Context context;
    private ArrayList<MyOrderModel.OrderItem> orderitemList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.tvqty.setText("QTY: "+orderitemList.get(i).getQuantity());
        viewHolder.tvprodname.setText(""+orderitemList.get(i).getProduct_name());
       // int total = orderitemList.get(i).getQuantity() * Integer.parseInt(orderitemList.get(i).getProduct_price());
        viewHolder.tvprice.setText(" "+orderitemList.get(i).getProduct_price());
    }

    public void clear() {
        orderitemList.clear();
    }

    public void addCartItem(ArrayList<MyOrderModel.OrderItem> orderList) {
        this.orderitemList.addAll(orderList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderitemList != null ? orderitemList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvprodname,tvprice,tvqty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvprodname = itemView.findViewById(R.id.tvItemName);
            tvprice = itemView.findViewById(R.id.tvPrice);
            tvqty = itemView.findViewById(R.id.tvQty);
        }
    }
}
