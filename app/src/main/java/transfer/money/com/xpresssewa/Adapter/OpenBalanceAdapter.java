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


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.View.TransactionDetailView;
import transfer.money.com.xpresssewa.util.CircleTransform;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;

public class OpenBalanceAdapter extends RecyclerView.Adapter<OpenBalanceAdapter.MyViewHolder> {
    private MainActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tt_username, tt_status, tt_from_currency, tt_to_currency;
        LinearLayout ll_selectrecipient;

        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            tt_username = view.findViewById(R.id.tt_username);
            tt_status = view.findViewById(R.id.tt_status);
            tt_from_currency = view.findViewById(R.id.tt_from_currency);
            tt_to_currency = view.findViewById(R.id.tt_to_currency);
            ll_selectrecipient = view.findViewById(R.id.ll_selectrecipient);
            DefaultConstatnts.setFont(ira1, tt_username);
            DefaultConstatnts.setFont(ira1, tt_status);
            DefaultConstatnts.setFont(ira1, tt_from_currency);
            DefaultConstatnts.setFont(ira1, tt_to_currency);


        }
    }


    public OpenBalanceAdapter(ArrayList<JSONObject> moviesList, MainActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;

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



            final JSONObject obj = moviesList.get(position);


//            holder.tt_username.setText(obj.getString("TransferTo"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tt_username.setText(Html.fromHtml("To <b>" + obj.getString("TransferTo") + "</b>", Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tt_username.setText(Html.fromHtml("To <b>" + obj.getString("TransferTo") + "</b>"));
            }

            holder.tt_from_currency.setText(obj.getString("FromAmount") + obj.getString("FromSymbol"));
            holder.tt_to_currency.setText(obj.getString("ToAmount") + "" + obj.getString("ToSymbol"));

            if (obj.getString("Type").equalsIgnoreCase("1")) {
                holder.tt_status.setText("InProgress");
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.greencolor));
            } else if (obj.getString("Type").equalsIgnoreCase("2")) {
                holder.tt_status.setText("Cancelled");
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
            } else if (obj.getString("Type").equalsIgnoreCase("3")) {
                holder.tt_status.setText("Completed");
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.greencolor));
            } else if (obj.getString("Type").equalsIgnoreCase("4")) {
                holder.tt_status.setText("Rejected");
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
            }


            holder.ll_selectrecipient.setTag(obj.get("TransactionId"));
            holder.ll_selectrecipient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(ira1, TransactionDetailView.class);
                        intent.putExtra("id", v.getTag() + "");
                        intent.putExtra("TransferTo", obj.getString("TransferTo"));
                        intent.putExtra("ToAmount", obj.getString("ToAmount") + "" + obj.getString("ToSymbol"));
                        ira1.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

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
                                .load(R.drawable.america_flag).transform(new CircleTransform())
                                .into(profileimage);
                    }
                });

          }
}