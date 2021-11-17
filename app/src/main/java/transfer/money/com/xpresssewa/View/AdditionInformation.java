package transfer.money.com.xpresssewa.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.annotations.Nullable;
import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.Adapter.TransferPurposeAdapter;
import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.communication.ServerHandler;
import transfer.money.com.xpresssewa.interfaces.CallBack;
import transfer.money.com.xpresssewa.util.AnimationForView;
import transfer.money.com.xpresssewa.util.IsAnimationEndedCallback;
import transfer.money.com.xpresssewa.util.SimpleDialog;
import transfer.money.com.xpresssewa.validation.Showtoast;

public class AdditionInformation extends AppCompatActivity {
AnimationForView animationForViews=new AnimationForView();
private int screenHeight,screenWidth;
private ArrayList<JSONObject> TransferPurpose,TransferReference;
public String selected_reason_Id="",selected_reference="";
public Dialog reasonDialog;
    public LinearLayout ll_if_other;
    TextView txt_enter_other;
public  View downView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_information);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        TransferPurpose=new ArrayList<>();
        TransferReference=new ArrayList<>();
        getScreenHeight();
        getTransferPurpose();
        init();
    }

    private void init()
    {
        ll_if_other=findViewById(R.id.ll_if_other);
        txt_enter_other=findViewById(R.id.txt_enter_other);
        findViewById(R.id.headerbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ll_selectReasonDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showReaonDialog(0);
            }
        });

        findViewById(R.id.ll_selectreference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReaonDialog(1);
            }
        });


        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_reason_Id.length()==0)
                {
                    new Showtoast().showToast(AdditionInformation.this, "Required", "Select a reason", findViewById(R.id.ll_linearlayoutadditional));
                     return;
                }
               else  if(selected_reference.length()==0)
                {
                    new Showtoast().showToast(AdditionInformation.this, "Required", "Select reference", findViewById(R.id.ll_linearlayoutadditional));
                    return;
                }
             else if(ll_if_other.getVisibility()==View.VISIBLE)
                {
                    if(txt_enter_other.getText().length()>0)
                    {
                        Intent intent=new Intent(AdditionInformation.this,ChoosePaymentOptions.class);
                        intent.putExtra("calculationData",getIntent().getStringExtra("calculationData"));
                        intent.putExtra("selectedRecipientData",getIntent().getStringExtra("selectedRecipientData"));
                        intent.putExtra("purposeID",selected_reason_Id);
                        intent.putExtra("refrenceId",selected_reference);
                        startActivityForResult(intent,1001);
                    }
                    else
                    {
                        new Showtoast().showToast(AdditionInformation.this, "Required", "Please give us other reference", findViewById(R.id.ll_linearlayoutadditional));
                    }
                }
                else
                {
                    Intent intent=new Intent(AdditionInformation.this,ChoosePaymentOptions.class);
                    intent.putExtra("calculationData",getIntent().getStringExtra("calculationData"));
                    intent.putExtra("selectedRecipientData",getIntent().getStringExtra("selectedRecipientData"));
                    intent.putExtra("purposeID",selected_reason_Id);
                    intent.putExtra("refrenceId",selected_reference);
                    startActivityForResult(intent,1001);
                }
            }
        });

    }

    private void showReaonDialog(int type)
    {
        final SimpleDialog simpleDialog = new SimpleDialog();
        reasonDialog = simpleDialog.simpleDailog(this, R.layout.transfer_purpose_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
         downView = reasonDialog.findViewById(R.id.ll_relativelayout);
          TextView transfer_title =reasonDialog.findViewById(R.id.transfer_title);
          reasonDialog.findViewById(R.id.ll_relativelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, reasonDialog);
            }
        });

        reasonDialog.findViewById(R.id.img_hideview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSourceDestinationView(downView, reasonDialog);
            }
        });
        animateUp(screenHeight, downView);
        ArrayList<JSONObject> commonObj;
        if(type==0)
        {
            commonObj=TransferPurpose;
            transfer_title.setText("Transfer Purpose");
        }
        else
        {
            commonObj=TransferReference;
            transfer_title.setText("Reference By");
        }



        RecyclerView recyclerViewForTransferReason =reasonDialog.findViewById(R.id.recycler_view_for_reason);
        TransferPurposeAdapter transferPurposeAdater = new TransferPurposeAdapter(commonObj, this,type);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewForTransferReason.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewForTransferReason.setItemAnimator(new DefaultItemAnimator());
        recyclerViewForTransferReason.setAdapter(transferPurposeAdater);

    }

    public void animateUp(int height, View sourcedestinationcontainer) {
        animationForViews.handleAnimation(AdditionInformation.this, sourcedestinationcontainer, 500, height, 00, IsAnimationEndedCallback.translationY, new IsAnimationEndedCallback() {
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

    private void getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }



    private void getTransferPurpose()
    {
        Map<String,String> m=new LinkedHashMap<>();;
        new ServerHandler().sendToServer(this, "Reason", m, 0,1, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {

                try
                {
                    TransferPurpose.clear();
                    TransferReference.clear();

                    JSONObject obj = new JSONObject(dta);
                    System.out.println("response reason=="+obj);
                    if(obj.getBoolean("status")) {

                        JSONArray jsonObjectsAr=obj.getJSONArray("TransferPurpose");
                        JSONArray transferReferenceObj=obj.getJSONArray("TransferReference");

                        for(int x=0;x<jsonObjectsAr.length();x++)
                        {
                            TransferPurpose.add(jsonObjectsAr.getJSONObject(x));
                        }
                        for(int x=0;x<transferReferenceObj.length();x++)
                        {
                            TransferReference.add(transferReferenceObj.getJSONObject(x));
                        }
                      } else {
                        new Showtoast().showToast(AdditionInformation.this, "Response", obj.getString("Message"), findViewById(R.id.ll_linearlayoutadditional));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)
        {
            if(data!=null) {
                Intent intent = new Intent();
                intent.putExtra("data", data.getStringExtra("data"));
                intent.putExtra("isSendScreenDataLoaded", data.getStringExtra("isSendScreenDataLoaded"));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}