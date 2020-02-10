package com.organicbharat.ui.cart;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.organicbharat.R;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.image_utils.ImageBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    CartItemClick cartitemClick;
    Context context;
    boolean lblOldPrice;

    private ArrayList<MyCartModel.Data> dataList = new ArrayList<>();

    public CartAdapter(boolean lblOldPrice){
        this.lblOldPrice=lblOldPrice;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart,viewGroup,false);
        return new ViewHolder(view);
    }

    //Add data into array list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //ImageBinding.loadImage(viewHolder.ivproduct, productList.get(i).getProd_image());

        ImageBinding.loadImage(viewHolder.imgProduct, dataList.get(i).getProd_image());
        viewHolder.tvname.setText(dataList.get(i).getProduct());
        viewHolder.tvnewprice.setText(dataList.get(i).getDiscounted_rate());
        viewHolder.tvOldprice.setText(dataList.get(i).getRate());
        viewHolder.tvqty.setText(""+dataList.get(i).getQty());

        if (lblOldPrice){
            viewHolder.tvOldprice.setVisibility(View.VISIBLE);
        }



    }

    public void setCartItemClick(CartItemClick cartItemClick) {
        this.cartitemClick = cartItemClick;
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
        ImageView ivdelete,imgProduct,ivdes,ivinc;
        TextView tvOldprice,tvnewprice,tvname,tvqty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivdelete = itemView.findViewById(R.id.iv_delete);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            ivdes = itemView.findViewById(R.id.iv_dec);
            ivinc = itemView.findViewById(R.id.iv_inc);
            tvname = itemView.findViewById(R.id.tv_productname);
            tvnewprice = itemView.findViewById(R.id.tv_newprice);
            tvqty = itemView.findViewById(R.id.tvqty);
            tvOldprice = itemView.findViewById(R.id.tvOldPrice);
            tvOldprice.setPaintFlags(tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartitemClick.onCartitemdeleteClick(dataList.get(getAdapterPosition()).getProduct_id(),dataList.get(getAdapterPosition()).getQty());
                }
            });

            ivinc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartitemClick.onCartQtyInc(dataList.get(getAdapterPosition()).getProduct_id(),dataList.get(getAdapterPosition()));
                }
            });

            ivdes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartitemClick.onCartQtyDec(dataList.get(getAdapterPosition()).getProduct_id(),dataList.get(getAdapterPosition()));
                }
            });


        }
    }
    //Interfaces for action

    public interface CartItemClick {
        public void onCartitemdeleteClick(int prodid,int qty);
        public void onCartQtyInc(int prodid, MyCartModel.Data data);

        public void onCartQtyDec(int prodid, MyCartModel.Data data);
    }

}
