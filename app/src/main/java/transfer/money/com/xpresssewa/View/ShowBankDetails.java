package transfer.money.com.xpresssewa.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.BaseActivity;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.registration.VeriFyTransactionOTP;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.util.UtilClass;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class ShowBankDetails extends BaseActivity {

    TextView txt_payeename,txt_AccountNumber,txt_BsbCode,txt_UniqueId;
    LinearLayout ll_bakdetail;
    TextView txt_note;
    private int type=0;
    private ImageView img_payeename_copy,img_AccountNumber_copy,img_BsbCode_copy,img_UniqueId_copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bank_details);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        init();
        getBankDetail();

    }

    @Override
    protected void init() {

        txt_payeename=findViewById(R.id.txt_payeename);
        txt_AccountNumber= findViewById(R.id.txt_AccountNumber);
        txt_BsbCode=findViewById(R.id.txt_BsbCode);
        txt_UniqueId=findViewById(R.id.txt_UniqueId);
        ll_bakdetail=findViewById(R.id.ll_bakdetail);
        txt_note=findViewById(R.id.txt_note);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            txt_note.setText(Html.fromHtml("<html>1.Funds must be sent from an account in your name.<br/>\n" +
                    "        2.You must include <font color='#3F70BC'> 86151E38 </font>in your bank transfer payment description field.<br/>\n" +
                    "        3.RTGS payments are not accepted. If you must send by RTGS, please contact customer support\n" +
                    "        We never takes money automatically from your account.<br/><br/>\n" +
                    "\n" +
                    "        You can use your online banking or mobile app to make your bank transfer to us.<br/><br/>" +
                    "" +
                    "Before completing the transaction click the \"Bank Transfer Done\" then make the deposit payment via bank.</html>", Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            txt_note.setText(Html.fromHtml("<html>1.Funds must be sent from an account in your name.<br/>\n" +
                    "        2.You must include <font color='#3F70BC'> 86151E38 </font>in your bank transfer payment description field.<br/>\n" +
                    "        3.RTGS payments are not accepted. If you must send by RTGS, please contact customer support\n" +
                    "        We never takes money automatically from your account.<br/><br/>\n" +
                    "\n" +
                    "        You can use your online banking or mobile app to make your bank transfer to us.<br/><br/>" +
                    "" +
                    "Before completing the transaction click the \"Bank Transfer Done\" then make the deposit payment via bank.</html>"));
            }


        findViewById(R.id.txt_paymentdone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showOtptions();
              //  addTransaction();
            }
        });
        findViewById(R.id.txt_paymentcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_payeename_copy=findViewById(R.id.img_payeename_copy);
        img_AccountNumber_copy=findViewById(R.id.img_AccountNumber_copy);
        img_BsbCode_copy=findViewById(R.id.img_BsbCode_copy);
        img_UniqueId_copy=findViewById(R.id.img_UniqueId_copy);

        img_payeename_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_payeename.getText().toString(),"Payee Name");
            }
        });

        img_AccountNumber_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_AccountNumber.getText().toString(),"Account Number");
            }
        });

        img_BsbCode_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_BsbCode.getText().toString(),"BSB code");
            }
        });
        img_UniqueId_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCode(txt_UniqueId.getText().toString(),"UniqueId");
            }
        });




    }

    private  void getBankDetail()
    {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        new ServerHandler().sendToServer(ShowBankDetails.this, "BankListBySymbol", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);

                    if(obj.getBoolean("status"))
                    {
                        txt_payeename.setText(obj.getString("PayeeName"));
                        txt_AccountNumber.setText(obj.getString("AccountNumber"));
                        txt_BsbCode.setText(obj.getString("BsbCode"));
                        txt_UniqueId.setText(obj.getString("UniqueId"));

                    } else {
                        new Showtoast().showToast(ShowBankDetails.this, "Response", obj.getString("Message"), findViewById(R.id.ll_activitylayout));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }





    Dialog confirmDialog;
      private void showOtptions()
        {
          try
           {
              type=1;
              SimpleDialog simpleDialog = new SimpleDialog();
               confirmDialog = simpleDialog.simpleDailog(this, R.layout.show_otp_option, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
              RadioButton rd_email = confirmDialog.findViewById(R.id.rd_email);
              RadioButton rd_phone = confirmDialog.findViewById(R.id.rd_phone);
              rd_email.setChecked(true);

              JSONObject loginData=new JSONObject(new SaveImpPrefrences().reterivePrefrence(this,DefaultConstatnts.login_detail).toString());
              String contactNumber=loginData.getString("ContactNumber");
              String email=loginData.getString("Email");

              String contactnumberMask=contactNumber.substring(contactNumber.length()-3,contactNumber.length());
              rd_phone.setText("Phone - XXXXXXX"+contactnumberMask);

              String[] emailAr=email.split("@");
              String maskEmail=emailAr[0].substring(emailAr[0].length()-1,emailAr[0].length())+"@"+emailAr[1];
              rd_email.setText("Email - XXXXX"+maskEmail);

              confirmDialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      confirmDialog.dismiss();
                  }
              });

              confirmDialog.findViewById(R.id.btnSendOtp).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      sendOtp();
                  }
              });

              rd_email.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      rd_phone.setChecked(false);
                      type=1;
                  }
              });

              rd_phone.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      type=2;
                      rd_email.setChecked(false);
                  }
              });



           }
          catch (Exception e)
          {
              e.printStackTrace();
          }
      }

      private void sendOtp()
      {
          Map<String, String> m = new LinkedHashMap<>();
          m.put("MemberId", UtilClass.member_id);
          m.put("Type",  type+"");

          System.out.println("Concated code==="+m);
          new ServerHandler().sendToServer(ShowBankDetails.this, "SendTransactionOTP",m, 0, 1, new CallBack() {
              @Override
              public void getRespone(String dta, ArrayList<Object> respons) {
                  try {

                      System.out.println("Concated code= response=="+dta);
                      JSONObject obj = new JSONObject(dta);
                      if(obj.getBoolean("status"))
                      {
                          confirmDialog.dismiss();
                          Intent intent=new Intent(ShowBankDetails.this,VeriFyTransactionOTP.class);
                          intent.putExtra("data",getIntent().getStringExtra("data"));
                          intent.putExtra("BankId",getIntent().getStringExtra("bank_id"));
                          intent.putExtra("BankRefNumber",txt_UniqueId.getText().toString());
                          startActivityForResult(intent,1001);



                      }
                      else {
                          new Showtoast().showToast(ShowBankDetails.this, "Transaction", obj.getString("Message"), ll_bakdetail);
                      }

                  } catch (Exception e) {
                      e.printStackTrace();
                  }


              }
          });
      }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Result back===="+requestCode+"==="+resultCode);

        Intent intent = new Intent();
        intent.putExtra("isSendScreenDataLoaded", "showTransaction");
        setResult(RESULT_OK, intent);
        finish();
    }
}