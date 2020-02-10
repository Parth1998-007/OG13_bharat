package com.organicbharat.ui.my_orders;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organicbharat.R;
import com.organicbharat.api_model.MySubscriptionOrderModel;
import com.organicbharat.databinding.ItemSubscriptionOrderBinding;
import com.organicbharat.utils.DateTimeHelper;

import java.util.ArrayList;

public class SubscriptionOrderAdapter extends RecyclerView.Adapter<SubscriptionOrderAdapter.ViewHolder> {
    private static final String TAG = "SubscriptionOrderAdapter";
    SubscriptionOrderItemClick orderitemClick;
    Context context;
    private ArrayList<MySubscriptionOrderModel.OrderList> orderList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        ItemSubscriptionOrderBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_subscription_order, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.tvOrderdate.setText(""+ DateTimeHelper.convertFormat(orderList.get(i).getCreated_date(),"yyyy-MM-dd HH:mm:ss","dd-MM-yyyy"));
        viewHolder.binding.tvOrderid.setText(""+orderList.get(i).getUnique_order_id());
        viewHolder.binding.tvOrderamount.setText("₹ "+orderList.get(i).getPayable_amount());
    }

    public void setOrderItemClick(SubscriptionOrderItemClick cartItemClick) {
        this.orderitemClick = cartItemClick;
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void clear() {
        orderList.clear();
    }

    public void addCartItem(ArrayList<MySubscriptionOrderModel.OrderList> orderList) {
        this.orderList.addAll(orderList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSubscriptionOrderBinding binding;
        public ViewHolder(ItemSubscriptionOrderBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            binding.llmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderitemClick.onOrderitemClick(orderList.get(getAdapterPosition()).getOrder_id());
                }
            });
        }
    }

    public interface SubscriptionOrderItemClick {
        public void onOrderitemClick(int orderid);
    }
}
