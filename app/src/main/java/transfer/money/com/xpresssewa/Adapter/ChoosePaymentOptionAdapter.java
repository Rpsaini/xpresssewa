package transfer.money.com.xpresssewa.Adapter;

import android.content.Context;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.ChoosePaymentOptions;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;


public class ChoosePaymentOptionAdapter extends RecyclerView.Adapter<ChoosePaymentOptionAdapter.MyViewHolder> {
    private ChoosePaymentOptions ira1;
    private JSONArray moviesList;
    Context mContext;
    public String calculationData="",selectedRecipientData="";
    private RadioButton alreadySelectedRadio;
    public JSONObject selectedOBj=new JSONObject();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_paymentidname,txt_feededuction,tv_exactfee;
        LinearLayout ll_selectbank;
        ImageView paymentTypeImg;
        RadioButton paymentMethod_radio;

        public MyViewHolder(View view) {
            super(view);
            txt_feededuction = view.findViewById(R.id.txt_feededuction);
            txt_paymentidname = view.findViewById(R.id.txt_paymentidname);
            ll_selectbank = view.findViewById(R.id.ll_selectbank);
            tv_exactfee = view.findViewById(R.id.tv_exactfee);
            paymentTypeImg = view.findViewById(R.id.paymentTypeImg);
            paymentMethod_radio = view.findViewById(R.id.paymentMethod_radio);
            DefaultConstatnts.setFont(ira1,txt_feededuction);
            DefaultConstatnts.setFont(ira1,txt_paymentidname);
            DefaultConstatnts.setFont(ira1,tv_exactfee);


        }
    }


    public ChoosePaymentOptionAdapter(JSONArray moviesList, ChoosePaymentOptions ct,String calculationData,String selectedRecipient) {
        this.moviesList = moviesList;
        this.ira1 = ct;
        this.calculationData=calculationData;
        this.selectedRecipientData=selectedRecipient;



        System.out.println("size------" + moviesList.length());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_payment_option_adapter, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try
        {
           final JSONObject obj =moviesList.getJSONObject(position);
            String pay_Method=obj.getString("Tittle");
            String ProcessingDays=obj.getString("ProcessingDays");


            JSONObject calculationObj=new JSONObject(calculationData);
            System.out.println("Selction===>"+obj+"==="+calculationObj);


          // String amountfrom= calculationObj.getString("AmountFrom")+" "+calculationObj.getString("FromSymbol");
           String amounttosybmol= calculationObj.getString("AmountTo")+" "+calculationObj.getString("ToSymbol");
           String exactfees= obj.getString("exactfees");

           holder.txt_paymentidname.setText(pay_Method);
           holder.txt_feededuction.setText(obj.getString("totalfee")+" "+calculationObj.getString("FromSymbol")+" is total fee, so recipient will get "+amounttosybmol+". Should arrive there by "+ProcessingDays+".");
           holder.tv_exactfee.setText("Fees: "+obj.getString("totalfee")+" "+calculationObj.getString("FromSymbol"));



            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.txt_feededuction.setText(Html.fromHtml("<b><font color='#3F70BC'><font color>"+obj.getString("totalfee")+"  "+calculationObj.getString("FromSymbol")+"</font><b> is total fee, so recipient will get <font color='#3F70BC'>"+amounttosybmol+".</font> Should arrive there by <font color='#3F70BC'>"+ProcessingDays+".</font>",Html.FROM_HTML_MODE_COMPACT));
                holder.tv_exactfee.setText(Html.fromHtml("Fees: <b><font color='#3F70BC'><font color>"+obj.getString("totalfee")+" "+calculationObj.getString("FromSymbol")+"</font>",Html.FROM_HTML_MODE_COMPACT));



            } else {

                holder.txt_feededuction.setText(Html.fromHtml("<b><font color='#3F70BC'><font color>"+obj.getString("totalfee")+"  "+calculationObj.getString("FromSymbol")+"</font><b> is total fee, so recipient will get <font color='#3F70BC'>"+amounttosybmol+".</font> Should arrive there by <font color='#3F70BC'>"+ProcessingDays+".</font>"));
                holder.tv_exactfee.setText(Html.fromHtml("Fees: <b><font color='#3F70BC'><font color>"+obj.getString("totalfee")+" "+calculationObj.getString("FromSymbol")+"</font>"));

            }





           holder.ll_selectbank.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   selectedOBj=obj;

               }
           });

           if(alreadySelectedRadio==null&& position==0)
           {
               holder.paymentMethod_radio.setChecked(true);
               alreadySelectedRadio=holder.paymentMethod_radio;
           }
           else
           {
               holder.paymentMethod_radio.setChecked(false);
           }

            holder.paymentMethod_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    alreadySelectedRadio.setChecked(false);
                    alreadySelectedRadio=holder.paymentMethod_radio;
                }
            });

            showImage(obj.getString("PaymentTypeImage"),holder.paymentTypeImg);
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

    private void showImage(String path,final ImageView profileimage)
    {
        if(path.length()>0) {
            Picasso.with(mContext)
                    .load(path)
                    .into(profileimage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(null)
                                    .load(R.drawable.account_icon)
                                    .into(profileimage);
                        }
                    });
        }

    }
}