package transfer.money.com.xpresssewa.View;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.KeyBoardAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.biomateric.BiometricUtils;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.registration.SignInActivity;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class SetPinActivity extends AppCompatActivity implements BiometricCallback {

    private String oldPin="",newPin="";
    private TextView createpin;
    private int attemptCount=0;
    private Showtoast showtoast;
    private RelativeLayout rr_setPin;
    private String MemberId="";
    private String pinType="";
    private FingerprintManager fingerprintManager;
    private FingerprintManager.AuthenticationCallback authenticationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        createpin=findViewById(R.id.createpin);
        rr_setPin=findViewById(R.id.rr_setPin);
        showtoast=new Showtoast();
        MemberId=new SaveImpPrefrences().reterivePrefrence(this,DefaultConstatnts.MemberId).toString();
        pinType=getIntent().getStringExtra(DefaultConstatnts.pinKey);

        init();

        findViewById(R.id.resetPin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showOPtionPopup(findViewById(R.id.resetPin));

            }
        });
    }

    private void showOPtionPopup(View pinBtn)
    {
        PopupMenu popup = new PopupMenu(SetPinActivity.this, pinBtn);
        popup.getMenuInflater().inflate(R.menu.set_pin_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(SetPinActivity.this,ForgotPassword.class);
                intent.putExtra("callFrom","Pin");
                startActivity(intent);
                return true;
            }
        });

        popup.show();//showing popup menu
    }
    int itemCount=0;
    private void init()
    {
        itemCount=0;
        final ArrayList<TextView> keysTextAr=new ArrayList<>();
        keysTextAr.add((TextView)findViewById(R.id.txt_pinone));
        keysTextAr.add((TextView)findViewById(R.id.txt_pintwo));
        keysTextAr.add((TextView)findViewById(R.id.txt_pinthree));
        keysTextAr.add((TextView)findViewById(R.id.txt_pinfour));

        for(int x=0;x<keysTextAr.size();x++)
        {
            keysTextAr.get(x).setText("");
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        GridView gridView = findViewById(R.id.grid_restaurantimages);
        final ArrayList<Integer> keysAr=new ArrayList<>();
        for(int x=1;x<10;x++)
        {
            keysAr.add(x);
        }
        keysAr.add(0);
        keysAr.add(R.drawable.delete);

        gridView.setAdapter(new KeyBoardAdapter(this, keysAr));
        if(pinType.equalsIgnoreCase(DefaultConstatnts.pinSetup))
        {

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    try {

                        if (position == 10) {
                            if (itemCount > 0) {
                                itemCount--;
                                TextView textView = keysTextAr.get(itemCount);
                                textView.setText("");
                            }

                        } else {
                            TextView textView = keysTextAr.get(itemCount);
                            textView.setText(keysAr.get(position) + "");
                            itemCount++;
                        }

                        if (attemptCount == 0) {
                            if (itemCount == 4) {

                                attemptCount = 1;
                                createpin.setText("Re-Enter Pin");
                                oldPin = keysTextAr.get(0).getText().toString() + keysTextAr.get(1).getText().toString() + keysTextAr.get(2).getText().toString() + keysTextAr.get(3).getText().toString();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        init();
                                    }
                                }, 500);

                            }
                        } else if (attemptCount == 1) {
                            if (itemCount == 4) {
                                attemptCount = 2;

                                newPin = keysTextAr.get(0).getText().toString() + keysTextAr.get(1).getText().toString() + keysTextAr.get(2).getText().toString() + keysTextAr.get(3).getText().toString();

                                if (oldPin.equalsIgnoreCase(newPin)) {
                                    sendToServer();
                                } else {
                                    showtoast.showToast(SetPinActivity.this, "Response", "Confirm key does not matched.", rr_setPin);
                                    attemptCount = 0;
                                    createpin.setText("Create your Pin");
                                    oldPin = "";
                                    newPin = "";
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            init();
                                        }
                                    }, 500);

                                }

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else if(pinType.equalsIgnoreCase(DefaultConstatnts.pinVerify))
        {
           String isFingerAuth = new SaveImpPrefrences().reterivePrefrence(SetPinActivity.this, UtilClass.isFingerAuth).toString();
           if(isFingerAuth.equalsIgnoreCase("1")) {
               biomatericPopUp();
           }
            createpin.setText("Enter "+getResources().getString(R.string.app_name)+" PIN");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    try {

                        if (position == 10) {
                            if (itemCount > 0) {
                                itemCount--;
                                TextView textView = keysTextAr.get(itemCount);
                                textView.setText("");
                            }

                        } else {
                            TextView textView = keysTextAr.get(itemCount);
                            textView.setText(keysAr.get(position) + "");
                            itemCount++;
                        }


                        if (itemCount == 4) {
                            oldPin = keysTextAr.get(0).getText().toString() + keysTextAr.get(1).getText().toString() + keysTextAr.get(2).getText().toString() + keysTextAr.get(3).getText().toString();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    itemCount=0;
                                    for(int x=0;x<keysTextAr.size();x++)
                                    {
                                        keysTextAr.get(x).setText("");
                                    }
                                    veriFyPin();

                                }
                            }, 300);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void sendToServer()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", MemberId);
        m.put("Pin", oldPin);
        m.put("OTP", "");
        m.put("IsForget", "NO");
        new ServerHandler().sendToServer(this, "SetPin", m, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try
                {
                    JSONObject obj=new JSONObject(dta);
                    System.out.println("setPin===="+obj);
                    if(obj.getBoolean("status"))
                      {

                          SaveImpPrefrences saveImpPrefrences=new SaveImpPrefrences();
                          String memberId=obj.getString("MemberId");
                          saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj.getString("IsKycApproved")+"",DefaultConstatnts.IsKycApproved);
                          saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj.getString("UserName")+"",DefaultConstatnts.UserName);
                          saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj+"",DefaultConstatnts.login_detail);
                          saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj.getString("Pin")+"",DefaultConstatnts.Pin);
                          saveImpPrefrences.savePrefrencesData(SetPinActivity.this,memberId+"", DefaultConstatnts.MemberId);

                          UtilClass.getUserData(SetPinActivity.this);
                          Intent signIn=new Intent(SetPinActivity.this, MainActivity.class);
                          signIn.putExtra(DefaultConstatnts.IsShowPin,"no");
                          startActivity(signIn);
                          finishAffinity();


                        }
                    else
                    {

                        showtoast.showToast(SetPinActivity.this,"Response",obj.getString("Message"),rr_setPin);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }

    private void veriFyPin()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", MemberId);
        m.put("Pin", oldPin);
        new ServerHandler().sendToServer(this, "LoginWithPin", m, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try
                {
                    JSONObject obj=new JSONObject(dta);
                    System.out.println("LOgin data==="+obj);
                    if(obj.getBoolean("status"))
                    {
                        SaveImpPrefrences saveImpPrefrences=new SaveImpPrefrences();
                        saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj.getString("IsKycApproved")+"",DefaultConstatnts.IsKycApproved);
                        saveImpPrefrences.savePrefrencesData(SetPinActivity.this,obj.getString("UserName")+"",DefaultConstatnts.UserName);
                        saveImpPrefrences.savePrefrencesData(SetPinActivity.this,oldPin+"",DefaultConstatnts.Pin);
                            Intent i=new Intent(SetPinActivity.this,MainActivity.class);
                            i.putExtra(DefaultConstatnts.IsShowPin,"no");
                            startActivity(i);
                            finish();
                    }
                    else
                    {
                        showtoast.showToast(SetPinActivity.this,"Response",obj.getString("Message"),rr_setPin);
                    }

                }
               catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    BiometricManager mBiometricManager;

    private void biomatericPopUp() {
        try {
        //    String isBiometricEnable = new SavePreferences().reterivePreference(this, AppSettings.enable_biometric) + "";
          //  if (isBiometricEnable.equalsIgnoreCase("on")) {
                mBiometricManager = new BiometricManager.BiometricBuilder(SetPinActivity.this)
                        .setTitle(getString(R.string.biometric_title))
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .build();

                //start authentication
                mBiometricManager.authenticate(SetPinActivity.this);
            }
         catch (Exception e) {
            e.printStackTrace();
        }
    }






    private boolean isSensorAvialable()
    {
        boolean f=BiometricUtils.isFingerprintAvailable(this);
        boolean h=BiometricUtils.isHardwareSupported(this);
        System.out.println("isfingerprint available==="+f+"=="+h);
        return f;
    }

    @Override
    public void onSdkVersionNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {

    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationCancelled() {

    }

    @Override
    public void onAuthenticationSuccessful() {
        oldPin =new SaveImpPrefrences().reterivePrefrence(SetPinActivity.this,DefaultConstatnts.Pin).toString();
                veriFyPin();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

    }
}



