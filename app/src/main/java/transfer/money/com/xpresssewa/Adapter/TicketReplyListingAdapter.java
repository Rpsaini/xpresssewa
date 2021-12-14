package transfer.money.com.xpresssewa.Adapter;



import android.content.Context;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.ticket.TicketDetailActivity;


public class TicketReplyListingAdapter extends RecyclerView.Adapter<TicketReplyListingAdapter.MyViewHolder> {
    private TicketDetailActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    private int type=0;
    private String ticketImageLink="";
    String ticketImageType="";



    public class MyViewHolder extends RecyclerView.ViewHolder
      {
         LinearLayout ll_myview,ll_you;
         TextView txt_my_text,txt_mytime,txt_you,txt_you_time;
         RecyclerView recycler_view_for_you,recycler_view_for_my;

        public MyViewHolder(View view) {
            super(view);
            ll_myview = view.findViewById(R.id.ll_myview);
            txt_my_text = view.findViewById(R.id.txt_my_text);
            txt_mytime = view.findViewById(R.id.txt_mytime);

            ll_you = view.findViewById(R.id.ll_you);
            txt_you = view.findViewById(R.id.txt_you);
            txt_you_time = view.findViewById(R.id.txt_you_time);
            recycler_view_for_you = view.findViewById(R.id.recycler_view_for_you);
            recycler_view_for_my = view.findViewById(R.id.recycler_view_for_my);

        }
    }


    public TicketReplyListingAdapter(ArrayList<JSONObject> moviesList, TicketDetailActivity ct,String ticketImageLink,String ticketImageType) {
        this.moviesList = moviesList;
        this.ira1 = ct;
        this.ticketImageLink=ticketImageLink;
        this.ticketImageType=ticketImageType;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_reply_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final JSONObject pos = (JSONObject) moviesList.get(position);
            String msg=pos.getString("Message");
            if(pos.getString("Type").equalsIgnoreCase("1"))
            {
                if(msg.length()==0)
                {
                    holder.txt_my_text.setVisibility(View.GONE);
                }
                else
                {
                    holder.txt_my_text.setVisibility(View.VISIBLE);
                }
                holder.txt_my_text.setText(msg);
                holder.txt_mytime.setText(pos.getString("LastUpdated"));
                holder.ll_myview.setVisibility(View.VISIBLE);
                holder.ll_you.setVisibility(View.GONE);

               String AttachFile=pos.getString("AttachFile");
               if(AttachFile.length()>0)
               {
                   String [] img=AttachFile.split(",");
                   showImages(AttachFile,holder.recycler_view_for_my,ticketImageType);

               }
            }
            else
            {
                if(msg.length()==0)
                {
                    holder.txt_you.setVisibility(View.GONE);
                }
                else
                {
                    holder.txt_you.setVisibility(View.VISIBLE);
                }
                holder.txt_you.setText(msg);
                holder.txt_you_time.setText(pos.getString("LastUpdated"));
                holder.ll_myview.setVisibility(View.GONE);

                String AttachFile=pos.getString("AttachFile");
                if(AttachFile.length()>0)
                {
                    showImages(AttachFile,holder.recycler_view_for_you,ticketImageType);


                }
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




    private void showImages(String AttachFile, RecyclerView recycler_view,String type)
    {
        ShowAllticketsImagesAdapter defaultTransferAdapter = new ShowAllticketsImagesAdapter(AttachFile.split(","), ira1,ticketImageLink,type);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(ira1, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(horizontalLayoutManagaer);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(defaultTransferAdapter);
    }

}