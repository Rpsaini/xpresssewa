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
import transfer.money.com.xpresssewa.View.AdditionInformation;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class TransferPurposeAdapter extends RecyclerView.Adapter<TransferPurposeAdapter.MyViewHolder> {
    private AdditionInformation ira1;
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


    public TransferPurposeAdapter(ArrayList<JSONObject> moviesList, AdditionInformation ct,int type) {
        this.moviesList = moviesList;
        this.ira1 = ct;
        this.type=type;

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
                         if(type==0)
                         {
                             ira1.selected_reason_Id=pos.getString("Id");
                             TextView txt_showselected_reason = ira1.findViewById(R.id.txt_showselected_reason);
                             txt_showselected_reason.setText(pos.getString("Title"));
                             if(ira1.selected_reason_Id.equalsIgnoreCase("5"))
                             {
                                 ira1.ll_if_other_transfer.setVisibility(View.VISIBLE);
                             }
                             else
                             {
                                 ira1.ll_if_other_transfer.setVisibility(View.GONE);
                             }


                         }
                         else
                         {   ira1.selected_reference=pos.getString("Id");
                             TextView txt_showselected_reference = ira1.findViewById(R.id.txt_showselected_reference);
                             txt_showselected_reference.setText(pos.getString("Title"));
                             if(ira1.selected_reference.equalsIgnoreCase("1"))
                             {
                                 ira1.ll_if_other.setVisibility(View.VISIBLE);
                             }
                             else
                             {
                                 ira1.ll_if_other.setVisibility(View.GONE);
                             }

                         }

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