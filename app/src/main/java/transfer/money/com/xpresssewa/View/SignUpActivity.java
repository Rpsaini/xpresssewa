package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.Intent;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.os.Bundle;

import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
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
import transfer.money.com.xpresssewa.Adapter.CountryRecyclerviewAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.AnimationForView;
import transfer.money.com.xpresssewa.util.IsAnimationEndedCallback;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.validation.Showtoast;
import transfer.money.com.xpresssewa.validation.Validation;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;

    @BindView(R.id.tv_sign_up)
    TextView tv_sign_up;

    private Showtoast showtoast;

    @BindView(R.id.email_layout_name)
    TextInputLayout email_layout_name;

    @BindView(R.id.pwd_layout_name)
    TextInputLayout pwd_layout_name;

    @BindView(R.id.profilespinner)
    Spinner profilespinner;

    @BindView(R.id.RRsignuptoplayout)
    RelativeLayout RRsignuptoplayout;

    @BindView(R.id.button_profile)
    ImageView button_profile;

    @BindView(R.id.profiletype)
    TextView profiletype;


    @BindView(R.id.ll_selectCountry)
    LinearLayout ll_selectCountry;

    @BindView(R.id.txt_selectcountry)
    TextView txt_selectcountry;

    @BindView(R.id.txt_terms)
    TextView txt_terms;


    @BindView(R.id.et_referral)
    EditText et_referral;

    private String isBuisness="1";
    Validation vd;

    private JSONArray countryAr=new JSONArray();

    AnimationForView animationForViews = new AnimationForView();
    private String countryName="",countryID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,  "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        getScreenHeight();
        showtoast=new Showtoast();
        vd=new Validation(this);
        init();
    }

    private void init() {
        tv_sign_up.setOnClickListener(this);
        tv_sign_in.setOnClickListener(this);



        Typeface face=Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf");
        tv_sign_in.setTypeface(face);

        getCountryList();

        ImageView headerImageview = findViewById(R.id.headerbackbutton);
        headerImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ll_selectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCountryList();
            }
        });


        email_layout_name.clearFocus();
        pwd_layout_name.clearFocus();


        button_profile.setTag(0 + "");
        isBuisness = "1";
        profiletype.setText("Personal Profile");
        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profile = v.getTag() + "";
                if (profile.equalsIgnoreCase("0")) {
                    isBuisness = "2";
                    button_profile.setImageResource(R.drawable.button_on);
                    button_profile.setTag("1");
                    profiletype.setText("Business Profile");
                } else {
                    isBuisness = "1";
                    button_profile.setImageResource(R.drawable.button);
                    button_profile.setTag("0");
                    profiletype.setText("Personal Profile");
                }

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txt_terms.setText(Html.fromHtml("By continuing you accept our <font color='#226ED4'> Terms of use</font> and <font color='#226ED4'> Privacy Policy</font>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            txt_terms.setText(Html.fromHtml("By continuing you accept our <font color='#226ED4'> Terms of use</font> and <font color='#226ED4'> Privacy Policy</font>"));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_sign_in:
                Intent call_sign_in=new Intent(this,SignInActivity.class);
                startActivity(call_sign_in);
                break;
            case  R.id.  tv_sign_up:
                register();
                break;
        }
    }



    private void register() {

        email_layout_name.setError("");
        pwd_layout_name.setError("");
        if (email_layout_name.getEditText().getText().toString().length() == 0) {
            email_layout_name.setError("Enter email address");
        }
        else if(!vd.checkEmail(email_layout_name.getEditText().getText().toString()))
        {
            email_layout_name.setError("Enter valid email address");
        }

        else if (pwd_layout_name.getEditText().getText().toString().length() == 0)
           {
            pwd_layout_name.setError("Enter password");
           }
        else if (countryName.length() == 0)
        {

            showtoast.showToast(SignUpActivity.this,"Select","Select Country",RRsignuptoplayout);
//            pwd_layout_name.setError("");
        }


        else
           {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("Email", email_layout_name.getEditText().getText().toString());
            m.put("Password", pwd_layout_name.getEditText().getText().toString());
            m.put("Type", isBuisness);
            m.put("RefCode", et_referral.getText().toString());
            m.put("CountryName", countryName);
            m.put("CountryId", countryID);



            new ServerHandler().sendToServer(this, "register", m, 0,1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        JSONObject obj = new JSONObject(dta);
                        if (obj.getBoolean("status"))
                        {
                            soSucessDialog();
                        }
                        else {
                            showtoast.showToast(SignUpActivity.this,"Registration", obj.getString("Message"),RRsignuptoplayout);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });


        }

    }

//    {
//        IsPhoneVerified = 1;
//        MemberId = 10033;
//        Message = success;
//        OTP = 170741;
//        RefCode = ok1ma10033;
//        ReponseCode = 1;
//        UserId = ok1ma10033;
//        Version = 1;
//        status = 1;
//    }
   private Dialog isPendingAmount;

    private void soSucessDialog()
    {
        if (isPendingAmount != null && isPendingAmount.isShowing()) {
            isPendingAmount.dismiss();
        }
        isPendingAmount = new Dialog(SignUpActivity.this);
        isPendingAmount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        isPendingAmount.setContentView(R.layout.dialog_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = isPendingAmount.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        isPendingAmount.setCancelable(true);
       // isPendingAmount.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        isPendingAmount.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        isPendingAmount.getWindow().setDimAmount(0);
        isPendingAmount.show();


        TextView okaybtn = isPendingAmount.findViewById(R.id.okaybtn);

        okaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPendingAmount.dismiss();
                finish();

            }
        });
    }

    private void getCountryList()
    {
        Map<String, String> m = new LinkedHashMap<>();
        new ServerHandler().sendToServer(this, "Country", m, 0,0, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {

                    countryAr = new JSONArray(dta);
                     } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private Dialog dialogConfirm;
    private RelativeLayout rr_countrylist;
    private void  showCountryList()
    {
        SimpleDialog simpleDialog = new SimpleDialog();
         dialogConfirm= simpleDialog.simpleDailog(this, R.layout.showcountrydialog, new ColorDrawable(android.graphics.Color.TRANSPARENT), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
        RecyclerView recyclerView =dialogConfirm.findViewById(R.id.recycler_view_for_country);

        final RelativeLayout rr_countrylist=dialogConfirm.findViewById(R.id.rr_copuntrylist);

        animateUp(screenHeight,rr_countrylist);

        CountryRecyclerviewAdapter mAdapter = new CountryRecyclerviewAdapter(countryAr, SignUpActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        dialogConfirm.findViewById(R.id.minimize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(rr_countrylist,dialogConfirm);

            }
        });
    }






    public void downSourceDestinationView(View sourcedestinationcontainer, final Dialog dialog) {
        animationForViews.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }


    private int screenHeight, screenWidth;

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public void animateUp(int height, View sourcedestinationcontainer) {
        animationForViews.handleAnimation(SignUpActivity.this, sourcedestinationcontainer, 500, height, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
            @Override
            public void getAnimationStatus(String status) {

                switch (status) {
                    case IsAnimationEndedCallback.animationCancel: {
                        break;
                    }
                    case IsAnimationEndedCallback.animationEnd: {
                            break;
                    }

                    case IsAnimationEndedCallback.animationRepeat: {
                        break;
                    }

                    case IsAnimationEndedCallback.animationStart: {
                        break;
                    }

                }
            }
        });
    }


    public  void setCountryData(JSONObject countryObj)
    {
        try {

           downSourceDestinationView(rr_countrylist,dialogConfirm);

            String countrycode=countryObj.getString("CountryCode");
            countryID= countryObj.getString("CountryId");
            countryName= countryObj.getString("CountryName");
            String CountryImage=countryObj.getString("CountryImage");

            txt_selectcountry.setText(countryName);
            showImage(CountryImage,(ImageView) findViewById(R.id.img_countryflag));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showImage(String path,final  ImageView profileimage)
    {
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

    }


}
