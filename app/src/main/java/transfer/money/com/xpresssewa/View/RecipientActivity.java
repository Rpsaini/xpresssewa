//package transfer.money.com.moneytransfer.View;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import me.anwarshahriar.calligrapher.Calligrapher;
//import transfer.money.com.moneytransfer.Adapter.RecipientListAdapter;
//import transfer.money.com.moneytransfer.R;
//import transfer.money.com.moneytransfer.communication.ServerHandler;
//import transfer.money.com.moneytransfer.interfaces.CallBack;
//import transfer.money.com.moneytransfer.savePrefrences.SaveImpPrefrences;
//import transfer.money.com.moneytransfer.util.DefaultConstatnts;
//import transfer.money.com.moneytransfer.util.UtilClass;
//import transfer.money.com.moneytransfer.validation.Showtoast;
//
//public class RecipientActivity extends AppCompatActivity {
//
//    Context mContext;
//    private FragmentActivity rootAct;
//    private RecipientListAdapter otherDetailsAdapterother, myselfDetailsAdapter;
//
////    @BindView(R.id.tv_title)
////    TextView tv_title;
//
//    @BindView(R.id.headerbackbutton)
//    ImageView headerbackbutton;
//
//
////    @BindView(R.id.iv_toolbar)
////    ImageView iv_toolbar;
//
////    @BindView(R.id.llwithoutrecipient)
////    LinearLayout llwithoutrecipient;
//
//    @BindView(R.id.AreadyAddedRecipientRecycer)
//    RecyclerView AreadyAddedRecipientRecycer;
//    private int take = 10, skip = 0, type = 1;
//    //type  1 for bank 2 for recipient
//    private int othr_take = 10, othr_skip = 0, othr_type = 2;
//    private String member_id = "";
//    private Showtoast showtoast;
//
//    private String symbol = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recipient);
//        showtoast = new Showtoast();
//
//        try {
//
//            Calligrapher calligrapher = new Calligrapher(this);
//            calligrapher.setFont(this, "MontserratRegular.ttf", true);
//
//            UtilClass.getUserData(this);
//            SaveImpPrefrences imp = new SaveImpPrefrences();
//            JSONObject loginDetail = new JSONObject(imp.reterivePrefrence(this, "login_detail").toString());
//            member_id = loginDetail.getString("MemberId");
//            ButterKnife.bind(this, this);
//
//
////            args = getArguments();
//            symbol = getIntent().getStringExtra("symbol");
//
//
//            init();
//            selfRecipientData();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void init() {
//
//        try
//        {
//            String login = new SaveImpPrefrences().reterivePrefrence(this, DefaultConstatnts.login_detail) + "";
//            System.out.println("Login Detail==="+login);
//            JSONObject jsonObject = new JSONObject(login);
//            UtilClass.getDefaultDestImage = jsonObject.getString("FlagImageDestination");
//            UtilClass.getDefaultSourceImage = jsonObject.getString("FlagImageSource");
//            UtilClass.defaultToSymble = jsonObject.getString("DestinationSymbol");
//            UtilClass.DefaultFromSymble = jsonObject.getString("SourceSymbol");
//
//            UtilClass.DefaultCountryname = jsonObject.getString("CountryName");
//            UtilClass.defaultSourceCountryId = jsonObject.getString("CountryId");
//            UtilClass.defaultSourceCountryId = jsonObject.getString("SDCountryId");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//        findViewById(R.id.see_more_my).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                skip++;
//                selfRecipientData();
//            }
//        });
//
//     findViewById(R.id.see_more_other).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                othr_skip++;
//                getOtherReciient();
//            }
//        });
//
//
//     //   tv_title.setText(getResources().getString(R.string.add_recipent));
////        iv_toolbar.setImageResource(R.drawable.plus);
//
//
////        iv_toolbar.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                redirectToReceipient();
////
////            }
////        });
//
////        if(symbol.length()>0)
////        {
////            tv_back.setVisibility(View.VISIBLE);
////            tv_title.setVisibility(View.GONE);
////
////        }
////        else
////        {
////            tv_back.setVisibility(View.GONE);
////            tv_title.setVisibility(View.VISIBLE);
////        }
//        headerbackbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//
//
//
//    ArrayList<JSONObject> mySelfAr = new ArrayList<>();
//    private void selfRecipientData() {
//        try {
//            mySelfAr.clear();
//            Map<String, String> m = new LinkedHashMap<>();
//            m.put("Symbol", symbol);
//            m.put("MemberId", member_id);
//            m.put("Take", take + "");
//            m.put("Skip", skip + "");
//            m.put("Type", type + "");
//            getOtherReciient();
//            new ServerHandler().sendToServer(this, "RecipientList", m, 1, 1, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        System.out.println("recipient data==back" + dta);
//                        JSONObject obj = new JSONObject(dta);
//
//                        if(obj.getString("status").equalsIgnoreCase("true"))
//                        {
//                            JSONArray recipientList = obj.getJSONArray("RecipientList");
//                            for (int x = 0; x < recipientList.length(); x++) {
//                                mySelfAr.add(recipientList.getJSONObject(x));
//                            }
//                            if (mySelfAr.size() == 0) {
//                                showtoast.showToast(RecipientActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_recipientlayoutmain));
//                            }
//                            if (skip == 0) {
//                                showDataSelfRecipient();
//                            } else {
//                                myselfDetailsAdapter.notifyDataSetChanged();
//                            }
//                        } else {
//                            //   showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void showDataSelfRecipient() {
//       findViewById(R.id.img_default).setVisibility(View.GONE);
//       findViewById(R.id.ll_recipientlayout).setVisibility(View.GONE);
//        findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.VISIBLE);
//
//        RecyclerView recipient_detail_recycler = findViewById(R.id.AreadyAddedRecipientRecycer);
//
//
//        myselfDetailsAdapter = new RecipientListAdapter(mySelfAr, RecipientActivity.this, symbol, null);
//        LinearLayoutManager horizontalLayoutManagaer
//                = new LinearLayoutManager(RecipientActivity.this, LinearLayoutManager.VERTICAL, false);
//        recipient_detail_recycler.setLayoutManager(horizontalLayoutManagaer);
//        recipient_detail_recycler.setItemAnimator(new DefaultItemAnimator());
//        recipient_detail_recycler.setAdapter(myselfDetailsAdapter);
//
//
//    }
//
//
//    ArrayList<JSONObject> otherRecipientAr = new ArrayList<>();
//    private void getOtherReciient() {
//        try {
//            otherRecipientAr.clear();
//
//            Map<String, String> m = new LinkedHashMap<>();
//            m.put("Symbol", symbol);
//            m.put("MemberId", member_id);
//            m.put("Take", othr_take + "");
//            m.put("Skip", othr_skip + "");
//            m.put("Type", othr_type + "");
//
//
//            System.out.println("Get recipient====" + m);
//            new ServerHandler().sendToServer(RecipientActivity.this, "RecipientList", m, 0, 1, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try
//                    {
//
//                        JSONObject obj = new JSONObject(dta);
//
//                        if (obj.getString("status").equalsIgnoreCase("true")) {
//
//                            JSONArray othrrecipientList = obj.getJSONArray("RecipientList");
//                            for (int x = 0; x < othrrecipientList.length(); x++) {
//                                otherRecipientAr.add(othrrecipientList.getJSONObject(x));
//                            }
//                            if (otherRecipientAr.size() == 0) {
//                                showtoast.showToast(RecipientActivity.this, "Response", obj.getString("Message"), findViewById(R.id.ll_recipientlayoutmain));
//                            }
//                            if (othr_skip == 0) {
//                                showOtherRecipient();
//                            } else {
//                                otherDetailsAdapterother.notifyDataSetChanged();
//                            }
//                        } else {
//                            // showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
//                        }
//
//
//                        if (mySelfAr.size() == 0 && otherRecipientAr.size() == 0) {
//                            findViewById(R.id.img_default).setVisibility(View.VISIBLE);
//                            TextView create_account = findViewById(R.id.create_account);
//                            create_account.setVisibility(View.VISIBLE);
//                            findViewById(R.id.ll_recipientlayout).setVisibility(View.VISIBLE);
//                            findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.GONE);
//
//                            create_account.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    redirectToReceipient();
//                                }
//                            });
//
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void showOtherRecipient() {
//        findViewById(R.id.img_default).setVisibility(View.GONE);
//        findViewById(R.id.ll_recipientlayout).setVisibility(View.GONE);
//        findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.VISIBLE);
//
//
//        RecyclerView AreadyAddedRecipientRecycerOther = findViewById(R.id.AreadyAddedRecipientRecycerOther);
//        otherDetailsAdapterother = new RecipientListAdapter(otherRecipientAr, this, symbol, null);
//        LinearLayoutManager horizontalLayoutManagaerother = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        AreadyAddedRecipientRecycerOther.setLayoutManager(horizontalLayoutManagaerother);
//        AreadyAddedRecipientRecycerOther.setItemAnimator(new DefaultItemAnimator());
//        AreadyAddedRecipientRecycerOther.setAdapter(otherDetailsAdapterother);
//
//
//    }
//
//    private void redirectToReceipient()
//    {
//        if (symbol.length() > 0)
//        {
//            //tv_title.setText(getResources().getString(R.string.select_recipent));
//            Intent i = new Intent(this, SelectRecipientsTypeActivity.class);
//            i.putExtra("SourceSymbol", getIntent().getStringExtra("SourceSymbol"));
//            i.putExtra("DestinationSymbol", getIntent().getStringExtra("DestinationSymbol"));
//            i.putExtra("FlagImageDestination", getIntent().getStringExtra("FlagImageDestination"));
//            i.putExtra("callFrom", getIntent().getStringExtra("callFrom"));
//            i.putExtra("SDCountryId", getIntent().getStringExtra("SDCountryId"));
//            i.putExtra("CountryId", getIntent().getStringExtra("CountryId"));
//            i.putExtra("data", getIntent().getStringExtra("data"));
//
//
//
//
//            startActivityForResult(i, 101);
//        } else {
//            Intent i = new Intent(this, SelectRecipientsTypeActivity.class);
//            i.putExtra("SourceSymbol", UtilClass.DefaultFromSymble);
//            i.putExtra("DestinationSymbol", UtilClass.defaultToSymble);
//            i.putExtra("FlagImageDestination", UtilClass.getDefaultDestImage);
//            i.putExtra("callFrom", "recipient");
//            i.putExtra("SDCountryId", UtilClass.defaultDestinationSDCountryId);
//            i.putExtra("CountryId", UtilClass.defaultDestinationCountryId);
//            startActivityForResult(i, 101);
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==101)
//        {
//            selfRecipientData();
//        }
//
//    }
//
//
//
//
//
//}