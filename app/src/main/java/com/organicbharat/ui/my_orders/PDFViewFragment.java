package com.organicbharat.ui.my_orders;


import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.organicbharat.R;
import com.organicbharat.databinding.FragmentPdfviewBinding;
import com.organicbharat.utils.AppLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PDFViewFragment extends Fragment {


    private FragmentPdfviewBinding binding;
    private String TAG="PDFViewFragment";
    String pdf_path = "";

    public PDFViewFragment() {
        // Required empty public constructor
    }

    public static PDFViewFragment newInstance(Bundle args) {
        PDFViewFragment fragment = new PDFViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pdfview, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        if(getArguments() != null){
            pdf_path = getArguments().getString("pdf_path");
        }

        WebSettings settings = binding.wvPdfviewr.getSettings();
        settings.setJavaScriptEnabled(true);
        binding.wvPdfviewr.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        PdfWebViewClient pdfWebViewClient = new PdfWebViewClient(getContext(), binding.wvPdfviewr);
        AppLog.e(TAG," "+pdf_path);
        if (getArguments() != null) {
            pdfWebViewClient.loadPdfUrl("https://docs.google.com/gview?embedded=true&url=" + pdf_path);
        }
    }

}
