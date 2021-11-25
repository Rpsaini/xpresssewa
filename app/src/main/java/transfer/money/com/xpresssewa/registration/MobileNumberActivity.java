package transfer.money.com.xpresssewa.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class MobileNumberActivity extends BaseActivity {

    private String countryMobilecode = "";

    private Showtoast showtoast;


    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;


    @BindView(R.id.tv_verify)
    TextView tv_verify;


    @BindView(R.id.ed_mobilenumber)
    EditText ed_mobilenumber;



    @BindView(R.id.ll_forgotmain)
    RelativeLayout ll_forgotmain;


    @BindView(R.id.headerbackbutton)
    ImageView headerbackbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        ButterKnife.bind(this);
        showtoast = new Showtoast();
        init();
    }

    @Override
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
            public void onClick(View v) {
                countryMobilecode = countryCodePicker.getSelectedCountryCode();
                if(ed_mobilenumber.getText().toString().length()<=0)
                {
                    showtoast.showToast(MobileNumberActivity.this, "Select", "Please enter Mobile number", ll_forgotmain);
                    return;
                }
                else if(ed_mobilenumber.getText().toString().length()!=10)
                {
                    showtoast.showToast(MobileNumberActivity.this, "Select", "Please enter valid Mobile number", ll_forgotmain);
                    return;
                }

                if (!countryMobilecode.equalsIgnoreCase("61"))//for austraila
                {
                    showtoast.showToast(MobileNumberActivity.this, "Select", "Sorry!! We provide remit services from Australia only,please provide Australia phone number to proceed. ", ll_forgotmain);
                    return;
                }

                Intent intent =new Intent(MobileNumberActivity.this,VerifyOTP.class);
                intent.putExtra("memberId",getIntent().getStringExtra("memberId"));
                intent.putExtra("Phone",ed_mobilenumber.getText().toString());
                intent.putExtra("PhoneExt",countryMobilecode);
                intent.putExtra("callfrom",getIntent().getStringExtra("callfrom"));
                startActivityForResult(intent,1001);



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)
        {
            if(data!=null)
            {
               if(data.getStringExtra("callfrom").equalsIgnoreCase("profile"))
               {
                   Intent intent=new Intent();
                   setResult(RESULT_OK,intent);
                   intent.putExtra("Phone",  data.getStringExtra("Phone"));
                   intent.putExtra("PhoneExt",  data.getStringExtra("PhoneExt"));
                   finish();


               }
               else
               {
                   Intent intent=new Intent();
                   setResult(RESULT_OK,intent);
                   intent.putExtra("data","back");
                   finish();
               }


            }
        }
    }


}