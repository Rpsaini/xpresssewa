package transfer.money.com.xpresssewa.View;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.validation.Showtoast;
import transfer.money.com.xpresssewa.validation.Validation;

public class ForgotPassword extends AppCompatActivity {

    private Validation vd;
    private  String callFrom="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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

        TextView createpin=findViewById(R.id.createpin);
        TextView tv_submit=findViewById(R.id.tv_submit);


         callFrom=getIntent().getStringExtra("callFrom");

         if(callFrom.equalsIgnoreCase("pin"))
         {
             createpin.setText("Forgot Pin");
             tv_submit.setText("Reset Pin");
         }

        vd=new Validation(this);
        final EditText et_email =findViewById(R.id.et_email);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(ForgotPassword.this);
                if(et_email.getText().toString().length()==0) {
                    new Showtoast().showToast(ForgotPassword.this, "Response", "Please enter your email address.", findViewById(R.id.rr_forgotmain));
                }
                else if(!vd.checkEmail(et_email.getText().toString()))
                {
                    new Showtoast().showToast(ForgotPassword.this, "Response", "Please enter valid email address.", findViewById(R.id.rr_forgotmain));
                }
                else
                {

                    getOtpRequest(et_email.getText().toString());


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111)
        {
            finish();
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


        Map<String,String> map = new LinkedHashMap<String, String>();
        map.put("Email", email);




        new ServerHandler().sendToServer(this, "Forget", map, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status"))
                    {
                        if(callFrom.equalsIgnoreCase("pin"))
                        {

                            Intent intent=new Intent(ForgotPassword.this,ForgotPinJ.class);
                            intent.putExtra("OTP", obj.getString("OTP"));
                            intent.putExtra("email", email);
                            intent.putExtra("MemberId", obj.getString("MemberId"));
                            startActivityForResult(intent,111);
                        }
                        else
                        {

                            Intent intent = new Intent(ForgotPassword.this, ForgotPasswordWithOtpJ.class);
                            intent.putExtra("email", email);
                            intent.putExtra("OTP", obj.getString("OTP"));
                            intent.putExtra("MemberId", obj.getString("MemberId"));
                            startActivityForResult(intent, 111);
                        }

                    }
                    else {
                        new Showtoast().showToast(ForgotPassword.this,"Registration", obj.getString("Message"),findViewById(R.id.rr_forgotmain));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }



}
