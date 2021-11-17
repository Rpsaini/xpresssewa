package transfer.money.com.xpresssewa.View;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.BankDetailAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class ReviewTransfer extends AppCompatActivity {

    private Map<String, String> m = new LinkedHashMap<>();
    LinearLayout ll_reviewKyc;
    private String transferamount = "", transFerto = "";
    private TextView tv_cancel, txt_note;
    private String imeiNumber = "", ip = "";
    private String PaymentId, TransactionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_transfer);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        TextView headerTitle = findViewById(R.id.headertexttitle);
        ll_reviewKyc = findViewById(R.id.ll_reviewKyc);
        tv_cancel = findViewById(R.id.tv_cancel);
        txt_note = findViewById(R.id.txt_note);
        getImeNumber();
        getIPAddress();
        System.out.println("Ip----" + ip);
        // headerTitle.setText(getResources().getString(R.string.reviewyourtransfer));
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {

            UtilClass.getUserData(this);

            final String calculationData = getIntent().getStringExtra("calculationData");
            String selectedObj = getIntent().getStringExtra("obj");
            final String selectedRecipientData = getIntent().getStringExtra("selectedRecipientData");

            String purposeId = getIntent().getStringExtra("purposeID");
            String referenceId = getIntent().getStringExtra("refrenceId");
            String username = new SaveImpPrefrences().reterivePrefrence(ReviewTransfer.this, DefaultConstatnts.UserName).toString();

            System.out.println("review transfer===" + calculationData + "===" + selectedObj + "===" + selectedRecipientData);
            System.out.println("referenceiud===" + purposeId + "===" + referenceId + "===" + username);


            JSONObject calculationObj = new JSONObject(calculationData);
            String convertedAmount = calculationObj.getString("ConvertAmount") + calculationObj.getString("ToSymbol");
            final JSONObject recipientObj = new JSONObject(selectedRecipientData);


            TextView txt_amount_transferred = findViewById(R.id.txt_amount_transferred);
            txt_amount_transferred.setText("An amount of " + convertedAmount + " estimated to reach in your account " + calculationObj.getString("ProcessingDays"));


            //JSONObject recipientData=new JSONObject(selectedObj);
            JSONArray FullDetail = recipientObj.getJSONArray("FullDetail");


            setBankData(FullDetail);


            TextView txt_account_holder_detail = findViewById(R.id.txt_account_holder_detail);
            //String recipientName=recipientObj.getString("Symbol")+" Bank detail for "+recipientObj.getString("ReciptentName");
            String recipientName = recipientObj.getString("ReciptentName");
            txt_account_holder_detail.setText(recipientName);


            JSONObject paymentMethod = calculationObj.getJSONArray("paymentMethod").getJSONObject(0);


            m = new LinkedHashMap<>();
            m.put("UserName", username);
            m.put("PaymentId", PaymentId);
            m.put("TransactionId", TransactionId);
            m.put("CurrencyId", calculationObj.getString("FromCurrencyId"));
            m.put("FromCurrencyId", calculationObj.getString("CurrencyId"));
            m.put("BankId", "0");
            m.put("ToSymbol", calculationObj.getString("ToSymbol"));
            m.put("FromSymbol", calculationObj.getString("FromSymbol"));
            m.put("PaymentTypeId", paymentMethod.getString("PaymentTypeId"));
            m.put("Amount", calculationObj.getString("AmountFrom"));
            m.put("BankRefNumber", "");
            m.put("TransferTo", recipientObj.getString("ReciptentName"));
            m.put("RecipientId", recipientObj.getString("Id"));
            m.put("TransferPurpose", purposeId);
            m.put("MemberId", UtilClass.member_id);
            m.put("TransferReference", referenceId);
            m.put("TWRecipientId", recipientObj.getString("TWRecipientId"));
            m.put("TWQuoteId", calculationObj.getString("QuoteID"));
            m.put("PaymentStatus", "1");//for poli
            m.put("IpAddress", ip);//for poli
            m.put("MacAddress", imeiNumber + "");//for poli

            //params


            transferamount = calculationObj.getString("AmountFrom") + calculationObj.getString("FromSymbol");
            transFerto = recipientObj.getString("ReciptentName");
            calculationObj.getString("AmountTo");

            TextView you_send = findViewById(R.id.you_send);
            TextView you_send_symbol = findViewById(R.id.you_send_symbol);
            TextView tv_usergetsAmount = findViewById(R.id.tv_usergetsAmount);
            TextView tv_usergetsAmount_symbol = findViewById(R.id.tv_usergetsAmount_symbol);
            TextView fee_applied = findViewById(R.id.fee_applied);
            TextView fee_applied_symbol = findViewById(R.id.fee_applied_symbol);


            TextView gurantee_rate = findViewById(R.id.gurantee_rate);
            TextView gurantee_rate_symbol = findViewById(R.id.gurantee_rate_symbol);


            you_send.setText(calculationObj.getString("AmountFrom"));
            you_send_symbol.setText(calculationObj.getString("FromSymbol"));
            tv_usergetsAmount.setText(calculationObj.getString("AmountTo"));
            tv_usergetsAmount_symbol.setText(calculationObj.getString("ToSymbol"));

            fee_applied.setText(calculationObj.getString("TotalFee"));
            fee_applied_symbol.setText(calculationObj.getString("FromSymbol"));

            gurantee_rate.setText(calculationObj.getString("GuaranteedRate") + " hrs");
            gurantee_rate_symbol.setText(calculationObj.getString("ConversionRate"));

            TextView tv_user_gets = findViewById(R.id.tv_user_gets);
            tv_user_gets.setText(recipientObj.getString("ReciptentName") + " will receive");

            String Recipientemail = recipientObj.getString("ReciptentEmail");
            String ReciptentName = recipientObj.getString("ReciptentName");

            txt_note.setText(ReciptentName + "(" + Recipientemail + ") will be informed via email.");
            findViewById(R.id.changeBankDetail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go  to recipient list
                    try {
                        Intent intent = new Intent();
                        recipientObj.put("calculation", calculationData + "");
                        intent.putExtra("data", recipientObj + "");
                        intent.putExtra("isSendScreenDataLoaded", "no");
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            findViewById(R.id.txt_changeamount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go  to recipient list
                    try {
                        Intent intent = new Intent();
                        recipientObj.put("calculation", calculationData + "");
                        intent.putExtra("data", recipientObj + "");
                        intent.putExtra("isSendScreenDataLoaded", "yes");
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        SimpleDialog simpleDialog = new SimpleDialog();
                        final Dialog confirmDialog = simpleDialog.simpleDailog(ReviewTransfer.this, R.layout.confirmation_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                        ImageView selected_Curreny = confirmDialog.findViewById(R.id.selected_Curreny);
                        TextView txt_currency_name = confirmDialog.findViewById(R.id.txt_currency_name);
                        TextView txt_no = confirmDialog.findViewById(R.id.txt_no);
                        TextView txt_yes = confirmDialog.findViewById(R.id.txt_yes);
                        TextView txt_msg = confirmDialog.findViewById(R.id.txt_msg);
                        selected_Curreny.setImageResource(R.drawable.checked);
                        txt_currency_name.setText("Cancel");
                        txt_msg.setText("Are you sure want to cancel this transaction ?");

                        txt_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();
                            }
                        });
                        txt_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent();
                                    recipientObj.put("calculation", calculationData + "");
                                    intent.putExtra("data", recipientObj + "");
                                    intent.putExtra("isSendScreenDataLoaded", "yes");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            //get payment id
            Map<String, String> calculationMap = new LinkedHashMap<>();
            calculationMap.put("RecipientId", m.get("TWRecipientId"));
            calculationMap.put("MemberId", UtilClass.member_id);
            calculationMap.put("ToSymbol", m.get("ToSymbol"));
            calculationMap.put("FromSymbol", m.get("FromSymbol"));
            calculationMap.put("AmountFrom", calculationObj.getString("AmountFrom"));
            calculationMap.put("AmountTo", calculationObj.getString("AmountTo"));
            calculationMap.put("Type", calculationObj.getString("Type"));
            calculationMap.put("CountryName", "");
            calculationMap.put("PaymentType", "");
            getCalculationData(calculationMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // loadData();

        findViewById(R.id.tv_transfer_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m.put("PaymentId", PaymentId);
                m.put("TransactionId", TransactionId);


                new ServerHandler().sendToServer(ReviewTransfer.this, "AddTransaction", m, 0, 1, new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons) {

                        try {
                            JSONObject obj = new JSONObject(dta);
                            if (obj.getString("Status").equalsIgnoreCase("Success")) {
                                Intent intent = new Intent(ReviewTransfer.this, MakePaymentByWeb.class);
                                intent.putExtra("paymentlink", obj.getString("NavigateUrl"));
                                intent.putExtra("amount", transferamount);
                                intent.putExtra("transferto", transFerto);
                                startActivityForResult(intent, 1002);


                            } else {
                                String msg = obj.getString("Message");
                                if (msg.length() == 0) {
                                    new Showtoast().showToast(ReviewTransfer.this, "Error", "Server communication error. .", ll_reviewKyc);
                                } else {
                                    new Showtoast().showToast(ReviewTransfer.this, "Error", msg, ll_reviewKyc);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                });
            }
        });

    }

    private void getCalculationData(Map<String, String> calculationData) {
        new ServerHandler().sendToServer(this, "Calculation", calculationData, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        JSONArray dataAr = obj.getJSONArray("PaymentTypesList");
                        System.out.println("Data arr--" + dataAr);
                        JSONObject dataObj = dataAr.getJSONObject(0);
                        PaymentId = dataObj.getString("PaymentId");
                        TransactionId = dataObj.getString("TransactionId");


                    } else {
                        String msg = obj.getString("Message");
                        new Showtoast().showToast(ReviewTransfer.this, "Error", msg, ll_reviewKyc);

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }


    private void setBankData(JSONArray bankData) {
        RecyclerView AreadyAddedRecipientRecycerOther = findViewById(R.id.recycler_bank_detail);
        BankDetailAdapter otherDetailsAdapterother = new BankDetailAdapter(bankData, this);
        LinearLayoutManager horizontalLayoutManagaerother = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        AreadyAddedRecipientRecycerOther.setLayoutManager(horizontalLayoutManagaerother);
        AreadyAddedRecipientRecycerOther.setItemAnimator(new DefaultItemAnimator());
        AreadyAddedRecipientRecycerOther.setAdapter(otherDetailsAdapterother);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Return code====" + requestCode);
        if (requestCode == 1002) {
            Intent intent = new Intent();
            intent.putExtra("isSendScreenDataLoaded", "isTransaction");
            intent.putExtra("calculation", "");
            intent.putExtra("data", "nodata");
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private int getImeNumber() {

        if (checkAndRequestPermissions() == 0) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telephonyManager.getDeviceId();

        }

        return 0;
    }

    private int checkAndRequestPermissions() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return 1;
        }

        return 0;
    }



    public String getIPAddress() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlip = "http://checkip.amazonaws.com/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ip=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error====" + error);
            }
        });

        queue.add(stringRequest);
    return "";

}





}
