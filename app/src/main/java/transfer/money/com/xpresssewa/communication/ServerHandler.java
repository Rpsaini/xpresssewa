package transfer.money.com.xpresssewa.communication;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.validation.InternetDialog;
import transfer.money.com.xpresssewa.validation.Showtoast;


public class ServerHandler {
    private Dialog progressdlg;
    private Showtoast showtoast;
    private InternetDialog internetDialog;
    private Context ct1;
    private String url1 = "";

    public ServerHandler()
    {
    }

    public boolean CheckInternetState(Context ct, int sholoader) {
        try {
            showtoast = new Showtoast();
            internetDialog=new InternetDialog();

            ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) {
                return true;
            } else {
                if (sholoader <= 0) {
                    internetDialog.InternetDialog(ct, "Communication error !");
                }
                return false;
            }
        } catch (Exception e)
        {
            //Toast.makeText(ct, "Exception CheckNetState", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void sendToServer(final Context ct, final String methodname, final Map<String, String> data, final int showloader, final int requestType, final CallBack cb) {
        ct1 = ct;
        if (progressdlg != null && progressdlg.isShowing()) {
            dissmissLoader();
        }
        if (CheckInternetState(ct, showloader))
        {
            try
            {
                data.put("DeviceCode",new SaveImpPrefrences().reterivePrefrence(ct,"device_token")+"");
                data.put("Version","1");
                data.put("PlatForm","android");
                data.put("Timestamp", System.currentTimeMillis()+"");
                data.put("Token", "MONEYTRANSFERXYZSta6907642209093911");

                String BankData="0";
                if(data.containsKey("BankData"))
                {
                    BankData=data.get("BankData");
                    data.remove("BankData");
                }

                String url1 = new SaveImpPrefrences().reterivePrefrence(ct, "url") + methodname;

                System.out.println("url-->"+url1);

                data.remove("Hash");
                String prepareKey=sortbykey(data);


                String has6=md5(prepareKey);
                data.put("Hash", has6);

                System.out.println("Preparekey===>"+prepareKey);

                if(!BankData.equalsIgnoreCase("0"))
                {
                    System.out.println("bank data lengths=="+BankData);
                    data.put("BankData",BankData);
                }

                System.out.println("before==="+data+"==="+url1);
                StringRequest stringRequest = new StringRequest(requestType, url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    cb.getRespone(response, null);
                                    dissmissLoader();
                                    System.out.println("onresponse==" + response);


                                } catch (Exception e) {
                                    cb.getRespone("error", null);
                                    if (showloader <= 0)
                                        showtoast.showToast(ct,"Communication","Communication error.",null);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    cb.getRespone("error", null);
                                    NetworkResponse networkResponse = error.networkResponse;
                                    if (networkResponse != null && networkResponse.statusCode == 401) {
                                        showtoast.showToast(ct1,"Unauthorized", "Unauthorized Access!",null);
                                    } else {
                                        showtoast.showToast(ct1,"Communication", "Communication error!",null);
                                    }
                                    if (progressdlg != null && progressdlg.isShowing()) {
                                        dissmissLoader();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return data;
                    }


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        try {
                            params.put("Authorization", "Basic " + convertToBase64("ravi10000:ravi123456"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return params;
                    }

                    ;
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                RequestQueue requestQueue = Volley.newRequestQueue(ct);
                requestQueue.add(stringRequest);
                stringRequest.setShouldCache(true);
                showProgressDialog();
                progressdlg.show();

                if (showloader >= 1) {
                    dissmissLoader();
                }
            } catch (Exception e) {
                if (progressdlg != null)
                    dissmissLoader();

            }
        }
    }


    public String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    private String convertToBase64(String text) {
        byte[] encodeValue = Base64.encode(text.getBytes(), Base64.DEFAULT);
        //byte[] decodeValue = Base64.decode(encodeValue, Base64.DEFAULT);

        System.out.println("base 64 =="+new String(encodeValue));
        return new String(encodeValue);
    }


    public String md5(String s) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(s.getBytes(), 0, s.length());
            String pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return password;
    }

    Handler hnd;
    Runnable runnable;
    int fadeEffect=0;
    private void showProgressDialog() {
        progressdlg = new Dialog(ct1);
        progressdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdlg.setContentView(R.layout.showprogressdialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = progressdlg.getWindow();
        lp.copyFrom(window.getAttributes());
        progressdlg.setCancelable(false);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        progressdlg.getWindow().setBackgroundDrawableResource(R.color.translucent_black_loader);
        progressdlg.getWindow().setDimAmount(0);
        progressdlg.show();
    }

    private void dissmissLoader()
    {
        if(hnd!=null)
        {
            hnd.removeCallbacks(runnable);
        }
       if(progressdlg!=null&&progressdlg.isShowing())
       {
           progressdlg.dismiss();
       }

        }

    public String sortbykey(Map<String, String> data) {

        String keyHas = "";
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println("Key = " + entry.getKey().trim() +
                    ", Value = " + entry.getValue().trim());
            keyHas = keyHas + entry.getValue();
        }
        System.out.println("before hash=="+keyHas);
        return keyHas;
    }


}



//        ffmpeg -i C:\Users\Webcom\Desktop\fallingflowe.avi -i C:\Users\Webcom\Desktop\filter4.png -filter_complex "[0:v]setpts=PTS/1.15,crop=iw/1.2:ih/1.2,boxblur=1:2,scale=1280:720 [v1]; [1:v][v1]scale2ref[wm][v1];[v1][wm]overlay=0:0,setdar=16/9" C:\Users\Webcom\Desktop\coutputdvideo.mp4-

