package com.organicbharat.ui.cart;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.MyCartModel;

import java.util.ArrayList;

public class OrderSummeryAdapter extends RecyclerView.Adapter<OrderSummeryAdapter.ViewHolder> {

    private static final String TAG = "OrderSummeryAdapter";
    Context context;
    private ArrayList<MyCartModel.Data> dataList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_summery,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvname.setText(dataList.get(i).getProduct());
        viewHolder.tvqty.setText(""+dataList.get(i).getQty());
        viewHolder.tvnewprice.setText(dataList.get(i).getDiscounted_rate());
    }


    public void clear() {
        dataList.clear();
    }

    public void addCartItem(ArrayList<MyCartModel.Data> productList) {
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
