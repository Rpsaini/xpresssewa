package transfer.money.com.xpresssewa.View;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

public class PriceComparisonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_comparison);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);

       // ButterKnife.bind(this);
     //   init();
    }

}
