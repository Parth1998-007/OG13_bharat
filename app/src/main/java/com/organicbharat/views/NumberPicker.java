package com.organicbharat.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organicbharat.R;

public class NumberPicker extends Fragment {
    int number=0;
    ImageView imgMinus;
    ImageView imgPlus;
    TextView tvNumber;
    private View view;
    private int min=0,max=100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_number_picker,container,false);
        imgMinus=view.findViewById(R.id.imgMinus);
        imgPlus=view.findViewById(R.id.imgPlus);
        tvNumber=view.findViewById(R.id.tvNumber);

        setViews();

        return view;
    }

    private void setViews() {
        number=min;
        tvNumber.setText(number+"");
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus();
            }
        });
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus();
            }
        });

    }

    private void minus() {
        if(number>min){
            number--;
            tvNumber.setText(number+"");
        }
    }

    private void plus() {
        if(number<max){
            number++;
            tvNumber.setText(number+"");
        }
    }
    public int getNumber()
    {
        return number;
    }
}
