package transfer.money.com.xpresssewa.View;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
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


        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        tv_get_started.setOnClickListener(this);
        imp = new SaveImpPrefrences();

        String isWatched = imp.reterivePrefrence(this, DefaultConstatnts.IsTutorialDone).toString();
        System.out.println("IS watched===" + isWatched);
        imp.savePrefrencesData(this, "https://expresssewa.webcomsystems.net.au/Api/", "url");
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
                //   tv_get_started.setVisibility(View.VISIBLE);
            } else {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                Intent signIn = new Intent(SplashActivity.this, MainActivity.class);
                signIn.putExtra(DefaultConstatnts.IsShowPin, "yes");
                startActivity(signIn);
                finish();
            }
//                }, 000);
        }
//        }
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






}