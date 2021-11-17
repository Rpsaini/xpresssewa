package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ReceiptDetailsActivity;

public class ReceiptDetailsViewAdapter extends RecyclerView.Adapter<ReceiptDetailsViewAdapter.MyViewHolder> {
    private ReceiptDetailsActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    Random rn = new Random();
    private int maximum=5 , minimum=0;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_userAns,tt_userTitle;
        LinearLayout ll_selectrecipient;


        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            txt_userAns=view.findViewById(R.id.txt_userAns);
            tt_userTitle=view.findViewById(R.id.txt_userTitle);
            ll_selectrecipient=view.findViewById(R.id.ll_selectrecipient);
        }
    }



    public ReceiptDetailsViewAdapter(ArrayList<JSONObject> moviesList, ReceiptDetailsActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;

    }

    @Override
    public ReceiptDetailsViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_details_row, parent, false);
        mContext = parent.getContext();
        return new ReceiptDetailsViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReceiptDetailsViewAdapter.MyViewHolder holder, final int position) {
        try {

            final JSONObject obj = moviesList.get(position);
            holder.tt_userTitle.setText(obj.getString("Title"));
            holder.txt_userAns.setText(obj.getString("Answer"));
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
