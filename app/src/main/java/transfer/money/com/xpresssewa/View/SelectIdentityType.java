package transfer.money.com.xpresssewa.View;

import android.content.Intent;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

public class SelectIdentityType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_identity_type);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        init();
    }
    private void init()
    {
        String htmlAsString = getString(R.string.instruction_html);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout ll_nationidcard =findViewById(R.id.ll_nationalidcard);
        LinearLayout ll_passport= findViewById(R.id.ll_passportimg);
        LinearLayout ll_driverlicence= findViewById(R.id.ll_driverlicence);

        ll_nationidcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent ineIntent=new Intent(SelectIdentityType.this,MessageActivity.class);
                ineIntent.putExtra("Title","National Id Card");
                startActivityForResult(ineIntent,101);

            }
        });


        ll_passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ineIntent=new Intent(SelectIdentityType.this,MessageActivity.class);
                ineIntent.putExtra("Title","Passport");
                startActivityForResult(ineIntent,101);
            }
        });

        ll_driverlicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ineIntent=new Intent(SelectIdentityType.this,MessageActivity.class);
                ineIntent.putExtra("Title","Driver`s Licence");
                startActivityForResult(ineIntent,101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            Intent intent=new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
    }



}