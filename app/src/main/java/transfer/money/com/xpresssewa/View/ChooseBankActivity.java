package transfer.money.com.xpresssewa.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.AddImageAdapter;
import transfer.money.com.xpresssewa.Adapter.ChooseBankAdapter;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class ChooseBankActivity extends BaseActivity {

    String  data="";
    public String selectedBankId="";
    private TextView tv_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        data =  getIntent().getStringExtra("data");
        init();
        LinkedHashMap<String, String> mapData = new Gson().fromJson(data, LinkedHashMap.class);
        getBankList(mapData.get("FromSymbol"));

    }

    @Override
    protected void init() {
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        tv_confirm =findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedBankId.length()>0)
                {
                   Intent intent=new Intent(ChooseBankActivity.this,ShowBankDetails.class);
                   intent.putExtra("data",data);
                   intent.putExtra("bank_id",selectedBankId);
                   startActivityForResult(intent,1002);
                }
                else
                {
                    new Showtoast().showToast(ChooseBankActivity.this, "Response", "No bank selected", findViewById(R.id.ll_activitylayout));
                }


            }
        });
    }

    protected void init(JSONArray bankAr) {
        RecyclerView recycler_view_default_payment = findViewById(R.id.recycler_banklist);
        ChooseBankAdapter defaultTransferAdapter = new ChooseBankAdapter(bankAr, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_default_payment.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_default_payment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_default_payment.setAdapter(defaultTransferAdapter);
    }

    private  void getBankList(String symbol)
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("Symbol", symbol);
        new ServerHandler().sendToServer(ChooseBankActivity.this, "UserBankList", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    System.out.println("response obj==" + obj);
                    if (obj.getBoolean("status"))
                    {
                        init(obj.getJSONArray("BankData"));

                    } else {
                        new Showtoast().showToast(ChooseBankActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_activitylayout));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void enableButton()
    {
        if(selectedBankId.length()==0)
        {
            tv_confirm.setAlpha(.5f);
            tv_confirm.setEnabled(false);
        }
        else
        {

            tv_confirm.setAlpha(1f);
            tv_confirm.setEnabled(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1002)
        {
            if(data!=null) {
                Intent intent = new Intent();
                intent.putExtra("isSendScreenDataLoaded", "showTransaction");
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }
}