package transfer.money.com.xpresssewa.Adapter;



        import android.annotation.TargetApi;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Build;

        import android.provider.MediaStore;
        import android.text.Html;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;


        import androidx.recyclerview.widget.RecyclerView;

        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;

        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.Random;

        import me.anwarshahriar.calligrapher.Calligrapher;
        import transfer.money.com.xpresssewa.R;
        import transfer.money.com.xpresssewa.View.MainActivity;
        import transfer.money.com.xpresssewa.View.TransactionDetailView;
        import transfer.money.com.xpresssewa.util.CircleTransform;
        import transfer.money.com.xpresssewa.util.DefaultConstatnts;
        import transfer.money.com.xpresssewa.util.SimpleDialog;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {
    private MainActivity ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;
//    Random rn = new Random();
//    private int maximum=5 , minimum=0;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tt_username,tt_status,tt_from_currency,tt_to_currency,user_short_name,date,txt_pendingreceipt;
        LinearLayout ll_selectrecipient;
        RelativeLayout rr_round;
        ImageView img_icon;


        public MyViewHolder(View view) {
            super(view);

            Calligrapher calligrapher = new Calligrapher(ira1);
            calligrapher.setFont(view, "MontserratRegular.ttf");
             tt_username=view.findViewById(R.id.tt_username);
             tt_status=view.findViewById(R.id.tt_status);
             tt_from_currency=view.findViewById(R.id.tt_from_currency);
             tt_to_currency=view.findViewById(R.id.tt_to_currency);
            ll_selectrecipient=view.findViewById(R.id.ll_selectrecipient);
            user_short_name=view.findViewById(R.id.user_short_name);
            txt_pendingreceipt=view.findViewById(R.id.txt_pendingreceipt);

            rr_round=view.findViewById(R.id.rr_round);
            img_icon=view.findViewById(R.id.img_icon);
            date=view.findViewById(R.id.date);
            DefaultConstatnts.setFont(ira1,tt_username);
            DefaultConstatnts.setFont(ira1,tt_status);
            DefaultConstatnts.setFont(ira1,tt_from_currency);
            DefaultConstatnts.setFont(ira1,tt_to_currency);


        }
    }



    public ActivityAdapter(ArrayList<JSONObject> moviesList, MainActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activitylist_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            final JSONObject obj = moviesList.get(position);
            holder.tt_from_currency.setText(obj.getString("FromAmount")+" "+obj.getString("FromSymbol"));
            holder.tt_to_currency.setText(obj.getString("ToAmount")+" "+obj.getString("ToSymbol"));

            String UploadReceipt =obj.getString("UploadReceipt");
            if(UploadReceipt.length()>1)
            {
                holder.txt_pendingreceipt.setText("See Receipt");
                holder.txt_pendingreceipt.setTextColor(ira1.getResources().getColor(R.color.greencolor));



                holder.txt_pendingreceipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        SimpleDialog simpleDialog = new SimpleDialog();
                        final Dialog confirmDialog = simpleDialog.simpleDailog(ira1, R.layout.show_receipt_image, new ColorDrawable(ira1.getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                        ImageView txt_showimage=confirmDialog.findViewById(R.id.txt_showimage);
                        TextView txt_close=confirmDialog.findViewById(R.id.ltxt_confirm);
                        showImage(UploadReceipt,txt_showimage);
                        txt_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                confirmDialog.dismiss();
                            }
                        });
                    }
                });


            }
            else
            {
                holder.txt_pendingreceipt.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
                holder.txt_pendingreceipt.setTag(position);
                holder.txt_pendingreceipt.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try {
                            int index = Integer.parseInt(view.getTag().toString());

                            JSONObject dataObj = moviesList.get(index);
                            String transactionId= dataObj.getString("TransactionId");
                            SimpleDialog simpleDialog = new SimpleDialog();
                            final Dialog confirmDialog = simpleDialog.simpleDailog(ira1, R.layout.uploadphoto_dialog, new ColorDrawable(ira1.getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                            ImageView txt_showimage = confirmDialog.findViewById(R.id.txt_showimage);
                            TextView txt_uploadimage = confirmDialog.findViewById(R.id.txt_uploadimage);
                            TextView ltxt_confirm = confirmDialog.findViewById(R.id.ltxt_confirm);
                            ImageView txt_close = confirmDialog.findViewById(R.id.txt_close);
                            txt_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmDialog.dismiss();
                                }
                            });

                            txt_uploadimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ira1.slideUpDown(txt_showimage, txt_uploadimage);

                                }
                            });
                            ltxt_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ira1.uploadImageToServer(transactionId + "", confirmDialog,index,moviesList,ActivityAdapter.this);
                                }
                            });

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }

            String statusName=obj.getString("statusname");
            if(statusName.equalsIgnoreCase("InProgress"))
            {
                holder.tt_status.setText(statusName);
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.pending_color_code));
            }
            else  if(statusName.equalsIgnoreCase("Cancelled"))
            {

                holder.tt_status.setText(statusName);
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
            }
            else if(statusName.equalsIgnoreCase("Completed"))
            {

                holder.tt_status.setText(statusName);
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.greencolor));
            }
            else if(statusName.equalsIgnoreCase("Rejected"))
            {
                holder.tt_status.setText(statusName);
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
            }
            else
            {
                holder.tt_status.setText(statusName);
                holder.tt_status.setTextColor(ira1.getResources().getColor(R.color.dark_red_color));
            }

            holder.date.setText("Transaction Id :"+obj.getString("Id"));

            String[] splitAr=obj.getString("TransferTo").split(" ");
            if(splitAr.length==2)
            {
                String userShortname=splitAr[0].substring(0,1)+""+splitAr[1].substring(0,1);
                holder.user_short_name.setText(userShortname);
            }
            else if(splitAr.length==1)
            {
                String userShortname=splitAr[0].substring(0,1)+""+splitAr[0].substring(1,2);
                holder.user_short_name.setText(userShortname);
            }


            String type=obj.getString("WalletType");

            if(type.equalsIgnoreCase("1"))//add
            {
                holder.img_icon.setImageResource(R.drawable.wallet);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tt_username.setText(Html.fromHtml("<b> Add money request</b>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tt_username.setText(Html.fromHtml("<b> Add money request</b>"));
                }
            }
            else if(type.equalsIgnoreCase("2"))
            {
                holder.img_icon.setImageResource(R.drawable.transactionicon);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tt_username.setText(Html.fromHtml("To <b>" + obj.getString("TransferTo") + "</b>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tt_username.setText(Html.fromHtml("To <b>" + obj.getString("TransferTo") + "</b>"));
                }

                holder.ll_selectrecipient.setTag(obj.get("TransactionId"));
                holder.ll_selectrecipient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(ira1, TransactionDetailView.class);
                            intent.putExtra("id", v.getTag() + "");
                            intent.putExtra("TransferTo", obj.getString("TransferTo"));
                            intent.putExtra("ToAmount", obj.getString("ToAmount")+" "+obj.getString("ToSymbol"));
                            ira1.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }
            else if(type.equalsIgnoreCase("3"))
            {
                holder.img_icon.setImageResource(R.drawable.conversion);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tt_username.setText(Html.fromHtml("<b>Money converted</b>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tt_username.setText(Html.fromHtml("<b>Money converted</b>"));
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



    private void showImage(String path, final ImageView profileimage) {
        Picasso.with(ira1)
                .load(path).transform(new CircleTransform())
                //.transform(new CircleTransform())
                //.transform(new CircleTransform())
                .into(profileimage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(null)
                                .load(R.drawable.america_flag).transform(new CircleTransform())
                                .into(profileimage);
                    }
                });

    }



}
