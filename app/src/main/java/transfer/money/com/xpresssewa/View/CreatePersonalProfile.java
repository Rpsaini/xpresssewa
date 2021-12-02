package transfer.money.com.xpresssewa.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.annotations.Nullable;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.placepicker.LocationPickerActivity;
import transfer.money.com.xpresssewa.registration.MobileNumberActivity;
import transfer.money.com.xpresssewa.registration.SignUpActivity;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class CreatePersonalProfile extends AppCompatActivity {
    //et_business_name,et_company_role,et_registration_number,et_website
    private TextInputLayout et_user_name, et_user_lname, et_business_address, et_city, et_postcode, et_other_occupation, et_mobile;
    private TextView company_continue;
    //et_dob
    private Spinner spinner_state, occupation_spinner;
    private Showtoast showtoast;
    private LinearLayout ll_main_layout;
    private ArrayList<String> OccupationListAr = new ArrayList<>();
    private TextView txt_date;
    private CountryCodePicker cpp;
    private String countryCode = "+91";
    private RadioButton rr_female, rr_Male, rr_other;
    private String gender = "1";
    private JSONArray OccupationList;
    private TextView txt_updateNumber;
    boolean isType=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_personal_profile);


        UtilClass.getUserData(this);


        et_user_name = findViewById(R.id.et_user_name);
        et_user_lname = findViewById(R.id.et_user_lname);
        et_business_address = findViewById(R.id.et_business_address);
        et_city = findViewById(R.id.et_city);
        et_postcode = findViewById(R.id.et_postcode);
        company_continue = findViewById(R.id.company_continue);
        ll_main_layout = findViewById(R.id.ll_main_layout);
        spinner_state = findViewById(R.id.spinner_state);
        et_mobile = findViewById(R.id.et_mobile);
        occupation_spinner = findViewById(R.id.occupation_spinner);
        txt_date = findViewById(R.id.txt_date);
        rr_Male = findViewById(R.id.rr_Male);
        rr_female = findViewById(R.id.rr_female);
        rr_other = findViewById(R.id.rr_other);
        showtoast = new Showtoast();
        cpp = (CountryCodePicker) findViewById(R.id.ccp);
        txt_updateNumber =  findViewById(R.id.txt_updateNumber);
        et_other_occupation =  findViewById(R.id.et_other_occupation);


        init();
        saveInformation();
        selectDate();
        selectAddress();

    }

    private void init() {
        try {
            String isKycApproved = new SaveImpPrefrences().reterivePrefrence(this, DefaultConstatnts.IsKycApproved).toString();
            System.out.println("is kyc approved===" + isKycApproved);

            if (!isKycApproved.equalsIgnoreCase("3"))//approved
            {
                company_continue.setText("CONTINUE");

            } else {
                company_continue.setText("UPDATE");
                et_user_name.setEnabled(false);
                et_user_lname.setEnabled(false);
                rr_Male.setEnabled(false);
                rr_female.setEnabled(false);
                rr_other.setEnabled(false);
                txt_date.setEnabled(false);
            }


            if (getIntent().hasExtra("userdata")) {
                String data = getIntent().getStringExtra("userdata");
                JSONObject dataObj = new JSONObject(data);

                et_user_name.getEditText().setText(dataObj.getString("FirstName"));
                et_user_lname.getEditText().setText(dataObj.getString("LastName"));

                et_business_address.getEditText().setText(dataObj.getString("Address"));
                et_city.getEditText().setText(dataObj.getString("City"));
                et_postcode.getEditText().setText(dataObj.getString("PostalCode"));
             // et_mobile.setText(dataObj.getString("PhoneExt"));
                et_mobile.getEditText().setText(dataObj.getString("Phone"));
                txt_date.setText(dataObj.getString("DateOfBirth"));
                String phnExt = dataObj.getString("PhoneExt");


                if (!phnExt.isEmpty()) {
                    int code = Integer.parseInt(phnExt.replace("+", ""));
                    cpp.setCountryForPhoneCode(code);
                } else {
                    cpp.setCountryForPhoneCode(Integer.parseInt(countryCode.replace("+", "")));
                }
                countryCode = phnExt + "";
                if(dataObj.has("Gender"))
                {
                    gender = dataObj.getString("Gender");
                    if(gender.equalsIgnoreCase("0"))
                    {
                        gender="1";
                    }
                }
                recipient_statecodeStr=dataObj.getString("StateCode");
                CreateBuisnessProfile.profileData = new LinkedHashMap<>();
                CreateBuisnessProfile.profileData.put("MemberId", dataObj.getString("MemberId"));
                CreateBuisnessProfile.profileData.put("FirstName", dataObj.getString("FirstName"));
                CreateBuisnessProfile.profileData.put("Gender", gender);
                CreateBuisnessProfile.profileData.put("StateCode", recipient_statecodeStr);
                CreateBuisnessProfile.profileData.put("LastName", dataObj.getString("LastName"));
                CreateBuisnessProfile.profileData.put("DateOfBirth", dataObj.getString("DateOfBirth"));
                CreateBuisnessProfile.profileData.put("PhoneExt", dataObj.getString("PhoneExt"));
                CreateBuisnessProfile.profileData.put("Phone", dataObj.getString("Phone"));
                CreateBuisnessProfile.profileData.put("CountryId", dataObj.getString("CountryId"));
                CreateBuisnessProfile.profileData.put("Address", dataObj.getString("Address"));
                CreateBuisnessProfile.profileData.put("City", dataObj.getString("City"));
                CreateBuisnessProfile.profileData.put("PostalCode", dataObj.getString("PostalCode"));
                CreateBuisnessProfile.profileData.put("OcuupationId", dataObj.getString("OcuupationId"));
                CreateBuisnessProfile.profileData.put("OtherOcuupation", dataObj.getString("OtherOcuupation"));
                CreateBuisnessProfile.profileData.put("BusinessCountryId", dataObj.getString("BusinessCountryId"));
                CreateBuisnessProfile.profileData.put("BusinessName", dataObj.getString("BusinessName"));
                CreateBuisnessProfile.profileData.put("CompanyTypeId", dataObj.getString("CompanyTypeId"));
                CreateBuisnessProfile.profileData.put("CompanyRoleId", dataObj.getString("CompanyRoleId"));
                CreateBuisnessProfile.profileData.put("RegistrationNumber", dataObj.getString("RegistrationNumber"));
                CreateBuisnessProfile.profileData.put("WebsiteName", dataObj.getString("WebsiteName"));
                CreateBuisnessProfile.profileData.put("BusinessCategoryId", dataObj.getString("BusinessCategoryId"));
                CreateBuisnessProfile.profileData.put("BusinessSubCategoryId", dataObj.getString("BusinessSubCategoryId"));
                CreateBuisnessProfile.profileData.put("BusinessAddress", dataObj.getString("BusinessAddress"));
                CreateBuisnessProfile.profileData.put("BusinessCity", dataObj.getString("BusinessCity"));
                CreateBuisnessProfile.profileData.put("BusinessState", dataObj.getString("BusinessState"));
                CreateBuisnessProfile.profileData.put("BusinessPostalCode", dataObj.getString("BusinessPostalCode"));
                CreateBuisnessProfile.profileData.put("State", dataObj.getString("State"));
                CreateBuisnessProfile.profileData.put("Type", dataObj.getString("Type"));




                getState(new HashMap<>());
                setOccupation(dataObj.getString("OcuupationId"),dataObj.getJSONArray("OccupationList"));

                OccupationListAr.add("Select Occupation");
                OccupationList = dataObj.getJSONArray("OccupationList");
                for (int x = 0; x < OccupationList.length(); x++)
                {
                    OccupationListAr.add(OccupationList.getJSONObject(x).getString("Title") + "");

                }


                ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, OccupationListAr);
                occupation_spinner.setAdapter(occupationAdapter);
                occupation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            JSONObject occupationObj = OccupationList.getJSONObject(position-1);
                            CreateBuisnessProfile.profileData.put("OcuupationId", occupationObj.getString("Id"));
                            if(occupationObj.getString("Id").equalsIgnoreCase("7"))
                            {
                                et_other_occupation.setVisibility(View.VISIBLE);
                                et_other_occupation.getEditText().setText(dataObj.getString("OtherOcuupation"));
                            }
                            else
                            {
                                et_other_occupation.setVisibility(View.GONE);

                                et_other_occupation.getEditText().setText("");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                papulateOcupationSpinner(dataObj.getString("OcuupationId"), OccupationList, occupation_spinner);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = cpp.getSelectedCountryCode() + "";
                cpp.getSelectedCountryName();

            }
        });

        if (gender.equalsIgnoreCase("1")) {
            rr_Male.setChecked(true);
            CompoundButtonCompat.setButtonTintList(rr_Male, ColorStateList.valueOf(getResources().getColor(R.color.blue_bt_color)));


        } else if (gender.equalsIgnoreCase("2")) {
            rr_female.setChecked(true);
            CompoundButtonCompat.setButtonTintList(rr_female, ColorStateList.valueOf(getResources().getColor(R.color.blue_bt_color)));
        } else if (gender.equalsIgnoreCase("3")) {
            rr_other.setChecked(true);
            CompoundButtonCompat.setButtonTintList(rr_other, ColorStateList.valueOf(getResources().getColor(R.color.blue_bt_color)));
        } else {
            rr_Male.setChecked(true);
            CompoundButtonCompat.setButtonTintList(rr_Male, ColorStateList.valueOf(getResources().getColor(R.color.blue_bt_color)));
        }

        rr_Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_female.setChecked(false);
                rr_other.setChecked(false);
                gender = "1";
            }
        });

        rr_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_Male.setChecked(false);
                rr_other.setChecked(false);
                gender = "2";
            }
        });

        rr_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_Male.setChecked(false);
                rr_female.setChecked(false);
                gender = "3";
            }
        });

        txt_updateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreatePersonalProfile.this, MobileNumberActivity.class);
                intent.putExtra("memberId",UtilClass.member_id);
                intent.putExtra("callfrom","profile");
                startActivityForResult(intent,1002);
            }
        });


    }

    private void saveInformation() {

        company_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_user_name.setError("");
                et_user_lname.setError("");
                et_business_address.setError("");
//              et_dob.setError("");
                et_city.setError("");
                et_postcode.setError("");
                et_mobile.setError("");


                if (et_user_name.getEditText().getText().toString().length() == 0) {
                    et_user_name.setError("Enter First name ");

                    return;
                }

                if (et_user_lname.getEditText().getText().toString().length() == 0) {
                    et_user_lname.setError("Enter Lastname");

                    return;
                }

                if (!rr_Male.isChecked() && !rr_female.isChecked() && !rr_other.isChecked())
                {
                    showtoast.showToast(CreatePersonalProfile.this, "Required", "Select your Gender", ll_main_layout);
                    return;
                }

                if (txt_date.getText().toString().length() == 0) {
                    showtoast.showToast(CreatePersonalProfile.this, "Required", "Enter DOB", ll_main_layout);
                }
                if (et_postcode.getEditText().getText().toString().length() == 0) {
                    et_postcode.setError("Enter Postal code");
                    return;
                }
                if (et_mobile.getEditText().getText().toString().length() == 0)
                {
                    et_mobile.setError("Enter Phone Number");
                    return;
                }

                if (et_mobile.getEditText().getText().toString().length()<8)
                {
                    et_mobile.setError("Enter valid phone number");
                    new Showtoast().showToast(CreatePersonalProfile.this,getResources().getString(R.string.app_name),"Enter valid phone number",ll_main_layout);
                    return;
                }

                if (et_business_address.getEditText().getText().toString().length() == 0) {
                    et_business_address.setError("Enter buisness address");
                    return;
                }


                if (et_city.getEditText().getText().toString().length() == 0) {
                    et_city.setError("Enter City name");

                    return;
                }




                if (occupation_spinner.getSelectedItemPosition() == 0) {
                    showtoast.showToast(CreatePersonalProfile.this, "Error", "Select Occupation", ll_main_layout);
                return;
                }

                if(et_other_occupation.getVisibility()==View.VISIBLE)
                {
                    if(et_other_occupation.getEditText().getText().toString().length()==0)
                    {
                        et_other_occupation.setError("Enter your occupation name");
                        return;
                    }
                    else {
                        CreateBuisnessProfile.profileData.put("OtherOcuupation", et_other_occupation.getEditText().getText().toString());
                    }



                }



                CreateBuisnessProfile.profileData.put("FirstName", et_user_name.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("LastName", et_user_lname.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("Gender", gender);
                CreateBuisnessProfile.profileData.put("Address", et_business_address.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("City", et_city.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("PostalCode", et_postcode.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("PhoneExt", countryCode);
                CreateBuisnessProfile.profileData.put("Phone", et_mobile.getEditText().getText() + "");
                CreateBuisnessProfile.profileData.put("DateOfBirth", txt_date.getText().toString());
                CreateBuisnessProfile.profileData.put("StateCode", recipient_statecodeStr);
                CreateBuisnessProfile.profileData.put("State", recipient_stateStr);





                new ServerHandler().sendToServer(CreatePersonalProfile.this, "UpdateUser", CreateBuisnessProfile.profileData, 0, 1, new CallBack() {
                    @Override
                    public void getRespone(String dta, ArrayList<Object> respons) {

                        try {
                            JSONObject obj = new JSONObject(dta);
                            if (obj.getBoolean("status")) {

                                String isKycApproved = new SaveImpPrefrences().reterivePrefrence(CreatePersonalProfile.this, DefaultConstatnts.IsKycApproved).toString();
                                System.out.println("Kyc status===" + isKycApproved);

                                SimpleDialog simpleDialog = new SimpleDialog();
                                final Dialog confirmDialog = simpleDialog.simpleDailog(CreatePersonalProfile.this, R.layout.confirmation_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
                                ImageView selected_Curreny = confirmDialog.findViewById(R.id.selected_Curreny);
                                TextView txt_currency_name = confirmDialog.findViewById(R.id.txt_currency_name);
                                TextView txt_no = confirmDialog.findViewById(R.id.txt_no);
                                txt_no.setVisibility(View.GONE);
                                TextView txt_yes = confirmDialog.findViewById(R.id.txt_yes);
                                TextView txt_msg = confirmDialog.findViewById(R.id.txt_msg);
                                selected_Curreny.setImageResource(R.drawable.checked);


                                if (isKycApproved.trim().equalsIgnoreCase("3") || isKycApproved.trim().equalsIgnoreCase("1")) {
                                    txt_currency_name.setText("Profile Updated");
                                    txt_msg.setText("Your profile has been successfully updated.");
                                    txt_yes.setText("OK");

                                    txt_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            confirmDialog.dismiss();
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("result", "");
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }
                                    });
                                } else if (DefaultConstatnts.isKyDockUploaded) {
                                    txt_currency_name.setText("Profile Updated");
                                    txt_msg.setText("Your profile has been successfully updated.");
                                    txt_yes.setText("OK");

                                    txt_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            confirmDialog.dismiss();
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("result", "");
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }
                                    });


                                } else {

                                    txt_currency_name.setText("Profile Updated");
                                    txt_msg.setText("Your profile has been successfully updated. Please complete your KYC");
                                    txt_yes.setText("OK");

                                    txt_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            confirmDialog.dismiss();
                                            Intent intent = new Intent(CreatePersonalProfile.this, SelectIdentityType.class);
                                            startActivityForResult(intent, 101);
                                        }
                                    });

                                }

                            } else {
                                showtoast.showToast(CreatePersonalProfile.this, "Error", obj.getString("Message"), ll_main_layout);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }





    private void clearFields() {

        et_business_address.getEditText().setText("");
        txt_date.setText("");
        et_city.getEditText().setText("");
        et_postcode.getEditText().setText("");
        et_mobile.getEditText().setText("");

    }


    private void papulateOcupationSpinner(String id, JSONArray array, Spinner spinner) {
        try {
            for (int x = 0; x < array.length(); x++) {
                JSONObject occupationObj = array.getJSONObject(x);
                if (id.equalsIgnoreCase(occupationObj.getString("Id"))) {
                    spinner.setSelection(x + 1);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    DatePickerDialog mDatePicker;
    private void selectDate() {
        txt_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker;
                Calendar  mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                mDatePicker = new DatePickerDialog(CreatePersonalProfile.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        txt_date.setText((selectedMonth + 1)+ "/" +selectedDay + "/" +selectedYear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(mCurrentDate.getTimeInMillis());
                mDatePicker.show();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Databac----"+requestCode+"==="+resultCode);

        if (requestCode == 101) {
            finish();
        }
        else if(requestCode==1002)//update phone
        {
            if(data!=null)
            {
                String Phone=data.getStringExtra("Phone");
                String PhoneExt= data.getStringExtra("PhoneExt");
                cpp.setCountryForPhoneCode(Integer.parseInt(PhoneExt));
                et_mobile.getEditText().setText(Phone);

            }
        }
        else if(requestCode==1003)//select address
        {
            isType=false;
            et_business_address.getEditText().setText(data.getStringExtra("sourcename"));
            hideKeyboard(CreatePersonalProfile.this);

            String locName= getLocationNAme(Double.parseDouble(data.getStringExtra("lat")),Double.parseDouble(data.getStringExtra("lng")));
            System.out.println("Location name---"+locName);

        }
    }

    private void selectAddress()
    {
        et_business_address.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isType=true;
                return false;
            }
        });

        et_business_address.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isType) {
                    if (s.length() > 3) {
                        Intent intent = new Intent(CreatePersonalProfile.this, LocationPickerActivity.class);
                        intent.putExtra("text", s.toString());
                        intent.putExtra("code", "AUS");
                        startActivityForResult(intent, 1003);

                    }
                }
            }
        });
    }

    JSONArray stateAr;
    private void getState(Map<String, String> m) {

        new ServerHandler().sendToServer(this, "State?CountryCode=" + UtilClass.CountryCode, m, 0, 0, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                     stateAr = new JSONArray(dta);
                    ArrayList<String> stateNameAr = new ArrayList<String>();
                    for (int x = 0; x < stateAr.length(); x++) {
                        stateNameAr.add(stateAr.getJSONObject(x).getString("StateName"));
                    }
                    recipient_stateStr = stateAr.getJSONObject(0).getString("StateName");
                    recipient_statecodeStr = stateAr.getJSONObject(0).getString("StateCode");

                    setState(stateNameAr, stateAr);
                    setSpinnerValue( CreateBuisnessProfile.profileData.get("StateCode"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String recipient_stateStr = "", recipient_statecodeStr = "";


    private void setState(ArrayList<String> stateAr, JSONArray dataAr) {

        ArrayAdapter<String> spinnerStateAdapter = new ArrayAdapter<String>
                        (this, R.layout.spinner_item, stateAr);
                spinner_state.setAdapter(spinnerStateAdapter);

          spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void setSpinnerValue(String code)
    {
        try
        {
            for (int x = 0; x < stateAr.length(); x++)
            {
                JSONObject data = stateAr.getJSONObject(x);
                String StateCode = data.getString("StateCode");
                System.out.println("State is==="+code+"==?=="+StateCode);
                if (StateCode.equalsIgnoreCase(code)) {
                    spinner_state.setSelection(x);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setOccupation(String Id,JSONArray occupationList)
    {
        try
        {

            for (int x = 0; x < occupationList.length(); x++) {
                JSONObject data = occupationList.getJSONObject(x);
                String occupationId = data.getString("Id");
                System.out.println("occupation is==="+Id+"==="+occupationId);
                if (occupationId == Id)
                {
                    occupation_spinner.setSelection(x);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String getLocationNAme(double lat,double lng)
    {
        try {
            Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = mGeocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && addresses.size() > 0)
            {
                et_city.getEditText().setText(addresses.get(0).getLocality());
                et_postcode.getEditText().setText(addresses.get(0).getPostalCode());

                }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

}

