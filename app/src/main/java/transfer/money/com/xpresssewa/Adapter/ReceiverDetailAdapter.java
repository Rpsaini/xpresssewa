package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;


public class ReceiverDetailAdapter extends RecyclerView.Adapter<ReceiverDetailAdapter.MyViewHolder> {
    private Object ira1;
    private JSONArray moviesList;
    Context mContext;





    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,title_detail;


        public MyViewHolder(View view)
        {
            super(view);

            title =view.findViewById(R.id.title);
            title_detail=view.findViewById(R.id.title_detail);

        }
    }


    public ReceiverDetailAdapter(JSONArray moviesList, Context ct)
    {
        this.moviesList = moviesList;

        System.out.println("size------"+moviesList.length());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_detail_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
            final JSONObject pos =(JSONObject) moviesList.get(position);
            holder.title.setText(pos.getString("Title"));
            holder.title_detail.setText(pos.getString("Answer"));


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

