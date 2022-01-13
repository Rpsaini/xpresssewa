package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.CurrencyAdapter;
import transfer.money.com.xpresssewa.Model.PriceComparisonResponse;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.UtilClass;


public class SendFragment extends Fragment implements View.OnClickListener {
    View view;
    MainActivity mContext;
    private String selectionType = "to";// selectedBasePairId = "";
    private ShimmerFrameLayout mShimmerViewContainer;
    private int progressHit = 0;
    private String recipientId = "";
    int screenWidth;
    private String FlagImage;
    private String CountryId, SDCountryId;
    private Bundle args;
    private LinearLayout ll_seemore;
    private JSONArray PaymentTypesList;



    private String  QuoteID = "";
    @BindView(R.id.spiner_conversion)
    Spinner spiner_conversion;

    @BindView(R.id.img_rotate)
    ImageView img_rotate;


    @BindView(R.id.txt_extimation)
    TextView txt_extimation;



    @BindView(R.id.iv_toolbar)
    ImageView iv_toolbar;

    @BindView(R.id.iv_drop_down)
    ImageView iv_drop_down;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_send_money)
    TextView tv_send_money;

    @BindView(R.id.ll_price_comparison)
    LinearLayout ll_price_comparison;

    @BindView(R.id.ll_price_comparison_hidden)
    RelativeLayout ll_price_comparison_hidden;


    @BindView(R.id.totalfee)
    TextView totalfee;

    @BindView(R.id.amountconverted)
    TextView amountconverted;

    @BindView(R.id.seemore)
    TextView seemore;

    @BindView(R.id.pricecomparison)
    TextView pricecomparison;

    @BindView(R.id.isent)
    EditText isent;

    @BindView(R.id.receiveAmt)
    EditText receiveAmt;

    @BindView(R.id.sentlinear)
    LinearLayout sentlinear;

    @BindView(R.id.semoreexpand)
    LinearLayout semoreexpand;


    /*price comparison*/
    @BindView(R.id.rv_price_comparison)
    RecyclerView rv_price_comparison;

    @BindView(R.id.tv_currency_from)
    TextView tv_currency_from;

    @BindView(R.id.tv_currency_to)
    TextView tv_currency_to;


    @BindView(R.id.linearfromdropdown)
    LinearLayout linearfromdropdown;

    @BindView(R.id.todropdownlinear)
    LinearLayout todropdownlinear;

    @BindView(R.id.from_currency_image)
    ImageView from_currency_image;

    @BindView(R.id.to_currency_image)
    ImageView to_currency_image;

    @BindView(R.id.rate)
    TextView rate;

    @BindView(R.id.tv_guaranteed_rate)
    TextView tv_guaranteed_rate;

    @BindView(R.id.outerrelativelayout)
    RelativeLayout outerrelativelayout;


    //eror  send  display=====
    @BindView(R.id.rel_layout_for_sent)
    RelativeLayout rel_layout_for_sent;

    @BindView(R.id.error_errormessage_liner_sent)
    LinearLayout error_errormessage_liner_sent;

    @BindView(R.id.error_arrow_up_img_sent)
    ImageView error_arrow_up_img_sent;

    @BindView(R.id.show_sent_Error_msg)
    TextView show_sent_Error_msg;

    //error receive

    @BindView(R.id.rel_layout_for_receive)
    RelativeLayout rel_layout_for_receive;

    @BindView(R.id.error_errormessage_liner_receive)
    LinearLayout error_errormessage_liner_receive;

    @BindView(R.id.error_arrow_up_img_recive)
    ImageView error_arrow_up_img_recive;

    @BindView(R.id.show_receive_Error_msg)
    TextView show_receive_Error_msg;
    //===============
    @BindView(R.id.llseemoredetailviewlayout)
    LinearLayout llseemoredetailviewlayout;

    @BindView(R.id.isenttextview)
    TextView isenttextview;

    @BindView(R.id.receivetextview)
    TextView receivetextview;

    @BindView(R.id.ourfee)
    TextView ourfee;

    @BindView(R.id.transfer_wise_fee)
    TextView transfer_wise_fee;

    @BindView(R.id.ll_transfer_wise_fee)
    LinearLayout ll_transfer_wise_fee;

    @BindView(R.id.transferwiseline)
    View transferwiseline;


    @BindView(R.id.upper_line)
    View upper_line;



    private RecyclerView.Adapter adapter_price;
    private ArrayList<PriceComparisonResponse> mPriceResponseArrayList;
    private int screenheight;

    private String FromAmount = "";
    private String ToAmount = "";
    private String ConvertAmount = "";
    private String ConversionRate = "";
    private String Fees = "";
    private String TotalFee = "";
    private String ProcessingDays = "";
    private String GuaranteedRate = "";
    private String OurFees = "";
    private String TwFee = "";
    private String PaymentTypeId="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(view, "MontserratRegular.ttf");


        mContext = (MainActivity) getActivity();
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenheight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        TextView txt_privacy=view.findViewById(R.id.txt_privacy);
        TextView txt_terms=view.findViewById(R.id.txt_terms);


        String checkBoxText = "By continue accept our  <a href="+UtilClass.termsurl+">Terms of use </a>"
                +" and <a href="+UtilClass.privacyurl+">Privacy Policy</a>";

        txt_terms.setText(Html.fromHtml(checkBoxText));
        txt_terms.setMovementMethod(LinkMovementMethod.getInstance());


        ButterKnife.bind(this, view);
        init();


        args = getArguments();
        if(args.containsKey("data"))
           {
            try {
                JSONObject ob = new JSONObject(args.getString("data"));
                if(ob.has("getAmountFromReviewActivity"))
                 {
                    recipientId = ob.getString("Id");
                    System.out.println("object===" + ob);
                    JSONObject calculationData = new JSONObject(ob.getString("calculation"));

                    System.out.println("Calculationnn=="+calculationData);

                    showImage(calculationData.getString("sourceFlag"), from_currency_image);
                    showImage(calculationData.getString("destFlag"), to_currency_image);

                    todropdownlinear.setClickable(false);
                    receiveAmt.setEnabled(false);
                    todropdownlinear.setAlpha(.8f);

                    tv_currency_from.setText(UtilClass.DefaultFromSymble);
                    isent.setText(calculationData.getString("AmountFrom"));
                    receiveAmt.setText(calculationData.getString("AmountTo"));
                    tv_currency_from.setText(calculationData.getString("FromSymbol"));
                    tv_currency_to.setText(calculationData.getString("ToSymbol"));
                    receiveAmt.setText("1");

                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("RecipientId",  "");
                    m.put("MemberId",  UtilClass.member_id);
                    m.put("ToSymbol", tv_currency_to.getText() + "");
                    m.put("FromSymbol", tv_currency_from.getText() + "");
                    m.put("AmountFrom", isent.getText() + "");
                    m.put("AmountTo", receiveAmt.getText() + "");
                    m.put("Type", "from");
                    m.put("CountryName", "");
                    m.put("PaymentType", "");
                    getCurrentCalculation(m, "initial");
                } else {
                    recipientId = ob.getString("Id");
                    System.out.println("object===" + ob);

                    showImage(ob.getString("Image"), to_currency_image);
                    showImage(UtilClass.getDefaultSourceImage, from_currency_image);

                    receivetextview.setText(ob.getString("ReciptentName") + " Gets");

                    todropdownlinear.setClickable(false);
                    receiveAmt.setEnabled(false);
                    todropdownlinear.setAlpha(.8f);

                    tv_currency_from.setText(UtilClass.DefaultFromSymble);
                    isent.setText("1000");


                    tv_currency_to.setText(ob.getString("Symbol"));
                    receiveAmt.setText("1");
                    System.out.println("recived text===" + tv_currency_from.getText() + "====" + ob.getString("Symbol"));


                    Map<String, String> m = new LinkedHashMap<>();
                    m.put("RecipientId",  "");
                    m.put("MemberId",  UtilClass.member_id);
                    m.put("ToSymbol", tv_currency_to.getText() + "");
                    m.put("FromSymbol", tv_currency_from.getText() + "");
                    m.put("AmountFrom", isent.getText() + "");
                    m.put("AmountTo", receiveAmt.getText() + "");
                    m.put("Type", "from");
                    m.put("CountryName", "");
                    m.put("PaymentType", "");
                    getCurrentCalculation(m, "initial");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            showImage(UtilClass.getDefaultSourceImage, from_currency_image);
            showImage(UtilClass.getDefaultDestImage, to_currency_image);
            tv_currency_from.setText(UtilClass.DefaultFromSymble);
            tv_currency_to.setText(UtilClass.defaultToSymble);

            todropdownlinear.setClickable(true);
            todropdownlinear.setAlpha(1f);
            Map<String, String> m = new LinkedHashMap<>();
            m.put("RecipientId",  "");
            m.put("MemberId",  UtilClass.member_id);
            m.put("ToSymbol", UtilClass.defaultToSymble);
            m.put("FromSymbol", UtilClass.DefaultFromSymble);
            m.put("AmountFrom", "1000");
            m.put("AmountTo", "1");
            m.put("Type", "both");
            m.put("CountryName", UtilClass.DefaultCountryname);
            m.put("PaymentType", "");
            getCurrentCalculation(m, "initial");
        }

        scheduler();
        onEditableListenerSend();


        return view;
    }

    private void init() {
        ll_seemore = view.findViewById(R.id.ll_seemore);
        iv_toolbar.setImageResource(R.drawable.notification_icon);
        iv_toolbar.setVisibility(View.GONE);
        tv_title.setText(R.string.send_money);
        tv_send_money.setOnClickListener(this);
        tv_send_money.setTag("0");
        // ll_price_comparison.setOnClickListener(this);
        sentlinear.setOnClickListener(this);
        linearfromdropdown.setOnClickListener(this);
        todropdownlinear.setOnClickListener(this);
        seemore.setOnClickListener(this);
        isent.setOnClickListener(this);
        receiveAmt.setOnClickListener(this);

        UtilClass.getUserData(getActivity());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.linearfromdropdown:
                selectionType = "from";
                showListingPopup("Select Currency", ((MainActivity) getActivity()).sourcecCurrncy, from_currency_image, tv_currency_from);


                break;
            case R.id.todropdownlinear:
                selectionType = "to";
                showListingPopup("Select Currency", ((MainActivity) getActivity()).destinationCurrency, to_currency_image, tv_currency_to);

                break;


            case R.id.tv_send_money:
                if(tv_send_money.getTag().toString().equalsIgnoreCase("0"))
                {
                    String isKycApproved = new SaveImpPrefrences().reterivePrefrence(getActivity(), DefaultConstatnts.IsKycApproved).toString();
                    if((isKycApproved.equalsIgnoreCase("1")||isKycApproved.equalsIgnoreCase("3")))//approved   1 recipient,3 recipient {
                       {
                           callRecipient();
                       }
                     else
                         {
                          ((MainActivity) getActivity()).callMyProfileFragment("personal");
                         }
                }

                break;



            case R.id.seemore:
                if (seemore.getTag().toString().equalsIgnoreCase("0")) {
                    semoreexpand.setVisibility(view.VISIBLE);
                    seemore.setText("See less");
                    seemore.setTag("1");
                    img_rotate.setRotation(0);
                } else {
                    semoreexpand.setVisibility(view.GONE);
                    seemore.setText("See more");
                    seemore.setTag("0");
                    img_rotate.setRotation(180);

                }
                break;


        }

    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);

            ll_price_comparison_hidden.startAnimation(bottomUp);
            ll_price_comparison_hidden.setVisibility(View.VISIBLE);
            initPriceComparisonList();
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_down);
            mPriceResponseArrayList.clear();
            ll_price_comparison_hidden.startAnimation(bottomDown);
            ll_price_comparison_hidden.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return ll_price_comparison_hidden.getVisibility() == View.VISIBLE;
    }


    protected void initPriceComparisonList() {
        mPriceResponseArrayList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_price_comparison.setLayoutManager(layoutManager);
        rv_price_comparison.setItemAnimator(new DefaultItemAnimator());
        setData();

    }

    @Override
    public void onStop() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onStop();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    private void setData() {
        PriceComparisonResponse obj = new PriceComparisonResponse();
        obj.setDifference("+15");
        obj.setId("1");
        obj.setInfo("We Give");
        obj.setPrice("750");

        mPriceResponseArrayList.add(obj);

        PriceComparisonResponse obj1 = new PriceComparisonResponse();
        obj1.setDifference("-15");
        obj1.setId("2");
        // obj.setInfo("We Give");
        obj1.setPrice("720");

        mPriceResponseArrayList.add(obj1);

        PriceComparisonResponse obj2 = new PriceComparisonResponse();
        obj2.setDifference("-25");
        obj2.setId("3");
        // obj.setInfo("We Give");
        obj2.setPrice("620");

        mPriceResponseArrayList.add(obj2);


        PriceComparisonResponse obj3 = new PriceComparisonResponse();
        obj3.setDifference("-25");
        obj3.setId("4");
        // obj.setInfo("We Give");
        obj3.setPrice("620");

        mPriceResponseArrayList.add(obj3);
    }


    private void getCurrentCalculation(Map<String, String> m, final String callType) {
        System.out.println("before to send==m=" + m);

        hideErrorMessages();
        showPlaceHolders();

        new ServerHandler().sendToServer(getActivity(), "Calculation", m, progressHit, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject obj = new JSONObject(dta);

                    System.out.println("response obj==" + obj);
                    if(obj.getBoolean("status"))
                      {
                        progressHit++;
                        if (obj.getJSONArray("DestinationList").length() > 0) {
                            ((MainActivity) getActivity()).sourcecCurrncy = obj.getJSONArray("SourceList");
                            ((MainActivity) getActivity()).destinationCurrency = obj.getJSONArray("DestinationList");
                            CountryId = obj.getString("DefaultToCountryId");
                            SDCountryId = obj.getString("DefaultToSdCountryId");

                            ll_price_comparison.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    slideUpDown();
                                }
                            });


                            iv_drop_down.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    slideUpDown();
                                }
                            });


                        }

                        showData(obj, 0);



                        hidePlaceHolders();
                        onEditableListenerSend();
                        onEditableListenerReceive();
                        PaymentTypesList = obj.getJSONArray("PaymentTypesList");
                        showSpinner(PaymentTypesList);
                        tv_send_money.setAlpha(1f);
                        tv_send_money.setTag("0");



                    } else {

                        tv_send_money.setAlpha(.5f);
                        tv_send_money.setTag("1");
                        showErrorMsg(callType, obj.getString("Message"));
                        //hidePlaceHolders();
                        mShimmerViewContainer.stopShimmerAnimation();
                        if (obj.getJSONArray("DestinationList").length() > 0) {
                            ((MainActivity) getActivity()).sourcecCurrncy = obj.getJSONArray("SourceList");
                            ((MainActivity) getActivity()).destinationCurrency = obj.getJSONArray("DestinationList");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    hidePlaceHolders();
                }

            }
        });
    }


    private Dialog listingDialog;
    private LinearLayout listviewout;

    private void showListingPopup(String currencyTitle, JSONArray currencyAray, ImageView currencyImg, TextView currencySymble) {
        try {
            if (listingDialog != null && listingDialog.isShowing()) {
                listingDialog.dismiss();
            }
            listingDialog = new Dialog(getActivity());
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
                    CurrencyAdapter mAdapter = new CurrencyAdapter(currencyAray, currencyTitle, SendFragment.this, currencyImg, currencySymble);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    currencyRecycler.setLayoutManager(mLayoutManager);
                    currencyRecycler.setItemAnimator(new DefaultItemAnimator());
                    currencyRecycler.setAdapter(mAdapter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

    }

    public void disMissDialog(JSONObject obj) {
        try {
            Animation animation = new TranslateAnimation(0, screenWidth, 00, 0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            listviewout.startAnimation(animation);
            listviewout.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listingDialog.dismiss();
                }
            }, 300);


            if (obj != null)
              {
                FlagImage = obj.getString("FlagImage");
                CountryId = obj.getString("CountryId");
                SDCountryId = obj.getString("SDCountryId");
                Map<String, String> m = new LinkedHashMap<>();
                m.put("RecipientId",  "");
                m.put("MemberId",  UtilClass.member_id);
                m.put("ToSymbol", tv_currency_to.getText().toString());
                m.put("FromSymbol", tv_currency_from.getText().toString());
                m.put("AmountFrom", isent.getText().toString());
                m.put("AmountTo", receiveAmt.getText().toString());
                m.put("Type", selectionType);
                m.put("CountryName", "");
                m.put("PaymentType", "");
                getCurrentCalculation(m, selectionType);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showErrorMsg(String type, String msg)
    {
        if (type.equalsIgnoreCase("from")) {
            error_errormessage_liner_sent.setVisibility(View.VISIBLE);
            error_arrow_up_img_sent.setVisibility(View.VISIBLE);
            show_sent_Error_msg.setText(msg);
            isenttextview.setTextColor(getActivity().getResources().getColor(R.color.dark_red_color));
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                rel_layout_for_sent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.redborder));
            } else {
                rel_layout_for_sent.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redborder));
            }
        } else {
            error_errormessage_liner_receive.setVisibility(View.VISIBLE);
            error_arrow_up_img_recive.setVisibility(View.VISIBLE);

            show_receive_Error_msg.setText(msg);
            receivetextview.setTextColor(getActivity().getResources().getColor(R.color.dark_red_color));
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                rel_layout_for_receive.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.redborder));
            } else {
                rel_layout_for_receive.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redborder));
            }

        }
    }

    private void hideErrorMessages() {
        try {
            error_errormessage_liner_receive.setVisibility(View.GONE);
            error_arrow_up_img_recive.setVisibility(View.GONE);
            error_errormessage_liner_sent.setVisibility(View.GONE);
            error_arrow_up_img_sent.setVisibility(View.GONE);
            isenttextview.setTextColor(getActivity().getResources().getColor(R.color.title_black_color));
            receivetextview.setTextColor(getActivity().getResources().getColor(R.color.title_black_color));


            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                rel_layout_for_receive.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.round_white_with_border));
                rel_layout_for_sent.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.round_white_with_border));

            } else {
                rel_layout_for_receive.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_white_with_border));
                rel_layout_for_sent.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_white_with_border));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void showPlaceHolders() {
        if (mShimmerViewContainer.getVisibility() != View.VISIBLE) {
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            llseemoredetailviewlayout.setVisibility(View.GONE);
            mShimmerViewContainer.startShimmerAnimation();
        }
    }

    private void hidePlaceHolders()
    {
        mShimmerViewContainer.setVisibility(View.GONE);
        llseemoredetailviewlayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.stopShimmerAnimation();

    }


    long last_text_edit = 0;
    final long delay = 1000; // 1 seconds after user stops typing
    final Handler handler = new Handler();
    Runnable input_finish_checker;
    private EditText selectedEdittext;


    private void scheduler() {

        input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    if (selectedEdittext != null) {
                        if (selectedEdittext.getTag().toString().equalsIgnoreCase("sent"))//to
                        {
                            //  getSelectedId(tv_currency_from.getText().toString(), "to");
                            Map<String, String> m = new LinkedHashMap<>();
                            m.put("RecipientId",  "");
                            m.put("MemberId",  UtilClass.member_id);
                            m.put("ToSymbol", tv_currency_to.getText().toString());
                            m.put("FromSymbol", tv_currency_from.getText().toString());
                            m.put("AmountFrom", isent.getText().toString());
                            m.put("AmountTo", receiveAmt.getText().toString());
                            m.put("Type", "from");
                            m.put("CountryName", "");
                            m.put("PaymentType", "");
                            getCurrentCalculation(m, "to");
                        } else {
                            //  getSelectedId(tv_currency_to.getText().toString(), "from");
                            Map<String, String> m = new LinkedHashMap<>();
                            m.put("RecipientId",  "");
                            m.put("MemberId",  UtilClass.member_id);
                            m.put("ToSymbol", tv_currency_to.getText().toString());
                            m.put("FromSymbol", tv_currency_from.getText().toString());
                            m.put("AmountFrom", isent.getText().toString());
                            m.put("AmountTo", receiveAmt.getText().toString());
                            m.put("Type", "to");
                            m.put("CountryName", "");
                            m.put("PaymentType", "");
                            getCurrentCalculation(m, "from");
                        }
                    }

                }
            }
        };

    }

    TextWatcher watcherForSent, watcherForReceive;
    private void onEditableListenerSend() {
        watcherForSent = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
                hideErrorMessages();
                showPlaceHolders();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 0) {
                    selectionType = "to";
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                    selectedEdittext = isent;
                }
            }
        };

        isent.addTextChangedListener(watcherForSent);
    }

    private void onEditableListenerReceive() {
        watcherForReceive = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
                hideErrorMessages();
                showPlaceHolders();
                System.out.println("inside receiver====>>>>>>");
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 0) {
                    selectionType = "from";
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                    selectedEdittext = receiveAmt;
                }
            }
        };

        receiveAmt.addTextChangedListener(watcherForReceive);
    }


    private void showSpinner(final JSONArray ar) {
        final ArrayList<String> dataAr = new ArrayList<>();
        for (int x = 0; x < ar.length(); x++) {
            try {
                JSONObject dataObj = ar.getJSONObject(x);
                dataAr.add(dataObj.getString("Tittle") + " " + dataObj.getString("exactfees") + " " + tv_currency_from.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spiiner_for_send, dataAr);
        arrayAdapter.setDropDownViewResource(R.layout.layout_spiiner_for_send);
        spiner_conversion.setAdapter(arrayAdapter);
        spiner_conversion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject dataObj = ar.getJSONObject(position);
                  //  showData(dataObj, 1);
                    ((TextView) parent.getChildAt(0)).setTextSize(12);
                    if(dataObj.has("PaymentTypesList"))
                    {
                        JSONArray PaymentTypesList = dataObj.getJSONArray("PaymentTypesList");
                        JSONObject jsonData = PaymentTypesList.getJSONObject(0);
                        System.out.println("Payment data====" + dataObj);

                        Map<String, String> m = new LinkedHashMap<>();
                        m.put("RecipientId",  "");
                        m.put("MemberId",  UtilClass.member_id);
                        m.put("ToSymbol", tv_currency_to.getText().toString());
                        m.put("FromSymbol", tv_currency_from.getText().toString());
                        m.put("AmountFrom", isent.getText().toString());
                        m.put("AmountTo", receiveAmt.getText().toString());
                        m.put("Type", selectionType);
                        m.put("CountryName", "");
                        PaymentTypeId=jsonData.getString("PaymentTypeId");
                        m.put("PaymentType", PaymentTypeId);

                        getCurrentCalculation(m, selectionType);

                       }
                    System.out.println("data selected===" + dataObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                string tutorialsName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showData(JSONObject obj, int callFrom) {
        try
           {
            if(callFrom == 0)
            {
                System.out.println("Data obj====>"+obj);
                FromAmount = obj.getString("FromAmount");
                ToAmount = obj.getString("ToAmount");
                ConvertAmount = obj.getString("ConvertAmount");
                ConversionRate = obj.getString("ConversionRate");
                Fees = obj.getString("Fees");
                TotalFee = obj.getString("TotalFee");
                ProcessingDays = obj.getString("ProcessingDays");
                GuaranteedRate = obj.getString("GuaranteedRate");
                OurFees = obj.getString("OurFees");
                TwFee = obj.getString("TwFee");
            }
            else
                {
                FromAmount = obj.getString("fromamount");
                ToAmount = obj.getString("toamount");
                ConvertAmount = obj.getString("convertamount");
                ConversionRate = obj.getString("conversionrate");
                Fees = obj.getString("exactfees");
                TotalFee = obj.getString("totalfee");
                ProcessingDays = obj.getString("ProcessingDays");
                GuaranteedRate = obj.getString("GuaranteedRate");
                OurFees = obj.getString("ourexactfee");
                TwFee = obj.getString("TwFee");
               }
            if (watcherForReceive != null) {
                receiveAmt.removeTextChangedListener(watcherForReceive);
            }
            if (watcherForSent != null) {
                isent.removeTextChangedListener(watcherForSent);
            }


            receiveAmt.setText(ToAmount);
            isent.setText(FromAmount);


               totalfee.setText(TotalFee +" "+ tv_currency_from.getText().toString());
               rate.setText(Fees +" "+ tv_currency_from.getText().toString());
               amountconverted.setText(ConvertAmount +" "+ tv_currency_from.getText().toString());
               tv_guaranteed_rate.setText( ConversionRate+"");
               ourfee.setText( OurFees +" "+ tv_currency_from.getText().toString());


//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//               {
//                totalfee.setText(Html.fromHtml("<b>" + TotalFee +" "+ tv_currency_from.getText().toString()+"</b> "  +" Total Fees", Html.FROM_HTML_MODE_COMPACT));
//                rate.setText(Html.fromHtml("<b>" + Fees +" "+ tv_currency_from.getText().toString()+"</b> " , Html.FROM_HTML_MODE_COMPACT));
//                amountconverted.setText(Html.fromHtml("<b>" + ConvertAmount +" "+ tv_currency_from.getText().toString()+"</b> " +" Amount Will converted ", Html.FROM_HTML_MODE_COMPACT));
//                tv_guaranteed_rate.setText(Html.fromHtml("<b>" + ConversionRate +   "</b> <font color=\"#226ED4\">Conversion Rate ("+GuaranteedRate+" hrs) </font>", Html.FROM_HTML_MODE_COMPACT));
//                ourfee.setText(Html.fromHtml("<b>" + OurFees +" "+ tv_currency_from.getText().toString()+"</b> " +" Our Fee", Html.FROM_HTML_MODE_COMPACT));
//                txt_extimation.setText(Html.fromHtml("Should arrive by <font color=\"#226ED4\"> <b>" + ProcessingDays + "</b></font> ", Html.FROM_HTML_MODE_COMPACT));
//                if(TwFee.length() > 0)
//                {
//                    if (Double.parseDouble(TwFee) > 0)
//                    {
//                        ll_transfer_wise_fee.setVisibility(View.VISIBLE);
//                        transferwiseline.setVisibility(View.VISIBLE);
//                        transfer_wise_fee.setText(Html.fromHtml("  <b>" + TwFee + tv_currency_from.getText().toString()+"</b> "+" Transfer Wise fee", Html.FROM_HTML_MODE_COMPACT));
//                    }
//                }
//            }
//            else
//                {
//                totalfee.setText(Html.fromHtml("<b>" + TotalFee +" "+ tv_currency_from.getText().toString()+"</b> " +" Total Fees"));
//                rate.setText(Html.fromHtml("<b>" + Fees +" "+ tv_currency_from.getText().toString()+"</b> " ));
//                amountconverted.setText(Html.fromHtml(" <b>" + ConvertAmount +" "+ tv_currency_from.getText().toString()+"</b> " +" Amount Will converted"));
//                tv_guaranteed_rate.setText(Html.fromHtml("  <b>" + ConversionRate  + "</b><font color=\"#226ED4\"> Conversion rate ("+GuaranteedRate+" hrs)</font>"));
//                ourfee.setText(Html.fromHtml(" <b>" + OurFees +" "+ tv_currency_from.getText().toString()+"</b>"  +" Our Fee"));
//                txt_extimation.setText(Html.fromHtml("Should arrive by <font color=\"#226ED4\"> <b>" + ProcessingDays + "</b> </font>"));
//                if (TwFee.length() > 0) {
//                    if (Double.parseDouble(TwFee) > 0) {
//                        ll_transfer_wise_fee.setVisibility(View.VISIBLE);
//                        transferwiseline.setVisibility(View.VISIBLE);
//                        transfer_wise_fee.setText(Html.fromHtml(" <b>" + TwFee + "</b> Transfer Wise fee"));
//                    }
//                }
//
//            }


            String ShowDropDown = obj.getString("ShowDropDown");
            if (ShowDropDown.equalsIgnoreCase("0"))
            {
                ll_seemore.setVisibility(View.GONE);
                upper_line.setVisibility(View.GONE);
            } else {
                ll_seemore.setVisibility(View.GONE);
                upper_line.setVisibility(View.GONE);
            }


            JSONObject paymentTypesList = obj.getJSONArray("PaymentTypesList").getJSONObject(0);
            QuoteID=paymentTypesList.getString("quoteid");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showImage(String path, final ImageView profileimage) {

        if (path.length() > 0) {
            Picasso.with(getActivity())
                    .load(path + "")
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


    private void getRecipient() {
        try {
            Intent i = new Intent(getActivity(), ReviewTransfer.class);
            i.putExtra("RecipientId", recipientId);
            JSONObject obj = new JSONObject();
            obj.put("ToSymbol", tv_currency_to.getText().toString());
            obj.put("FromSymbol", tv_currency_from.getText().toString());
            obj.put("AmountFrom", isent.getText().toString());
            obj.put("AmountTo", receiveAmt.getText().toString());
            obj.put("ConvertAmount", ConvertAmount);
            obj.put("ConversionRate", ConversionRate);
            obj.put("Fees", Fees);
            obj.put("TotalFee", TotalFee);
            obj.put("ProcessingDays", ProcessingDays);
            obj.put("GuaranteedRate", GuaranteedRate);
            obj.put("OurFees", OurFees);
            i.putExtra("data", obj + "");
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {

        super.onResume();
    }


    private void callRecipient()
    {
        try {
            JSONObject obj = new JSONObject();
            obj.put("ToSymbol", tv_currency_to.getText().toString());
            obj.put("FromSymbol", tv_currency_from.getText().toString());
            obj.put("AmountFrom", isent.getText().toString());
            obj.put("AmountTo", receiveAmt.getText().toString());
            obj.put("ConvertAmount", ConvertAmount);
            obj.put("ConversionRate", ConversionRate);
            obj.put("Fees", Fees);
            obj.put("TotalFee", TotalFee);
            obj.put("ProcessingDays", ProcessingDays);
            obj.put("GuaranteedRate", GuaranteedRate);
            obj.put("OurFees", OurFees);
            obj.put("paymentMethod", PaymentTypesList);
            obj.put("Type", selectionType);

            JSONArray sourcecCurrncy = ((MainActivity) getActivity()).sourcecCurrncy;
            JSONArray destinationCurrency = ((MainActivity) getActivity()).destinationCurrency;

            for (int x = 0; x < sourcecCurrncy.length(); x++)
            {
                JSONObject sourceData = sourcecCurrncy.getJSONObject(x);
                String symbol = sourceData.getString("Symbol");
                String sourceFlagImage = sourceData.getString("FlagImage");

                System.out.println("Symbol==="+symbol);
                if (symbol.trim().equalsIgnoreCase(tv_currency_from.getText().toString().trim())) {
                    obj.put("CurrencyId", sourceData.getString("CurrencyId").trim());
                    obj.put("sourceFlag",sourceFlagImage.trim());
                    break;
                }

            }
            for(int x = 0; x < destinationCurrency.length(); x++)
            {
                JSONObject destinationObj = destinationCurrency.getJSONObject(x);
                String symbolDest = destinationObj.getString("Symbol");
                String destFlagImage = destinationObj.getString("FlagImage");
                System.out.println("Symbol=2=="+symbolDest);
                if (symbolDest.trim().equalsIgnoreCase(tv_currency_to.getText().toString().trim()))
                {
                    obj.put("FromCurrencyId", destinationObj.getString("CurrencyId").trim());
                    obj.put("destFlag",destFlagImage.trim());
                    break;
                }
            }

            obj.put("QuoteID", QuoteID);




            MainActivity callTorecipient = (MainActivity) getActivity();
            callTorecipient.callToRecipientWithSymbol(tv_currency_to.getText().toString(), tv_currency_from.getText().toString(),
                    obj.getString("destFlag"), "sendFrg", SDCountryId, CountryId, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


