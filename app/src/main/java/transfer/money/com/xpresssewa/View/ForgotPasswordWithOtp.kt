//package transfer.money.com.xpresssewa.View;
//
//import android.content.Intent
//import android.os.Bundle
//
//import android.view.View
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.textfield.TextInputLayout
//import me.anwarshahriar.calligrapher.Calligrapher
//import org.json.JSONObject
//import transfer.money.com.xpresssewa.R
//import transfer.money.com.xpresssewa.communication.ServerHandler
//import transfer.money.com.xpresssewa.interfaces.CallBack
//import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences
//import transfer.money.com.xpresssewa.validation.Showtoast
//import java.util.*
//
//class ForgotPasswordWithOtp : AppCompatActivity() {
//    lateinit var showtoast: Showtoast;
//    lateinit var rr_mainlayout: RelativeLayout;
//    lateinit var serverHandler: ServerHandler;
//    lateinit var saveImpPrefrences: SaveImpPrefrences;
//    lateinit var RRsignuptoplayout: RelativeLayout;
//    lateinit var txt_resendOtp: TextView;
//    lateinit var OTP: String;
//    lateinit var edittextOtp: String;
//    lateinit var MemberId: String;
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_forgot_password_with_otp)
//        val calligrapher = Calligrapher(this)
//        calligrapher.setFont(this, "MontserratRegular.ttf", true)
//        showtoast = Showtoast();
//        rr_mainlayout = findViewById(R.id.RRsignuptoplayout);
//        RRsignuptoplayout = findViewById(R.id.RRsignuptoplayout);
//        txt_resendOtp = findViewById(R.id.txt_resendOtp);
//        saveImpPrefrences = SaveImpPrefrences();
//        serverHandler = ServerHandler();
//
//        OTP=intent.getStringExtra("OTP")
//        MemberId=intent.getStringExtra("MemberId")
//      //  getOtpRequest();
//        init();
//    }
//
//    fun init() {
//        var img = findViewById<ImageView>(R.id.headerbackbutton)
//        img.setOnClickListener(View.OnClickListener {
//
//            finish()
//        })
//
//
//        var input_txt_otp = findViewById<TextInputLayout>(R.id.input_txt_otp);
//        var input_txt_password = findViewById<TextInputLayout>(R.id.input_txt_password);
//        var input_txt_confirm_password = findViewById<TextInputLayout>(R.id.input_txt_confirm_password);
//        var tv_submit = findViewById<TextView>(R.id.tv_submit);
//
//
//
//
//
//        tv_submit.setOnClickListener(View.OnClickListener {
//
//            if (input_txt_otp.editText?.text.toString().length == 0) {
//                input_txt_otp.error = "Enter OTP";
//            }
////            if (input_txt_otp.editText?.text.toString().contentEquals(OTP)) {
////                input_txt_password.error = "Enter OTP is invalid";
////            }
//
//            if (input_txt_password.editText?.text.toString().length == 0) {
//                input_txt_password.error = "Enter password";
//
//            } else if (input_txt_confirm_password.editText?.text.toString().length == 0) {
//                input_txt_confirm_password.error = "Enter Confirmed password";
//                //showtoast.showToast(this,"Required","Enter Confirmed password",rr_mainlayout);
//
//            } else if (!input_txt_password.editText?.text.toString().contentEquals(input_txt_confirm_password.editText?.text.toString())) {
//                input_txt_confirm_password.error = "Confirmed password does not matched";
//            } else {
////                if(OTP.equals(edittextOtp))
////                {
////
////                }
//
//                sendToServer(input_txt_otp.editText?.text.toString(),input_txt_password.editText?.text.toString());
//            }
//
//
//        })
//
//
//        txt_resendOtp.setOnClickListener(View.OnClickListener {
//            getOtpRequest();
//
//        })
//
//
//
//    }
//
//    fun sendToServer(otp:String,password: String) {
//
//        var map = LinkedHashMap<String, String>();
//        map.put("MemberId", MemberId);
//        map.put("Password", password);
//        map.put("OTP", otp);
//        map.put("IsForget", "YES");
//        serverHandler.sendToServer(this, "ResetPassword", map, 0, 1, CallBack { dta, respons ->
//            try {
//                var reqObj = JSONObject(dta)
//
//
//                if (reqObj.getBoolean("status"))
//                {
//                    var intent =Intent()
//                    setResult(RESULT_OK, intent);
//                    finish();
//
//                } else {
//                    showtoast.showToast(this, "Response", reqObj.getString("Message"), RRsignuptoplayout);
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//
//        )
//
//    }
//
//
//    fun getOtpRequest() {
//
//
//        var map = LinkedHashMap<String, String>();
//        map.put("Email", intent.getStringExtra("email"));
//
//        serverHandler.sendToServer(this, "Forget", map, 0, 1, CallBack { dta, respons ->
//            try {
//
//                var reqObj = JSONObject(dta)
//
//                if (reqObj.getBoolean("status")) {
//                    OTP = reqObj.getString("OTP")
//                    MemberId = reqObj.getString("MemberId")
//                } else {
//                    showtoast.showToast(this, "Not Matched", reqObj.getString("Message"), RRsignuptoplayout);
//
//                }
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
////    fun lanbda()
////    {y
////       var lambda:(Int)->Unit={s:Int->println(s)}
////
////    }
////    fun addNumber(a:Int,b:Int,mylambda:(Int)->Unit)
////    {
////         val add=a+b;
////         mylambda(add)
////
////    }
//
//
//}