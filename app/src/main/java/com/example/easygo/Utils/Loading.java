package com.example.easygo.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class Loading {

    ProgressDialog progress;
    Context context ;

    public Loading(Context context) {
        this.context = context;
        progress = new ProgressDialog(context);
    }
    public void showCancelBooking(){
        progress.setTitle("Cancel Booking");
        progress.setMessage("Cancel Booking . . .");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    public void showBooking(){
        progress.setTitle("Booking");
        progress.setMessage("Booking in progress . . .");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    public void showLoading(int percent){
        progress.setTitle(percent +" %");
        progress.setMessage("Uploading . . .");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    public void showLoading(){
        progress.setTitle("تحميل");
        progress.setMessage("جاري جاري ارسال الكود . . .");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void update(int percent){
        progress.setTitle(percent +" %");
    }

    public void dismissLoading(){
        progress.dismiss();
    }
}
