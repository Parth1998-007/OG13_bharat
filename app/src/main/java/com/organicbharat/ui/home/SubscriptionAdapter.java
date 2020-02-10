package com.organicbharat.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.image_utils.ImageBinding;

import java.util.ArrayList;
//set Subscription data and create Interface for action
public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    Context context;
    private ArrayList<ProductModel.ProductData> productlist=new ArrayList<>();
    subscriptionClick subscriptionClick;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subscription,viewGroup,false);
        return new ViewHolder(view);
    }

    public void setSubscriptionClick(subscriptionClick subscriptionClick) {
        this.subscriptionClick = subscriptionClick;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageBinding.loadImage(viewHolder.imgSubscription,productlist.get(i).getProd_image());
        viewHolder.tvrate.setText("@"+productlist.get(i).getRate()+" Per Ltr");
    }

    @Override
    public int getItemCount() {
        return productlist!=null?productlist.size():0;
    }

    public void addSubscription(ArrayList<ProductModel.ProductData> productlist) {
        this.productlist.addAll(productlist);
        notifyDataSetChanged();
    }

    public void clear() {
        productlist.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSubscription;
        TextView tvrate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSubscription = itemView.findViewById(R.id.imgSubscription);
            tvrate = itemView.findViewById(R.id.tv_size);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(subscriptionClick != null){
                        subscriptionClick.onsubscriptionClick(productlist.get(getAdapterPosition()).getProduct_id());
                    }
                }
            });
        }
    }
    public interface subscriptionClick{
        public void onsubscriptionClick(int prodid);
    }



}
