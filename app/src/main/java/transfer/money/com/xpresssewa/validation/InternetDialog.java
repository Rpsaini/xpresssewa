package transfer.money.com.xpresssewa.validation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import transfer.money.com.xpresssewa.R;



public class InternetDialog {
    private Dialog toastDialog;

    public InternetDialog() {

    }

    public Dialog InternetDialog(final Context act, final String... msg) {
        try {
            if (toastDialog != null && toastDialog.isShowing()) {
                toastDialog.dismiss();
            }
            toastDialog = new Dialog(act);
            toastDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

            toastDialog.findViewById(R.id.internetouter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toastDialog.dismiss();
                }
            });


            toastDialog.findViewById(R.id.ll_inner_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toastDialog.dismiss();
                }
            });


         }
        catch (Exception e) {
            e.printStackTrace();
        }
        return toastDialog;
    }


}