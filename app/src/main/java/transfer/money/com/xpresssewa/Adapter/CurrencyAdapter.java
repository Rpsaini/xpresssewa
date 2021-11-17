package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.AddBalance;
import transfer.money.com.xpresssewa.View.ConvertMoney;
import transfer.money.com.xpresssewa.View.RecipientDynamicActivity;
import transfer.money.com.xpresssewa.View.SendFragment;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MyViewHolder> {
    private Object ira1;
    private JSONArray moviesList;
    Context mContext;
    private ImageView currencyImage;
    private TextView currencySymble;




    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView currencyFlag;
        TextView currencyname;
        TextView currencyDollar;
        LinearLayout countrrllistingouter;

        public MyViewHolder(View view)
        {
            super(view);
            currencyname =view.findViewById(R.id.currencyname);
            currencyFlag=view.findViewById(R.id.currencyFlag);
            currencyDollar=view.findViewById(R.id.currencyDollar);
            countrrllistingouter=view.findViewById(R.id.countrrllistingouter);

        }
    }


    public CurrencyAdapter(JSONArray moviesList, String currencyTitle, Object sendFrg,ImageView currencyImage,TextView currencySymble)
    {
        this.moviesList = moviesList;
        ira1 = sendFrg;
        this.currencyImage=currencyImage;
        this.currencySymble=currencySymble;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
              final JSONObject pos =(JSONObject) moviesList.get(position);
              holder.currencyname.setText(pos.getString("Symbol"));
              holder.currencyDollar.setText(pos.getString("CurrencyName"));
              showImage(pos.getString("FlagImage"),holder.currencyFlag);



            holder.countrrllistingouter.setTag(position+"");
            holder.countrrllistingouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                  try {
                      int sel_position = new Integer(v.getTag() + "");
                      JSONObject obj = moviesList.getJSONObject(sel_position);
                      showImage(obj.getString("FlagImage"),currencyImage);
                      currencySymble.setText(obj.getString("Symbol").trim());
                      if(ira1 instanceof  SendFragment) {
                          ((SendFragment)ira1).disMissDialog(obj);
                      }
                      else if(ira1 instanceof  RecipientDynamicActivity)
                      {
                          ((RecipientDynamicActivity)ira1).disMissDialog(obj);
                          DefaultConstatnts.setFont(((RecipientDynamicActivity)ira1),currencySymble);
                      }
                      else if(ira1 instanceof AddBalance)
                      {
                          ((AddBalance)ira1).disMissDialog(obj);
                          DefaultConstatnts.setFont(((AddBalance)ira1),currencySymble);
                      }

                      else if(ira1 instanceof ConvertMoney)
                      {
                          ((ConvertMoney)ira1).disMissDialog(obj);
                           DefaultConstatnts.setFont(((ConvertMoney)ira1),currencySymble);
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

        Context ct=null;
        if(ira1 instanceof  SendFragment)
        {
            ct=((SendFragment)ira1).getActivity();
        }
        else if(ira1 instanceof RecipientDynamicActivity)
        {
            ct=(RecipientDynamicActivity)ira1;
        }


        Picasso.with(ct)
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

