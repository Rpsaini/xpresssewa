package transfer.money.com.xpresssewa.View;

import android.os.Build;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.fragments.TransactionDetailFrg;
import transfer.money.com.xpresssewa.fragments.TransactionUpdateFrg;

public class TransactionDetailView extends AppCompatActivity {
   public String transactionId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail_view);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        setFragments(new TransactionUpdateFrg());
        transactionId= getIntent().getStringExtra("id");
        init();
    }

      private void init()
      {
        TextView txt_amount=findViewById(R.id.txt_amount);
        TextView txt_receivername=findViewById(R.id.txt_receivername);
        TextView txt_status=findViewById(R.id.txt_status);
        findViewById(R.id.backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              //txt_amount.setText("Sent <b>" +getIntent().getStringExtra("ToAmount")) + "</b>", Html.FROM_HTML_MODE_COMPACT);
              txt_receivername.setText(Html.fromHtml("To <b>" + getIntent().getStringExtra("TransferTo") + "</b>", Html.FROM_HTML_MODE_COMPACT));
              txt_amount.setText(Html.fromHtml("Sent <b>" + getIntent().getStringExtra("ToAmount") + "</b>", Html.FROM_HTML_MODE_COMPACT));
          } else {
             // txt_amount.setText(getIntent().getStringExtra("ToAmount"));
              txt_receivername.setText(Html.fromHtml("To <b>" + getIntent().getStringExtra("TransferTo") + "</b>"));
              txt_amount.setText(Html.fromHtml("Sent <b>" + getIntent().getStringExtra("ToAmount") + "</b>"));
          }

        final View txt_line_update = findViewById(R.id.txt_line_update);
        final View txt_line_detail = findViewById(R.id.txt_line_detail);
        final TextView txt_update =  findViewById(R.id.txt_update);
        final TextView txt_detail =  findViewById(R.id.txt_detail);

//       txt_detail.setTextColor(getResources().getColor(R.color.blue_bt_color));
//          txt_update.setTextColor(getResources().getColor(R.color.black_color));

          txt_update.setTextColor(getResources().getColor(R.color.blue_bt_color));
          txt_detail.setTextColor(getResources().getColor(R.color.black_color));
          txt_line_update .setBackgroundColor(getResources().getColor(R.color.skybluecolorcode));
          txt_line_detail.setBackgroundColor(getResources().getColor(R.color.light_grey_color));
          setFragments(new TransactionUpdateFrg());


        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_update.setTextColor(getResources().getColor(R.color.blue_bt_color));
                txt_detail.setTextColor(getResources().getColor(R.color.black_color));
                txt_line_update.setBackgroundColor(getResources().getColor(R.color.skybluecolorcode));
                txt_line_detail.setBackgroundColor(getResources().getColor(R.color.light_grey_color));
                setFragments(new TransactionUpdateFrg());
            }
        });

        txt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_detail.setTextColor(getResources().getColor(R.color.blue_bt_color));
                txt_update.setTextColor(getResources().getColor(R.color.black_color));
                txt_line_detail.setBackgroundColor(getResources().getColor(R.color.skybluecolorcode));
                txt_line_update.setBackgroundColor(getResources().getColor(R.color.light_grey_color));
                setFragments(new TransactionDetailFrg());
            }
        });


    }


    private void setFragments(Fragment fragments) {
        Bundle args = new Bundle();
        fragments.setArguments(args);
        FragmentTransaction ft_main = getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.ll_container, fragments);
        ft_main.commit();
    }



}