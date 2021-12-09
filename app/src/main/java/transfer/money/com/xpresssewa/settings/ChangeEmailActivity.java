package transfer.money.com.xpresssewa.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.transform.Result;

import io.reactivex.annotations.Nullable;
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

public class ChangeEmailActivity extends BaseActivity {


    private Validation vd;
    private  String callFrom="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);
        TextView headertexttitle =findViewById(R.id.headertexttitle);
        ImageView headerImageview =findViewById(R.id.headerbackbutton);
        headerImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headertexttitle.setVisibility(View.VISIBLE);

        TextView tv_submit=findViewById(R.id.tv_submit);


        vd=new Validation(this);
        final EditText et_email =findViewById(R.id.et_email);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(ChangeEmailActivity.this);
                if(et_email.getText().toString().length()==0) {
                    new Showtoast().showToast(ChangeEmailActivity.this, "Response", "Please enter your email address.", findViewById(R.id.rr_forgotmain));
                }
                else if(!vd.checkEmail(et_email.getText().toString()))
                {
                    new Showtoast().showToast(ChangeEmailActivity.this, "Response", "Please enter valid email address.", findViewById(R.id.rr_forgotmain));
                }
                else
                {

                    getOtpRequest(et_email.getText().toString());


                }
            }
        });

    }


    @Override
    protected void init() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       System.out.println("Request code==="+requestCode);
        if(requestCode==1001)
        {
            if(data!=null)
            {
                Intent intent=new Intent();
                intent.putExtra("data",data.getStringExtra("data"));
                setResult(RESULT_OK,intent);
                finish();

            }
        }
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

    private void getOtpRequest(final String email) {


        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Email", email);
        map.put("MemberId", UtilClass.member_id);

        new ServerHandler().sendToServer(this, "ChangeEmailOtp", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {

                        Intent intent=new Intent(ChangeEmailActivity.this,ChangeEmailValidate.class);
                        intent.putExtra("email", map.get("Email"));
                        startActivityForResult(intent,1001);



                    } else {
                        new Showtoast().showToast(ChangeEmailActivity.this, "ChangeEamil", obj.getString("Message"), findViewById(R.id.rr_forgotmain));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    }