package com.organicbharat.ui.cart;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.RepeatOrder;
import com.organicbharat.utils.AppPref;

import java.util.ArrayList;

public class OrderSummerErrorAdapter extends RecyclerView.Adapter<OrderSummerErrorAdapter.ViewHolder> {

    private static final String TAG = "OrderSummerErrorAdapter";
    Context context;
    AppPref appPref;
    private ArrayList<RepeatOrder.ErrorItem> dataList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_summery,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvname.setText(dataList.get(i).getProduct());
        viewHolder.tvqty.setText(""+dataList.get(i).getStatus());
        viewHolder.tvnewprice.setText(dataList.get(i).getProduct_price());
        appPref.set("productQty",dataList.get(i).getStatus());
        appPref.set("productId",dataList.get(i).getId());

    }

    public void clear() {
        dataList.clear();
    }

    public void addCartItem(ArrayList<RepeatOrder.ErrorItem> productList) {
        this.dataList.addAll(productList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOldprice,tvnewprice,tvname,tvqty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tv_productname);
            tvnewprice = itemView.findViewById(R.id.tv_newprice);
            tvqty = itemView.findViewById(R.id.tvqty);
        }
    }
}
