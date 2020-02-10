package com.organicbharat.ui.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organicbharat.R;
import com.organicbharat.api_model.CategoryModel;
import com.organicbharat.image_utils.ImageBinding;

import java.util.ArrayList;

//Set category items in adapter and create inteface for onclick

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder> {
    Context context;
    private ArrayList<CategoryModel.CategoryData> categoryList=new ArrayList<>();
    CategoryClick categoryClick;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_home,viewGroup,false);
        return new ViewHolder(view);
    }

    public void clear(){
        categoryList.clear();
    }

    public void addCategory(ArrayList<CategoryModel.CategoryData> categoryList){
        this.categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }

    public void setCategoryClick(CategoryClick categoryClick) {
        this.categoryClick = categoryClick;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        ImageBinding.loadImage(viewHolder.imgCategory,categoryList.get(i).getCategory_image());
        viewHolder.tvName.setText(categoryList.get(i).getCategory());
    }

    @Override
    public int getItemCount() {
        return categoryList!=null?categoryList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvName = itemView.findViewById(R.id.tvName);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(categoryClick!=null){


                        categoryClick.onCategoryClick(categoryList.get(getAdapterPosition()).getCat_id());

                    }
                }
            });
        }
    }
    interface CategoryClick{
        public void onCategoryClick(int catid);
    }
}
