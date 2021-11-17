package transfer.money.com.xpresssewa.View;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.TransactionAdapter;
import transfer.money.com.xpresssewa.R;

public class TransactionListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelistmain);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        // add data for displaying in expandable list view
        showRecentActivity();
    }

    private void showRecentActivity() {
        RecyclerView showImageRecycler = findViewById(R.id.recyclerview);
        TransactionAdapter mAdapter = new TransactionAdapter(new ArrayList<String>(), this);

        showImageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        showImageRecycler.setItemAnimator(new DefaultItemAnimator());
        showImageRecycler.setAdapter(mAdapter);
    }
}