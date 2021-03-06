package transfer.money.com.xpresssewa.ticket;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.ViewTicketAdapter;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class MyTicketsActivity extends AppCompatActivity {
   RecyclerView view_all_tickets_recycler;
   private int skipCount=0;
   private ArrayList<JSONObject> datAr=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);

        actions();
        getAllTickets();
    }
    private void actions()
    {
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView headertexttitle =findViewById(R.id.headertexttitle1);
        headertexttitle.setText("My Tickets");


    }

    ViewTicketAdapter transferPurposeAdater;
    private void init()
    {
        view_all_tickets_recycler=findViewById(R.id.view_all_tickets_recycler);
        transferPurposeAdater = new ViewTicketAdapter(datAr, MyTicketsActivity.this);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_all_tickets_recycler.setLayoutManager(horizontalLayoutManagaer);
        view_all_tickets_recycler.setItemAnimator(new DefaultItemAnimator());
        view_all_tickets_recycler.setAdapter(transferPurposeAdater);

        view_all_tickets_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0)
                {
                    getAllTickets();
                    //scrolled to BOTTOM
                }else if (!recyclerView.canScrollVertically(-1) && dy < 0)
                {
                    //scrolled to TOP

                }
            }
        });


    }

    private void getAllTickets() {
        UtilClass.getUserData(MyTicketsActivity.this);
        Map<String, String> m = new LinkedHashMap<>();
        m.put("TicketId", "");
        m.put("Priority", "0");
        m.put("IsClosed", "0");
        m.put("CategoryId", "0");
        m.put("CategoryId", "0");
        m.put("MemberId", UtilClass.member_id);
        m.put("Take", "10");
        m.put("Skip", skipCount+"");

        new ServerHandler().sendToServer(this, "GetTickets", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject obj = new JSONObject(dta);
                    System.out.println("TicketData back==" + obj);
                    if (obj.getBoolean("status")) {

                        JSONArray jsonObjectsAr = obj.getJSONArray("TicketList");
                        for (int x = 0; x < jsonObjectsAr.length(); x++) {
                            datAr.add(jsonObjectsAr.getJSONObject(x));
                        }
                        if(skipCount==0)
                        {
                            init();
                        }
                        else
                        {
                            transferPurposeAdater.notifyDataSetChanged();
                        }


                    } else {
                        if(skipCount==0) {
                            new Showtoast().showToast(MyTicketsActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }
}