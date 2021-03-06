package transfer.money.com.xpresssewa.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.content.Intent;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import me.anwarshahriar.calligrapher.Calligrapher;

import org.json.JSONObject;

import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.validation.Showtoast;

import java.util.*;

public class ForgotPasswordWithOtpJ extends BaseActivity {

    Showtoast showtoast;
    RelativeLayout rr_mainlayout;
    ServerHandler serverHandler;
    SaveImpPrefrences saveImpPrefrences;
    RelativeLayout RRsignuptoplayout;
    TextView txt_resendOtp;
    String OTP;
    String edittextOtp;
    String MemberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_with_otp);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        showtoast = new Showtoast();
        rr_mainlayout = findViewById(R.id.RRsignuptoplayout);
        RRsignuptoplayout = findViewById(R.id.RRsignuptoplayout);
        txt_resendOtp = findViewById(R.id.txt_resendOtp);
        saveImpPrefrences = new SaveImpPrefrences();
        serverHandler = new ServerHandler();

        OTP = getIntent().getStringExtra("OTP");
        MemberId = getIntent().getStringExtra("MemberId");
        //  getOtpRequest();
        init();
    }

    protected void init() {
        ImageView img = findViewById(R.id.headerbackbutton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextInputLayout input_txt_otp = findViewById(R.id.input_txt_otp);
        TextInputLayout input_txt_password = findViewById(R.id.input_txt_password);
        TextInputLayout input_txt_confirm_password = findViewById(R.id.input_txt_confirm_password);
        TextView tv_submit = findViewById(R.id.tv_submit);


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_txt_password.setError("");
                input_txt_confirm_password.setError("");

                if (input_txt_otp.getEditText().getText().toString().length() == 0) {
                    input_txt_otp.setError("Please enter OTP");
                    return;
                }
                if (input_txt_password.getEditText().getText().toString().length() == 0) {
                    input_txt_password.setError("Please enter password");
                    return;

                } else if (input_txt_confirm_password.getEditText().getText().toString().length() == 0) {
                    input_txt_confirm_password.setError("Please enter Confirmed password");
                    return;

                } else if (!input_txt_password.getEditText().getText().toString().contentEquals(input_txt_confirm_password.getEditText().getText().toString())) {
                    input_txt_confirm_password.setError("Confirmed password does not matched");
                    return;
                } else if (input_txt_password.getEditText().getText().toString().length() > 0 && input_txt_password.getEditText().getText().toString().length() <= 7) {
                    input_txt_password.setError("Please enter minimum 8 character including 1 number");
                } else if (!containsDigit(input_txt_password.getEditText().getText().toString())) {
                    input_txt_password.setError("Please enter minimum 8 character including 1 number");
                } else {
                    sendToServer(input_txt_otp.getEditText().getText().toString(), input_txt_password.getEditText().getText().toString());
                }
            }
        });


        txt_resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtpRequest();
            }
        });


    }

    private void sendToServer(String otp, String password) {

        LinkedHashMap map = new LinkedHashMap<String, String>();
        map.put("MemberId", MemberId);
        map.put("Password", password);
        map.put("OTP", otp);
        map.put("IsForget", "YES");

        serverHandler.sendToServer(this, "ResetPassword", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject reqObj = new JSONObject(dta);


                    if (reqObj.getBoolean("status"))
                    {

                        SimpleDialog simpleDialog = new SimpleDialog();
                        final Dialog confirmDialog = simpleDialog.simpleDailog(ForgotPasswordWithOtpJ.this, R.layout.confirmation_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);

                        ImageView selected_Curreny = confirmDialog.findViewById(R.id.selected_Curreny);
                        TextView txt_currency_name = confirmDialog.findViewById(R.id.txt_currency_name);
                        TextView txt_no = confirmDialog.findViewById(R.id.txt_no);
                        txt_no.setVisibility(View.GONE);
                        TextView txt_yes = confirmDialog.findViewById(R.id.txt_yes);
                        TextView txt_msg = confirmDialog.findViewById(R.id.txt_msg);
                        selected_Curreny.setImageResource(R.drawable.success);

                        txt_currency_name.setText("Success !");
                        txt_msg.setText("Password has been changes sucessfully.");
                        txt_yes.setText("OK");

                        txt_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();

                            }
                        });





                    } else {
                        showtoast.showToast(ForgotPasswordWithOtpJ.this, "Response", reqObj.getString("Message"), RRsignuptoplayout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void getOtpRequest() {


        LinkedHashMap map = new LinkedHashMap<String, String>();
        map.put("Email", getIntent().getStringExtra("email"));

        serverHandler.sendToServer(this, "Forget", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {

                    JSONObject reqObj = new JSONObject(dta);

                    if (reqObj.getBoolean("status")) {
                        OTP = reqObj.getString("OTP");
                        MemberId = reqObj.getString("MemberId");
                    } else {
                        showtoast.showToast(ForgotPasswordWithOtpJ.this, "Not Matched", reqObj.getString("Message"), RRsignuptoplayout);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


}