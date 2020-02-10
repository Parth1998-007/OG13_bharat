package com.organicbharat.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.Window;


public class AppDialog {
    static Dialog dialog;

    public static Dialog showNoNetworkDialog(Context context) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       /* dialog.setContentView(R.layout.dialog_no_network);
        dialog.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        dialog.show();
        return dialog;
    }

    public static Dialog showRetryDialog(Context context, final RetryListener retryListener) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       /* dialog.setContentView(R.layout.dialog_retry);

        dialog.findViewById(R.id.tvRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryListener.onRetryClick(dialog);
            }
        });*/
        dialog.show();

        return dialog;
    }

    public interface RetryListener {
        void onRetryClick(Dialog dialog);
    }
    public static Dialog showConfirmDialog(Context context, String msg, final AppDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null){
                    listener.onOkClick(dialog);
                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        return dialog;
    }

    public interface AppDialogListener{
        public void onOkClick(DialogInterface dialog);
    }

}
