package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.TransactionListActivity;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private TransactionListActivity ira1;
    private ArrayList<String> moviesList;
    Context mContext;



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView categoryname,shortdescription;

        public MyViewHolder(View view)
        {
            super(view);


        }
    }


    public TransactionAdapter(ArrayList<String> moviesList, TransactionListActivity ira)
    {
        this.moviesList = moviesList;
        ira1 = ira;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_items, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {





        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size()+10;
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
