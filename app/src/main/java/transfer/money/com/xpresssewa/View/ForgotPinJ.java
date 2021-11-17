package transfer.money.com.xpresssewa.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class ForgotPinJ extends AppCompatActivity {
    ArrayList<EditText> otpArray= new ArrayList<EditText>();
    ArrayList<EditText> pinArray=new  ArrayList<EditText>();
    ArrayList<EditText> pinconfArray=new  ArrayList<EditText>();

    ServerHandler serverHandler;
     Showtoast showtoast;
     SaveImpPrefrences saveImpPrefrences;
     String OTP;
     RelativeLayout RRsignuptoplayout;
    TextView tv_submit;
    TextView txt_resendOtp ;
     ImageView headerbackbutton;
     private  int j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);





        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        RRsignuptoplayout = findViewById(R.id.RRsignuptoplayout);
        tv_submit = findViewById(R.id.tv_submit);
        txt_resendOtp = findViewById(R.id.txt_resendOtp);
        headerbackbutton = findViewById(R.id.headerbackbutton);
        serverHandler = new ServerHandler();
        saveImpPrefrences = new SaveImpPrefrences();
        showtoast =new  Showtoast();
        //getOtpRequest();
        init();
    }
    private void init() {
        OTP=getIntent().getStringExtra("OTP");
        otpArray = new ArrayList();
        pinArray = new ArrayList();
        pinconfArray = new ArrayList();


        otpArray.add(findViewById(R.id.txt_otpone));
        otpArray.add(findViewById(R.id.txt_otptwo));
        otpArray.add(findViewById(R.id.txt_otpthree));
        otpArray.add(findViewById(R.id.txt_otpfour));


        pinArray.add(findViewById(R.id.txt_pinone));
        pinArray.add(findViewById(R.id.txt_pintwo));
        pinArray.add(findViewById(R.id.txt_pinthree));
        pinArray.add(findViewById(R.id.txt_pinfour));


        pinconfArray.add(findViewById(R.id.txt_pinone_conf));
        pinconfArray.add(findViewById(R.id.txt_pin_two_conf));
        pinconfArray.add(findViewById(R.id.txt_pinthree_conf));
        pinconfArray.add(findViewById(R.id.txt_pinfour_conf));



        for (int i=0;i<4;i++)
        {

            System.out.println("i======"+i);

            otpArray.get(i).setTag(i);
            pinArray.get(i).setTag(i);
            pinconfArray.get(i).setTag(i);

            otpArray.get(i).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if(keyCode == KeyEvent.KEYCODE_DEL) {
                        String tag = otpArray.get(Integer.parseInt(v.getTag()+"")).getTag().toString();
                        int index= Integer.parseInt(tag);
                        if (index > 0) {
                                otpArray.get(Integer.parseInt(v.getTag()+"")).setText("");
                                otpArray.get(Integer.parseInt(v.getTag()+"") - 1).requestFocus();
                            }

                    }
                    return false;
                }
            });





            pinArray.get(i).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        String tag = v.getTag().toString();
                        int index= Integer.parseInt(tag+"");

                        if (index > 0) {
                                pinArray.get(index).setText("");
                                pinArray.get(index - 1).requestFocus();
                            }
                        }

                    return false;
                }
            });





            pinconfArray.get(i).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        String tag = v.getTag().toString();
                        int index= Integer.parseInt(tag);



                            if (index > 0) {
                                pinconfArray.get(index).setText("");
                                pinconfArray.get(index - 1).requestFocus();
                            }
                        }

                    return false;
                }
            });



        }

        addTextWatcher();



        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePinRequest();
            }
        });

        txt_resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtpRequest();
            }
        });

        headerbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void addTextWatcher()
    {
        otpArray.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String tag = otpArray.get(0).getTag().toString();
                int index = Integer.parseInt(tag);


                System.out.println();

                if (index < 3) {
                    if (s.length()> 0) {
                        otpArray.get(0 + 1).requestFocus();
                    }

                }

            }
        });
        otpArray.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String tag = otpArray.get(1).getTag().toString();
                int index = Integer.parseInt(tag);


                System.out.println();

                if (index < 3) {
                    if (s.length()> 0) {
                        otpArray.get(1 + 1).requestFocus();
                    }

                }

            }
        });
        otpArray.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String tag = otpArray.get(2).getTag().toString();
                int index = Integer.parseInt(tag);


                System.out.println();

                if (index < 3) {
                    if (s.length()> 0) {
                        otpArray.get(2 + 1).requestFocus();
                    }

                }

            }
        });
        otpArray.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String tag = otpArray.get(3).getTag().toString();
                int index = Integer.parseInt(tag);


                System.out.println();

                if (index < 3) {
                    if (s.length()> 0) {
                        otpArray.get(3 + 1).requestFocus();
                    }

                }

            }
        });


        pinArray.get(0).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String tag = pinArray.get(0).getTag().toString();
                    int index= Integer.parseInt(tag);
                    if (index < 3) {
                            if (s.length() > 0)
                            {

                                pinArray.get(0 + 1).requestFocus();
                            }
                        }
                    }


            });
        pinArray.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinArray.get(1).getTag().toString();
                int index= Integer.parseInt(tag);
                if (index < 3) {
                    if (s.length() > 0)
                    {

                        pinArray.get(1 + 1).requestFocus();
                    }
                }
            }


        });
        pinArray.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinArray.get(2).getTag().toString();
                int index= Integer.parseInt(tag);
                if (index < 3) {
                    if (s.length() > 0)
                    {

                        pinArray.get(2 + 1).requestFocus();
                    }
                }
            }


        });
        pinArray.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinArray.get(3).getTag().toString();
                int index= Integer.parseInt(tag);
                if (index < 3) {
                    if (s.length() > 0)
                    {

                        pinArray.get(3 + 1).requestFocus();
                    }
                }
            }


        });


        pinconfArray.get(0).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String tag = pinconfArray.get(0).getTag().toString();
                    int index = Integer.parseInt(tag);



                        if (index < 3) {
                            if (s.length()> 0) {

                                pinconfArray.get(0 + 1).requestFocus();
                            }

                    }
                }
            });
        pinconfArray.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinconfArray.get(1).getTag().toString();
                int index = Integer.parseInt(tag);



                if (index < 3) {
                    if (s.length()> 0) {

                        pinconfArray.get(1 + 1).requestFocus();
                    }

                }
            }
        });
        pinconfArray.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinconfArray.get(2).getTag().toString();
                int index = Integer.parseInt(tag);



                if (index < 3) {
                    if (s.length()> 0) {

                        pinconfArray.get(2 + 1).requestFocus();
                    }

                }
            }
        });
        pinconfArray.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tag = pinconfArray.get(3).getTag().toString();
                int index = Integer.parseInt(tag);



                if (index < 3) {
                    if (s.length()> 0) {

                        pinconfArray.get(3 + 1).requestFocus();
                    }

                }
            }
        });

    }

    private void getOtpRequest() {

        //  var logindetail = saveImpPrefrences.reterivePrefrence(this, DefaultConstatnts.login_detail).toString();
        //   var obj = JSONObject(logindetail)
        LinkedHashMap<String,String> map =new  LinkedHashMap<String, String>();
        map.put("Email", getIntent().getStringExtra("email"));


        serverHandler.sendToServer(this, "Forget", map, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try {
                    JSONObject reqObj = new JSONObject(dta);

                    if (reqObj.getBoolean("status"))
                    {
                        OTP = reqObj.getString("OTP");
                    } else {
                        showtoast.showToast(ForgotPinJ.this, "Not Matched", reqObj.getString("Message"), RRsignuptoplayout);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });




    }


    private void savePinRequest()
    {

        String enteredOtp="";
        String pin="";
        String conf_pin="";

        for(int i=0;i<4;i++ )
        {

            enteredOtp=enteredOtp.concat(otpArray.get(i).getText().toString());

            pin=pin.concat(pinArray.get(i).getText().toString());
            conf_pin=conf_pin.concat(pinconfArray.get(i).getText().toString());
        }

        System.out.println("pin----"+enteredOtp+"==="+pin+"==="+conf_pin);


        if(enteredOtp.length()==0)
        {
            showtoast.showToast(this, "Error","Entered OTP " , RRsignuptoplayout);
        }
        else if(pin.length()==0)
        {
            showtoast.showToast(this, "Error","Entered PIN " , RRsignuptoplayout);
        }
        else if(conf_pin.length()==0)
        {
            showtoast.showToast(this, "Error","Confirmed PIN " , RRsignuptoplayout);
        }

        else if(!OTP.equals(enteredOtp))
        {
            showtoast.showToast(this, "Error","Entered OTP is not matched" , RRsignuptoplayout);
        }
        else if(!pin.equals(conf_pin))
        {
            showtoast.showToast(this, "Error","Confirm pin does not matched" , RRsignuptoplayout);
        }
        else{


            String memberid = saveImpPrefrences.reterivePrefrence(this, DefaultConstatnts.MemberId).toString();
            LinkedHashMap map =new  LinkedHashMap<String, String>();

            map.put("MemberId",memberid);
            map.put("Pin",pin);
            map.put("OTP", enteredOtp);
            map.put("IsForget", "YES");


            serverHandler.sendToServer(this, "SetPin", map, 0, 1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {

                        JSONObject reqObj = new JSONObject(dta);

                        System.out.println("Change Pin response==="+reqObj);
                        if (reqObj.getBoolean("status")) {
                            new SaveImpPrefrences().savePrefrencesData(ForgotPinJ.this, reqObj.getString("Pin") + "", DefaultConstatnts.Pin);
                            // val i = Intent(this, MainActivity::class.java)
                            //i.putExtra(DefaultConstatnts.IsShowPin, "no")
                            //startActivity(i)

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("result", "");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();



                        } else {
                            showtoast.showToast(ForgotPinJ.this, "Response", reqObj.getString("Message"), RRsignuptoplayout);
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


        }
    }
}
