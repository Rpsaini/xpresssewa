package transfer.money.com.xpresssewa.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.TransactionUpdateAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.TransactionDetailView;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class TransactionUpdateFrg extends Fragment {

    private View view;
    LinearLayout ll_transactionstatus;
    private  TransactionDetailView transactionDetailView;
    public TransactionUpdateFrg() {
        // Required empty public constructor
    }


    public static TransactionUpdateFrg newInstance(String param1, String param2) {
        TransactionUpdateFrg fragment = new TransactionUpdateFrg();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_transaction_update_frg, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "MontserratRegular.ttf", true);
        transactionDetailView=(TransactionDetailView)getActivity();


         getTransactionDetail();
        return view;
    }
    private void initRecycler(JSONArray dataAr)
    {
        RecyclerView transaction_update_recycler =view.findViewById(R.id.transaction_update_recycler);
        TransactionUpdateAdapter mAdapter = new TransactionUpdateAdapter(dataAr,transactionDetailView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        transaction_update_recycler.setLayoutManager(mLayoutManager);
        transaction_update_recycler.setItemAnimator(new DefaultItemAnimator());
        transaction_update_recycler.setAdapter(mAdapter);
    }

    private void getTransactionDetail()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        m.put("TransactionId", transactionDetailView.transactionId);

        new ServerHandler().sendToServer(transactionDetailView, "TransactionStatus", m, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try
                {
                    JSONObject obj=new JSONObject(dta);
                    System.out.println("transactionDetail===="+obj);
                    if(obj.getBoolean("status"))
                    {

                        JSONArray jsonDataAr =obj.getJSONArray("data");
                        initRecycler(jsonDataAr);

                    }
                    else
                    {
                        new Showtoast().showToast(transactionDetailView,"Response",obj.getString("Message"),ll_transactionstatus);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }




}