package transfer.money.com.xpresssewa.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.View.SetPinActivity;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class LoginWithOtp extends BaseActivity {
    private String concateOtp;
    private Showtoast showtoast;

    private ArrayList<EditText> otpArray = new ArrayList<>();
    @BindView(R.id.ll_verifyotp)
    LinearLayout ll_verifyotp;


    @BindView(R.id.ed_one)
    EditText ed_one;


    @BindView(R.id.ed_two)
    EditText ed_two;


    @BindView(R.id.ed_three)
    EditText ed_three;


    @BindView(R.id.ed_four)
    EditText ed_four;

    @BindView(R.id.ed_five)
    EditText ed_five;

    @BindView(R.id.ed_six)
    EditText ed_six;

    @BindView(R.id.headerbackbutton)
    ImageView headerbackbutton;


    @BindView(R.id.tv_verify)
    TextView tv_verify;

    @BindView(R.id.ll_main_layout)
    RelativeLayout ll_main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        ButterKnife.bind(this);
        showtoast = new Showtoast();
        init();

    }
    protected void init()
    {
        headerbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                concateOtp = ed_one.getText() + "" + ed_two.getText() + "" + ed_three.getText() + "" + ed_four.getText() + "" + "" + ed_five.getText() + "" + "" + ed_six.getText() + "";
                if(concateOtp.length()==6)
                {
                    verifyOtp();
                }
                else
                {
                    showtoast.showToast(LoginWithOtp.this, "Registration", "Please enter OTP.", ll_main_layout);
                }

            }
        });
        otpInputLayout();

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


    private void otpInputLayout() {
        ed_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_two.requestFocus();
                }

            }
        });

        ed_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_three.requestFocus();
                }
            }
        });

        ed_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_four.requestFocus();
                }

            }
        });

        ed_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_five.requestFocus();
                }

            }
        });

        ed_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_six.requestFocus();
                }

            }
        });

        ed_six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed_six.requestFocus();
                }

            }
        });



        clearOTP();

    }

    private void clearOTP() {
        otpArray.clear();
        otpArray.add(ed_one);
        otpArray.add(ed_two);
        otpArray.add(ed_three);
        otpArray.add(ed_four);
        otpArray.add(ed_five);
        otpArray.add(ed_six);

        ed_one.setTag("0");
        ed_two.setTag("1");
        ed_three.setTag("2");
        ed_four.setTag("3");
        ed_five.setTag("4");
        ed_six.setTag("5");


        for (int x = 0; x < otpArray.size(); x++) {
            otpArray.get(x).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        int index = Integer.parseInt(v.getTag().toString());
                        if (index > 0) {
                            otpArray.get(index).setText("");
                            otpArray.get(index - 1).requestFocus();
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void verifyOtp()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("Email", getIntent().getStringExtra("Email"));
        m.put("Password",  getIntent().getStringExtra("Password"));
        m.put("OTP",  concateOtp);



        System.out.println("Concated code==="+m);
        new ServerHandler().sendToServer(LoginWithOtp.this, "LoginWithOtp",m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    System.out.println("Concated code= response=="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if(obj.getBoolean("status"))
                    {

                        SaveImpPrefrences saveImpPrefrences=new SaveImpPrefrences();
                        String memberId=obj.getString("MemberId");
                        saveImpPrefrences.savePrefrencesData(LoginWithOtp.this,obj.getString("IsKycApproved")+"",DefaultConstatnts.IsKycApproved);
                        saveImpPrefrences.savePrefrencesData(LoginWithOtp.this,obj.getString("UserName")+"",DefaultConstatnts.UserName);
                        saveImpPrefrences.savePrefrencesData(LoginWithOtp.this,obj+"",DefaultConstatnts.login_detail);
                        saveImpPrefrences.savePrefrencesData(LoginWithOtp.this,obj.getString("Pin")+"",DefaultConstatnts.Pin);
                        saveImpPrefrences.savePrefrencesData(LoginWithOtp.this,memberId+"", DefaultConstatnts.MemberId);


                        Intent signIn = new Intent(LoginWithOtp.this, MainActivity.class);
                        signIn.putExtra(DefaultConstatnts.IsShowPin, "yes");
                        startActivity(signIn);
                        finishAffinity();

                    }
                    else {
                        showtoast.showToast(LoginWithOtp.this, "Registration", obj.getString("Message"), ll_main_layout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }





}