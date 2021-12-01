package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import transfer.money.com.xpresssewa.Adapter.CurrencyAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.UtilClass;

public class AddBalance extends AppCompatActivity {
    int screenheight, screenWidth;
    LinearLayout listviewout, ll_calculation_Box;
    private String FlagImage, CountryId, SDCountryId;
    Dialog listingDialog;
    private String toSymbolImage="";
    ImageView from_currency_image, to_currency_symbol;

    EditText ed_amount;
    JSONArray CurrencyList = new JSONArray();
    JSONArray WalletList = new JSONArray();
    TextView txt_current_balance, tv_curreny_to, tv_currency_from;
    LinearLayout ll_selectReasonDialog;

    TextView txt_youwillpay, txt_totalfee, txt_amountwillconvert, txt_gurantee_rate, txt_label_gurantee;
    ImageView error_arrow_up_img_sent;
    LinearLayout error_errormessage_liner_sent;
    TextView show_sent_Error_msg;
    ProgressBar pb_showProgress;
    TextView tv_send_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        getScreenHeight();
        init();
        addMoneyList();
    }

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenheight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    private void init() {
        ed_amount = findViewById(R.id.ed_amount);
        from_currency_image = findViewById(R.id.from_currency_image);
        tv_currency_from = findViewById(R.id.tv_currency_from);
        LinearLayout linearfromdropdown = findViewById(R.id.linearfromdropdown);
        to_currency_symbol = findViewById(R.id.to_currency_symbol);
        txt_current_balance = findViewById(R.id.txt_current_balance);
        tv_curreny_to = findViewById(R.id.txt_selected_tosymbol);
        ll_selectReasonDialog = findViewById(R.id.ll_selectReasonDialog);
        ll_calculation_Box = findViewById(R.id.ll_calculation_Box);
        txt_label_gurantee = findViewById(R.id.txt_label_gurantee);
        error_arrow_up_img_sent = findViewById(R.id.error_arrow_up_img_sent);
        error_errormessage_liner_sent = findViewById(R.id.error_errormessage_liner_sent);
        show_sent_Error_msg = findViewById(R.id.show_sent_Error_msg);
        pb_showProgress = findViewById(R.id.pb_showProgress);
        tv_send_money = findViewById(R.id.tv_send_money);
        tv_send_money.setTag("0");



        txt_youwillpay = findViewById(R.id.txt_youwillpay);
        txt_totalfee = findViewById(R.id.txt_totalfee);
        txt_amountwillconvert = findViewById(R.id.txt_amountwillconvert);
        txt_gurantee_rate = findViewById(R.id.txt_gurantee_rate);


        getIntent().getStringExtra("Id");

        tv_currency_from.setText(getIntent().getStringExtra("WalletSymbol"));
        showImage(getIntent().getStringExtra("FlagImage"), from_currency_image);

        txt_current_balance.setText("Total balance : "+getIntent().getStringExtra("TotalAmount")+""+getIntent().getStringExtra("WalletSymbol"));

        linearfromdropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListingPopup("Select Currency", WalletList, from_currency_image, tv_currency_from);
            }
        });

        ll_selectReasonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListingPopup("Select Currency", CurrencyList, to_currency_symbol, tv_curreny_to);
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>0)
                {
                    pb_showProgress.setVisibility(View.VISIBLE);
                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("ToSymbol", tv_currency_from.getText().toString());
                    m.put("FromSymbol", tv_curreny_to.getText().toString());
                    m.put("Amount", ed_amount.getText().toString());
                    m.put("PaymentType", "");
                    m.put("MemberId", UtilClass.member_id);
                    calculateApi(m,1);
                }
            }
        });

        tv_send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equalsIgnoreCase("1"))
                {

                }

            }
        });

    }

    private void showImage(String path, final ImageView profileimage) {
        try {
            Picasso.with(this)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addMoneyList() {

        Map<String, String> m = new LinkedHashMap<>();
        m.put("CountryName", "India");
        m.put("MemberId", UtilClass.member_id);
        new ServerHandler().sendToServer(AddBalance.this, "AddMoneyList", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Data list===" + dta);

                    JSONObject jsonObject = new JSONObject(dta);
                    if (jsonObject.getBoolean("status")) {
                        String DefaultFromSymbol = jsonObject.getString("DefaultFromSymbol");
                        String DefaultToSymbol = jsonObject.getString("DefaultToSymbol");
                       // String Totalbalance = jsonObject.getString("Totalbalance");
                        // txt_current_balance.setText("Balance : " + Totalbalance + DefaultFromSymbol);
                        WalletList = jsonObject.getJSONArray("WalletList");
                        CurrencyList = jsonObject.getJSONArray("CurrencyList");

                        for(int x=0;x<CurrencyList.length();x++)
                        {
                            JSONObject currencyData=CurrencyList.getJSONObject(x);

                            if(currencyData.getString("Symbol").equalsIgnoreCase(DefaultFromSymbol))
                            {
                                toSymbolImage=currencyData.getString("FlagImage");
                                break;
                            }
                        }

                        if(toSymbolImage.length()>0)
                        {
                            showImage(toSymbolImage, to_currency_symbol);
                        }
                        tv_curreny_to.setText(DefaultFromSymbol);


                        Map<String, String> m = new LinkedHashMap<>();
                        m.put("ToSymbol", tv_currency_from.getText().toString());
                        m.put("FromSymbol", tv_curreny_to.getText().toString());
                        m.put("Amount", ed_amount.getText().toString());
                        m.put("PaymentType", "");
                        m.put("MemberId", UtilClass.member_id);
                        calculateApi(m,0);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void showListingPopup(String currencyTitle, JSONArray currencyAray, ImageView currencyImg, TextView currencySymble) {
        try {
            if (listingDialog != null && listingDialog.isShowing()) {
                listingDialog.dismiss();
            }
            listingDialog = new Dialog(this);
            listingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            listingDialog.setContentView(R.layout.select_currency_dialog_layout);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = listingDialog.getWindow();
            lp.copyFrom(window.getAttributes());
            listingDialog.setCancelable(false);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            listingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            listingDialog.show();

            listviewout = listingDialog.findViewById(R.id.listviewout);

            Animation animation = new TranslateAnimation(screenWidth, 0, 0, 0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            listviewout.startAnimation(animation);
            listviewout.setVisibility(View.VISIBLE);


            listingDialog.findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    disMissDialog(null);
                }
            });

            if (currencyAray != null) {
                if (currencyAray.length() > 0) {
                    RecyclerView currencyRecycler = listingDialog.findViewById(R.id.currencyRecycler);
                    CurrencyAdapter mAdapter = new CurrencyAdapter(currencyAray, currencyTitle, AddBalance.this, currencyImg, currencySymble);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    currencyRecycler.setLayoutManager(mLayoutManager);
                    currencyRecycler.setItemAnimator(new DefaultItemAnimator());
                    currencyRecycler.setAdapter(mAdapter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void disMissDialog(JSONObject obj) {
        try {
            Animation animation = new TranslateAnimation(0, screenWidth, 00, 0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            listviewout.startAnimation(animation);
            listviewout.setVisibility(View.GONE);


            if (obj != null) {
                FlagImage = obj.getString("FlagImage");
                CountryId = obj.getString("CountryId");
                SDCountryId = obj.getString("SDCountryId");


                Map<String, String> m = new LinkedHashMap<>();
                m.put("ToSymbol", tv_currency_from.getText().toString());
                m.put("FromSymbol", tv_curreny_to.getText().toString());
                m.put("Amount", ed_amount.getText().toString());
                m.put("PaymentType", "");
                m.put("MemberId", UtilClass.member_id);
                calculateApi(m,0);

            } else {

            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listingDialog.dismiss();
                }
            }, 300);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void calculateApi(Map<String, String> m,int progress) {
        System.out.println("Calculation before===" + m);

        new ServerHandler().sendToServer(AddBalance.this, "MoneyCalculation", m, progress, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("Calculation list===" + dta);
                    pb_showProgress.setVisibility(View.GONE);
                    JSONObject calculationObj = new JSONObject(dta);
                    if (calculationObj.getString("status").equalsIgnoreCase("true")) {
//                          JSONArray PaymentTypesList=calculationObj.getJSONArray("PaymentTypesList");
//                          JSONObject dataObj=PaymentTypesList.getJSONObject(0);
                        calculationObj.getString("ToAmount");  //1000
                        calculationObj.getString("FromAmount");  //"57543.9",
                        calculationObj.getString("ConvertAmount");//58407.06
                        calculationObj.getString("ConversionRate");//57.5439
                        calculationObj.getString("Fees");//863.158447
                        calculationObj.getString("ProcessingDays");//jan 18
                        calculationObj.getString("GuaranteedRate");//48


                        txt_youwillpay.setText(calculationObj.getString("FromAmount") + "" + tv_curreny_to.getText());
                        txt_totalfee.setText(calculationObj.getString("Fees") + "" + tv_curreny_to.getText());
                        txt_amountwillconvert.setText(calculationObj.getString("ConvertAmount") + "" + tv_curreny_to.getText());
                        txt_gurantee_rate.setText(calculationObj.getString("ConversionRate"));
                        txt_label_gurantee.setText("Guranteed Rate (" + calculationObj.getString("GuaranteedRate") + "hrs)");
                        ll_calculation_Box.setVisibility(View.VISIBLE);

                        error_arrow_up_img_sent.setVisibility(View.GONE);
                        error_errormessage_liner_sent.setVisibility(View.GONE);
                        show_sent_Error_msg.setText("");
                        tv_send_money.setAlpha(1f);
                        tv_send_money.setTag("1");

                    } else {

                        error_arrow_up_img_sent.setVisibility(View.VISIBLE);
                        error_errormessage_liner_sent.setVisibility(View.VISIBLE);
                        show_sent_Error_msg.setText(calculationObj.getString("Message"));
                        ll_calculation_Box.setVisibility(View.GONE);
                        tv_send_money.setAlpha(.5f);
                        tv_send_money.setTag("0");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}