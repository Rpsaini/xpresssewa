package transfer.money.com.xpresssewa.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ReviewTransfer;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class BankDetailAdapter extends RecyclerView.Adapter<BankDetailAdapter.MyViewHolder> {
    private ReviewTransfer ira1;
    private JSONArray moviesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_account_title,txt_recipient_acountnumber;
        LinearLayout ll_banklist;

        public MyViewHolder(View view) {
            super(view);
            txt_account_title = view.findViewById(R.id.txt_account_title);
            txt_recipient_acountnumber = view.findViewById(R.id.txt_recipient_acountnumber);
            ll_banklist = view.findViewById(R.id.ll_banklist);
            DefaultConstatnts.setFont(ira1,txt_account_title);
            DefaultConstatnts.setFont(ira1,txt_recipient_acountnumber);
        }
    }


    public BankDetailAdapter(JSONArray moviesList, ReviewTransfer ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;



        System.out.println("size------" + moviesList.length());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_detail_adapter_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
            final JSONObject obj =moviesList.getJSONObject(position);
            holder.txt_account_title.setText(obj.getString("Title"));
            holder.txt_recipient_acountnumber.setText(obj.getString("Answer"));



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.length();
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