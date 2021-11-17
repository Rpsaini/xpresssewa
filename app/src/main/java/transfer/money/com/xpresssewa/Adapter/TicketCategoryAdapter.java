package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.ticket.AddTicket;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class TicketCategoryAdapter extends RecyclerView.Adapter<TicketCategoryAdapter.MyViewHolder> {
    private AddTicket ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    private int type=0;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_reason;
        LinearLayout ll_selectReason;

        public MyViewHolder(View view) {
            super(view);
            txt_reason = view.findViewById(R.id.txt_reason);
            ll_selectReason = view.findViewById(R.id.ll_selectReason);
            DefaultConstatnts.setFont(ira1,  txt_reason);


            Typeface face=Typeface.createFromAsset(ira1.getAssets(), "MontserratRegular.ttf");
            txt_reason.setTypeface(face);

        }
    }


    public TicketCategoryAdapter(ArrayList<JSONObject> moviesList, AddTicket ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transfer_adpter_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final JSONObject pos = (JSONObject) moviesList.get(position);
            holder.txt_reason.setText(pos.getString("Title"));
            //Id
            holder.ll_selectReason.setTag(pos.getString("Id"));
            holder.ll_selectReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                            ira1.selected_reason_Id=pos.getString("Id");
                            TextView txt_showselected_reason = ira1.findViewById(R.id.txt_showselected_reason);
                            txt_showselected_reason.setText(pos.getString("Title"));


                        ira1.downSourceDestinationView(ira1.downView,ira1.reasonDialog);
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


}