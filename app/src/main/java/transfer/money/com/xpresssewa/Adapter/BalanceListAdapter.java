package transfer.money.com.xpresssewa.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ActivityFragment;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.CircleTransform;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;

public class BalanceListAdapter extends RecyclerView.Adapter<BalanceListAdapter.MyViewHolder> {
    private MainActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    ActivityFragment activityFragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_currency_desc, txt_currency_name;
        ImageView img_currencyImg;

        LinearLayout ll_perform_action_on_curreny;

        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            txt_currency_name = view.findViewById(R.id.txt_currency_name);
            txt_currency_desc = view.findViewById(R.id.txt_currency_desc);
            img_currencyImg = view.findViewById(R.id.img_currencyicon);
            ll_perform_action_on_curreny = view.findViewById(R.id.ll_perform_action_on_curreny);

        }
    }

    public BalanceListAdapter(ArrayList<JSONObject> moviesList, MainActivity ira, ActivityFragment activityFragment) {
        this.moviesList = moviesList;
        ira1 = ira;
        this.activityFragment=activityFragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.open_balance_list_adapter, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            final JSONObject dataObj = moviesList.get(position);
            //holder.txt_currency_name.setText(dataObj.getString("CurrencyName"));
            holder.txt_currency_name.setText(dataObj.getString("Symbol")+" "+dataObj.getString("CurrencyName"));
            showImage(dataObj.getString("FlagImage"), holder.img_currencyImg);




            holder.ll_perform_action_on_curreny.setTag(position);
            holder.ll_perform_action_on_curreny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final JSONObject dataObj=moviesList.get(Integer.parseInt(v.getTag()+""));

                        SimpleDialog simpleDialog = new SimpleDialog();
                        final Dialog confirmDialog = simpleDialog.simpleDailog(ira1, R.layout.confirmation_dialog, new ColorDrawable(ira1.getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                        ImageView selected_Curreny=confirmDialog.findViewById(R.id.selected_Curreny);
                        TextView txt_currency_name=confirmDialog.findViewById(R.id.txt_currency_name);
                        TextView txt_no=confirmDialog.findViewById(R.id.txt_no);
                        TextView txt_yes=confirmDialog.findViewById(R.id.txt_yes);

                        showImage(dataObj.getString("FlagImage"),selected_Curreny);
                        txt_currency_name.setText(dataObj.getString("Symbol"));
                        txt_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    confirmDialog.dismiss();
                                    addThisCurrency(dataObj.getString("Symbol"), dataObj.getString("SDCountryId"), dataObj.getString("CurrencyId"), dataObj.getString("CountryId"));
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });


                        txt_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDialog.dismiss();
                            }
                        });




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
//            "Symbol": "INR",
//                    "FlagImage": "https:\/\/intremit.webcomsystems.net.au\/\/assets\/images\/flags\/PNGFlags\/in.png",
//                    "CurrencyName": "Indian Rupee",
//                    "SDCountryId": 75,
//                    "CurrencyId": 3,
//                    "CountryId": 2

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
                //.transform(new CircleTransform())
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(null)
                                .load(R.drawable.america_flag).transform(new CircleTransform())
                                .into(profileimage);
                    }
                });

    }


    private void addThisCurrency(String Symbol,String SDCountryId,String CurrencyId,String CountryId) {
        UtilClass.getUserData(ira1);
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        m.put("CountryId", CountryId);
        m.put("CurrencyId", CurrencyId);
        m.put("SDCountryId", SDCountryId);
        m.put("Symbol", Symbol);


        new ServerHandler().sendToServer(ira1, "AddWallet", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);

                    System.out.println("response obj==" + obj);

                    if (obj.getBoolean("status"))
                    {
                        Toast.makeText(ira1,obj.getString("Message"),Toast.LENGTH_LONG).show();
                    //    new Showtoast().showToast(ira1, "Response", obj.getString("Message"), activityFragment.view.findViewById(R.id.ll_activitylayout));


                    } else {
                        Toast.makeText(ira1,obj.getString("Message"),Toast.LENGTH_LONG).show();
                      //  new Showtoast().showToast(ira1, "Response", obj.getString("Message"), activityFragment.view.findViewById(R.id.ll_activitylayout));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}