package transfer.money.com.xpresssewa.View;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

public class SelectRecipientsTypeActivity extends AppCompatActivity {

    @BindView(R.id.headerbackbutton)
    ImageView headerbackbutton;

    @BindView(R.id.ll_my_profile)
    LinearLayout ll_my_profile;

    @BindView(R.id.ll_business_someoneelse)
    LinearLayout ll_business_someoneelse;

    @BindView(R.id.ll_buisness)
    LinearLayout ll_buisness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipients_type);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        init();



    }

    private void init() {
        headerbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRecipientsTypeActivity.this, RecipientDynamicActivity.class);
                i.putExtra("rec_type", "Myself");
                i.putExtra("rec_type_str", "1");
                i.putExtra("DestinationSymbol", getIntent().getStringExtra("DestinationSymbol"));
                i.putExtra("SourceSymbol", getIntent().getStringExtra("SourceSymbol"));
                i.putExtra("FlagImageDestination", getIntent().getStringExtra("FlagImageDestination"));
                i.putExtra("callFrom", getIntent().getStringExtra("callFrom"));
                i.putExtra("SDCountryId", getIntent().getStringExtra("SDCountryId"));
                System.out.println("Sd country id===="+getIntent().getStringExtra("SDCountryId"));
                i.putExtra("CountryId", getIntent().getStringExtra("CountryId"));
                if(getIntent().hasExtra("data"))
                {
                    i.putExtra("data", getIntent().getStringExtra("data"));
                }

                startActivityForResult(i, 102);
            }
        });


        ll_business_someoneelse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRecipientsTypeActivity.this, RecipientDynamicActivity.class);
                i.putExtra("rec_type", "Someone Else");
                i.putExtra("rec_type_str", "2");
                i.putExtra("DestinationSymbol", getIntent().getStringExtra("DestinationSymbol"));
                i.putExtra("SourceSymbol", getIntent().getStringExtra("SourceSymbol"));
                i.putExtra("FlagImageDestination", getIntent().getStringExtra("FlagImageDestination"));
                i.putExtra("callFrom", getIntent().getStringExtra("callFrom"));
                i.putExtra("SDCountryId", getIntent().getStringExtra("SDCountryId"));
                i.putExtra("CountryId", getIntent().getStringExtra("CountryId"));
                if(getIntent().hasExtra("data"))
                {
                    i.putExtra("data", getIntent().getStringExtra("data"));
                }
                startActivityForResult(i, 102);
            }
        });

        ll_buisness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRecipientsTypeActivity.this, RecipientDynamicActivity.class);
                i.putExtra("rec_type", "Business");
                i.putExtra("rec_type_str", "3");
                i.putExtra("DestinationSymbol", getIntent().getStringExtra("DestinationSymbol"));
                i.putExtra("SourceSymbol", getIntent().getStringExtra("SourceSymbol"));
                i.putExtra("FlagImageDestination", getIntent().getStringExtra("FlagImageDestination"));
                i.putExtra("callFrom", getIntent().getStringExtra("callFrom"));
                i.putExtra("SDCountryId", getIntent().getStringExtra("SDCountryId"));
                i.putExtra("CountryId", getIntent().getStringExtra("CountryId"));

                if(getIntent().hasExtra("data"))
                {
                    i.putExtra("data", getIntent().getStringExtra("data"));
                }

                startActivityForResult(i, 102);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102)
        {

            if(data!=null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }


}
