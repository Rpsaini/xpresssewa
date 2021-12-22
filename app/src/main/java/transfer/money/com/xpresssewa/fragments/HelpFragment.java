package transfer.money.com.xpresssewa.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.ViewTicketAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.ticket.AddTicket;
import transfer.money.com.xpresssewa.ticket.MyTicketsActivity;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;
public class HelpFragment extends Fragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_toolbar)
    ImageView iv_toolbar;


    @BindView(R.id.view_all_tickets_recycler)
    RecyclerView view_all_tickets_recycler;



    @BindView(R.id.nodataFound)
    LinearLayout nodataFound;

    @BindView(R.id.btnraiseticekt)
    TextView btnraiseticekt;


    private View view;
    private MainActivity mainActivity;

    private int skipCount = 0;
    private ArrayList<JSONObject> datAr = new ArrayList<>();

    public HelpFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(view, "MontserratRegular.ttf");
        mainActivity = (MainActivity) getActivity();
        ButterKnife.bind(this, view);
        init();
        return view;

    }

    private void init()
    {
        tv_title.setText(getResources().getString(R.string.support));
        iv_toolbar.setImageResource(R.drawable.plus);
        iv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddTicket.class);
                getActivity().startActivity(intent);
            }
        });

        btnraiseticekt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTicket.class);
                getActivity().startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        datAr.clear();
        skipCount=0;
        getAllTickets();

    }

    ViewTicketAdapter transferPurposeAdater;
    private void setRecyclerData() {

        transferPurposeAdater = new ViewTicketAdapter(datAr, mainActivity);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);
        view_all_tickets_recycler.setLayoutManager(horizontalLayoutManagaer);
        view_all_tickets_recycler.setItemAnimator(new DefaultItemAnimator());
        view_all_tickets_recycler.setAdapter(transferPurposeAdater);

        view_all_tickets_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {

                    getAllTickets();
                    //scrolled to BOTTOM
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    //scrolled to TOP

                }
            }
        });


    }

    private void getAllTickets()
      {

        UtilClass.getUserData(mainActivity);
        Map<String, String> m = new LinkedHashMap<>();
        m.put("TicketId", "");
        m.put("Priority", "0");
        m.put("IsClosed", "0");
        m.put("CategoryId", "0");
        m.put("CategoryId", "0");
        m.put("MemberId", UtilClass.member_id);
        m.put("Take", "10");
        m.put("Skip", skipCount + "");

        System.out.println("Skip count=="+m);

        new ServerHandler().sendToServer(mainActivity, "GetTickets", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject obj = new JSONObject(dta);
                    if(obj.getBoolean("status"))
                    {
                        nodataFound.setVisibility(View.GONE);
                        JSONArray jsonObjectsAr = obj.getJSONArray("TicketList");
                        for (int x = 0; x < jsonObjectsAr.length(); x++)
                        {
                            datAr.add(jsonObjectsAr.getJSONObject(x));
                        }
                        if(skipCount == 0)
                         {
                            setRecyclerData();
                         }
                         else {
                            transferPurposeAdater.notifyDataSetChanged();
                           }
                    }
                    else
                        {
                        if(skipCount == 0)
                          {
                            nodataFound.setVisibility(View.VISIBLE);
                          }
                        }
                    skipCount++;
                    } catch (Exception e) {
                    e.printStackTrace();
                }
              }
        });
    }


}