//package transfer.money.com.xpresssewa.View
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import me.anwarshahriar.calligrapher.Calligrapher
//import org.json.JSONObject
//import transfer.money.com.xpresssewa.R
//import transfer.money.com.xpresssewa.communication.ServerHandler
//import transfer.money.com.xpresssewa.interfaces.CallBack
//import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences
//import transfer.money.com.xpresssewa.util.DefaultConstatnts
//import transfer.money.com.xpresssewa.validation.Showtoast
//import java.util.*
//
//public class ForgotPin : AppCompatActivity() {
//    lateinit var otpArray: ArrayList<EditText>;
//    lateinit var pinArray: ArrayList<EditText>;
//    lateinit var pinconfArray: ArrayList<EditText>;
//    lateinit var serverHandler: ServerHandler;
//    lateinit var showtoast: Showtoast;
//    lateinit var saveImpPrefrences: SaveImpPrefrences;
//    lateinit var OTP: String;
//    lateinit var RRsignuptoplayout: RelativeLayout;
//    lateinit var tv_submit: TextView;
//    lateinit var txt_resendOtp: TextView;
//    lateinit var headerbackbutton: ImageView;
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_forgot_pin)
//        val calligrapher = Calligrapher(this)
//        calligrapher.setFont(this, "MontserratRegular.ttf", true)
//        RRsignuptoplayout = findViewById(R.id.RRsignuptoplayout);
//        tv_submit = findViewById(R.id.tv_submit);
//        txt_resendOtp = findViewById(R.id.txt_resendOtp);
//        headerbackbutton = findViewById(R.id.headerbackbutton);
//        serverHandler = ServerHandler()
//        saveImpPrefrences = SaveImpPrefrences()
//        showtoast = Showtoast();
//        //getOtpRequest();
//        init();
//    }
//
//    fun init() {
//        OTP=intent.getStringExtra("OTP");
//        otpArray = ArrayList();
//        pinArray = ArrayList();
//        pinconfArray = ArrayList();
//
//
//        otpArray.add(findViewById<EditText>(R.id.txt_otpone))
//        otpArray.add(findViewById<EditText>(R.id.txt_otptwo))
//        otpArray.add(findViewById<EditText>(R.id.txt_otpthree))
//        otpArray.add(findViewById<EditText>(R.id.txt_otpfour))
//
//
//        pinArray.add(findViewById<EditText>(R.id.txt_pinone))
//        pinArray.add(findViewById<EditText>(R.id.txt_pintwo))
//        pinArray.add(findViewById<EditText>(R.id.txt_pinthree))
//        pinArray.add(findViewById<EditText>(R.id.txt_pinfour))
//
//
//        pinconfArray.add(findViewById<EditText>(R.id.txt_pinone_conf))
//        pinconfArray.add(findViewById<EditText>(R.id.txt_pin_two_conf))
//        pinconfArray.add(findViewById<EditText>(R.id.txt_pinthree_conf))
//        pinconfArray.add(findViewById<EditText>(R.id.txt_pinfour_conf))
//
//
//
//        for (i in 0..3) {
//
//            otpArray.get(i).setTag(i);
//            pinArray.get(i).setTag(i);
//            pinconfArray.get(i).setTag(i);
//
//            otpArray.get(i).setOnKeyListener(object : View.OnKeyListener {
//                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        var tag = otpArray.get(i).getTag().toString();
//                        val index: Int? = tag.toInt()
//
//                        if (index != null) {
//                            println("length===" + index)
//                            if (index > 0) {
//                                otpArray.get(i).setText("");
//                                otpArray.get(i - 1).requestFocus()
//                            }
//                        }
//                    }
//                    return false
//                }
//            })
//            otpArray.get(i).addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//
//
//                    var tag = otpArray.get(i).getTag().toString();
//                    val index: Int? = tag.toInt()
//
//                    if (index != null) {
//                        println("length===" + index)
//                        if (index < 3) {
//                            if (s?.length!! > 0) {
//
//                                otpArray.get(i + 1).requestFocus()
//                            }
//                        }
//                    }
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
//
//
//            pinArray.get(i).setOnKeyListener(object : View.OnKeyListener {
//                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        var tag = pinArray.get(i).getTag().toString();
//                        val index: Int? = tag.toInt()
//
//                        if (index != null) {
//                            println("length===" + index)
//                            if (index > 0) {
//                                pinArray.get(i).setText("");
//                                pinArray.get(i - 1).requestFocus()
//                            }
//                        }
//                    }
//                    return false
//                }
//            })
//            pinArray.get(i).addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//
//
//                    var tag = pinArray.get(i).getTag().toString();
//                    val index: Int? = tag.toInt()
//
//                    if (index != null) {
//                        println("length===" + index)
//                        if (index < 3) {
//                            if (s?.length!! > 0) {
//
//                                pinArray.get(i + 1).requestFocus()
//                            }
//                        }
//                    }
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
//
//
//            pinconfArray.get(i).setOnKeyListener(object : View.OnKeyListener {
//                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        var tag = pinconfArray.get(i).getTag().toString();
//                        val index: Int? = tag.toInt()
//
//                        if (index != null) {
//                            println("length===" + index)
//                            if (index > 0) {
//                                pinconfArray.get(i).setText("");
//                                pinconfArray.get(i - 1).requestFocus()
//                            }
//                        }
//                    }
//                    return false
//                }
//            })
//            pinconfArray.get(i).addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//
//
//                    var tag = pinconfArray.get(i).getTag().toString();
//                    val index: Int? = tag.toInt()
//
//                    if (index != null) {
//                        println("length===" + index)
//                        if (index < 3) {
//                            if (s?.length!! > 0) {
//
//                                pinconfArray.get(i + 1).requestFocus()
//                            }
//                        }
//                    }
//
//                }
//
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
//        }
//
//
//        tv_submit.setOnClickListener(View.OnClickListener {
//
//            savePinRequest();
//
//        })
//        txt_resendOtp.setOnClickListener(View.OnClickListener {
//            getOtpRequest();
//        })
//        headerbackbutton.setOnClickListener(View.OnClickListener {
//
//            finish()
//        })
//
//    }
//
//    fun getOtpRequest() {
//
//      //  var logindetail = saveImpPrefrences.reterivePrefrence(this, DefaultConstatnts.login_detail).toString();
//     //   var obj = JSONObject(logindetail)
//        var map = LinkedHashMap<String, String>();
//        map.put("Email", intent.getStringExtra("email"));
//
//
//
//        serverHandler.sendToServer(this, "Forget", map, 0, 1, CallBack { dta, respons ->
//            try {
//
//                var reqObj = JSONObject(dta)
//
//                if (reqObj.getBoolean("status"))
//                {
//                    OTP = reqObj.getString("OTP")
//                } else {
//                    showtoast.showToast(this, "Not Matched", reqObj.getString("Message"), RRsignuptoplayout);
//                }
//
//                //   {"Message":"success","ReponseCode":1,"OTP":"1922","UserId":"gurna10038","RefCode":"gurna10038","Version":"1","Pin":1234,"IsPhoneVerified":true,"MemberId":10038,"status":true}
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//
//        )
//    }
//
//
//    fun savePinRequest()
//    {
//
//        var enteredOtp:String="";
//        var  pin:String="";
//        var  conf_pin:String="";
//
//        for(i in 0..3)
//        {
//
//            enteredOtp=enteredOtp.plus(otpArray.get(i).text.toString())
//            println("entered otp==="+otpArray.get(i).text.toString())
//            pin=pin.plus(pinArray.get(i).text.toString())
//            conf_pin=conf_pin.plus(pinconfArray.get(i).text.toString())
//        }
//
//        println("pin----"+enteredOtp+"==="+pin+"==="+conf_pin)
//
//
//        if(enteredOtp.length==0)
//        {
//            showtoast.showToast(this, "Error","Entered OTP " , RRsignuptoplayout);
//        }
//        else if(pin.length==0)
//        {
//            showtoast.showToast(this, "Error","Entered PIN " , RRsignuptoplayout);
//        }
//        else if(conf_pin.length==0)
//        {
//            showtoast.showToast(this, "Error","Confirmed PIN " , RRsignuptoplayout);
//        }
//
//       else if(!OTP.equals(enteredOtp))
//        {
//            showtoast.showToast(this, "Error","Entered OTP is not matched" , RRsignuptoplayout);
//        }
//        else if(!pin.equals(conf_pin))
//        {
//            showtoast.showToast(this, "Error","Confirm pin does not matched" , RRsignuptoplayout);
//        }
//        else{
//
//
//            var memberid = saveImpPrefrences.reterivePrefrence(this, DefaultConstatnts.MemberId).toString();
//            var map = LinkedHashMap<String, String>();
//
//            map.put("MemberId",memberid);
//            map.put("Pin",pin);
//            map.put("OTP", enteredOtp);
//            map.put("IsForget", "YES");
//
//
//            serverHandler.sendToServer(this, "SetPin", map, 0, 1, CallBack { dta, respons ->
//                try {
//
//                    var reqObj = JSONObject(dta)
//
//                    System.out.println("Change Pin response==="+reqObj)
//                    if (reqObj.getBoolean("status")) {
//                        SaveImpPrefrences().savePrefrencesData(this, reqObj.getString("Pin") + "", DefaultConstatnts.Pin)
//                       // val i = Intent(this, MainActivity::class.java)
//                        //i.putExtra(DefaultConstatnts.IsShowPin, "no")
//                        //startActivity(i)
//
//                        val resultIntent = Intent()
//                        resultIntent.putExtra("result", "")
//                        setResult(Activity.RESULT_OK, resultIntent)
//                        finish()
//
//
//
//                    } else {
//                        showtoast.showToast(this, "Response", reqObj.getString("Message"), RRsignuptoplayout);
//                    }
//
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            });
//        }
// }
//}
