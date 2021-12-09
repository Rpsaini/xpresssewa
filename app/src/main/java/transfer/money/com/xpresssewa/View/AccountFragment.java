//ffmpeg -i C:\Users\Webcom\Desktop\20181202_133103.jpg -compression_level 50 C:\Users\Webcom\Desktop\sahilcompress50.jpg

package transfer.money.com.xpresssewa.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.dialogs.AccountDialog;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.settings.SettingsScreenActivity;
import transfer.money.com.xpresssewa.ticket.AddTicket;
import transfer.money.com.xpresssewa.util.AlertDialogs;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;
import transfer.money.com.xpresssewa.util.DialogCallBack;
import transfer.money.com.xpresssewa.util.UtilClass;

public class AccountFragment extends Fragment implements View.OnClickListener {

    View view;
    Context mContext;

    @BindView(R.id.iv_toolbar)
    ImageView iv_toolbar;

    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.create_account)
    TextView create_account;


    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.id_txt_count)
    TextView id_txt_count;


    @BindView(R.id.rr_notification)
    RelativeLayout rr_notification;


    private JSONObject dataObj = new JSONObject();
    private String type = "0";
    private int screenheight, screenWidth;
    private AppCompatActivity mainActivity;

    public boolean isUserPersonalProfileDOne = false;
    public String usernameStr = "";
    public String businessprofileName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        UtilClass.getUserData(getActivity());

        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(view, "MontserratRegular.ttf");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenheight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        mainActivity = (MainActivity) getActivity();
        mContext = getActivity();
        ButterKnife.bind(this, view);
        init();

        // getProfileData();

        return view;
    }

    private void init() {
        create_account.setOnClickListener(this);
        iv_toolbar.setImageResource(R.drawable.profile_icon);
        tv_title.setText(R.string.account);

        final ImageView iv_toolbar = view.findViewById(R.id.iv_toolbar);
        iv_toolbar.setImageResource(R.drawable.icondots);

        iv_toolbar.setColorFilter(ContextCompat.getColor(getActivity(), R.color.blue_bt_color), android.graphics.PorterDuff.Mode.SRC_IN);


        iv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), iv_toolbar);
                popup.getMenuInflater()
                        .inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.menu_help) {
                            Intent intent = new Intent(getActivity(), AddTicket.class);
                            getActivity().startActivity(intent);

                        } else if (item.getItemId() == R.id.menu_logout) {
                            AlertDialogs alertDialogs = new AlertDialogs();
                            alertDialogs.alertDialog(mainActivity, "Logout", "Would you like to logout ?", "Yes", "No", new DialogCallBack() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {

                                    if (buttonPressed.equalsIgnoreCase("Yes")) {
                                        logout();
                                    }
                                }
                            });
                        } else if (item.getItemId() == R.id.menu_setting) {
                            Intent intent = new Intent(getActivity(), SettingsScreenActivity.class);
                            getActivity().startActivity(intent);

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });


        view.findViewById(R.id.tv_account_expand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccountDialog().showAccountDialog(getActivity(), AccountFragment.this);
            }
        });




        rr_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);

            }
        });


    }



    public void logout() {
        SaveImpPrefrences saveImpPrefrences = new SaveImpPrefrences();
        String devicetoken = saveImpPrefrences.reterivePrefrence(mainActivity, "device_token") + "";
        saveImpPrefrences.savePrefrencesData(mainActivity, "yes", DefaultConstatnts.IsTutorialDone);
        saveImpPrefrences.savePrefrencesData(mainActivity, "0", "login_detail");
        saveImpPrefrences.savePrefrencesData(mainActivity, devicetoken, "device_token");

        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
        getActivity().finishAffinity();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_account:
                new AccountDialog().showAccountDialog(getActivity(), this);
                break;
        }
    }


    private void getProfileData() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("MemberId", UtilClass.member_id);
        new ServerHandler().sendToServer(getActivity(), "User", m, 0, 1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getBoolean("status"))
                    {
                        if (obj.getString("FirstName").length() == 0) {
                            username.setVisibility(View.GONE);
                            create_account.setVisibility(View.GONE);
                            isUserPersonalProfileDOne = false;
                        } else {
                            username.setVisibility(View.VISIBLE);
                            create_account.setVisibility(View.VISIBLE);
                            isUserPersonalProfileDOne = true;
                            username.setText(obj.getString("FirstName") + " " + obj.getString("LastName"));
                            create_account.setText("Edit Account");
                            usernameStr = obj.getString("FirstName") + " " + obj.getString("LastName");
                            type = obj.getString("Type");//1 Personal  2 business 4 both
                            businessprofileName = obj.getString("BusinessName");

                        }
                        dataObj = obj;

                        CreateBuisnessProfile.profileData.put("MemberId", dataObj.getString("MemberId"));
                        CreateBuisnessProfile.profileData.put("FirstName", dataObj.getString("FirstName"));
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

                        System.out.println("Kyc==="+obj.getString("IsKycApproved"));

                        String kycStatus=obj.getString("IsKycApproved");
                        new SaveImpPrefrences().savePrefrencesData(getActivity(),kycStatus,DefaultConstatnts.IsKycApproved);

                            if(getArguments() != null)
                            {
                                if (getArguments().containsKey("Callfrom"))
                                {
                                    if(getArguments().getString("Callfrom").equalsIgnoreCase("edit_personal"))
                                    {
                                        getArguments().remove("Callfrom");
                                        if(kycStatus.equalsIgnoreCase("2"))
                                        {
                                            Intent i = new Intent(getActivity(), CreatePersonalProfile.class);
                                            i.putExtra("userdata", dataObj + "");
                                            startActivityForResult(i, 102);
                                        }
                                    }

                                }
                            }


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }


    public void editProfile(int profileType) {

        if (profileType == 0) {
            Intent i = new Intent(getActivity(), CreatePersonalProfile.class);
            i.putExtra("userdata", dataObj + "");
            startActivityForResult(i, 102);


        } else if (profileType == 1) {
            Intent intent = new Intent(mContext, CreateBuisnessProfile.class);
            intent.putExtra("userdata", dataObj + "");
            intent.putExtra("type", "2"); //type 1 for personal  2 for buisness 4 for both
            startActivityForResult(intent, 101);

        }


    }


    @Override
    public void onResume() {
        super.onResume();
        getProfileData();

        int count = Integer.parseInt(new SaveImpPrefrences().reterivePrefrence(getActivity(), DefaultConstatnts.notification_count).toString());
        if (count > 0) {
            id_txt_count.setText(count + "");
            id_txt_count.setVisibility(View.VISIBLE);
        }
        else
        {
            id_txt_count.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            // getProfileData();
        }
    }


}


