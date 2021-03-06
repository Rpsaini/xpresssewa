package transfer.money.com.xpresssewa.View;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.registration.SignInActivity;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.viewpager.SwipeViewPager;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_get_started)
    TextView tv_get_started;
    private SaveImpPrefrences imp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getContactNumberSplit();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        tv_get_started.setOnClickListener(this);
        imp = new SaveImpPrefrences();

        String isWatched = imp.reterivePrefrence(this, DefaultConstatnts.IsTutorialDone).toString();
        System.out.println("IS watched===" + isWatched);
        imp.savePrefrencesData(this, BaseActivity.baseurl+"api/", "url");
        if (isWatched.equalsIgnoreCase("0")) {
            Intent signIn = new Intent(SplashActivity.this, SwipeViewPager.class);
            startActivity(signIn);
            finish();
        } else {
            //  TextView tv_get_started = findViewById(R.id.tv_get_started);

            String login_detail = imp.reterivePrefrence(SplashActivity.this, "login_detail").toString();

            System.out.println("login detail===" + login_detail);
            if (login_detail.equalsIgnoreCase("0")) {
                Intent signIn = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(signIn);
                finish();

            } else {

                // Intent signIn = new Intent(SplashActivity.this, MainActivity.class);
                // todo signIn.putExtra(DefaultConstatnts.IsShowPin, "yes");


                Intent signIn = new Intent(SplashActivity.this, SetPinActivity.class);
                signIn.putExtra(DefaultConstatnts.pinKey, DefaultConstatnts.pinVerify);
                startActivity(signIn);
                finish();
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_started:

                Intent signIn = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(signIn);
                break;
        }

    }


   private void getContactNumberSplit()
    {
        String contactNumber="9088787678";
        String email="abc@gmail.com";
        String contactnumberMask=contactNumber.substring(contactNumber.length()-3,contactNumber.length());

        System.out.println("contact number ==="+contactnumberMask);

        String[] emailAr=email.split("@");
        String maskEmail=emailAr[0].substring(emailAr[0].length()-1,emailAr[0].length())+"@"+emailAr[1];
        System.out.println("masked email==="+maskEmail);




    }





}
