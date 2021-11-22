package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.AddBalance;
import transfer.money.com.xpresssewa.View.ChooseBankActivity;
import transfer.money.com.xpresssewa.View.ConvertMoney;


public class ChooseBankAdapter extends RecyclerView.Adapter<ChooseBankAdapter.MyViewHolder> {
    private ChooseBankActivity ira1;
    private JSONArray moviesList;
    Context mContext;
    private RadioButton commonView;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        RadioButton switch_select;
        TextView txt_bankname,txt_currencyname;
        LinearLayout ll_bank_list;

        public MyViewHolder(View view)
        {
            super(view);
            switch_select =view.findViewById(R.id.switch_select);
            txt_bankname=view.findViewById(R.id.txt_bankname);
            txt_currencyname=view.findViewById(R.id.txt_currencyname);
            ll_bank_list=view.findViewById(R.id.ll_bank_list);


        }
    }


    public ChooseBankAdapter(JSONArray moviesList, ChooseBankActivity bankActivity)
    {
        this.moviesList = moviesList;
        ira1 = bankActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_bank_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
            final JSONObject pos =(JSONObject) moviesList.get(position);
            holder.txt_currencyname.setText(pos.getString("Symbol"));
            holder.txt_bankname.setText(pos.getString("BankName"));



            holder.switch_select.setTag(pos.getString("Id")+"");

            holder.switch_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {
                        ira1.selectedBankId=buttonView.getTag()+"";
                        ira1.enableButton();
                    }
                    if(commonView!=null)
                    {
                        commonView.setChecked(false);
                    }
                    commonView=holder.switch_select;



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




}

