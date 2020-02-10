package com.organicbharat.ui.address;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.organicbharat.R;
import com.organicbharat.api_model.AddressModel;
import com.organicbharat.databinding.ItemAddressListBinding;

import java.util.ArrayList;

public class DeliveryAddressListAdapter extends RecyclerView.Adapter<DeliveryAddressListAdapter.MyDeliveryAddress> {
    Context context;
    private ArrayList<AddressModel.Data> addressListRes = new ArrayList<>();
    private AddressItemClick addressItemClick;


    @NonNull
    @Override
    public MyDeliveryAddress onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        ItemAddressListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_address_list, viewGroup, false);
        return new MyDeliveryAddress(binding);
    }

    public void clear() {
        addressListRes.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDeliveryAddress myDeliveryAddress, int i) {
        myDeliveryAddress.binding.tvAddress.setText("" + addressListRes.get(i).getAddress() + ",");
        myDeliveryAddress.binding.tvcity.setText("" + addressListRes.get(i).getCity());
        myDeliveryAddress.binding.tvpincode.setText(" - " + addressListRes.get(i).getPincode());

        //Check Is Default and checked checkbox



        if(addressListRes.get(i).getIsdefault() == 1){
            myDeliveryAddress.binding.cbAddress.setChecked(true);
        }else {
            if(addressListRes.size()==1){
                myDeliveryAddress.binding.cbAddress.setChecked(true);
            }else {
                myDeliveryAddress.binding.cbAddress.setChecked(false);
            }
        }


    }

    @Override
    public int getItemCount() {
        return addressListRes != null ? addressListRes.size() : 0;
    }

    public void addAddress(ArrayList<AddressModel.Data> dataList) {
        addressListRes.addAll(dataList);
        notifyDataSetChanged();
    }

    class MyDeliveryAddress extends RecyclerView.ViewHolder {

        private final ItemAddressListBinding binding;

        public MyDeliveryAddress(@NonNull ItemAddressListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeliveryAddress.addAddress = 0;
                    Bundle b = new Bundle();
                    b.putString("list_address", new Gson().toJson(addressListRes.get(getAdapterPosition())));
                    ((DeliveryAddress) context).changeFrag(DeliveryShowAddressFragment.newInstance(b), true, false);
                }
            });
            binding.cbAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     addressItemClick.onAddressItemClick(addressListRes.get(getAdapterPosition()));
                }
            });
            binding.llRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressItemClick.onAddressDeleteClick(addressListRes.get(getAdapterPosition()));
                }
            });
        }
    }

    //Itenfacess
    public interface AddressItemClick {
        public void onAddressItemClick(AddressModel.Data model);

        void onAddressDeleteClick(AddressModel.Data data);
    }

    public void setAddressItemClick(AddressItemClick cartItemClick) {
        this.addressItemClick = cartItemClick;
    }
}
