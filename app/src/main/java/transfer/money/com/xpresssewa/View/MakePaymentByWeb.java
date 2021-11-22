package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.util.SimpleDialog;


public class MakePaymentByWeb extends AppCompatActivity {
    private ProgressBar viewprogressbar;
    private static final String TAG = "MainActivity";
    private String transferAMount = "", transferTo = "";
    private Dialog paymentWaitingDialog;
    private RelativeLayout rr_payment_sucess,rr_rocket_fly;
    private TextView txt_seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitingDialog();
        setContentView(R.layout.activity_make_payment_by_web);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);

        WebView mywebview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mywebview.loadUrl(getIntent().getStringExtra("paymentlink"));
        viewprogressbar = findViewById(R.id.viewprogressbar);
        mywebview.setWebViewClient(new WebViewController());

        transferTo = getIntent().getStringExtra("transferto");
        transferAMount = getIntent().getStringExtra("amount");


    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            viewprogressbar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            System.out.println("Pageurl===" + url);
            viewprogressbar.setVisibility(View.GONE);
            String SuccessUrl = "polisuccess";
            String failedUrl = "polifailure";
            String pendingUrl = "policancel";
            String Receipt = "Receipt?";

            if (url.contains(Receipt))
            {

                showPayemtDialog("Success !", "You sent " + transferAMount + " to " + transferTo + " should reach there in a next few second.", 1, R.drawable.success);
                WebView mywebview = (WebView) findViewById(R.id.webview);
                WebSettings webSettings = mywebview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                mywebview.loadUrl(BaseActivity.baseurl +"/polisuccess?"+url.split("\\?")[1]);
                viewprogressbar = findViewById(R.id.viewprogressbar);
                mywebview.setWebViewClient(new WebViewController());

            }

                if (url.contains(failedUrl))
                {
                showPayemtDialog("Failed", "Payment Transfer has been failed.", -1, R.drawable.failed);
            } else if (url.contains(pendingUrl)) {
                showPayemtDialog("Cancel", "Your transfer has been cancel. Please try again. ", 0, R.drawable.failed);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    paymentWaitingDialog.dismiss();
                }
            },2000);

            super.onPageFinished(view, url);

        }


        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("inside on pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("inside on pause");
    }


    private TextView goback;
    private Dialog dialogConfirm;
    public void showPayemtDialog(String mainTitleStr, String Message, final int status, int imageId)
      {
          if(dialogConfirm==null)
             {

              SimpleDialog simpleDialog = new SimpleDialog();
              dialogConfirm = simpleDialog.simpleDailog(MakePaymentByWeb.this, R.layout.payment_success_failure_dialog, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);

              TextView title = dialogConfirm.findViewById(R.id.title);
              TextView mainTitle = dialogConfirm.findViewById(R.id.mainTitle);
              ImageView imageTick = dialogConfirm.findViewById(R.id.imageTick);
              rr_payment_sucess = dialogConfirm.findViewById(R.id.rr_payment_sucess);
              rr_rocket_fly = dialogConfirm.findViewById(R.id.rr_rocket_fly);
              txt_seconds = dialogConfirm.findViewById(R.id.txt_seconds);
              TextView txt_mainheader = dialogConfirm.findViewById(R.id.txt_mainheader);
              goback = dialogConfirm.findViewById(R.id.goback);
              ImageView img_rocketfly = dialogConfirm.findViewById(R.id.img_rocketfly);
              if(status==1)
              {
                  txt_seconds.setText("00:30");
                  changeLoader();
              }
              else
              {
                  rr_payment_sucess.setVisibility(View.VISIBLE);
                  rr_rocket_fly.setVisibility(View.GONE);
                  txt_mainheader.setText(mainTitleStr);
              }

              goback.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if (status == 1) {
                          dialogConfirm.dismiss();
                          Intent intent = new Intent();
                          intent.putExtra("isSendScreenDataLoaded", "showTransaction");
                          setResult(RESULT_OK, intent);
                          finish();
                      } else {
                          dialogConfirm.dismiss();
                          finish();
                      }
                  }
              });


              title.setText(Message);
              mainTitle.setText(mainTitleStr);
              imageTick.setImageResource(imageId);

          }



    }

    int progress=30;
    private void changeLoader()
    {
        progress--;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(progress>0)
                {
                    if(progress<=9)
                    {
                        txt_seconds.setText("00:0"+progress);
                    }
                    else
                    {
                        txt_seconds.setText("00:"+progress);
                    }

                    changeLoader();
                }
                else
                {
                    rr_payment_sucess.setVisibility(View.VISIBLE);
                    rr_rocket_fly.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }


    private void waitingDialog()
    {
        SimpleDialog simpleDialog = new SimpleDialog();
        paymentWaitingDialog = simpleDialog.simpleDailog(this, R.layout.payment_message_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        TextView txt_msg=paymentWaitingDialog.findViewById(R.id.txt_msg);
        Typeface face= Typeface.createFromAsset(getAssets(), "MontserratRegular.ttf");

            txt_msg.setTypeface(face);

    }

    }






