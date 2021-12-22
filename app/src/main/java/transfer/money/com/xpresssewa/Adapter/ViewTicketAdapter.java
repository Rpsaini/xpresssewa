package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.MainActivity;
import transfer.money.com.xpresssewa.ticket.MyTicketsActivity;
import transfer.money.com.xpresssewa.ticket.TicketDetailActivity;


public class ViewTicketAdapter extends RecyclerView.Adapter<ViewTicketAdapter.MyViewHolder> {
    private AppCompatActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
    private int type=0;



    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView txt_id,txt_department,txt_subject,txt_priority,txt_status,txt_date;
       LinearLayout ll_view_ticket;

        public MyViewHolder(View view) {
            super(view);
            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");

            txt_id=view.findViewById(R.id.txt_id);
            txt_department=view.findViewById(R.id.txt_department);
            txt_subject=view.findViewById(R.id.txt_subject);
            txt_priority=view.findViewById(R.id.txt_priority);
            txt_status=view.findViewById(R.id.txt_status);
            ll_view_ticket=view.findViewById(R.id.ll_view_ticket);
            txt_date=view.findViewById(R.id.txt_date);

        }
    }


    public ViewTicketAdapter(ArrayList<JSONObject> moviesList, AppCompatActivity ct) {
        this.moviesList = moviesList;
        this.ira1 = ct;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ticket_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            final JSONObject pos = (JSONObject) moviesList.get(position);

            holder.txt_id.setText(pos.getString("TicketId"));
            holder.txt_department.setText(pos.getString("CategoryName"));
            holder.txt_subject.setText(pos.getString("TicketTitle"));
            String priority=pos.getString("Priority");
            String status=pos.getString("status");
            holder.txt_priority.setText(priority);
            holder.txt_status.setText(status);
            holder.txt_date.setText(pos.getString("ShortDate"));

            if(priority.equalsIgnoreCase("High"))
            {
                holder.txt_priority.setTextColor(ira1.getResources().getColor(R.color.status_red));
            }
            else  if(priority.equalsIgnoreCase("Medium"))
            {
                holder.txt_priority.setTextColor(ira1.getResources().getColor(R.color.blue_bt_color));
            }
            else  if(priority.equalsIgnoreCase("low"))
            {
                holder.txt_priority.setTextColor(ira1.getResources().getColor(R.color.status_green));
            }

            if(status.equalsIgnoreCase("open"))
            {
                holder.txt_priority.setTextColor(ira1.getResources().getColor(R.color.status_green));
            }
            else if(status.equalsIgnoreCase("close"))
            {
                holder.txt_priority.setTextColor(ira1.getResources().getColor(R.color.status_red));
            }


            holder.ll_view_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Detail===="+holder.ll_view_ticket);
                    Intent intent=new Intent(ira1, TicketDetailActivity.class);
                    intent.putExtra("ticketData",pos+"");
                    ira1.startActivity(intent);
                }
            });




            //                        "TicketList":
            //                        [{

//                                    "TicketTitle": "hello",
//                                    "MainMessage": "fdesdf",
//                                    "Status": "Open",
//                                    "Priority": "Low",
//                                    "CategoryName": "Account",
//                                    "CategoryId": 2,
//                                    "TicketId": "d1eda21a-27be-4e88-a5c5-d18ade32d748",
//                                    "ShortDate": "16 March",
//                                    "CreatedDate": "Tuesday, 16 March 2021 11:21",
//                                    "message": "List Found",
//                                    "status": "Success"

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