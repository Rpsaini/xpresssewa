package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.RecipientListAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class RecipientFragment extends Fragment {

    View view;
    Context mContext;
    private MainActivity rootAct;
    private RecipientListAdapter otherDetailsAdapterother, myselfDetailsAdapter;

    @BindView(R.id.otherRecipietTV)
    TextView otherRecipietTV;

    @BindView(R.id.myAccountTV)
    TextView myAccountTV;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_back)
    ImageView tv_back;


    @BindView(R.id.iv_toolbar)
    ImageView iv_toolbar;

    @BindView(R.id.llwithoutrecipient)
    LinearLayout llwithoutrecipient;

    @BindView(R.id.AreadyAddedRecipientRecycer)
    RecyclerView AreadyAddedRecipientRecycer;

    private int take = 5, skip = 0, type = 1;
    //type  1 for bank 2 for recipient
    private int othr_take = 5, othr_skip = 0, othr_type = 2;
    private String member_id = "";
    private Showtoast showtoast;

    private String symbol = "";
    public Bundle args;
    private EditText ed_search;
    ArrayList<String> searchArMySelf = new ArrayList<>();
    ArrayList<String> searchArOther = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtoast = new Showtoast();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_recipient, container, false);
            Calligrapher calligrapher = new Calligrapher(getActivity());
            calligrapher.setFont(view, "MontserratRegular.ttf");
            mContext = getActivity();
            rootAct = (MainActivity) getActivity();

            UtilClass.getUserData(getActivity());
            SaveImpPrefrences imp = new SaveImpPrefrences();
            JSONObject loginDetail = new JSONObject(imp.reterivePrefrence(getActivity(), "login_detail").toString());
            member_id = loginDetail.getString("MemberId");
            ButterKnife.bind(this, view);

            args = getArguments();
            symbol = args.getString("symbol");


            init();
            selfRecipientData();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void init() {

        try {
            String login = new SaveImpPrefrences().reterivePrefrence(getActivity(), DefaultConstatnts.login_detail) + "";
            System.out.println("Login Detail===" + login);
            JSONObject jsonObject = new JSONObject(login);
            UtilClass.getDefaultDestImage = jsonObject.getString("FlagImageDestination");
            UtilClass.getDefaultSourceImage = jsonObject.getString("FlagImageSource");
            UtilClass.defaultToSymble = jsonObject.getString("DestinationSymbol");
            UtilClass.DefaultFromSymble = jsonObject.getString("SourceSymbol");

            UtilClass.DefaultCountryname = jsonObject.getString("CountryName");
            UtilClass.defaultSourceCountryId = jsonObject.getString("CountryId");
            UtilClass.defaultSourceCountryId = jsonObject.getString("SDCountryId");
           } catch (Exception e) {
            e.printStackTrace();
        }

        ed_search = view.findViewById(R.id.ed_search);

        view.findViewById(R.id.see_more_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skip++;
                selfRecipientData();
            }
        });

        view.findViewById(R.id.see_more_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othr_skip++;
                getOtherReciient();
            }
        });


        tv_title.setText(getResources().getString(R.string.add_recipent));
        iv_toolbar.setImageResource(R.drawable.plus);


        iv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectToReceipient();

            }
        });


        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    if (s.length() > 0) {
                        ArrayList<JSONObject> dataAr = new ArrayList<>();
                        for (int x = 0; x < searchArOther.size(); x++) {
                            String recipientName = searchArOther.get(x);
                            if (recipientName.contains(s.toString().toLowerCase())) {
                                dataAr.add(otherRecipientAr.get(x));
                            }
                        }
                        showOtherRecipient(dataAr);


                        ArrayList<JSONObject> dataArmyself = new ArrayList<>();
                        for (int x = 0; x < searchArMySelf.size(); x++) {
                            String recipientName = searchArMySelf.get(x);
                            if (recipientName.contains(s.toString().toLowerCase())) {
                                dataArmyself.add(mySelfAr.get(x));
                            }
                        }
                        showDataSelfRecipient(dataArmyself);

                    } else {
                        showOtherRecipient(otherRecipientAr);
                        showDataSelfRecipient(mySelfAr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    ArrayList<JSONObject> mySelfAr = new ArrayList<>();

    private void selfRecipientData() {
        try {
            mySelfAr.clear();
            Map<String, String> m = new LinkedHashMap<>();
            m.put("Symbol", symbol);
            m.put("MemberId", member_id);
            m.put("Take", take + "");
            m.put("Skip", skip + "");
            m.put("Type", type + "");
            System.out.println("Get recipient====" + m);
            getOtherReciient();
            new ServerHandler().sendToServer(getActivity(), "RecipientList", m, 1, 1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject obj = new JSONObject(dta);

                        if (obj.getString("status").equalsIgnoreCase("true")) {
                            JSONArray recipientList = obj.getJSONArray("RecipientList");
                            for (int x = 0; x < recipientList.length(); x++) {
                                mySelfAr.add(recipientList.getJSONObject(x));
                                searchArMySelf.add(recipientList.getJSONObject(x).getString("ReciptentName").toLowerCase());
                            }
                            if (mySelfAr.size() == 0) {
                                showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                            }
                            if (skip == 0) {
                                showDataSelfRecipient(mySelfAr);
                            } else {
                                myselfDetailsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            //   showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDataSelfRecipient(ArrayList<JSONObject> dataObj) {
        view.findViewById(R.id.img_default).setVisibility(View.GONE);
        view.findViewById(R.id.ll_recipientlayout).setVisibility(View.GONE);
        view.findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.VISIBLE);


        if (mySelfAr.size() > 4) {
            mySelfAr.add(null);
        }

        RecyclerView recipient_detail_recycler = view.findViewById(R.id.AreadyAddedRecipientRecycer);
        myselfDetailsAdapter = new RecipientListAdapter(dataObj, (MainActivity) getActivity(), symbol, this, getString(R.string.myaccount));
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recipient_detail_recycler.setLayoutManager(horizontalLayoutManagaer);
        recipient_detail_recycler.setItemAnimator(new DefaultItemAnimator());
        recipient_detail_recycler.setAdapter(myselfDetailsAdapter);


    }

    public void loadMoreRecipient()
    {
        othr_skip = othr_skip + 1;
        getOtherReciient();
    }

    ArrayList<JSONObject> otherRecipientAr = new ArrayList<>();

    private void getOtherReciient() {
        try {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("Symbol", symbol);
            m.put("MemberId", member_id);
            m.put("Take", othr_take + "");
            m.put("Skip", othr_skip + "");
            m.put("Type", othr_type + "");
            System.out.println("Before to send---" + m);

            new ServerHandler().sendToServer(getActivity(), "RecipientList", m, 0, 1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        Log.e(RecipientFragment.class.getSimpleName(), "recipient data2==" + dta);

                        JSONObject obj = new JSONObject(dta);

                        if (obj.getString("status").equalsIgnoreCase("true")) {

                            JSONArray othrrecipientList = obj.getJSONArray("RecipientList");
                            for (int x = 0; x < othrrecipientList.length(); x++) {


                                otherRecipientAr.add(othrrecipientList.getJSONObject(x));
                                searchArOther.add(othrrecipientList.getJSONObject(x).getString("ReciptentName").toLowerCase());

                            }
                            if (otherRecipientAr.size() == 0) {
                                showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                            }
                            if (othr_skip == 0) {
                                showOtherRecipient(otherRecipientAr);
                            } else {
                                otherDetailsAdapterother.notifyDataSetChanged();
                            }
                        } else {
                            // showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                        }


                        if (mySelfAr.size() == 0 && otherRecipientAr.size() == 0) {
                            view.findViewById(R.id.img_default).setVisibility(View.VISIBLE);
                            TextView create_account = view.findViewById(R.id.create_account);
                            create_account.setVisibility(View.VISIBLE);
                            view.findViewById(R.id.ll_recipientlayout).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.GONE);

                            create_account.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectToReceipient();
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    RecyclerView AreadyAddedRecipientRecycerOther;

    private void showOtherRecipient(final ArrayList<JSONObject> dataObjAr) {
        view.findViewById(R.id.img_default).setVisibility(View.GONE);
        view.findViewById(R.id.ll_recipientlayout).setVisibility(View.GONE);
        view.findViewById(R.id.ll_linerlayout_recycler).setVisibility(View.VISIBLE);



            AreadyAddedRecipientRecycerOther = view.findViewById(R.id.AreadyAddedRecipientRecycerOther);
            otherDetailsAdapterother = new RecipientListAdapter(dataObjAr, (MainActivity) getActivity(), symbol, this, getString(R.string.otheraccount));
            LinearLayoutManager horizontalLayoutManagaerother = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            AreadyAddedRecipientRecycerOther.setLayoutManager(horizontalLayoutManagaerother);
            AreadyAddedRecipientRecycerOther.setItemAnimator(new DefaultItemAnimator());
            AreadyAddedRecipientRecycerOther.setAdapter(otherDetailsAdapterother);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();

                try {
                    SimpleDialog simpleDialog = new SimpleDialog();
                    final Dialog confirmDialog = simpleDialog.simpleDailog(rootAct, R.layout.confirmation_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                    ImageView selected_Curreny = confirmDialog.findViewById(R.id.selected_Curreny);
                    TextView txt_currency_name = confirmDialog.findViewById(R.id.txt_currency_name);
                    TextView txt_no = confirmDialog.findViewById(R.id.txt_no);
                    selected_Curreny.setVisibility(View.GONE);
                    TextView txt_yes = confirmDialog.findViewById(R.id.txt_yes);
                    TextView txt_msg = confirmDialog.findViewById(R.id.txt_msg);
                    selected_Curreny.setImageResource(R.drawable.checked);

                    txt_yes.setText("Yes");
                    txt_no.setText("No");
                    txt_msg.setText("Would you like to delete this recipient?");
                    txt_currency_name.setTextSize(20);

                    JSONObject dataObj = dataObjAr.get(position);
                    String ReciptentName = dataObj.getString("ReciptentName");
                    final String id = dataObj.getString("Id");
                    txt_currency_name.setText(ReciptentName);

                    txt_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleteResipient(id, position, dataObjAr, confirmDialog);


                        }
                    });
                    txt_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmDialog.dismiss();
                            otherDetailsAdapterother.notifyDataSetChanged();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(AreadyAddedRecipientRecycerOther);

    }

    public void redirectToReceipient() {
        if (symbol.length() > 0) {
            tv_title.setText(getResources().getString(R.string.select_recipent));
            Intent i = new Intent(getActivity(), SelectRecipientsTypeActivity.class);
            i.putExtra("SourceSymbol", args.getString("SourceSymbol"));
            i.putExtra("DestinationSymbol", args.getString("DestinationSymbol"));
            i.putExtra("FlagImageDestination", args.getString("FlagImageDestination"));
            i.putExtra("callFrom", args.getString("callFrom"));
            i.putExtra("SDCountryId", args.getString("SDCountryId"));
            i.putExtra("CountryId", args.getString("CountryId"));
            i.putExtra("data", args.getString("data"));

            startActivityForResult(i, 101);
        } else {
            try {
                String login = new SaveImpPrefrences().reterivePrefrence(getActivity(), DefaultConstatnts.login_detail) + "";


                JSONObject jsonObject = new JSONObject(login);

                Intent i = new Intent(getActivity(), SelectRecipientsTypeActivity.class);
                i.putExtra("SourceSymbol", jsonObject.getString("SourceSymbol"));
                i.putExtra("DestinationSymbol", jsonObject.getString("DestinationSymbol"));
                i.putExtra("FlagImageDestination", jsonObject.getString("FlagImageDestination"));
                i.putExtra("callFrom", "recipient");
                i.putExtra("SDCountryId", jsonObject.getString("SDCountryId"));
                i.putExtra("CountryId", jsonObject.getString("CountryId"));


                System.out.println("Default cuntry===" + UtilClass.defaultDestinationSDCountryId + "===" + UtilClass.defaultDestinationCountryId);
                startActivityForResult(i, 101);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            selfRecipientData();
        }

    }


    public void verifyingDocuments() {
        SimpleDialog simpleDialog = new SimpleDialog();
        final Dialog confirmDialog = simpleDialog.simpleDailog((MainActivity) getActivity(), R.layout.verify_your_documents, new ColorDrawable(getActivity().getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        ImageView headerbackbutton = confirmDialog.findViewById(R.id.headerbackbutton);
        headerbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }


    private void deleteResipient(final String id, final int pos, final ArrayList<JSONObject> dataAr, final Dialog confirmDia) {


        Map<String, String> m = new LinkedHashMap<>();
        m.put("Id", id);
        m.put("MemberId", member_id);

        new ServerHandler().sendToServer(getActivity(), "DeleteRecipient", m, 1, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("delete resp" + dta);
                    JSONObject obj = new JSONObject(dta);

                    if (obj.getString("status").equalsIgnoreCase("true")) {
                        dataAr.remove(pos);
                        confirmDia.dismiss();
                        otherDetailsAdapterother.notifyDataSetChanged();
                    } else {
                        showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


}



