package transfer.money.com.xpresssewa.settings;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.SplashActivity;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.ticket.MyTicketsActivity;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class SettingsScreenActivity extends BaseActivity {

    ImageView img_isTouchlockenable;
    ImageView img_autolock;
    RelativeLayout rr_changeemail;
    RelativeLayout rr_notificationprefrences;
    RelativeLayout rr_sendfeedback;
//  RelativeLayout rr_reportabug;
    RelativeLayout rr_about, rr_viewticket;
    private String isFingerAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);
        init();

    }

    protected void init() {
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_isTouchlockenable = findViewById(R.id.img_isTouchlockenable);
        img_autolock = findViewById(R.id.img_autolock);
        rr_changeemail = findViewById(R.id.rr_changeemail);
        rr_notificationprefrences = findViewById(R.id.rr_notificationprefrences);
        rr_sendfeedback = findViewById(R.id.rr_sendfeedback);
        rr_about = findViewById(R.id.rr_about);
        rr_viewticket = findViewById(R.id.rr_viewticket);

        SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();
        isFingerAuth = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, UtilClass.isFingerAuth).toString();
        final String isLockScreen = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, UtilClass.isLockScreen).toString();


        if (isFingerAuth.toString().equalsIgnoreCase("1")) {
            img_isTouchlockenable.setImageResource(R.drawable.button_on);
        } else {
            img_isTouchlockenable.setImageResource(R.drawable.button);
        }

        img_isTouchlockenable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFingerAuth = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, UtilClass.isFingerAuth).toString();
                if (isFingerAuth.equalsIgnoreCase("0")) {
                    img_isTouchlockenable.setImageResource(R.drawable.button_on);
                    saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "1", UtilClass.isFingerAuth);
                } else {
                    img_isTouchlockenable.setImageResource(R.drawable.button);
                    saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "0", UtilClass.isFingerAuth);
                }

            }

        });


        img_autolock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLockScreen.equalsIgnoreCase("0")) {
                    //enable
                    saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "1", UtilClass.isLockScreen);
                } else {
                    //diable
                    saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "0", UtilClass.isLockScreen);
                }
            }
        });

        rr_viewticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsScreenActivity.this, MyTicketsActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.rr_changeemail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsScreenActivity.this, ChangeEmailActivity.class);
                startActivityForResult(intent,1001);
            }
        });


        findViewById(R.id.rr_privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrls(UtilClass.privacyurl);
            }
        });

        findViewById(R.id.rr_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalUrls("https://demo.webcomsystems.net.au/about");

            }
        });

        findViewById(R.id.rr_changepassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsScreenActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.rr_sendfeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
            }
        });

        findViewById(R.id.rr_deactivate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog simpleDialog = new SimpleDialog();
                final Dialog confirmDialog = simpleDialog.simpleDailog(SettingsScreenActivity.this, R.layout.deactibvate_account, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                TextView tv_yes=confirmDialog.findViewById(R.id.tv_yes);
                TextView txt_close=confirmDialog.findViewById(R.id.tv_cancel);


                txt_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        confirmDialog.dismiss();
                    }
                });


                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deactiveAccount(confirmDialog);
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)//change email
        {
            if(data!=null)
            {
                new Showtoast().showToast(SettingsScreenActivity.this, "ChangeEamil", data.getStringExtra("data"), findViewById(R.id.rr_forgotmain));
            }

        }
    }

    private void deactiveAccount(Dialog confirmDialog) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("MemberId", UtilClass.member_id);

        new ServerHandler().sendToServer(this, "AccountDeactivate", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {

                        confirmDialog.dismiss();
                        logout();

                    } else {
                        new Showtoast().showToast(SettingsScreenActivity.this, "ChangeEamil", obj.getString("Message"), findViewById(R.id.rr_forgotmain));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void logout()
    {

        SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();
        String devicetoken = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, "device_token") + "";
        saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "yes", DefaultConstatnts.IsTutorialDone);
        saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, "0", "login_detail");
        saveImpPrefrences.savePrefrencesData(SettingsScreenActivity.this, devicetoken, "device_token");

        Intent intent = new Intent(SettingsScreenActivity.this, SplashActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}