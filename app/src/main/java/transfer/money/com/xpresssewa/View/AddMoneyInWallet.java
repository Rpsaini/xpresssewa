package transfer.money.com.xpresssewa.View;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.util.CircleTransform;

public class AddMoneyInWallet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_in_wallet);
        init();
    }
    private void init()
    {
        try
            {
            String walletData = getIntent().getStringExtra("data");
            JSONObject jsonObject=new JSONObject(walletData);
            final String WalletSymbol=jsonObject.getString("WalletSymbol");
            final String TotalAmount=jsonObject.getString("TotalAmount");
            final String FlagImage=jsonObject.getString("FlagImage");
            final String Id=jsonObject.getString("Id");

            TextView txt_currency_balance =findViewById(R.id.txt_currency_balance);
            ImageView curreny_icon=findViewById(R.id.curreny_icon);
            TextView txt_symbol=findViewById(R.id.txt_symbol);

            showImage(FlagImage,curreny_icon);
            txt_currency_balance.setText(TotalAmount);
            txt_symbol.setText(WalletSymbol);

            findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 finish();
                }
            });


            findViewById(R.id.add_money).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(AddMoneyInWallet.this,AddBalance.class);
                    intent.putExtra("WalletSymbol",WalletSymbol);
                    intent.putExtra("TotalAmount",TotalAmount);
                    intent.putExtra("FlagImage",FlagImage);
                    intent.putExtra("Id",Id);
                    startActivity(intent);
                }
            });



            findViewById(R.id.convert_money).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AddMoneyInWallet.this,ConvertMoney.class);
                    intent.putExtra("WalletSymbol",WalletSymbol);
                    intent.putExtra("TotalAmount",TotalAmount);
                    intent.putExtra("FlagImage",FlagImage);
                    intent.putExtra("Id",Id);
                    startActivity(intent);
                }
            });

            findViewById(R.id.send_money).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AddMoneyInWallet.this,MainActivity.class);
                    intent.putExtra("external_call","yes");
                    startActivity(intent);
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void showImage(String path, final ImageView profileimage) {
        try {
            Picasso.with(this)
                    .load(path).transform(new CircleTransform())
                    .into(profileimage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(null)
                                    .load(R.drawable.america_flag)
                                    .into(profileimage);
                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}