package transfer.money.com.xpresssewa.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import me.anwarshahriar.calligrapher.Calligrapher
import transfer.money.com.xpresssewa.BaseActivity
import transfer.money.com.xpresssewa.R

class ChooseProfile : BaseActivity() {
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_profile)
        val calligrapher = Calligrapher(this)
        calligrapher.setFont(this, "MontserratRegular.ttf", true)

        findViewById<LinearLayout>(R.id.ll_business_profile).setOnClickListener{
            intent = Intent(applicationContext, CreateBuisnessProfile::class.java)
            startActivity(intent)

        }


        findViewById<LinearLayout>(R.id.ll_my_profile).setOnClickListener(View.OnClickListener {

            intent= Intent(applicationContext,CreateBuisnessProfile::class.java)
            startActivity(intent)

        })
    }
}
