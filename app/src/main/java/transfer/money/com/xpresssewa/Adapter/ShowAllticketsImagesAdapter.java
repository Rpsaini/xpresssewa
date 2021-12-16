package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.ticket.TicketDetailActivity;


public class ShowAllticketsImagesAdapter extends RecyclerView.Adapter<ShowAllticketsImagesAdapter.MyViewHolder> {
    private TicketDetailActivity ira1;
    private String[] imagesAr;
    Context mContext;
    private String imagePath="";
    private String type="";

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ticketImage;
        public MyViewHolder(View view)
        {
            super(view);
            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            ticketImage=view.findViewById(R.id.ticketsimag);
        }
    }


    public ShowAllticketsImagesAdapter(String[] imagesAr, TicketDetailActivity ct,String imagePath,String type)
    {
        this.imagesAr = imagesAr;
        this.ira1 = ct;
        this.imagePath=imagePath;
        this.type=type;
      }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showtickets_all_images, parent, false);
        if (type.equalsIgnoreCase("main_ticket"))
        {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_ticket_images_list_row, parent, false);
        }

        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final String path =  imagesAr[position];

            showImage(imagePath+path,holder.ticketImage);




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return imagesAr.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void showImage(String path, final ImageView profileimage)
    {
        System.out.println("Image path==="+path);
        Picasso.with(ira1)
                .load(path)
                .placeholder(R.drawable.placeholderimg)
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError()
                    {
                        Picasso.with(null)
                                .load(R.drawable.placeholderimg)
                                .into(profileimage);
                    }
                });

    }

}