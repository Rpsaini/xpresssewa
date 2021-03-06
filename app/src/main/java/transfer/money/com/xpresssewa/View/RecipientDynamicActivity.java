package transfer.money.com.xpresssewa.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.CurrencyAdapter;
import transfer.money.com.xpresssewa.Adapter.DynamicFormAdapter;
import transfer.money.com.xpresssewa.Model.DateInputMask;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.placepicker.LocationPickerActivity;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;
import transfer.money.com.xpresssewa.validation.Validation;


public class RecipientDynamicActivity extends AppCompatActivity {

    int screenheight, screenWidth;
    @BindView(R.id.lldynamiclayout)
    RecyclerView lldynamiclayout;

    @BindView(R.id.selectcurrency)
    LinearLayout selectcurrency;

    @BindView(R.id.tv_currency_from)
    TextView tv_currency_from;

    @BindView(R.id.from_currency_image)
    ImageView from_currency_image;


    @BindView(R.id.recipient_type)
    TextView recipient_type;

    @BindView(R.id.headerbackbutton)
    ImageView headerbackbutton;

    @BindView(R.id.recipient_state)
    Spinner recipient_state;

    @BindView(R.id.recipient_city)
    TextInputLayout recipient_city;

    @BindView(R.id.recipient_zipcode)
    TextInputLayout recipient_zipcode;

    @BindView(R.id.recipient_name)
    public TextInputLayout recipient_name;

    @BindView(R.id.recipient_lastname)
    public TextInputLayout recipient_lastname;

    @BindView(R.id.recipient_email)
    TextInputLayout recipient_email;

    @BindView(R.id.RRtoplayout_recipient)
    RelativeLayout RRtoplayout_recipient;

    @BindView(R.id.recipient_address)
    TextInputLayout recipient_address;

    @BindView(R.id.recipient_buisnessname)
    TextInputLayout recipient_buisnessname;


    @BindView(R.id.tv_send_money)
    TextView tv_send_money;


    @BindView(R.id.spinner_bank_name)
    Spinner spinner_bank_name;

    @BindView(R.id.select_date)
    TextInputLayout select_date;

    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.ed_mobilenumber)
    EditText ed_mobilenumber;


//    @BindView(R.id.select_date_error)
//    TextView select_date_error;

    @BindView(R.id.recipient_name_joint)
    TextInputLayout recipient_name_joint;


    @BindView(R.id.recipient_lastname_joint)
    TextInputLayout recipient_lastname_joint;


//    @BindView(R.id.line_dob)
//    View line_dob;


    @BindView(R.id.relation_of_benificary)
    TextInputLayout relation_of_benificary;


    private boolean isType = true;
    private JSONArray recipientBankDetailDynaicAr;
    private String rec_type, SourceSymbol, FlagImageDestination, callFrom, DestinationSymbol, rec_typetext;
    private Showtoast showtoast;
    private JSONArray destinationListofSymble;
    public ArrayList<Object> dynamicFieldAr = new ArrayList<>();
    private String SDCountryId, CountryId;
    private String recipient_stateStr = "", recipient_statecodeStr = "", countryMobilecode = "", bankCode = "";
    private JSONArray dynamicForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_dynamic);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenheight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        showtoast = new Showtoast();

        getScreenHeight();


        rec_typetext = getIntent().getStringExtra("rec_type");

        recipient_type.setText(rec_typetext);
        rec_type = getIntent().getStringExtra("rec_type_str");


        DestinationSymbol = getIntent().getStringExtra("DestinationSymbol");
        SourceSymbol = getIntent().getStringExtra("SourceSymbol");
        FlagImageDestination = getIntent().getStringExtra("FlagImageDestination");


        callFrom = getIntent().getStringExtra("callFrom");
        SDCountryId = getIntent().getStringExtra("SDCountryId");
        CountryId = getIntent().getStringExtra("CountryId");


        countryMobilecode = countryCodePicker.getSelectedCountryCode();
        countryCodePicker.getChildAt(0).setEnabled(false);

        if (rec_typetext.equalsIgnoreCase("Myself"))//1
        {
            recipient_email.setVisibility(View.GONE);
            recipient_state.setVisibility(View.VISIBLE);
            recipient_city.setVisibility(View.VISIBLE);
            recipient_zipcode.setVisibility(View.VISIBLE);
            recipient_address.setVisibility(View.VISIBLE);
            select_date.setVisibility(View.GONE);

        } else if (rec_typetext.equalsIgnoreCase("Someone Else"))//2
        {
            relation_of_benificary.setVisibility(View.VISIBLE);
        } else if (rec_typetext.equalsIgnoreCase("Business"))//3
        {
            recipient_state.setVisibility(View.GONE);
            recipient_city.setVisibility(View.VISIBLE);
            recipient_zipcode.setVisibility(View.GONE);
            recipient_address.setVisibility(View.GONE);
            recipient_buisnessname.setVisibility(View.VISIBLE);
            relation_of_benificary.setVisibility(View.VISIBLE);
            recipient_address.setVisibility(View.VISIBLE);
        } else if (rec_typetext.equalsIgnoreCase("Joint"))//5
        {
            recipient_buisnessname.setVisibility(View.GONE);
            recipient_name_joint.setVisibility(View.VISIBLE);
            recipient_lastname_joint.setVisibility(View.VISIBLE);
            relation_of_benificary.setVisibility(View.VISIBLE);

        }

        recipient_lastname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setAccountName();
            }
        });

        Map<String, String> m = new LinkedHashMap<>();
        m.put("SourceSymbol", SourceSymbol);
        m.put("DestinationSymbol", DestinationSymbol);
        m.put("Type", rec_type);
        MRequestRecipientForm(m);

        headerbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipient();
            }
        });

        new DateInputMask(select_date.getEditText());

//        select_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog mDatePicker;
//                Calendar mCurrentDate = Calendar.getInstance();
//                int mYear = mCurrentDate.get(Calendar.YEAR);
//                int mMonth = mCurrentDate.get(Calendar.MONTH);
//                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
//
//                mDatePicker = new DatePickerDialog(RecipientDynamicActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
//                        select_date.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
//                    }
//                }, mYear, mMonth, mDay);
//                mDatePicker.getDatePicker().setMaxDate(mCurrentDate.getTimeInMillis());
//                mDatePicker.show();
//            }
//        });

        recipient_address.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isType = true;
                return false;
            }
        });

        recipient_address.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isType) {
                    if (s.length() > 3) {
                        Intent intent = new Intent(RecipientDynamicActivity.this, LocationPickerActivity.class);
                        intent.putExtra("text", s.toString());
                        intent.putExtra("code", "NP");
                        startActivityForResult(intent, 1001);
                        hideKeyboard(RecipientDynamicActivity.this);
                        isType = false;
                    }
                }
            }
        });

//        recipient_address.getEditText().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        tv_currency_from.setText(DestinationSymbol);
        showImage(FlagImageDestination, from_currency_image);
    }

    private void init(JSONArray dataAr) {
        dynamicForm = dataAr;
        dynamicFieldAr.clear();
        DynamicFormAdapter mAdapter = new DynamicFormAdapter(dataAr, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lldynamiclayout.setLayoutManager(mLayoutManager);
        lldynamiclayout.setItemAnimator(new DefaultItemAnimator());
        lldynamiclayout.setAdapter(mAdapter);

        selectcurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(destinationListofSymble!=null)
                {
                    //if(!callFrom.equalsIgnoreCase("sendFrg"))
                    {
                        showListingPopup("Select Currency", destinationListofSymble, from_currency_image, tv_currency_from);
                    }
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

            if (currencyAray.length() > 0) {
                RecyclerView currencyRecycler = listingDialog.findViewById(R.id.currencyRecycler);
                CurrencyAdapter mAdapter = new CurrencyAdapter(currencyAray, currencyTitle, RecipientDynamicActivity.this, currencyImg, currencySymble);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                currencyRecycler.setLayoutManager(mLayoutManager);
                currencyRecycler.setItemAnimator(new DefaultItemAnimator());
                currencyRecycler.setAdapter(mAdapter);
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

            if (obj != null) {
                SDCountryId = obj.getString("SDCountryId").trim();
                CountryId = obj.getString("CountryId").trim();
                Map<String, String> m = new LinkedHashMap<>();
                m.put("SourceSymbol", SourceSymbol);
                m.put("DestinationSymbol", tv_currency_from.getText().toString());
                m.put("Type", rec_type);

                MRequestRecipientForm(m);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MRequestRecipientForm(Map<String, String> m) {
        MrequestBankDropDown(new HashMap<>(), m.get("DestinationSymbol"));

        new ServerHandler().sendToServer(this, "RecipientForm", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status")) {
                        recipientBankDetailDynaicAr = obj.getJSONArray("RecipientFormList");
                        init(recipientBankDetailDynaicAr);
                        destinationListofSymble = obj.getJSONArray("DestinationList");
                        getState(new HashMap<>(), destinationListofSymble.getJSONObject(0).getString("CountryCode"));


                    } else {
                        showImage(SourceSymbol, from_currency_image);
                        tv_currency_from.setText(DestinationSymbol);
                        showtoast.showToast(RecipientDynamicActivity.this, "Response", obj.getString("Message"), RRtoplayout_recipient);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void MrequestBankDropDown(Map<String, String> m, String destinationSymbol) {
        new ServerHandler().sendToServer(this, "BankDropDown?Symbol=" + destinationSymbol, m, 0, 0, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONArray dataAr = new JSONArray(dta);
                    ArrayList<String> bankNamesAr = new ArrayList<String>();
                    for (int x = 0; x < dataAr.length(); x++) {
                        bankNamesAr.add(dataAr.getJSONObject(x).getString("BankName"));
                    }
                    if (dataAr.length() > 0) {
                        bankCode = dataAr.getJSONObject(0).getString("BankCode");
                        setSpinnerData(bankNamesAr, dataAr);
                    }
                    //  getState(new HashMap<>());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getState(Map<String, String> m, String symbol) {
        new ServerHandler().sendToServer(this, "State?CountryCode=" + symbol, m, 0, 0, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONArray dataAr = new JSONArray(dta);
                    ArrayList<String> stateNameAr = new ArrayList<String>();
                    for (int x = 0; x < dataAr.length(); x++) {
                        System.out.println("State name===" + dataAr.getJSONObject(x).getString("StateName"));
                        stateNameAr.add(dataAr.getJSONObject(x).getString("StateName"));
                    }
                    recipient_stateStr = dataAr.getJSONObject(0).getString("StateName");
                    recipient_statecodeStr = dataAr.getJSONObject(0).getString("StateCode");
                    setState(stateNameAr, dataAr);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setSpinnerData(ArrayList<String> bankName, JSONArray dataAr) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (RecipientDynamicActivity.this, R.layout.spinner_item, bankName);

        spinner_bank_name.setAdapter(adapter);
        spinner_bank_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    bankCode = dataAr.getJSONObject(position).getString("BankCode");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setState(ArrayList<String> stateAr, JSONArray dataAr) {
        recipient_state.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (RecipientDynamicActivity.this, R.layout.spinner_item, stateAr);

        recipient_state.setAdapter(adapter);

        recipient_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    recipient_stateStr = dataAr.getJSONObject(position).getString("StateName");
                    recipient_statecodeStr = dataAr.getJSONObject(position).getString("StateCode");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void showImage(String path, final ImageView profileimage) {

        if (path.length() > 0) {
            Picasso.with(RecipientDynamicActivity.this)
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


    private void addRecipient() {
        Map<String, String> mainObj = new LinkedHashMap<>();
        String recipient_nameStr = recipient_name.getEditText().getText().toString();
        String recipient_lastnameStr = recipient_lastname.getEditText().getText().toString();
        String recipient_emailStr = recipient_email.getEditText().getText().toString();
        String recipient_cityStr = recipient_city.getEditText().getText().toString();
        String recipient_zipcodeStr = recipient_zipcode.getEditText().getText().toString();
        String recipient_addressStr = recipient_address.getEditText().toString();
        String tv_currency_fromStr = tv_currency_from.getText().toString();

        recipient_name.setError("");
        recipient_lastname.setError("");
        recipient_email.setError("");
        recipient_city.setError("");
        recipient_zipcode.setError("");
        recipient_address.setError("");
        relation_of_benificary.setError("");
        recipient_buisnessname.setError("");
        recipient_lastname_joint.setError("");
        recipient_name_joint.setError("");
        select_date.setError("");

        String dateOfBirth=select_date.getEditText().getText().toString();
        if(dateOfBirth.length()>0)
        {
            String [] ar=dateOfBirth.split("/");
            String day=ar[0];
            String month=ar[1];
            String year=ar[2];

        }
        else
        {
            dateOfBirth=select_date.getEditText().getText().toString();

        }

        if (recipient_nameStr.length() == 0) {
            recipient_name.setError("Enter first name and middle name of the account holder");
            return;
        }
        if (recipient_lastnameStr.length() == 0) {
            recipient_lastname.setError("Enter last Name of the account holder");
            return;
        }

        if (recipient_name_joint.getVisibility() == View.VISIBLE) {
            if (recipient_name_joint.getEditText().getText().toString().length() == 0) {
                recipient_name_joint.setError("Enter first name and middle name of the joint account holder");
                return;
            }
        }
        if (recipient_lastname_joint.getVisibility() == View.VISIBLE) {
            if (recipient_lastname_joint.getEditText().getText().toString().length() == 0) {
                recipient_lastname_joint.setError("Enter last Name of the joint account holder");
                return;
            }
        }


        if (recipient_buisnessname.getVisibility() == View.VISIBLE) {

            if (recipient_buisnessname.getEditText().getText().toString().length() == 0) {
                recipient_buisnessname.setError("Enter receipient buisness name");
                return;
            }
        }

        if (recipient_email.getVisibility() == View.VISIBLE) {
            if (recipient_email.getEditText().getText().toString().length() == 0) {
                recipient_email.setError("Enter Email address");
                return;
            }

            if (!new Validation(RecipientDynamicActivity.this).checkEmail(recipient_emailStr)) {
                recipient_email.setError("Enter valid email address");
                return;
            }
        }


        if (recipient_name_joint.getVisibility() == View.VISIBLE) {
            if (recipient_name_joint.getEditText().getText().toString().length() == 0) {
                recipient_lastname.setError("Enter first name and middle name of the account holder");
                return;
            }

        }
        if (recipient_lastname_joint.getVisibility() == View.VISIBLE) {
            if (recipient_lastname_joint.getEditText().getText().toString().length() == 0) {
                recipient_lastname.setError("Enter last Name of the joint account holder");
                return;
            }
        }


        if (ed_mobilenumber.getVisibility() == View.VISIBLE) {
            if (ed_mobilenumber.getText().toString().length() == 0) {
                ed_mobilenumber.setError("Please enter mobile number");
                return;
            } else if (ed_mobilenumber.getText().toString().length() < 8) {
                ed_mobilenumber.setError("Please enter valid mobile number");
                showtoast.showToast(RecipientDynamicActivity.this, "Response", "Please enter valid mobile number.", RRtoplayout_recipient);
                return;
            }
        }
        if (recipient_address.getVisibility() == View.VISIBLE) {
            if (recipient_address.getEditText().getText().length() == 0) {
                recipient_address.setError("Enter your Address");
                return;
            }
        }

        countryMobilecode = countryCodePicker.getSelectedCountryCode();
        if (recipient_city.getVisibility() == View.VISIBLE) {
            if (recipient_cityStr.length() == 0) {
                recipient_city.setError("Please Enter City");
                return;
            }
        }
//        if(recipient_zipcode.getVisibility()==View.VISIBLE)
//        {
//            if (recipient_zipcode.getEditText().getText().length() == 0) {
//                recipient_zipcode.setError("Please enter Zipcode");
//                return;
//            }
//        }





        if (relation_of_benificary.getVisibility() == View.VISIBLE) {
            if (relation_of_benificary.getEditText().getText().toString().length() == 0) {
                relation_of_benificary.setError("Enter beneficiary relation");
                return;
            }
        }


        JSONArray uploadBankDataAr = new JSONArray();
        int isError = 0;
        for (int x = 0; x < dynamicFieldAr.size(); x++) {
            JSONObject uploadToServerObj = new JSONObject();
            if (dynamicFieldAr.get(x) instanceof TextInputLayout) {
                try {

                    TextInputLayout tip = (TextInputLayout) dynamicFieldAr.get(x);
                    tip.setError("");
                    JSONObject obj = recipientBankDetailDynaicAr.getJSONObject(x);
                    if (tip.getEditText().getText().toString().length() == 0) {
                        isError = 1;
                        tip.setError("Enter " + obj.getString("Title"));
                        break;
                    } else {
                        uploadToServerObj.put("Id", obj.getString("Id"));
                        uploadToServerObj.put("FieldType", obj.getString("FieldType"));
                        uploadToServerObj.put("Type", obj.getString("Type"));
                        uploadToServerObj.put("Answer", tip.getEditText().getText().toString());
                        uploadToServerObj.put("RcId", obj.getString("ReciptentFormId"));
                        uploadToServerObj.put("TwType", obj.getString("TwType"));
                        uploadToServerObj.put("UniqueId", obj.getString("UniqueId"));
                        uploadBankDataAr.put(uploadToServerObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        if (isError == 0) {
            try {
                if (tv_currency_fromStr.trim().equalsIgnoreCase(DestinationSymbol)) {
                    mainObj.put("SourceSymbol", SourceSymbol);
                } else {
                    mainObj.put("SourceSymbol", DestinationSymbol);
                }


                mainObj.put("BankCode", bankCode);
                mainObj.put("JointAccountName", recipient_name_joint.getEditText().getText().toString());
                mainObj.put("JointAccountLastName", recipient_lastname_joint.getEditText().getText().toString());
                mainObj.put("JointAccountLastName", recipient_lastname_joint.getEditText().getText().toString());
                mainObj.put("Relation", relation_of_benificary.getEditText().getText().toString());
                mainObj.put("BusinessName", recipient_buisnessname.getEditText().getText().toString());

                mainObj.put("DestinationSymbol", tv_currency_fromStr.trim());
                mainObj.put("DOB", dateOfBirth);
                mainObj.put("ContactNumber", countryMobilecode + "" + ed_mobilenumber.getText().toString());
                mainObj.put("Type", rec_type);
                mainObj.put("Address", recipient_address.getEditText().getText().toString());
                mainObj.put("State", recipient_stateStr);
                mainObj.put("StateCode", recipient_statecodeStr);
                mainObj.put("City", recipient_cityStr);
                mainObj.put("ZipCode", recipient_zipcodeStr);
                mainObj.put("Email", recipient_emailStr);
                mainObj.put("Name", recipient_nameStr);
                mainObj.put("LastName", recipient_lastnameStr);

                mainObj.put("MemberId", UtilClass.getUserData(RecipientDynamicActivity.this).getString("MemberId"));

                mainObj.put("CountryId", CountryId);
                mainObj.put("SDCountryId", SDCountryId);
                mainObj.put("Id", "0");
                mainObj.put("BankData", uploadBankDataAr.toString());
                System.out.println("Before to send===" + mainObj);


                new ServerHandler().sendToServer(this, "AddRecipient", mainObj, 0, 1, new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons) {
                        try {
                            JSONObject obj = new JSONObject(dta);
                            if (obj.getBoolean("status")) {
                                showtoast.showToast(RecipientDynamicActivity.this, "Response", obj.getString("Message"), RRtoplayout_recipient);


                                if (getIntent().hasExtra("data"))//call by send money
                                {
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("result", "");
                                    setResult(RESULT_OK, resultIntent);
                                    finish();

                                } else {
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("result", "");
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }


                            } else {

                                showtoast.showToast(RecipientDynamicActivity.this, "Response", obj.getString("Message"), RRtoplayout_recipient);
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

    }


    private int screenHeight;

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {

                isType = false;
                recipient_address.getEditText().setText(data.getStringExtra("sourcename"));
                hideKeyboard(RecipientDynamicActivity.this);
                if(Double.parseDouble(data.getStringExtra("lat"))>0)
                {
                    getLocationNAme(Double.parseDouble(data.getStringExtra("lat")), Double.parseDouble(data.getStringExtra("lng")));
                }
                recipient_address.setError("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getLocationNAme(double lat, double lng) {
        try {
            Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = mGeocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0)
            {
                recipient_city.getEditText().setText(addresses.get(0).getLocality());
                recipient_zipcode.getEditText().setText(addresses.get(0).getPostalCode());
//                ed_statename.getEditText().setText(addresses.get(0).getAdminArea());


            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setAccountName() {
        try {
            if (dynamicFieldAr.size() > 0) {

                String name = recipient_name.getEditText().getText() + " " + recipient_lastname.getEditText().getText().toString();
                for (int x = 0; x < dynamicForm.length(); x++) {

                    if (dynamicForm.getJSONObject(x).getString("TwType").equalsIgnoreCase("accountName")) {
                        TextInputLayout textInputLayout = (TextInputLayout) dynamicFieldAr.get(x);
                        textInputLayout.getEditText().setText("");
                        textInputLayout.getEditText().setText(name);
                        textInputLayout.getEditText().setEnabled(false);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



