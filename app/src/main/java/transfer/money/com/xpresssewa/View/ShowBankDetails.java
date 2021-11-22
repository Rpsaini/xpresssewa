package transfer.money.com.xpresssewa.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        System.out.println("bank data==="+mapData);
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