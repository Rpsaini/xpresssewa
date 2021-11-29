package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.AdditionInformation;
import transfer.money.com.xpresssewa.View.MainActivity;
//import transfer.money.com.moneytransfer.View.RecipientActivity;
import transfer.money.com.xpresssewa.View.ReceiptDetailsActivity;
import transfer.money.com.xpresssewa.View.RecipientFragment;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;

public class RecipientListAdapter extends RecyclerView.Adapter<RecipientListAdapter.MyViewHolder> {
    private AppCompatActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    private String symbol="";
    private RecipientFragment recipientFragment;
    String userShortname = null;
    String recipient_type = null;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_userflag;
        TextView tt_username, tt_bankaccountnumber, tt_send, tt_username_short,user_short_name,txt_loadmore,tt_someoneelse;
        LinearLayout ll_selectrecipient,ll_listrow;


        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");

            tt_username_short = view.findViewById(R.id.tt_username_short);
            user_short_name = view.findViewById(R.id.user_short_name);
            img_userflag = view.findViewById(R.id.img_userflag);
            tt_username = view.findViewById(R.id.tt_username);
            tt_bankaccountnumber = view.findViewById(R.id.tt_bankaccountnumber);
            tt_send = view.findViewById(R.id.tt_send);
            ll_selectrecipient= view.findViewById(R.id.ll_selectrecipient);
            txt_loadmore= view.findViewById(R.id.txt_loadmore);
            ll_listrow= view.findViewById(R.id.ll_listrow);
            tt_someoneelse= view.findViewById(R.id.tt_someoneelse);

            DefaultConstatnts.setFont(ira1,  tt_username_short);
            DefaultConstatnts.setFont(ira1,  user_short_name);
            DefaultConstatnts.setFont(ira1,  tt_bankaccountnumber);
            DefaultConstatnts.setFont(ira1,  tt_send);
            DefaultConstatnts.setFont(ira1,  user_short_name);

        }
    }

    public RecipientListAdapter(ArrayList<JSONObject> moviesList, AppCompatActivity ira, String symbol, RecipientFragment recipientFragment) {
        this.moviesList = moviesList;
        ira1 = ira;
        this.symbol=symbol;
        this.recipientFragment=recipientFragment;

    }

    public RecipientListAdapter(ArrayList<JSONObject> moviesList, AppCompatActivity ira, String symbol, RecipientFragment recipientFragment,String recipient_Type) {
        this.moviesList = moviesList;
        ira1 = ira;
        this.symbol=symbol;
        this.recipientFragment=recipientFragment;
        this.recipient_type=recipient_Type;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipient_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final JSONObject obj = moviesList.get(position);
            if(obj!=null)
            {
                String ReciptentLastName=obj.getString("ReciptentLastName");
                holder.tt_username_short.setText(obj.getString("CountryCode"));
                holder.tt_bankaccountnumber.setText(obj.getString("ReciptentAccountNumber"));
                holder.tt_username.setText(obj.getString("ReciptentName")+" "+ReciptentLastName);


                String ReciptentSection=obj.getString("ReciptentSection");
                if(ReciptentSection.equalsIgnoreCase("2"))
                {
                    holder.tt_someoneelse.setText("(Someone else)");
                }
                else if(ReciptentSection.equalsIgnoreCase("3"))
                {
                    holder.tt_someoneelse.setText("(Business)");
                }

                showImage(obj.getString("Image"), holder.img_userflag);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    holder.tt_send.setText(Html.fromHtml("<b>" + obj.getString("Symbol") + "</b>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tt_send.setText(Html.fromHtml("<b>" + obj.getString("Symbol") + "</b>"));
                }

                String[] splitAr = (obj.getString("ReciptentName")+" "+ReciptentLastName).split(" ");


                if(splitAr.length==1)
                {
                    userShortname = splitAr[0].substring(0, 1) + "" + splitAr[0].substring(1, 2);
                    holder.user_short_name.setText(userShortname);
                }
                else if (splitAr.length >=2)
                {
                    userShortname = splitAr[0].substring(0, 1) + "" + splitAr[splitAr.length-1].substring(0, 1);
                    holder.user_short_name.setText(userShortname);
                }

                if(ira1 instanceof MainActivity)
                  {
                    holder.tt_send.setTag(position);
                    holder.ll_selectrecipient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (recipientFragment.args.containsKey("callFrom"))
                              {
                                if (recipientFragment.args.getString("callFrom").equalsIgnoreCase("sendFrg")) {
                                    String isKycApproved = new SaveImpPrefrences().reterivePrefrence(ira1, DefaultConstatnts.IsKycApproved).toString();
                                    if (!isKycApproved.equalsIgnoreCase("3"))//approved
                                    {
                                        recipientFragment.verifyingDocuments();
                                    } else {
                                        Intent intent = new Intent(mContext, AdditionInformation.class);
                                        intent.putExtra("calculationData", recipientFragment.args.getString("data"));
                                        intent.putExtra("selectedRecipientData", obj + "");
                                        ira1.startActivityForResult(intent, 1001);
                                    }
                                }
                            }
                            else
                                {
                                Intent intent = new Intent(mContext, ReceiptDetailsActivity.class);
                                intent.putExtra("full_details",obj.toString());
                                intent.putExtra("short_name",  holder.user_short_name.getText().toString());
                                intent.putExtra("recipient_type", recipient_type);
                                ira1.startActivity(intent);
                               }
                        }
                    });
                }
            }
           else
               {
               holder.ll_listrow.setVisibility(View.GONE);
//                holder.txt_loadmore.setVisibility(View.VISIBLE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    holder.txt_loadmore.setText(Html.fromHtml("<u>Load More</u>", Html.FROM_HTML_MODE_COMPACT));
//                } else {
//                    holder.txt_loadmore.setText(Html.fromHtml("<u>Load More</u>"));
//                }
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
                .load(path)
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

}
