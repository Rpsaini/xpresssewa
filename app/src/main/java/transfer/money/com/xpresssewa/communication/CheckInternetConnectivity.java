package transfer.money.com.xpresssewa.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import transfer.money.com.xpresssewa.validation.Showtoast;


public class CheckInternetConnectivity extends BroadcastReceiver {
    Context ct;



    @Override
    public void onReceive(Context context, Intent intent) {


        System.out.println("on broadcast receiver has been called=="+ct);


        if (checkWifiConnect(context)) {


                new Showtoast().showToast(ct, "Internet","Internet Connected",null);

        } else {
                new Showtoast().showToast(ct, "Internet","Internet connectivity issue",null);


        }
    }

    private boolean checkWifiConnect(Context ct) {
        ConnectivityManager manager = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
