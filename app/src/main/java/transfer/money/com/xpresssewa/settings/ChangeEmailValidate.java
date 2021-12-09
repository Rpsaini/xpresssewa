package transfer.money.com.xpresssewa.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class ChangeEmailValidate extends BaseActivity {


    EditText et_otp,et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email_validate);
        actions();
        init();

    }

    @Override
    protected void init()
    {

        et_otp=findViewById(R.id.et_otp);
        et_password=findViewById(R.id.et_password);



    }
    private void actions()
    {
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_otp.getText().toString().length()==0) {
                    new Showtoast().showToast(ChangeEmailValidate.this, "Response", "Please enter OTP.", findViewById(R.id.rr_forgotmain));
                }
                else if(et_password.getText().toString().length()==0)
                {
                    new Showtoast().showToast(ChangeEmailValidate.this, "Response", "Please enter you password.", findViewById(R.id.rr_forgotmain));
                }
                else {
                    changeEmail();
                }


            }
        });



    }
    private void changeEmail() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Email", getIntent().getStringExtra("email"));
        map.put("Password", et_password.getText().toString());
        map.put("Otp", et_otp.getText().toString());
        map.put("MemberId", UtilClass.member_id);

        new ServerHandler().sendToServer(this, "ChangeEmail", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {

                       Intent intent=new Intent();
                       intent.putExtra("data",obj.getString("Message"));
                       setResult(RESULT_OK,intent);
                       finish();


                    } else {
                        new Showtoast().showToast(ChangeEmailValidate.this, "ChangeEamil", obj.getString("Message"), findViewById(R.id.rr_forgotmain));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }



}