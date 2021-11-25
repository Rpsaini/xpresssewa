package transfer.money.com.xpresssewa.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class VerifyOTP extends BaseActivity {
    private String concateOtp;
    private Showtoast showtoast;

    private ArrayList<EditText> otpArray = new ArrayList<>();
    @BindView(R.id.ll_verifyotp)
    LinearLayout ll_verifyotp;

    @BindView(R.id.txt_sendOtp)
    TextView txt_sendOtp;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        ButterKnife.bind(this);
        showtoast = new Showtoast();
        init();
        sendOtp(1,false);
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
                    showtoast.showToast(VerifyOTP.this, "Registration", "Please enter OTP.", ll_main_layout);
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


        txt_sendOtp.setTag("0");
        txt_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_verifyotp.setVisibility(View.VISIBLE);
                if (txt_sendOtp.getTag().toString().equalsIgnoreCase("0"))
                 {
                     sendOtp(0,true);

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

    int counter = 60;
    Handler handler;

    private void handleResendButton() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
              {
                counter--;
                if (counter > 0) {
                    txt_sendOtp.setTag("1");
                    txt_sendOtp.setText("You can use Resend OTP in : " + counter + " Second");
                    handler.postDelayed(this, 1000);
                } else {
                    counter = 60;
                    txt_sendOtp.setText("Resend OTP");
                    txt_sendOtp.setTag("0");
                    handler.removeCallbacks(this);

                }

            }
        }, 1000);
    }

    private void verifyOtp()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", getIntent().getStringExtra("memberId"));
        m.put("Phone",  getIntent().getStringExtra("Phone"));
        m.put("PhoneExt",  getIntent().getStringExtra("PhoneExt"));
        m.put("Otp",  concateOtp);

        System.out.println("Concated code==="+m);
        new ServerHandler().sendToServer(VerifyOTP.this, "VerifyOtp",m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    System.out.println("Concated code= response=="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if(obj.getBoolean("status"))
                    {

                        if(getIntent().getStringExtra("callfrom").equalsIgnoreCase("profile"))
                        {

                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                intent.putExtra("Phone",  getIntent().getStringExtra("Phone"));
                                intent.putExtra("PhoneExt",  getIntent().getStringExtra("PhoneExt"));
                                intent.putExtra("callfrom",  "profile");
                                finish();

                        }
                        else//signup
                            {
                            if (obj.getBoolean("IsEmailVerified"))
                            {
                                SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();
                                saveImpPrefrences.savePrefrencesData(VerifyOTP.this, "2", DefaultConstatnts.IsKycApproved);
                                saveImpPrefrences.savePrefrencesData(VerifyOTP.this, "0", DefaultConstatnts.UserName);
                                saveImpPrefrences.savePrefrencesData(VerifyOTP.this, "", DefaultConstatnts.login_detail);
                                saveImpPrefrences.savePrefrencesData(VerifyOTP.this, "0", DefaultConstatnts.Pin);
                                saveImpPrefrences.savePrefrencesData(VerifyOTP.this, getIntent().getStringExtra("memberId") + "", DefaultConstatnts.MemberId);

                                Intent signIn = new Intent(VerifyOTP.this, MainActivity.class);
                                signIn.putExtra(DefaultConstatnts.IsShowPin, "yes");
                                startActivity(signIn);
                                finishAffinity();
                            }
                            else
                            {
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                intent.putExtra("callfrom",  "signup");
                                finish();
                            }
                        }

                      }
                    else {
                        showtoast.showToast(VerifyOTP.this, "Registration", obj.getString("Message"), ll_main_layout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }



    private void sendOtp(int shoLoader,boolean isStartTimer)
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", getIntent().getStringExtra("memberId"));
        m.put("Phone",  getIntent().getStringExtra("Phone"));
        m.put("PhoneExt",  getIntent().getStringExtra("PhoneExt"));

        System.out.println("SendOtp==="+m);
        new ServerHandler().sendToServer(VerifyOTP.this, "SendOtp", m, shoLoader, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                     System.out.println("Send otp==="+dta);
                    JSONObject obj = new JSONObject(dta);
                    if(obj.getBoolean("status"))
                    {
                        if(isStartTimer)
                        {
                            handleResendButton();
                        }
                       //{"Message":"Success","ReponseCode":1,"OTP":"159090","UserId":"test110134","RefCode":"","Version":"1","Pin":0,"IsPhoneVerified":false,"IsEmailVerified":false,"MemberId":10134,"status":true}
                    } else {
                        showtoast.showToast(VerifyOTP.this, "Registration", obj.getString("Message"), ll_main_layout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


}