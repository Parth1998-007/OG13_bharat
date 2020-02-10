package com.organicbharat.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.ProductModel;
import com.organicbharat.image_utils.ImageBinding;
import com.organicbharat.utils.AppLog;
import com.organicbharat.utils.DateTimeHelper;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.internal.operators.parallel.ParallelRunOn;

public class SubscriptionProductAdapter extends RecyclerView.Adapter<SubscriptionProductAdapter.ViewHolder> {

    Context context;
    private static final String TAG = "SubscriptionProductAdapter";
    private ArrayList<ProductModel.ProductData> productlist = new ArrayList<>();
    subscriptionproductClick subscriptionClick;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subscription_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageBinding.loadImage(viewHolder.ivprodimage, productlist.get(i).getProd_image());
        viewHolder.tvprodname.setText(productlist.get(i).getProduct());
        viewHolder.tvproddesc.setText(productlist.get(i).getDescription());
        viewHolder.tvProductSize.setText(productlist.get(i).getSize());


        if (productlist.get(i).getIs_subscription() == 1) {

            Log.e(TAG, "onBindViewHolder:..... " + productlist.get(i).getIs_paused());

           if (productlist.get(i).getIs_paused()==1){
                viewHolder.btpause.setVisibility(View.GONE);
                viewHolder.btresume.setVisibility(View.VISIBLE);
            }else {
               viewHolder.btresume.setVisibility(View.GONE);
               viewHolder.btpause.setVisibility(View.GONE);

               viewHolder.llorder.setVisibility(View.VISIBLE);
               // AppLog.e(TAG,"date: "+DateTimeHelper.getDate(productlist.get(i).getEnd_date()));
               Calendar cal = Calendar.getInstance();
               int todate = Integer.valueOf(DateTimeHelper.convertFormat(productlist.get(i).getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(0, 2));
               int tomonth = Integer.valueOf(DateTimeHelper.convertFormat(productlist.get(i).getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(3, 5));
               int toyear = Integer.valueOf(DateTimeHelper.convertFormat(productlist.get(i).getEnd_date(), "yyyy-MM-dd", "dd-MM-yyyy").substring(6, 10));
               AppLog.e(TAG, "Date : " + todate);
               AppLog.e(TAG, "Month : " + tomonth);
               AppLog.e(TAG, "Year : " + toyear);
               cal.set(Calendar.YEAR, toyear);
               cal.set(Calendar.MONTH, tomonth - 1);
               cal.set(Calendar.DAY_OF_MONTH, todate);
               Calendar calendar = Calendar.getInstance();

               if (cal.getTimeInMillis() <= calendar.getTimeInMillis()) {
                   viewHolder.btpause.setVisibility(View.GONE);
                   viewHolder.btresume.setVisibility(View.GONE);
               } else {
                   viewHolder.btpause.setVisibility(View.GONE);
                   viewHolder.btresume.setVisibility(View.GONE);
               }
           }



        } else {
            viewHolder.llorder.setVisibility(View.GONE);
        }
    }

    public void addSubscription(ArrayList<ProductModel.ProductData> productlist) {
        this.productlist.addAll(productlist);
        notifyDataSetChanged();
    }

    public void clear() {
        productlist.clear();
    }

    public void setSubscriptionClick(subscriptionproductClick subscriptionClick) {
        this.subscriptionClick = subscriptionClick;
    }

    @Override
    public int getItemCount() {
        return productlist != null ? productlist.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvprodname, tvproddesc, tvProductSize;
        ImageView ivprodimage;
        LinearLayout llorder;
        Button btresume, btpause, btrepeat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductSize=itemView.findViewById(R.id.tvProductSize);
            ivprodimage = itemView.findViewById(R.id.imgSubscription);
            tvprodname = itemView.findViewById(R.id.tv_prodname);
            tvproddesc = itemView.findViewById(R.id.tv_proddetail);
            llorder = itemView.findViewById(R.id.ll_order);
            btpause = itemView.findViewById(R.id.btPauseOrder);
            btresume = itemView.findViewById(R.id.btresumeOrder);
            btrepeat = itemView.findViewById(R.id.btrepeatOrder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subscriptionClick != null) {
                        subscriptionClick.onsubscriptionproductClick(productlist.get(getAdapterPosition()).getProduct_id());
                    }
                }
            });

            //For Resume Order
            btresume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subscriptionClick != null) {
                        subscriptionClick.onResumeClick(productlist.get(getAdapterPosition()).getOrder_id(),btpause,btresume);
                    }
                    /*
                    btpause.setVisibility(View.VISIBLE);
                    btresume.setVisibility(View.GONE);*/
                }
            });
            //For Repeat Order
            btrepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subscriptionClick != null) {
                        subscriptionClick.onRepeatClick(productlist.get(getAdapterPosition()).getProduct_id());
                    }
                }
            });

            //For Pause Order
            btpause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subscriptionClick != null) {
                        subscriptionClick.onPauseClick(productlist.get(getAdapterPosition()).getOrder_id(), productlist.get(getAdapterPosition()).getEnd_date(),btpause,btresume);
                    }
                  /*  btresume.setVisibility(View.VISIBLE);
                    btpause.setVisibility(View.GONE);*/

                }
            });
        }
    }
    //Intefaces Methods
    public interface subscriptionproductClick {
        void onsubscriptionproductClick(int prodid);

        void onResumeClick(int orderid, Button btpause, Button btresume);

        void onRepeatClick(int prodid);

        void onPauseClick(int orderid, String end_date, Button btpause, Button btresume);
    }

}