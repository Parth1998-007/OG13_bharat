package com.organicbharat.ui.my_orders;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.MyOrderModel;

import com.organicbharat.databinding.ItemMyOrderBinding;
import com.organicbharat.utils.DateTimeHelper;

import java.util.ArrayList;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private static final String TAG = "MyOrdersAdapter";
    OrderItemClick orderitemClick;
    Context context;
    private ArrayList<MyOrderModel.Order> orderList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
       /* View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order,parent,false);
        return new ViewHolder(view);*/
        ItemMyOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_order, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.binding.tvOrderdate.setText("" + DateTimeHelper.convertFormat(orderList.get(i).getCreated_date(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy"));
        viewHolder.binding.tvOrderid.setText("" + orderList.get(i).getUnique_order_id());
        viewHolder.binding.tvOrderamount.setText("₹ " + orderList.get(i).getPayable_amount());//orderList.get(i).getamount()

        setOrderstatus(i, viewHolder);
    }

    private void setOrderstatus(int i, ViewHolder viewHolder) {
        if (orderList.get(i).getOrder_status().equalsIgnoreCase("Order Placed")) {
            viewHolder.binding.tvOrderplace.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.binding.ivStatusplace0.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_current));
            viewHolder.binding.ivLineplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.tvAccepted.setTextColor(context.getResources().getColor(R.color.colorTextLight));
            viewHolder.binding.ivStatusplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.ivLineplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.ivStatusplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.ivStatusTransit1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.ivLineTransit2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.tvTransit.setTextColor(context.getResources().getColor(R.color.colorTextLight));
            viewHolder.binding.tvDelivered.setTextColor(context.getResources().getColor(R.color.colorTextLight));
        } else if (orderList.get(i).getOrder_status().equalsIgnoreCase("Accepted")) {
            viewHolder.binding.tvOrderplace.setTextColor(context.getResources().getColor(R.color.colorText));
            viewHolder.binding.ivStatusplace0.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));
            viewHolder.binding.tvAccepted.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.binding.ivStatusplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_current));

            viewHolder.binding.ivStatusTransit1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.ivLineTransit2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.tvTransit.setTextColor(context.getResources().getColor(R.color.colorTextLight));


            viewHolder.binding.ivLineplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.ivStatusplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.tvDelivered.setTextColor(context.getResources().getColor(R.color.colorTextLight));
        } else if (orderList.get(i).getOrder_status().equalsIgnoreCase("In Transit")) {
            viewHolder.binding.tvOrderplace.setTextColor(context.getResources().getColor(R.color.colorText));
            viewHolder.binding.ivStatusplace0.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));
            viewHolder.binding.tvAccepted.setTextColor(context.getResources().getColor(R.color.colorText));
            viewHolder.binding.ivStatusplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));
            viewHolder.binding.ivStatusplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));

            viewHolder.binding.ivStatusTransit1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_not_done));
            viewHolder.binding.ivLineTransit2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_not_done));
            viewHolder.binding.tvTransit.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            viewHolder.binding.tvDelivered.setTextColor(context.getResources().getColor(R.color.colorTextLight));
        } else if (orderList.get(i).getOrder_status().equalsIgnoreCase("Delivered")) {
            viewHolder.binding.tvOrderplace.setTextColor(context.getResources().getColor(R.color.colorText));
            viewHolder.binding.ivStatusplace0.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));
            viewHolder.binding.tvAccepted.setTextColor(context.getResources().getColor(R.color.colorText));
            viewHolder.binding.ivStatusplace1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));

            viewHolder.binding.ivStatusTransit1.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_done));
            viewHolder.binding.ivLineTransit2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_line_done));
            viewHolder.binding.tvTransit.setTextColor(context.getResources().getColor(R.color.colorText));

            viewHolder.binding.ivStatusplace2.setImageDrawable(context.getResources().getDrawable(R.drawable.order_status_current));
            viewHolder.binding.tvDelivered.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    public void setOrderItemClick(OrderItemClick cartItemClick) {
        this.orderitemClick = cartItemClick;
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void clear() {
        orderList.clear();
    }

    public void addCartItem(ArrayList<MyOrderModel.Order> orderList) {
        this.orderList.addAll(orderList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMyOrderBinding binding;

        public ViewHolder(ItemMyOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.llmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderitemClick.onOrderitemClick(orderList.get(getAdapterPosition()).getOrder_id());
                }
            });

        }

       /* TextView tvOrderdate,tvOrderamount,tvOrderid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderdate = itemView.findViewById(R.id.tv_orderdate);
            tvOrderamount = itemView.findViewById(R.id.tv_orderamount);
            tvOrderid = itemView.findViewById(R.id.tv_orderid);
        }*/
    }

    public interface OrderItemClick {
        public void onOrderitemClick(int orderid);
    }
}
