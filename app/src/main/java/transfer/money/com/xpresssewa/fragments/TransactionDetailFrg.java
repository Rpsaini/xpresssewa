package transfer.money.com.xpresssewa.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import transfer.money.com.xpresssewa.Adapter.BankDetailsAdapterListing;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.TransactionDetailView;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;


public class TransactionDetailFrg extends Fragment {

    private  TransactionDetailView transactionDetail;
    private View view;
    FrameLayout ll_transactiondeatil;
    public TransactionDetailFrg() {
        // Required empty public constructor
    }


    public static TransactionDetailFrg newInstance(String param1, String param2) {
        TransactionDetailFrg fragment = new TransactionDetailFrg();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_transaction_detail_frg, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(getActivity(), "MontserratRegular.ttf", true);
        ll_transactiondeatil=view.findViewById(R.id.ll_transactiondeatil);
        transactionDetail=(TransactionDetailView)getActivity();
        UtilClass.getUserData(transactionDetail);
        getTransactionDetail();
        return view;
    }


    private void getTransactionDetail()
        {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        m.put("TransactionId", transactionDetail.transactionId);
        new ServerHandler().sendToServer(transactionDetail, "TransactionDetail", m, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try
                  {
                    JSONObject obj=new JSONObject(dta);
                    System.out.println("transactionDetail==>>>>=="+obj);
                    if(obj.getBoolean("status"))
                    {
                        JSONObject dataObj=obj.getJSONObject("data");
                        setData(dataObj);
                        JSONArray bankdataAr =obj.getJSONArray("BankData");
                        showBankDataAr(bankdataAr);
                    }
                    else
                    {
                        new Showtoast().showToast(transactionDetail,"Response",obj.getString("Message"),ll_transactiondeatil);
                    }

                   }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setData(JSONObject dataObj)
    {
        try {
            TextView txt_yousave = view.findViewById(R.id.txt_yousave);
            TextView txt_ourfee = view.findViewById(R.id.txt_ourfee);
            TextView txt_convertedamount = view.findViewById(R.id.txt_convertedamount);
            TextView txt_exchangerate = view.findViewById(R.id.txt_exchangerate);
           // TextView txt_refnumber = view.findViewById(R.id.txt_refnumber);
            TextView txt_transactionnumber = view.findViewById(R.id.txt_transactionnumber);
            TextView txt_status = view.findViewById(R.id.txt_status);
            TextView txt_guaranteedrate = view.findViewById(R.id.txt_guaranteedrate);


             System.out.println("Status==="+dataObj);

            txt_yousave.setText(dataObj.getString("FromAmount")+" "+dataObj.getString("FromSymbol"));
            txt_ourfee.setText(dataObj.getString("Fee")+" "+dataObj.getString("FromSymbol"));
            txt_convertedamount.setText(dataObj.getString("ToAmount")+" "+dataObj.getString("ToSymbol"));
            txt_exchangerate.setText(dataObj.getString("GuaranteedRate"));
            txt_transactionnumber.setText("#"+ dataObj.getString("TransactionId"));


            txt_guaranteedrate.setText("Guaranteed Rate");

            String status=dataObj.getString("TransferWiseStatus");
            if(status.equalsIgnoreCase("cancelled"))
            {
                txt_status.setTextColor(getResources().getColor(R.color.dark_red_color));
            }
            else
            {
                txt_status.setTextColor(getResources().getColor(R.color.button_green));
            }
            txt_status.setText(status);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void showBankDataAr(JSONArray dataAr)
    {

        RecyclerView recyclerview_showbankdetail =view.findViewById(R.id.recyclerview_showbankdetail);
        BankDetailsAdapterListing mAdapter = new BankDetailsAdapterListing(dataAr,(TransactionDetailView)getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_showbankdetail.setLayoutManager(mLayoutManager);
        recyclerview_showbankdetail.setItemAnimator(new DefaultItemAnimator());
        recyclerview_showbankdetail.setAdapter(mAdapter);


//            "Answer": "12345678912345",
//            "TWType": "",
//            "Title": "Account Number",
    }
}