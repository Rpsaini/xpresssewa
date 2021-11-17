package transfer.money.com.xpresssewa.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ActivityFragment;
import transfer.money.com.xpresssewa.View.AddMoneyInWallet;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.CircleTransform;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {
    private MainActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    ActivityFragment activityFragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_balance,txt_currency;

        ImageView img_currencyicon;
        LinearLayout ll_perform_action_on_curreny;


        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            txt_balance=view.findViewById(R.id.txt_balance);
            txt_currency=view.findViewById(R.id.txt_currency);
            img_currencyicon=view.findViewById(R.id.img_currencyicon);
            ll_perform_action_on_curreny=view.findViewById(R.id.ll_perform_action_on_curreny);



        }
    }

    public WalletListAdapter(ArrayList<JSONObject> moviesList, MainActivity ira, ActivityFragment activityFragment) {
        this.moviesList = moviesList;
        ira1 = ira;
        this.activityFragment=activityFragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

                final JSONObject dataObj=moviesList.get(position);
                if(dataObj!=null) {
                    holder.txt_currency.setText(dataObj.getString("WalletSymbol"));
                    holder.txt_balance.setText(dataObj.getString("TotalAmount"));
                    showImage(dataObj.getString("FlagImage"), holder.img_currencyicon);
                    holder.ll_perform_action_on_curreny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent=new Intent(ira1, AddMoneyInWallet.class);
                            intent.putExtra("data",dataObj+"");
                            ira1.startActivity(intent);
                        }
                    });
                }
                else
                {
                    holder.txt_currency.setText("OPEN");
                    holder.txt_balance.setText("BALANCE");
                    holder.img_currencyicon.setImageResource(R.drawable.ic_open_balance);

                       holder.ll_perform_action_on_curreny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SimpleDialog simpleDialog = new SimpleDialog();
                            final Dialog currencyDialog = simpleDialog.simpleDailog(ira1, R.layout.choose_balance_dialog, new ColorDrawable(ira1.getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                            RecyclerView balance_currency_recycler=currencyDialog.findViewById(R.id.balance_currency_recycler);
                            getBalanceData(balance_currency_recycler);
                            final RelativeLayout ll_relativelayout =currencyDialog.findViewById(R.id.ll_relativelayout);
                            ira1.animateUp(ira1.screenHeight,ll_relativelayout);
                            currencyDialog.findViewById(R.id.img_hideview).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activityFragment.getwalletData();
                                    ira1.downSourceDestinationView(ll_relativelayout,currencyDialog);
                                }
                            });

                            ll_relativelayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activityFragment.getwalletData();
                                    ira1.downSourceDestinationView(ll_relativelayout,currencyDialog);
                                }
                            });

                        }
                       });
                }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void showImage(String path, final ImageView profileimage) {
        Picasso.with(ira1)
                .load(path).transform(new CircleTransform())
                //.transform(new CircleTransform())
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(null)
                                .load(R.drawable.america_flag)
                                .into(profileimage);
                    }
                });

    }


    private void getBalanceData(final RecyclerView recyclerView)
    {

        Map<String,String> m=new LinkedHashMap<>();
        m.put("CountryName", "India");
        new ServerHandler().sendToServer(ira1, "WalletList", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);

                    System.out.println("response obj==" + obj);
                    if (obj.getBoolean("status"))
                    {

                        JSONArray dataJAr =obj.getJSONArray("data");
                        ArrayList<JSONObject> datAr=new ArrayList<>();
                        for(int x=0;x<dataJAr.length();x++)
                        {
                            datAr.add(dataJAr.getJSONObject(x));
                        }

                        showCurrenyList(recyclerView,datAr);


                    } else {
                        new Showtoast().showToast(ira1,"Response",obj.getString("error"),ira1.findViewById(R.id.rel_navigationview));

                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private  void showCurrenyList(RecyclerView recyclerView,ArrayList<JSONObject> dataAr)
    {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(ira1, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        BalanceListAdapter mAdapter = new BalanceListAdapter(dataAr, ira1,activityFragment);
        recyclerView.setAdapter(mAdapter);

    }


}