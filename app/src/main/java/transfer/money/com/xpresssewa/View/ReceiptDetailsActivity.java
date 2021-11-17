package transfer.money.com.xpresssewa.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import transfer.money.com.xpresssewa.Adapter.ReceiptDetailsViewAdapter;
import transfer.money.com.xpresssewa.R;


public class ReceiptDetailsActivity extends AppCompatActivity {
    private ImageView backIC,img_nodatafound;
    private TextView shortNameTV,userName,userAccount;
    private RecyclerView receiptDetailsRV;
    private JSONObject fullDetailsData;
    private JSONArray jsonArray;
    private String short_name,recipient_type;
    private ArrayList<JSONObject> fullDetailsList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept_details);
        init();
        setOnClickListener();
        getReceiptDetailsData();
    }

    private void init(){
        backIC=findViewById(R.id.backarrow);
        img_nodatafound=findViewById(R.id.img_nodatafound);
        receiptDetailsRV=findViewById(R.id.receiptDetailRV);
        shortNameTV=findViewById(R.id._shorNameTV);
        userName=findViewById(R.id.txt_username);
        userAccount=findViewById(R.id.txt_userAccount);
        img_nodatafound.setVisibility(View.VISIBLE);
    }
    private void getReceiptDetailsData(){
        try {
            if (getIntent().hasExtra("full_details")){

                fullDetailsData = new JSONObject(getIntent().getStringExtra("full_details"));
            }
            if (getIntent().hasExtra("short_name")){

                short_name = getIntent().getStringExtra("short_name");
            }
            if (getIntent().hasExtra("recipient_type")){

                recipient_type = getIntent().getStringExtra("recipient_type");
            }
            Log.e(ReceiptDetailsActivity.class.getSimpleName(),"data::"+fullDetailsData);


            if(fullDetailsData.has("ReciptentName")){
                JSONObject accountHolderNameObj=new JSONObject();
                accountHolderNameObj.put("Title","Account Holder Name");
                accountHolderNameObj.put("Answer",fullDetailsData.getString("ReciptentName"));
                fullDetailsList.add(accountHolderNameObj);
            }

            if(fullDetailsData.has("ReciptentEmail")){
                JSONObject emailObj=new JSONObject();
                emailObj.put("Title","Recipient's Email");
                emailObj.put("Answer",fullDetailsData.getString("ReciptentEmail"));
                fullDetailsList.add(emailObj);
            }



            JSONObject recipientTypeObj=new JSONObject();
            recipientTypeObj.put("Title","Recipient Type");
            recipientTypeObj.put("Answer",recipient_type);
            fullDetailsList.add(recipientTypeObj);

            if(fullDetailsData.has("FullDetail")){
                jsonArray=fullDetailsData.getJSONArray("FullDetail");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject obj=jsonArray.getJSONObject(i);
                    fullDetailsList.add(obj);
                }

            }

            if(fullDetailsData.has("Address")){
                JSONObject addressObj=new JSONObject();
                addressObj.put("Title","Address");
                addressObj.put("Answer",fullDetailsData.getString("Address"));
                fullDetailsList.add(addressObj);
            }

            if(fullDetailsData.has("City")){
                JSONObject cityObj=new JSONObject();
                cityObj.put("Title","City");
                cityObj.put("Answer",fullDetailsData.getString("City"));
                fullDetailsList.add(cityObj);
            }

            if(fullDetailsData.has("State")){
                JSONObject stateObj=new JSONObject();
                stateObj.put("Title","State");
                stateObj.put("Answer",fullDetailsData.getString("State"));
                fullDetailsList.add(stateObj);
            }


            if(fullDetailsData.has("CountryCode")){
                JSONObject countryCodeObj=new JSONObject();
                countryCodeObj.put("Title","CountryCode");
                countryCodeObj.put("Answer",fullDetailsData.getString("CountryCode"));
                fullDetailsList.add(countryCodeObj);
            }


            if(fullDetailsData.has("Symbol")){
                JSONObject reciptentAccountNumberObj=new JSONObject();
                reciptentAccountNumberObj.put("Title","Symbol");
                reciptentAccountNumberObj.put("Answer",fullDetailsData.getString("Symbol"));
                fullDetailsList.add(reciptentAccountNumberObj);
            }




            if(fullDetailsData.has("ZipCode")){
                JSONObject zipCodeObj=new JSONObject();
                zipCodeObj.put("Title","ZipCode");
                zipCodeObj.put("Answer",fullDetailsData.getString("ZipCode"));
                fullDetailsList.add(zipCodeObj);
            }

            shortNameTV.setText(short_name);
            String userNa=fullDetailsData.getString("ReciptentName")+" "+fullDetailsData.getString("ReciptentLastName");
            userName.setText(userNa);

            String userAcc=fullDetailsData.getString("Symbol")+" Account";
            userAccount.setText(userAcc);

          //  userAccount.setText(short_name);
            initRecyler(fullDetailsList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setOnClickListener(){
        backIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initRecyler(ArrayList<JSONObject> data)
    {
        if(data.size()>0)
        {
            img_nodatafound.setVisibility(View.GONE);
        }

        ReceiptDetailsViewAdapter mAdapter = new ReceiptDetailsViewAdapter(data, ReceiptDetailsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReceiptDetailsActivity.this);
        receiptDetailsRV.setLayoutManager(mLayoutManager);
        receiptDetailsRV.setItemAnimator(new DefaultItemAnimator());
        receiptDetailsRV.setAdapter(mAdapter);
    }

}
