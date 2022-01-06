package transfer.money.com.xpresssewa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;


import io.reactivex.disposables.Disposable;


@SuppressWarnings("ConstantConditions")
public abstract class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;
    protected static Context mContext;
    //    protected android.support.v7.widget.Toolbar myToolbar;
    public static Disposable disposable = null;
    // public static SaveDataInPrefrences pref_data;
    protected Gson gson;
    //    protected Intent downloadService;
    protected ProgressBar downloadProgressBar;
    protected TextView progressPercentage;
    LinearLayout ll_cancel;
    public static String baseurl ="https://api.xpressewa.com/";


    private boolean downloadBound = false;
//    private android.support.v7.app.AlertDialog downloadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;


        getProgressDialog();
    }

    /**
     * After Called API, dispose call after success or failure.
     */
    public static void disposeCall() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /*  Method to initialization of UI and other required parameters*/
    protected abstract void init();

    public void getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            Log.e("context", "progress initialized");
        }
    }

    public int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            // Context mContext=getApplicationContext();
            versionCode = mContext.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setHomeEnabled() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    protected void alertDialogFinish(String title, String message, String positive, String negative, Context cxt) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setTitle(title);
        builder.setMessage(message);
        //Yes Button
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //No Button
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"No button Clicked",Toast.LENGTH_LONG).show();

                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        //error
        alertDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // unbindDownloadService();
    }

    private void checkPermsissionTo() {
        if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
        } else {
            //You already have the permission, just go ahead.
            //    proceedAfterPermission();
        }
    }


    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int SWITCH_ACTIVITY_RESULT = 102;
    /*
   @description: loading images from server with the help of picasso
   * */


    public boolean containsDigit(String s) {
        boolean containsDigit = false;
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

    public void openExternalUrls(String url) {
        try {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void copyCode(String code, String codeName) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("qrcode", code);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, codeName + " copied", Toast.LENGTH_LONG).show();


    }


}
