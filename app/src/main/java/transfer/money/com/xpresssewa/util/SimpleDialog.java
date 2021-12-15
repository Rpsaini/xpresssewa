package transfer.money.com.xpresssewa.util;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;

import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

//
//public class SimpleDialog {
//}
public class SimpleDialog
{
    //new ColorDrawable(android.graphics.Color.TRANSPARENT)       Dialog Color
    //WindowManager.LayoutParams.MATCH_PARENT                     Dialog Width
    //WindowManager.LayoutParams.MATCH_PARENT                     Dialog Hieght
    //
    private Dialog simpledialog;
    public Dialog simpleDailog(AppCompatActivity appCompatActivity, int view, ColorDrawable colorDrawable, int dialogWidth, int dialogHeight, boolean setCancelable)
    {
        if(simpledialog != null && simpledialog.isShowing()) {
            simpledialog.dismiss();
        }
        simpledialog = new Dialog(appCompatActivity);
        simpledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        simpledialog.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = simpledialog.getWindow();
        lp.copyFrom(window.getAttributes());
        simpledialog.setCancelable(setCancelable);
        lp.width = dialogWidth;
        lp.height = dialogHeight;
        window.setAttributes(lp);
        simpledialog.getWindow().setBackgroundDrawable(colorDrawable);
        simpledialog.show();
        return simpledialog;
    }
}