package com.organicbharat.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.organicbharat.R;
import com.organicbharat.api_model.MyCartModel;
import com.organicbharat.api_model.ProductModel;

import com.organicbharat.image_utils.ImageBinding;
import com.organicbharat.utils.AppLog;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private static final String TAG = "ProductAdapter";
    ProductClick productClick;
    Context context;
    private ArrayList<ProductModel.ProductData> productList = new ArrayList<>();
    private ArrayList<MyCartModel.Data> cartList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setProductClick(ProductClick productClick) {
        this.productClick = productClick;
    }

    public void clear() {
        productList.clear();
        notifyDataSetChanged();
    }

    public void addCategory(ArrayList<ProductModel.ProductData> productList) {
        this.productList.addAll(productList);
        AppLog.e(TAG,"productlistsize:  "+productList.size());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        ImageBinding.loadImage(viewHolder.ivproduct, productList.get(i).getProd_image());
        viewHolder.tvName.setText(productList.get(i).getProduct());
        viewHolder.tvnewprice.setText(productList.get(i).getDiscounted_rate());
        viewHolder.tvOldPrice.setText(productList.get(i).getRate());
        viewHolder.tvproductdetails.setText(productList.get(i).getDescription());
        //viewHolder.tvqty.setText(""+productList.get(i).getQty());
        viewHolder.tvqty.setText(productList.get(i).getItem_in_cart() + "");
        viewHolder.tv_productsize.setText(productList.get(i).getSize() + "( Delivery Time : "+productList.get(i).getDelivery_time()+" )");

        if (productList.get(i).getAvailibility() ==1) {
            viewHolder.tvproductstatus.setVisibility(View.INVISIBLE);
            viewHolder.lladd.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tvproductstatus.setVisibility(View.VISIBLE);
            viewHolder.lladd.setVisibility(View.INVISIBLE);
        }

       /* if (productList.get(i).getItem_in_cart()>0)//productList.get(i).getCount() > 0
        {
            viewHolder.tvqty.setText(productList.get(i).getItem_in_cart() + "");
            viewHolder.btadd.setVisibility(View.GONE);
            viewHolder.lladd.setVisibility(View.VISIBLE);
            AppLog.e(TAG," product adapter hereeee1");
        } else {
            AppLog.e(TAG," product adapter hereeee2");
            viewHolder.btadd.setVisibility(View.GONE);
            viewHolder.lladd.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;//7
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOldPrice;
        ImageView ivproduct, ivinc, ivdes;
        TextView tvName, tvproductdetails, tvnewprice, tvqty,tvproductstatus,tv_productsize;
        Button btadd;
        LinearLayout lladd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_productsize = itemView.findViewById(R.id.tv_productsize);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            ivproduct = itemView.findViewById(R.id.imgSubscription);
            tvName = itemView.findViewById(R.id.tv_productname);
            tvproductdetails = itemView.findViewById(R.id.tv_productdetail);
            tvproductstatus = itemView.findViewById(R.id.tvproductstatus);
            tvnewprice = itemView.findViewById(R.id.tv_newprice);
            tvqty = itemView.findViewById(R.id.tvqty);
            ivinc = itemView.findViewById(R.id.iv_inc);
            ivdes = itemView.findViewById(R.id.iv_dec);
            btadd = itemView.findViewById(R.id.btadd);
            lladd = itemView.findViewById(R.id.lladd);
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            ivproduct.setOnClickListener(this);
            tvproductdetails.setOnClickListener(this);
            tvName.setOnClickListener(this);
            ivinc.setOnClickListener(this);
            ivdes.setOnClickListener(this);
            btadd.setOnClickListener(this);
            lladd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgSubscription:
                case R.id.tv_productdetail:
                case R.id.tvname:
                    if (productClick != null) {
                        productClick.onProductClick(productList.get(getAdapterPosition()).getProduct_id(),productList.get(getAdapterPosition()).getItem_in_cart());
                    }
                    break;
                case R.id.btadd: {
                    if (productList.get(getAdapterPosition()).getAvailibility() == 1) {
                        productClick.onProductQtyInc(productList.get(getAdapterPosition()));
                        lladd.setVisibility(View.VISIBLE);
                        btadd.setVisibility(View.GONE);
                    } else {
                        lladd.setVisibility(View.GONE);
                        btadd.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case R.id.iv_inc:
                    AppLog.e(TAG, " " + tvqty.getText().toString() + " : " + productList.get(getAdapterPosition()).getQty());
                    if (productList.get(getAdapterPosition()).getAvailibility() ==1) {
                        increment();
                    } else {
                        Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.iv_dec:
                    if (productList.get(getAdapterPosition()).getAvailibility() == 1) {
                        decrement();
                    } else {
                        Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        private void increment() {
            if (productClick != null) {
                productClick.onProductQtyInc(productList.get(getAdapterPosition()));
            }
        }

        private void decrement() {
            if (productClick != null) {
                productClick.onProductQtyDec(productList.get(getAdapterPosition()));
            }
        }

    }

    public interface ProductClick {
        public void onProductClick(int prodid, int item_in_cart);

        public void onProductQtyInc(ProductModel.ProductData productData);

        public void onProductQtyDec(ProductModel.ProductData productData);
    }

}
