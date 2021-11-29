package transfer.money.com.xpresssewa.View;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.ticket.MyTicketsActivity;
import transfer.money.com.xpresssewa.util.UtilClass;

public class SettingsScreenActivity extends AppCompatActivity {

    ImageView img_isTouchlockenable;
    ImageView img_autolock;
    RelativeLayout rr_changeemail;
    RelativeLayout rr_notificationprefrences;
    RelativeLayout rr_sendfeedback;
    RelativeLayout rr_reportabug;
    RelativeLayout rr_about, rr_viewticket;
    private String isFingerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        init();

    }

    private void init() {
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
        rr_reportabug = findViewById(R.id.rr_reportabug);
        rr_about = findViewById(R.id.rr_about);
        rr_viewticket = findViewById(R.id.rr_viewticket);

        SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();
        isFingerAuth = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, UtilClass.isFingerAuth).toString();
        final String isLockScreen = saveImpPrefrences.reterivePrefrence(SettingsScreenActivity.this, UtilClass.isLockScreen).toString();

        System.out.println("Is finger autrh==="+isFingerAuth);
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

    }
}