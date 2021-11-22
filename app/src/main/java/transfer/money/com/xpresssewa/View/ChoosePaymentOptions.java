package transfer.money.com.xpresssewa.View;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.ChoosePaymentOptionAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.util.UtilClass;

public class ChoosePaymentOptions extends AppCompatActivity {
    private String calculationData="",selectedRecipientData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_options);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        try {

            String calculationData = getIntent().getStringExtra("calculationData");
            String selectedRecipientData = getIntent().getStringExtra("selectedRecipientData");

            JSONObject paymentArray = new JSONObject(calculationData);
            JSONArray paymentMethodAr=paymentArray.getJSONArray("paymentMethod");

            String amountfrom= paymentArray.getString("AmountFrom")+" "+paymentArray.getString("FromSymbol");
            TextView txt_howtosendyou =findViewById(R.id.txt_howtosendyou);
            txt_howtosendyou.setText("Choose payment options");

            init(paymentMethodAr,calculationData,selectedRecipientData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init(JSONArray paymentMethodAr,String calculationData,String selectedRecipientData )
    {

        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recycler_view_default_payment = findViewById(R.id.recycler_view_default_payment);

        final ChoosePaymentOptionAdapter defaultTransferAdapter = new ChoosePaymentOptionAdapter(paymentMethodAr, this,calculationData,selectedRecipientData);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_default_payment.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_default_payment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_default_payment.setAdapter(defaultTransferAdapter);



        findViewById(R.id.tv_Continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoosePaymentOptions.this, ReviewTransfer.class);
                intent.putExtra("calculationData",defaultTransferAdapter.calculationData);
                intent.putExtra("obj",defaultTransferAdapter.selectedOBj+"");
                intent.putExtra("selectedRecipientData",defaultTransferAdapter.selectedRecipientData+"");
                intent.putExtra("purposeID",getIntent().getStringExtra("purposeID"));
                intent.putExtra("refrenceId",getIntent().getStringExtra("refrenceId"));
                intent.putExtra(UtilClass.transferReason,getIntent().getStringExtra(UtilClass.transferReason));
                intent.putExtra(UtilClass.transferReference,getIntent().getStringExtra(UtilClass.transferReference));
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
                Intent intent = new Intent();
                intent.putExtra("data", data.getStringExtra("data"));
                intent.putExtra("isSendScreenDataLoaded", data.getStringExtra("isSendScreenDataLoaded"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}