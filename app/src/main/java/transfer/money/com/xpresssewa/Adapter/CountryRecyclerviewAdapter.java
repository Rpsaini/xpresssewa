package transfer.money.com.xpresssewa.Adapter;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.registration.SignUpActivity;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class CountryRecyclerviewAdapter extends RecyclerView.Adapter<CountryRecyclerviewAdapter.MyViewHolder> {

    private AppCompatActivity ira1;
    private JSONArray moviesList;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView countryflag;
        TextView countryname;
        LinearLayout countrrllistingouter;
        public MyViewHolder(View view)
        {
            super(view);
            countryflag =view.findViewById(R.id.countryflag);
            countryname=view.findViewById(R.id.countryname);

            countrrllistingouter=view.findViewById(R.id.countrrllistingouter);
            DefaultConstatnts.setFont(ira1,countryname);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
        }
    }


    public CountryRecyclerviewAdapter(JSONArray moviesList, AppCompatActivity sendFrg)
    {
        this.moviesList = moviesList;
        ira1 = sendFrg;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_country_adapter, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {


            final JSONObject pos =(JSONObject) moviesList.get(position);
            holder.countryname.setText(pos.getString("CountryName"));
          //  holder.countryname.setText(pos.getString("CountryCode"));
         //   holder.countryname.setText(pos.getString("CountryId"));


             showImage(pos.getString("CountryImage"),holder.countryflag);



            holder.countrrllistingouter.setTag(position+"");
            holder.countrrllistingouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                       int pos= Integer.parseInt(v.getTag()+"");
                        if(ira1 instanceof SignUpActivity)
                        {

                          SignUpActivity signUpActivity=  (SignUpActivity)ira1;
                          signUpActivity.setCountryData(moviesList.getJSONObject(pos));
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
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


    private void showImage(String path,final  ImageView profileimage)
    {
        Picasso.with(mContext)
                .load(path)
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(null)
                                .load(R.drawable.america_flag)
                                .into(profileimage);
                    }
                });

    }


}
