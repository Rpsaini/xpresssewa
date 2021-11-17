package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ActivityFragment;
import transfer.money.com.xpresssewa.ticket.AddTicket;


public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.MyViewHolder> {
    private AddTicket ira1;
    private ArrayList<String> moviesList;
    Context mContext;
    ActivityFragment activityFragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        RelativeLayout rr_image;
        ImageView img_selected,img_selectedimg;

        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");

            rr_image =view.findViewById(R.id.rr_image);
            img_selected =view.findViewById(R.id.img_selected);
            img_selectedimg =view.findViewById(R.id.img_selectedimg);


        }
    }

    public AddImageAdapter(ArrayList<String> moviesList, AddTicket ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_image_layout, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        try {

           // final String dataObj = moviesList.get(position);
            holder.rr_image.setTag(position);
            holder.rr_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     ira1.slideUpDown(holder.img_selectedimg,Integer.parseInt(v.getTag()+""));

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