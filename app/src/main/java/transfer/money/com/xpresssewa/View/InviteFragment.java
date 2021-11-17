package transfer.money.com.xpresssewa.View;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class InviteFragment extends Fragment {

    View view;
    Context mContext;


    @BindView(R.id.iv_toolbar)
    ImageView iv_toolbar;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private String RefCode="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
       {
        view=inflater.inflate(R.layout.fragment_invite, container, false);
        Calligrapher calligrapher = new Calligrapher(getActivity());
        calligrapher.setFont(view, "MontserratRegular.ttf");
        mContext=getActivity();

        try {

            ButterKnife.bind(this, view);
            String login_detail = new SaveImpPrefrences().reterivePrefrence(getActivity(), "login_detail").toString();
            JSONObject jsonObject = new JSONObject(login_detail);
            RefCode=jsonObject.getString("RefCode");
            System.out.println("LOgin details===="+jsonObject);
            init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }
    private void init()
    {
//        {"Message":"success","UserName":"Tushar Bedi","UserId":"tusha10000","DestinationSymbol":"INR ","SourceSymbol":"AUD ","FlagImageDestination":"https:\/\/intremit.webcomsystems.net.au\/\/assets\/images\/flags\/PNGFlags\/in.png","FlagImageSource":"https:\/\/intremit.webcomsystems.net.au\/\/assets\/images\/flags\/PNGFlags\/au.png","SDCountryId":5,"CountryId":2,"CountryCode":"AU","CountryName":"Australia","UserImage":"","Email":"tushar.bx@gmail.com","ContactNumber":"998-815-1580","Pin":1234,"Version":"1","IsPhoneVerified":false,"RefCode":"tusha10000","CreatedDate":"2021-01-06T13:01:48","ReponseCode":1,"IsKycApproved":3,"MemberId":10000,"status":true}
        iv_toolbar.setImageResource(R.drawable.refer_icon);
        tv_title.setText(R.string.invite);
        iv_toolbar.setVisibility(View.GONE);


        view.findViewById(R.id.txt_copy_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Referral Code", RefCode);
                clipboard.setPrimaryClip(clip);

                new Showtoast().showToast(getActivity(),"Copy","Code copied",view.findViewById(R.id.ll_mainlayout));

            }
        });


        view.findViewById(R.id.tv_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDia("PlayStore_url",RefCode);

            }
        });

    }
    private void shareDia(String shareAppLink,String refCode)
    {
        try
        {
            String sarcode = "";
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "";

            sAux = "My "+getResources().getString(R.string.app_name)+" Referral code is "+refCode+" and App Link is " + "\n\n";
            sAux = sAux + sarcode;

            sAux = "My "+getResources().getString(R.string.app_name)+" Referral code is "+refCode+" and App Link is "+shareAppLink+", share with friend on each new user registration you will get 50 points and more Benefits.";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));





        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
