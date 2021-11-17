package transfer.money.com.xpresssewa.util;

import android.graphics.Typeface;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DefaultConstatnts {
    public static String pinSetup="pinSetup";
    public static String pinVerify="pinVerify";
    public static String pinKey="pinKey";
    public static String Pin="Pin";

    public static String MemberId="MemberId";

    public static String IsShowPin="IsShowPin";
    public static String login_detail="login_detail";
    public static String IsTutorialDone="IsTutorialDone";
    public static String IsKycApproved="IsKycApproved";
    public static String UserName="UserName";
    public static boolean isKyDockUploaded=false;
    public static String getNotificationData="notificationdata";
    public static String notification_count="notification_count";



    public static void setFont(AppCompatActivity appCompatActivity, View ViFont)
    {
        Typeface face= Typeface.createFromAsset(appCompatActivity.getAssets(), "MontserratRegular.ttf");
        if(ViFont instanceof TextView)
        {
            ((TextView)ViFont).setTypeface(face);
        }
        else if(ViFont instanceof EditText)
        {
            ((EditText)ViFont).setTypeface(face);
        }

    }

}
