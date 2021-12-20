package transfer.money.com.xpresssewa.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class AccountWebview extends AppCompatActivity {

    @BindView(R.id.headertexttitle)
    TextView headertexttitle;

    @BindView(R.id.iv_back)
    LinearLayout iv_back;

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.ll_mainsize)
    LinearLayout  ll_mainsize;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_webview);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        init();
    }


    protected void init() {

        headertexttitle.setText("My Account");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        loadWebView(UtilClass.frankieKycUrl+""+UtilClass.member_id+"/android");

    }

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


    private void loadWebView(String url)
    {

        showProgressDialog();
        System.out.println("Url=="+url);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                WebSettings webSettings = webview.getSettings();
                //webSettings.setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/80.0.3987.163 Chrome/80.0.3987.163 Safari/537.36");

                webSettings.setJavaScriptEnabled(true);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setSupportMultipleWindows(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setSupportZoom(true);
                webSettings.setDisplayZoomControls(false);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
                else
                {
                    webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }

                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

                webview.setWebViewClient(new WebViewController());
                webview.loadUrl(url);

                webview.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                webview.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(event.getAction()==KeyEvent.ACTION_DOWN)
                        {
                            if(keyCode==KeyEvent.KEYCODE_BACK)
                            {
//                                if(mywebview.canGoBack())
//                                   {
//                                    mywebview.goBack();
//                                   }

                            }
                        }
                        return false;
                    }
                });
              openFileChooser();
            }
        }, 50);

    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
//            ArrayList<String> internalWebViewUrl = AppSettings.setInternalDomainsUrls();
//            if(internalWebViewUrl.size()>0) {
//                if (internalWebViewUrl.contains(Uri.parse(url).getHost())) {
//                    view.loadUrl(url);
//                    return false;
//                }
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
//            else
//            {
                return false;
//            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressDialog();
        }

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
        }


    }


    private void openFileChooser() {

        String permission = Manifest.permission.CAMERA;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }


        webview.setWebChromeClient(new WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    new Showtoast().showToast(AccountWebview.this,"Cannot Open File Chooser","",ll_mainsize);

                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       super.onActivityResult(requestCode,resultCode,intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            new Showtoast().showToast(AccountWebview.this,"Failed to Upload Image","",ll_mainsize);

    }

    Dialog progressdlg;
    private void showProgressDialog() {
        progressdlg = new Dialog(this);
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

    private void hideProgressDialog()
    {
        if(progressdlg!=null&&progressdlg.isShowing())
        {
            progressdlg.dismiss();
        }
    }

}