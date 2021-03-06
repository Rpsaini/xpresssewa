package transfer.money.com.xpresssewa.validation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.registration.MobileNumberActivity;
import transfer.money.com.xpresssewa.registration.SignUpActivity;


public class Showtoast {
    private Dialog toastDialog;

    public Showtoast() {

    }

    public void showToast(final Context act, final String title, String msg, View view) {
        try
        {
         if (toastDialog != null && toastDialog.isShowing()) {
            toastDialog.dismiss();
        }
        toastDialog = new Dialog(act);
        toastDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(title.equalsIgnoreCase("Internet"))
        {
            toastDialog.setContentView(R.layout.internetdialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = toastDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            toastDialog.setCancelable(true);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            toastDialog.show();

            TextView noconnection=toastDialog.findViewById(R.id.noconnection);
            noconnection.setText(title);

            toastDialog.findViewById(R.id.internetouter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toastDialog.hide();
                }
            });

        }
        else if(title.equalsIgnoreCase("Communication"))
        {
            toastDialog.setContentView(R.layout.internetdialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = toastDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            toastDialog.setCancelable(true);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            toastDialog.show();
            TextView noconnection=toastDialog.findViewById(R.id.noconnection);
            noconnection.setText(title);

            ImageView nointernetimage=toastDialog.findViewById(R.id.nointernetimage);

            toastDialog.findViewById(R.id.internetouter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toastDialog.hide();
                }
            });


        }
        else if(title.equalsIgnoreCase("Unauthorized"))
        {
            toastDialog.setContentView(R.layout.internetdialog);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = toastDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            toastDialog.setCancelable(true);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            toastDialog.show();
            ImageView nointernetimage=toastDialog.findViewById(R.id.nointernetimage);
            nointernetimage.setImageResource(R.drawable.anauthorized);
            TextView noconnection=toastDialog.findViewById(R.id.noconnection);
            noconnection.setText(title);

            toastDialog.findViewById(R.id.internetouter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toastDialog.hide();
                }
            });
        }
        else
        {
            Dialog validationPopup;
            validationPopup = new Dialog(act);
            validationPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            validationPopup.setContentView(R.layout.toastdialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = validationPopup.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            validationPopup.setCancelable(true);
            validationPopup.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
            validationPopup.getWindow().setDimAmount(0);
            validationPopup.show();
            TextView popuptitle=validationPopup.findViewById(R.id.popuptitle);
            popuptitle.setText(msg);
            TextView okaybtn = validationPopup.findViewById(R.id.okaybtn);
            okaybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validationPopup.dismiss();

                }
            });





 //           Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
   //         snackbar.show();
        }

        } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
