package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.NotificationActivity;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private NotificationActivity ira1;
    private JSONArray moviesList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_notification_icon;
        TextView tt_noti_title, tt_notideatil, tt_noti_date;


        public MyViewHolder(View view)
           {
            super(view);
            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
            tt_noti_title = view.findViewById(R.id.tt_noti_title);
            img_notification_icon = view.findViewById(R.id.img_notification_icon);
            tt_notideatil = view.findViewById(R.id.tt_notideatil);
            tt_noti_date = view.findViewById(R.id.tt_noti_date);
           }
    }

    public NotificationAdapter(JSONArray moviesList, NotificationActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_adapter, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final JSONObject obj = moviesList.getJSONObject(position);

            String title = obj.getString("Title");
            String Message = obj.getString("Message");
            String CreatedDate = obj.getString("CreatedDate");
            String IsRead = obj.getString("IsRead");
            String Id = obj.getString("Id");
            String Type = obj.getString("Type");

            holder.tt_noti_title.setText(title);
            holder.tt_notideatil.setText(Message);
            holder.tt_noti_date.setText(CreatedDate);

            if (Type.equalsIgnoreCase("1")) {
                //kyc
                holder.img_notification_icon.setImageResource(R.drawable.ic_kyc_img);

            }

            if (Type.equalsIgnoreCase("2")) {
                holder.img_notification_icon.setImageResource(R.drawable.ic_ticket_image);
                //ticket

            }
            if (Type.equalsIgnoreCase("3")) {
                //.transfer
                holder.img_notification_icon.setImageResource(R.drawable.ic_money_transfer_img);
            }

            if (IsRead.equalsIgnoreCase("true"))
            {
               // holder.img_notification_icon.setColorFilter(ContextCompat.getColor(ira1, R.color.blue_bt_color), PorterDuff.Mode.SRC);
                holder.tt_noti_title.setTextColor(ira1.getResources().getColor(R.color.blue_bt_color));
            }
            else {
                holder.tt_noti_title.setTextColor(ira1.getResources().getColor(R.color.grey_color));
//                holder.img_notification_icon.setColorFilter(ContextCompat.getColor(ira1, R.color.grey_color), PorterDuff.Mode.SRC);
                }
            }
        catch(Exception e)
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


    private void showImage(String path, final ImageView profileimage) {
        Picasso.with(ira1)
                .load(path)
                //.transform(new CircleTransform())
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError()
                    {
                        Picasso.with(null)
                                .load(R.drawable.america_flag)
                                .into(profileimage);
                    }
                });

         }

}