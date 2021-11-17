package transfer.money.com.xpresssewa.View;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.AnimationForView;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.IsAnimationEndedCallback;
import transfer.money.com.xpresssewa.util.UtilClass;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    @BindView(R.id.rel_navigationview)
    RelativeLayout rel_navigationview;


    @BindView(R.id.fl_top)
    FrameLayout fl_top;


    FragmentTransaction ft_main;
    public JSONArray sourcecCurrncy, destinationCurrency;
    SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();

    AnimationForView animationForView;
    public int screenHeight,screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animationForView=new AnimationForView();
        getScreenHeight();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        ButterKnife.bind(this);
        UtilClass.getUserData(this);
        DefaultConstatnts.isKyDockUploaded=false;

        getNotification();

        sendBroadcast(new Intent(UtilClass.notificationBroadCast));
        try {

            String pin=saveImpPrefrences.reterivePrefrence(MainActivity.this, "Pin") +"";
            String MemberId=saveImpPrefrences.reterivePrefrence(MainActivity.this, DefaultConstatnts.MemberId) +"";
            String login=saveImpPrefrences.reterivePrefrence(MainActivity.this, DefaultConstatnts.login_detail) +"";


            JSONObject jsonObject=new JSONObject(login);
            UtilClass.getDefaultDestImage=jsonObject.getString("FlagImageDestination");
            UtilClass.getDefaultSourceImage=jsonObject.getString("FlagImageSource");
            UtilClass.defaultToSymble=jsonObject.getString("DestinationSymbol");
            UtilClass.DefaultFromSymble=jsonObject.getString("SourceSymbol");

            UtilClass.DefaultCountryname=jsonObject.getString("CountryName");
            UtilClass.defaultSourceCountryId=jsonObject.getString("CountryId");
            UtilClass.defaultSourceCountryId=jsonObject.getString("SDCountryId");


            if(pin.equalsIgnoreCase("0"))
            {
                Intent intent = new Intent(MainActivity.this, SetPinActivity.class);
                intent.putExtra(DefaultConstatnts.pinKey, DefaultConstatnts.pinSetup);
                startActivity(intent);
                finish();

            }
            else if(getIntent().getStringExtra(DefaultConstatnts.IsShowPin).equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainActivity.this, SetPinActivity.class);
                intent.putExtra(DefaultConstatnts.pinKey, DefaultConstatnts.pinVerify);
                startActivity(intent);
                finish();
            }

        }
        catch (Exception e) {

        }


        String isKycApproved=new SaveImpPrefrences().reterivePrefrence(this, DefaultConstatnts.IsKycApproved).toString();
        if(isKycApproved.equalsIgnoreCase("3"))//approved
        {
             // done
        }
        else if(isKycApproved.equalsIgnoreCase("2"))
        {
            //Myprofile
        }
        else if(isKycApproved.equalsIgnoreCase("1"))
        {
            //inprocess  at invoice
        }



        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getNotificationData();


    }

    protected void init() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // if(menuItem)
                bottomMenu(menuItem);
                return false;
                //My Contests
            }
        });



        if(getIntent().hasExtra("external_call"))
        {

            MenuItem menu=navigationView.getMenu().getItem(2);
            menu.setChecked(true);
            SendFragment sendFragment = new SendFragment();
            Bundle args = new Bundle();

            sendFragment.setArguments(args);
            replaceMainFragment(sendFragment,"send");
        }
        else
        {
            MenuItem menu=navigationView.getMenu().getItem(0);
            menu.setChecked(true);

            ActivityFragment activityFragment = new ActivityFragment();
            replaceMainFragment(activityFragment,"activity");
        }

    }

    private void replaceMainFragment(Fragment upcoming, String tag) {

        ft_main = getSupportFragmentManager().beginTransaction();
        ft_main.replace(R.id.fl_top, upcoming);
        //ft_main.addToBackStack(tag);
        ft_main.commit();
    }

    private void bottomMenu(MenuItem menuItem)
    {

        menuItem.setChecked(true);
        switch (menuItem.getItemId())
        {

            case R.id.menu_activity:
                ActivityFragment activityFragment = new ActivityFragment();
                replaceMainFragment(activityFragment,"activity");
                break;
            case R.id.menu_account:
                AccountFragment accountFragment = new AccountFragment();
                replaceMainFragment(accountFragment,"account");
                break;
            case R.id.menu_send:
                SendFragment sendFragment = new SendFragment();
                Bundle args = new Bundle();
                sendFragment.setArguments(args);
                replaceMainFragment(sendFragment,"send");
                break;

            case R.id.menu_recipients:
                RecipientFragment recipientFragment = new RecipientFragment();
                Bundle bd = new Bundle();
                bd.putString("symbol", "");
                recipientFragment.setArguments(bd);
                replaceMainFragment(recipientFragment,"recipient");

                break;
            case R.id.menu_invite:
                InviteFragment inviteFragment = new InviteFragment();
                replaceMainFragment(inviteFragment,"invite");

                break;


        }
    }

    public void sendFromSelectedList(JSONObject dataObj) {

        MenuItem menu=navigationView.getMenu().getItem(2);
        menu.setChecked(true);
        SendFragment sendFragment = new SendFragment();
        Bundle args = new Bundle();
        args.putString("data", dataObj + "");
        sendFragment.setArguments(args);
        replaceMainFragment(sendFragment,"send");

    }

    public void callToRecipientWithSymbol(String DestinationSymbol, String SourceSymbol, String
            FlagImageDestination, String callFrom, String SDCountryId, String CountryId, JSONObject obj) {

        RecipientFragment sendFragment = new RecipientFragment();
        MenuItem menu=navigationView.getMenu().getItem(3);
        menu.setChecked(true);

        Bundle args = new Bundle();
        args.putString("symbol", DestinationSymbol);
        args.putString("DestinationSymbol", DestinationSymbol + "");
        args.putString("SourceSymbol", SourceSymbol + "");
        args.putString("FlagImageDestination", FlagImageDestination + "");
        args.putString("callFrom", callFrom + "");
        args.putString("SDCountryId", SDCountryId + "");
        args.putString("CountryId", CountryId + "");
        args.putString("data", obj + "");
        sendFragment.setArguments(args);
        replaceMainFragment(sendFragment,"recipient");





//        Intent args=new Intent(MainActivity.this,RecipientActivity.class);
//        args.putExtra("symbol", DestinationSymbol);
//        args.putExtra("DestinationSymbol", DestinationSymbol + "");
//        args.putExtra("SourceSymbol", SourceSymbol + "");
//        args.putExtra("FlagImageDestination", FlagImageDestination + "");
//        args.putExtra("callFrom", callFrom + "");
//        args.putExtra("SDCountryId", SDCountryId + "");
//        args.putExtra("CountryId", CountryId + "");
//        args.putExtra("data", obj + "");
//        startActivity(args);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Result code==="+requestCode);
        if (requestCode == 101)//Call from recipient fragment
        {
            RecipientFragment recipientFragment = new RecipientFragment();
            Bundle bd = new Bundle();
            bd.putString("symbol", "");
            recipientFragment.setArguments(bd);
            replaceMainFragment(recipientFragment,"recipient");
        }
        else if(requestCode==1001)//call from review activity to change amount  and symbol etc etc
        {
            if(data!=null)
            {
                if (data.getStringExtra("isSendScreenDataLoaded").equalsIgnoreCase("yes"))
                {
                    try {
                        JSONObject dataFromReview=new JSONObject(data.getStringExtra("data"));
                        dataFromReview.put("getAmountFromReviewActivity","yes");
                        sendFromSelectedList(dataFromReview);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(data.getStringExtra("isSendScreenDataLoaded").equalsIgnoreCase("isTransaction"))
                {
                    MenuItem menu=navigationView.getMenu().getItem(0);
                    menu.setChecked(true);
                    ActivityFragment sendFragment = new ActivityFragment();
                    Bundle args = new Bundle();
                    sendFragment.setArguments(args);
                    replaceMainFragment(sendFragment,"activity");
                }

            }

        }


    }




  public void callMyProfileFragment(String editPersonal)
   {

       MenuItem menu=navigationView.getMenu().getItem(1);
       menu.setChecked(true);
    AccountFragment accountFragment = new AccountFragment();

    Bundle bundle=new Bundle();
    bundle.putString("Callfrom","edit_personal");
    accountFragment.setArguments(bundle);

    replaceMainFragment(accountFragment,"editPersonal");

   }


   public void animateUp(int height, View sourcedestinationcontainer) {
        animationForView.handleAnimation(MainActivity.this, sourcedestinationcontainer, 500, height, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
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


    public void downSourceDestinationView(View sourcedestinationcontainer, final Dialog dialog) {
        animationForView.handleAnimation(this, sourcedestinationcontainer, 500, 00, screenHeight, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
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

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//    }


    int exitCount=1;
    @Override
    public void onBackPressed() {
        if(exitCount>=2)
        {
            finishAffinity();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Tap again to exit "+getResources().getString(R.string.app_name)+" app",Toast.LENGTH_LONG).show();
            exitCount++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitCount=1;
                }
            },3000);

        }

    }



    private void getNotificationData()
    {
        try {
            UtilClass.getUserData(MainActivity.this);
            Map<String, String> m = new LinkedHashMap<>();
            m.put("MemberId", UtilClass.member_id);
            new ServerHandler().sendToServer(MainActivity.this, "GetNotifications", m, 1, 1, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("notification data==" + dta);
                        JSONObject obj = new JSONObject(dta);

                        if (obj.getString("status").equalsIgnoreCase("true"))
                        {
                            new SaveImpPrefrences().savePrefrencesData(MainActivity.this,obj.getJSONArray("SubCategoriesList")+"",DefaultConstatnts.getNotificationData);
                        }
                        else
                        {
                            //showtoast.showToast(getActivity(), "Response", obj.getString("Message"), view.findViewById(R.id.ll_recipientlayoutmain));
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


    private void getNotification() {
        BroadcastReceiver getChatBroadCast;
        getChatBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getNotificationData();
                    }
                });

            }
        };
        registerReceiver(getChatBroadCast, new IntentFilter(UtilClass.notificationBroadCast));


    }




}
