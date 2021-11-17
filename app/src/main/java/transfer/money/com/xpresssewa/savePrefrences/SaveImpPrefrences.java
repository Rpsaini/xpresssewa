package transfer.money.com.xpresssewa.savePrefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.UtilClass;

public class SaveImpPrefrences
{
    private String prefName = "moneytransfer";

    public void savePrefrencesData(Context ct, Object data, String sharekey)
    {
        SharedPreferences sharedpreferences = ct.getSharedPreferences(prefName, ct.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();


        if (sharekey.equals("url"))
        {
            try {
                editor.putString("url", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
       else if (sharekey.equals("login_detail")) {
            try {
                editor.putString("login_detail", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals(DefaultConstatnts.MemberId)) {
            try {
                editor.putString(DefaultConstatnts.MemberId, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }

        else if (sharekey.equals(DefaultConstatnts.Pin)) {
            try {
                editor.putString(DefaultConstatnts.Pin, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }


        else if (sharekey.equals("device_token")) {
            try {
                editor.putString("device_token", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }


        else if (sharekey.equals("image_url")) {
            try {
                editor.putString("image_url", data+"");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals("socket")) {
            try {
                editor.putString("socket", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }


        else if (sharekey.equals("mapkey")) {
            try {
                editor.putString("mapkey", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }


        else if (sharekey.equals("duty")) {
            try {
                editor.putString("duty", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }

        else if (sharekey.equals("bdetails")) {
            try {
                editor.putString("bdetails", data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals(DefaultConstatnts.IsTutorialDone)) {
            try {
                editor.putString(DefaultConstatnts.IsTutorialDone, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }

            else if (sharekey.equals(DefaultConstatnts.IsKycApproved)) {
            try {
                editor.putString(DefaultConstatnts.IsKycApproved, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }
        }
             else if (sharekey.equals(DefaultConstatnts.UserName)) {
                try {
                    editor.putString(DefaultConstatnts.UserName, data + "");
                } catch (Exception e) {
                    Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
                }

        }

        else if (sharekey.equals(DefaultConstatnts.getNotificationData)) {
            try {
                editor.putString(DefaultConstatnts.getNotificationData, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals(DefaultConstatnts.getNotificationData)) {
            try {
                editor.putString(DefaultConstatnts.getNotificationData, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals(DefaultConstatnts.notification_count)) {
            try {
                editor.putString(DefaultConstatnts.notification_count, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }
        else if (sharekey.equals(UtilClass.isFingerAuth)) {
            try {
                editor.putString(UtilClass.isFingerAuth, data + "");
            } catch (Exception e) {
                Toast.makeText(ct, e.getMessage() + " ", Toast.LENGTH_LONG).show();
            }

        }





        editor.commit();
    }

    public Object reterivePrefrence(Context ct, String shareKey) {
        if (ct != null) {
            SharedPreferences prefs = ct.getSharedPreferences(prefName, ct.MODE_PRIVATE);
            if (shareKey.equals("login_detail")) {
                if (prefs.contains("login_detail"))//sarpanch pass
                {
                    return prefs.getString("login_detail", "");
                }
            }
           else if (shareKey.equals("url")) {
                if (prefs.contains("url"))//sarpanch pass
                {
                    return prefs.getString("url", "");
                }
            }

            else if (shareKey.equals("device_token")) {
                if (prefs.contains("device_token"))// pass
                {
                    return prefs.getString("device_token", "");
                }
            }

            else if (shareKey.equals("image_url")) {
                if (prefs.contains("image_url"))// pass
                {
                    return prefs.getString("image_url", "");
                }
            }

            else if (shareKey.equals("socket")) {
                if (prefs.contains("socket"))// pass
                {
                    return prefs.getString("socket", "");
                }
            }


            else if (shareKey.equals("mapkey")) {
                if (prefs.contains("mapkey"))// pass
                {
                    return prefs.getString("mapkey", "");
                }
            }


            else if (shareKey.equals("duty")) {
                if (prefs.contains("duty"))// pass
                {
                    return prefs.getString("duty", "");
                }
            }


            else if (shareKey.equals("bdetails")) {
                if (prefs.contains("bdetails"))// pass
                {
                    return prefs.getString("bdetails", "");
                }
            }

            else if (shareKey.equals(DefaultConstatnts.MemberId)) {
                if (prefs.contains(DefaultConstatnts.MemberId))// pass
                {
                    return prefs.getString(DefaultConstatnts.MemberId, "");
                }
            }

            else if (shareKey.equals(DefaultConstatnts.Pin)) {
                if (prefs.contains(DefaultConstatnts.Pin))// pass
                {
                    return prefs.getString(DefaultConstatnts.Pin, "");
                }
            }


            else if (shareKey.equals(DefaultConstatnts.IsTutorialDone)) {
                if (prefs.contains(DefaultConstatnts.IsTutorialDone))// pass
                {
                    return prefs.getString(DefaultConstatnts.IsTutorialDone, "");
                }
            }

            else if (shareKey.equals(DefaultConstatnts.IsKycApproved)) {
                if (prefs.contains(DefaultConstatnts.IsKycApproved))// pass
                {
                    return prefs.getString(DefaultConstatnts.IsKycApproved, "");
                }
            }
            else if (shareKey.equals(DefaultConstatnts.UserName)) {
                if (prefs.contains(DefaultConstatnts.UserName))// pass
                {
                    return prefs.getString(DefaultConstatnts.UserName, "");
                }
            }

            else if (shareKey.equals(DefaultConstatnts.getNotificationData))
            {
                if (prefs.contains(DefaultConstatnts.getNotificationData))// pass
                {
                    return prefs.getString(DefaultConstatnts.getNotificationData, "");
                }
            }



            else if (shareKey.equals(DefaultConstatnts.notification_count))
            {
                if (prefs.contains(DefaultConstatnts.notification_count))// pass
                {
                    return prefs.getString(DefaultConstatnts.notification_count, "");
                }
            }

            else if (shareKey.equals(UtilClass.isFingerAuth))
            {
                if (prefs.contains(UtilClass.isFingerAuth))// pass
                {
                    return prefs.getString(UtilClass.isFingerAuth, "");
                }
            }

         }



            return "0";
        }


}
