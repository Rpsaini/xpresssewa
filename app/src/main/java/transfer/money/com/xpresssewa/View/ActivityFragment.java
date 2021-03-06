package transfer.money.com.xpresssewa.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.ActivityAdapter;
import transfer.money.com.xpresssewa.Adapter.WalletListAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;

public class ActivityFragment extends Fragment implements View.OnClickListener {

    public View view;
    Context mContext;

    RecyclerView recycler_view_of_activity, wallet_recycler;
    ImageView img_nodatafound;

    RelativeLayout ll_activitylayout;

    RelativeLayout rr_activity_and_wallet, rr_upper_layout;
    ImageView img_show_warring;
    TextView txt_activity_label;
    private TextView txt_label;
    private String isKycApproved = "";
    private int skipCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(view, "MontserratRegular.ttf");
        ButterKnife.bind(this, view);
        mContext = getActivity();

        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        recycler_view_of_activity = view.findViewById(R.id.recycler_view_of_activity);
        wallet_recycler = view.findViewById(R.id.wallet_recycler);
        img_nodatafound = view.findViewById(R.id.img_nodatafound);
        ll_activitylayout = view.findViewById(R.id.ll_activitylayout);
        img_show_warring = view.findViewById(R.id.img_show_warring);
        rr_upper_layout = view.findViewById(R.id.rr_upper_layout);
        txt_label = view.findViewById(R.id.txt_label);
        txt_activity_label = view.findViewById(R.id.txt_activity_label);

        rr_activity_and_wallet = view.findViewById(R.id.rr_activity_and_wallet);
        chekIsUserKycisIsDone();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_verification_pending:
                chekIsUserKycisIsDone();
                break;
        }
    }

    private void chekIsUserKycisIsDone()
    {

        isKycApproved = new SaveImpPrefrences().reterivePrefrence(getActivity(), DefaultConstatnts.IsKycApproved).toString();
        TextView verification_text = view.findViewById(R.id.verification_text);
        TextView verification_subtext = view.findViewById(R.id.verification_subtext);

        System.out.println("kyc status==="+isKycApproved);
        View activityView = view.findViewById(R.id.ll_verification_pending);
        if(isKycApproved.equalsIgnoreCase("1"))
        {
            txt_activity_label.setVisibility(View.VISIBLE);
            txt_label.setVisibility(View.GONE);
            rr_upper_layout.setVisibility(View.GONE);
            activityView.setVisibility(View.VISIBLE);
            verification_subtext.setText("We are verifying your account");
            verification_text.setText("Verification in progress");
            img_show_warring.setVisibility(View.VISIBLE);

            activityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //Todo show dialog   ((MainActivity) getActivity()).callMyProfileFragment("personal");
                    successDialog();
                }
            });
        } else if (isKycApproved.equalsIgnoreCase("4")) {
            txt_activity_label.setVisibility(View.VISIBLE);
            txt_label.setVisibility(View.VISIBLE);
            rr_upper_layout.setVisibility(View.GONE);
            activityView.setVisibility(View.VISIBLE);
            img_show_warring.setVisibility(View.GONE);

            verification_subtext.setText("Verify your account");
            verification_text.setText("Declined !");
            activityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent=new Intent(getActivity(),);
                }
            });
            activityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ((MainActivity) getActivity()).callMyProfileFragment("personal");
                }
            });


        } else if (isKycApproved.equalsIgnoreCase("2")) {
            txt_activity_label.setVisibility(View.VISIBLE);
            rr_upper_layout.setVisibility(View.GONE);
            activityView.setVisibility(View.VISIBLE);
            img_show_warring.setVisibility(View.VISIBLE);

            activityView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).callMyProfileFragment("personal");
                }
            });

        } else {
            txt_activity_label.setVisibility(View.GONE);
            txt_label.setText("");
            txt_label.setVisibility(View.GONE);
            rr_upper_layout.setVisibility(View.GONE);
            activityView.setVisibility(View.GONE);
            img_show_warring.setVisibility(View.GONE);
            rr_activity_and_wallet.setVisibility(View.VISIBLE);
        }


        getActivityData();
        getwalletData();


    }


    ArrayList<JSONObject> datAr = new ArrayList<>();

    private void getActivityData() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        m.put("StartDate", "");
        m.put("EndDate", "");
        m.put("Take", "8");
        m.put("Skip", skipCount + "");
        m.put("Type", "2");

        new ServerHandler().sendToServer(getActivity(), "Transactions", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {

                        JSONArray TransactionList = obj.getJSONArray("TransactionList");
                        for (int x = 0; x < TransactionList.length(); x++) {
                            datAr.add(TransactionList.getJSONObject(x));
                        }

                        if (skipCount == 0) {
                            initRecyler(datAr);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {

                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity.tabCount == 0) {
                            mainActivity.sendFromSelectedList(null);
                        }
                        if(datAr.size()==0) {
                            if (isKycApproved.equalsIgnoreCase("3"))
                            {
                                img_show_warring.setVisibility(View.VISIBLE);
                                img_show_warring.setImageResource(R.drawable.noactivityfound);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    ActivityAdapter mAdapter;

    private void initRecyler(ArrayList<JSONObject> data) {

        if (data.size() > 0) {
            img_nodatafound.setVisibility(View.GONE);
        }

        mAdapter = new ActivityAdapter(data, (MainActivity) getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recycler_view_of_activity.setLayoutManager(mLayoutManager);
        recycler_view_of_activity.setItemAnimator(new DefaultItemAnimator());
        recycler_view_of_activity.setAdapter(mAdapter);
        recycler_view_of_activity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0)
                {
                    loadNextData();
                    //scrolled to BOTTOM
                }else if (!recyclerView.canScrollVertically(-1) && dy < 0)
                {
                    //scrolled to TOP
                    System.out.println("Scrool at top===");
                }
            }
        });


    }






    public void getwalletData() {

        Map<String,String> m=new LinkedHashMap<>();
        m.put("MemberId",UtilClass.member_id);

        new ServerHandler().sendToServer(getActivity(), "Wallets", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    System.out.println("wallet data obj==" + obj);
                    ArrayList<JSONObject> walletAr=new ArrayList<>();
                    walletAr.add(null);
                    if (obj.getBoolean("status")) {

                        JSONArray dataAr =obj.getJSONArray("data");

                        for(int x=0;x<dataAr.length();x++)
                        {
                            walletAr.add(dataAr.getJSONObject(x));
                        }

                    } else {
                     //   new Showtoast().showToast(getActivity(),"Response",obj.getString("Message"),ll_activitylayout);

                    }
                    walletRecycler(walletAr);

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }


    private void walletRecycler(ArrayList<JSONObject> data)
    {
        if(data.size()>0)
        {
         //   img_show_warring.setVisibility(View.GONE);
        }

        wallet_recycler.setNestedScrollingEnabled(false);
        wallet_recycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        wallet_recycler.setHasFixedSize(true);
        wallet_recycler.setNestedScrollingEnabled(true);
        wallet_recycler.setItemAnimator(new DefaultItemAnimator());
        WalletListAdapter mAdapter = new WalletListAdapter(data, (MainActivity) getActivity(),this);
        wallet_recycler.setAdapter(mAdapter);
    }

    public void loadNextData()
    {
        skipCount++;
        getActivityData();

    }


    private void successDialog()
    {
       SimpleDialog simpleDialog = new SimpleDialog();

       Dialog dialogConfirm = simpleDialog.simpleDailog((MainActivity)getActivity(), R.layout.successdialog, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
       Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "MontserratRegular.ttf");

        TextView uploadtext=dialogConfirm.findViewById(R.id.uploadtext);
        TextView lbl_maintitle=dialogConfirm.findViewById(R.id.lbl_maintitle);
        uploadtext.setTypeface(face);
        lbl_maintitle.setTypeface(face);

        dialogConfirm.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialogConfirm.dismiss();
            }
        });
        dialogConfirm.findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
            }
        });
    }
}
