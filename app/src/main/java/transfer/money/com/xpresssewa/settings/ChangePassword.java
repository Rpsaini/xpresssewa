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

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ForgotPassword;
import transfer.money.com.xpresssewa.View.ForgotPasswordWithOtpJ;
import transfer.money.com.xpresssewa.View.ForgotPinJ;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;
import transfer.money.com.xpresssewa.validation.Validation;

public class ChangePassword extends BaseActivity {


    EditText input_txt_old_password, et_password, et_cionfirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);
        init();

    }

    @Override
    protected void init() {


        input_txt_old_password = findViewById(R.id.input_txt_old_password);
        et_password = findViewById(R.id.et_password);
        et_cionfirm_password = findViewById(R.id.et_cionfirm_password);
        findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (input_txt_old_password.getText().toString().length() == 0) {
                    new Showtoast().showToast(ChangePassword.this, "Response", "Please enter old password.", findViewById(R.id.rr_forgotmain));
                } else if (et_password.getText().toString().length() == 0) {
                    new Showtoast().showToast(ChangePassword.this, "Response", "Please enter new password.", findViewById(R.id.rr_forgotmain));
                } else if (!et_password.getText().toString().equals(et_cionfirm_password.getText().toString())) {
                    new Showtoast().showToast(ChangePassword.this, "Response", "Entered confirm password doest't matched with new password.", findViewById(R.id.rr_forgotmain));
                } else {
                    changePassword();
                }

            }
        });
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void changePassword() {


        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("OldPassword", input_txt_old_password.getText().toString());
        map.put("NewPassword", et_password.getText().toString());
        map.put("ConfirmPassword", et_cionfirm_password.getText().toString());
        map.put("MemberId", UtilClass.member_id);


        new ServerHandler().sendToServer(this, "ChangePassword", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        input_txt_old_password.setText("");
                        et_password.setText("");
                        input_txt_old_password.setText("");
                        new Showtoast().showToast(ChangePassword.this, "Password Changed", obj.getString("Message"), findViewById(R.id.rr_forgotmain));

                    } else {
                        new Showtoast().showToast(ChangePassword.this, "Registration", obj.getString("Message"), findViewById(R.id.rr_forgotmain));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



}
}