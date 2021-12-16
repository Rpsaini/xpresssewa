package transfer.money.com.xpresssewa.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

public class BankDetailsAdapterListing extends RecyclerView.Adapter<BankDetailsAdapterListing.MyViewHolder> {
    private AppCompatActivity ira1;
    private JSONArray moviesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title,txt_answer;
        LinearLayout ll_banklist;

        public MyViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_title);
            txt_answer = view.findViewById(R.id.txt_answer);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
          }
    }


    public BankDetailsAdapterListing(JSONArray moviesList, AppCompatActivity ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;



        System.out.println("size------" + moviesList.length());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showbank_list_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
            final JSONObject obj =moviesList.getJSONObject(position);
            holder.txt_title.setText(obj.getString("Title"));
            holder.txt_answer.setText(obj.getString("Answer"));



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