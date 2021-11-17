package transfer.money.com.xpresssewa.View;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.NotificationAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        actions();
        getNotification();

    }

    private void init(JSONArray datAr)
    {
        RecyclerView recycler_view_for_notification = findViewById(R.id.recycler_view_for_notification);
        NotificationAdapter transferPurposeAdater = new NotificationAdapter(datAr, NotificationActivity.this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_for_notification.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_for_notification.setItemAnimator(new DefaultItemAnimator());
        recycler_view_for_notification.setAdapter(transferPurposeAdater);
    }

    private void actions() {
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


private void getNotification()
{
    try {
        SaveImpPrefrences saveImpPrefrences=new SaveImpPrefrences();

        saveImpPrefrences.savePrefrencesData(this,"0",DefaultConstatnts.notification_count);

        String notificationData = saveImpPrefrences.reterivePrefrence(this, DefaultConstatnts.getNotificationData) + "";
        if (!notificationData.equalsIgnoreCase("0"))
        {
            JSONArray jsonArray = new JSONArray(notificationData);
            init(jsonArray);
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }



}

}
