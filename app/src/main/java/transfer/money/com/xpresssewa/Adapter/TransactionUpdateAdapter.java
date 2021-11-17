package transfer.money.com.xpresssewa.Adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.TransactionDetailView;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class TransactionUpdateAdapter extends RecyclerView.Adapter<TransactionUpdateAdapter.MyViewHolder> {
    private TransactionDetailView ira1;
    private JSONArray dataAR;
    Context mContext;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView transactiondate,transaction_process;
        LinearLayout ll_transationupdatelist;
        View txt_view_line;
        ImageView ring_arrow,img_ringdrawable;

        public MyViewHolder(View view) {
            super(view);
            transactiondate = view.findViewById(R.id.transactiondate);
            transaction_process = view.findViewById(R.id.transaction_process);
            ll_transationupdatelist = view.findViewById(R.id.ll_transationupdatelist);
            txt_view_line = view.findViewById(R.id.txt_view_line);
            ring_arrow = view.findViewById(R.id.ring_arrow);
            img_ringdrawable = view.findViewById(R.id.img_ringdrawable);

            DefaultConstatnts.setFont(ira1,  transactiondate);
            DefaultConstatnts.setFont(ira1,  transaction_process);


        }
    }


    public TransactionUpdateAdapter(JSONArray moviesList, TransactionDetailView ct) {
        this.dataAR = moviesList;
        this.ira1 = ct;




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_update_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {

            System.out.println("View line---"+position+"==="+(dataAR.length()-1));
            if(position==dataAR.length()-1)
            {
                holder.txt_view_line.setVisibility(View.GONE);
            }



            final JSONObject dataObj = (JSONObject) dataAR.get(position);

            dataObj.getString("Title");

            holder.transaction_process.setText(dataObj.getString("Description"));


            String [] dateSplitted=dataObj.getString("UpdatedDate").split("T");
            String dateStr=dateSplitted[0];
//          SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MMM-dd");
            holder.transactiondate.setText(dateStr);


            if(dataObj.getString("TransactionStatus").equalsIgnoreCase("1"))
            {

                holder.img_ringdrawable.setImageResource(R.drawable.lite_green_roundcircle);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.lite_green), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.lite_green));
            }
            else if(dataObj.getString("TransactionStatus").equalsIgnoreCase("2"))
            {
                holder.img_ringdrawable.setImageResource(R.drawable.red_drawable_circle);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.dark_red_color), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.dark_red_color));
            }
            else if(dataObj.getString("TransactionStatus").equalsIgnoreCase("3"))
            {
                holder.img_ringdrawable.setImageResource(R.drawable.green_ring_drawable);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.greencolor), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.greencolor));
            }
            else if(dataObj.getString("TransactionStatus").equalsIgnoreCase("4"))
            {
                holder.img_ringdrawable.setImageResource(R.drawable.red_drawable_circle);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.dark_red_color), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.dark_red_color));
            }
            else if(dataObj.getString("TransactionStatus").equalsIgnoreCase("5"))
            {
                holder.img_ringdrawable.setImageResource(R.drawable.red_drawable_circle);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.dark_red_color), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.dark_red_color));
            }
            else if(dataObj.getString("TransactionStatus").equalsIgnoreCase("6"))
            {
                holder.img_ringdrawable.setImageResource(R.drawable.red_drawable_circle);
                holder.ring_arrow.setColorFilter(ContextCompat.getColor(ira1, R.color.dark_red_color), android.graphics.PorterDuff.Mode.SRC_IN);;
                holder.txt_view_line.setBackgroundColor(ContextCompat.getColor(ira1, R.color.dark_red_color));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataAR.length();
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