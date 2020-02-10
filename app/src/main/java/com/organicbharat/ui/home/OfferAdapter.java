package com.organicbharat.ui.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.organicbharat.R;
import com.organicbharat.api_model.OfferListModel;
import com.organicbharat.image_utils.ImageBinding;

import java.util.ArrayList;

//Home Screen Slider And Image loading into adapter
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    Context context;
    ArrayList<OfferListModel.OfferList> offerList=new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offer,viewGroup,false);
        return new ViewHolder(view);
    }
    public void addOffers(ArrayList<OfferListModel.OfferList> offerList){
        this.offerList.addAll(offerList);
        notifyDataSetChanged();
    }

    public void clear(){
        offerList.clear();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageBinding.loadImage(viewHolder.imageView,offerList.get(i%offerList.size()).getOffer());
    }

    @Override
    public int getItemCount() {
        return offerList!=null && offerList.size()>0?Integer.MAX_VALUE:0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgOffer);
        }
    }

}
