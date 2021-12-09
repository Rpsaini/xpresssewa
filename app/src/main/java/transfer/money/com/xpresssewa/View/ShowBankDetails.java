package transfer.money.com.xpresssewa.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

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

public class ShowBankDetails extends BaseActivity {

    TextView txt_payeename,txt_AccountNumber,txt_BsbCode,txt_UniqueId;
    LinearLayout ll_bakdetail;
    TextView txt_note;
    private ImageView img_payeename_copy,img_AccountNumber_copy,img_BsbCode_copy,img_UniqueId_copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bank_details);
        init();
        getBankDetail();

    }

    @Override
    protected void init() {

        txt_payeename=findViewById(R.id.txt_payeename);
        txt_AccountNumber= findViewById(R.id.txt_AccountNumber);
        txt_BsbCode=findViewById(R.id.txt_BsbCode);
        txt_UniqueId=findViewById(R.id.txt_UniqueId);
        ll_bakdetail=findViewById(R.id.ll_bakdetail);
        txt_note=findViewById(R.id.txt_note);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            txt_note.setText(Html.fromHtml("<html>1.Funds must be sent from an account in your name.<br/>\n" +
                    "        2.You must include <font color='#3F70BC'> 86151E38 </font>in your bank transfer payment description field.<br/>\n" +
                    "        3.RTGS payments are not accepted. If you must send by RTGS, please contact customer support\n" +
                    "        We never takes money automatically from your account.<br/><br/>\n" +
                    "\n" +
                    "        You can use your online banking or mobile app to make your bank transfer to us.</html>", Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            txt_note.setText(Html.fromHtml("<html>1.Funds must be sent from an account in your name.<br/>\n" +
                    "        2.You must include <font color='#3F70BC'> 86151E38 </font>in your bank transfer payment description field.<br/>\n" +
                    "        3.RTGS payments are not accepted. If you must send by RTGS, please contact customer support\n" +
                    "        We never takes money automatically from your account.<br/><br/>\n" +
                    "\n" +
                    "        You can use your online banking or mobile app to make your bank transfer to us.</html>"));
            }


        findViewById(R.id.txt_paymentdone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTransaction();
            }
        });
        findViewById(R.id.txt_paymentcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_payeename_copy=findViewById(R.id.img_payeename_copy);
        img_AccountNumber_copy=findViewById(R.id.img_AccountNumber_copy);
        img_BsbCode_copy=findViewById(R.id.img_BsbCode_copy);
        img_UniqueId_copy=findViewById(R.id.img_UniqueId_copy);


        img_payeename_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_payeename.getText().toString(),"Payee Name");
            }
        });

        img_AccountNumber_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_AccountNumber.getText().toString(),"Account Number");
            }
        });

        img_BsbCode_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_BsbCode.getText().toString(),"BSB code");
            }
        });
        img_UniqueId_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_UniqueId.getText().toString(),"UniqueId");
            }
        });




    }

    private  void getBankDetail()
    {


        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        new ServerHandler().sendToServer(ShowBankDetails.this, "BankListBySymbol", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    System.out.println("response bank obj==" + obj);
                    if(obj.getBoolean("status"))
                    {
                        txt_payeename.setText(obj.getString("PayeeName"));
                        txt_AccountNumber.setText(obj.getString("AccountNumber"));
                        txt_BsbCode.setText(obj.getString("BsbCode"));
                        txt_UniqueId.setText(obj.getString("UniqueId"));

                    } else {
                        new Showtoast().showToast(ShowBankDetails.this, "Response", obj.getString("Message"), findViewById(R.id.ll_activitylayout));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }



    private void addTransaction()
    {
        String data =  getIntent().getStringExtra("data");
        LinkedHashMap<String, String> mapData = new Gson().fromJson(data, LinkedHashMap.class);
        mapData.put("BankId",getIntent().getStringExtra("bank_id"));
        mapData.put("BankRefNumber",txt_UniqueId.getText().toString());


        new ServerHandler().sendToServer(ShowBankDetails.this, "AddTransaction", mapData, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getString("Status").equalsIgnoreCase("Success"))
                     {
                         Intent intent = new Intent();
                         intent.putExtra("isSendScreenDataLoaded", "showTransaction");
                         setResult(RESULT_OK, intent);
                         finish();
                     }
                    else {
                        String msg = obj.getString("Message");
                        if (msg.length() == 0) {
                            new Showtoast().showToast(ShowBankDetails.this, "Error", "Server communication error. .", ll_bakdetail);
                        } else {
                            new Showtoast().showToast(ShowBankDetails.this, "Error", msg, ll_bakdetail);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

    }



}