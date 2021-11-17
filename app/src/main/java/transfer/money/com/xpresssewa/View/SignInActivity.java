package transfer.money.com.xpresssewa.View;

import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_sign_up)
    TextView tv_sign_up;

//    @BindView(R.id.iv_back)
//    LinearLayout iv_back;

    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;


    @BindView(R.id.email_layout_name)
    TextInputLayout email_layout_name;


    @BindView(R.id.pwd_layout_name)
    TextInputLayout pwd_layout_name;


    @BindView(R.id.tv_forgot_pwd)
    TextView tv_forgot_pwd;

    @BindView(R.id.logintoplayout)
    RelativeLayout logintoplayout;
    private Showtoast showtoast;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tv_sign_up.setOnClickListener(this);
        tv_sign_in.setOnClickListener(this);
      //  iv_back.setOnClickListener(this);
        tv_forgot_pwd.setOnClickListener(this);
        showtoast=new Showtoast();

        Typeface face=Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf");
        tv_sign_up.setTypeface(face);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up:
                Intent call_sign_up = new Intent(this, SignUpActivity.class);
                startActivity(call_sign_up);
                break;

//            case R.id.iv_back:
//                onBackPressed();
//                break;

            case R.id.tv_forgot_pwd:
                Intent i=new Intent(SignInActivity.this,ForgotPassword.class);
                i.putExtra("callFrom","Password");
                startActivity(i);
                break;


            case R.id.tv_sign_in:
                email_layout_name.setError("");
                pwd_layout_name.setError("");
                if (email_layout_name.getEditText().getText().toString().length() == 0) {
                    email_layout_name.setError("Enter email address");
                } else if (pwd_layout_name.getEditText().getText().toString().length() == 0) {
                    pwd_layout_name.setError("Enter password");
                } else {
                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();


                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(tsLong);


                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("Email", email_layout_name.getEditText().getText().toString());
                    m.put("Password", pwd_layout_name.getEditText().getText().toString());
                    new ServerHandler().sendToServer(this, "login", m, 0,1, new CallBack() {
                        @Override
                        public void getRespone(String dta, ArrayList<Object> respons) {

                            try
                            {
                                JSONObject obj=new JSONObject(dta);

                                System.out.println("Login object==="+obj);
                               // {"Message":"Time Expired","UserName":"","UserId":"","DestinationSymbol":"","SourceSymbol":"","FlagImageDestination":"","FlagImageSource":"","SDCountryId":0,"CountryId":0,"CountryCode":"","CountryName":"","UserImage":"","Email":"","ContactNumber":"","Pin":0,"Version":"","IsPhoneVerified":false,"RefCode":"","CreatedDate":"2020-12-22T10:32:52.9343113Z","ReponseCode":3,"MemberId":0,"status":false}
                                if(obj.getBoolean("status"))
                                {

                                    SaveImpPrefrences saveImpPrefrences=new SaveImpPrefrences();
                                    String memberId=obj.getString("MemberId");
                                    new SaveImpPrefrences().savePrefrencesData(SignInActivity.this,obj.getString("IsKycApproved")+"",DefaultConstatnts.IsKycApproved);
                                    new SaveImpPrefrences().savePrefrencesData(SignInActivity.this,obj.getString("UserName")+"",DefaultConstatnts.UserName);
                                    saveImpPrefrences.savePrefrencesData(SignInActivity.this,obj+"",DefaultConstatnts.login_detail);
                                    saveImpPrefrences.savePrefrencesData(SignInActivity.this,obj.getString("Pin")+"",DefaultConstatnts.Pin);
                                    saveImpPrefrences.savePrefrencesData(SignInActivity.this,memberId+"", DefaultConstatnts.MemberId);

                                    Intent signIn=new Intent(SignInActivity.this, MainActivity.class);
                                    signIn.putExtra(DefaultConstatnts.IsShowPin,"yes");
                                    startActivity(signIn);
                                    finishAffinity();

                                 }
                                  else
                                     {
                                        showtoast.showToast(SignInActivity.this,"Response",obj.getString("Message"),logintoplayout);
                                     }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}






