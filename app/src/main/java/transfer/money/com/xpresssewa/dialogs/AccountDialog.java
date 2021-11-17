package transfer.money.com.xpresssewa.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.View.AccountFragment;

public class AccountDialog
{
    private Dialog dialog;
    private LinearLayout ll_price_comparison_hidden;
    private ImageView down_to_bottom;
    private Context ct;
    private LinearLayout v;
    private AccountFragment accountFragment;


    public void showAccountDialog(Context ct1, AccountFragment accountFragment1)
    {
        ct=ct1;
        this.accountFragment=accountFragment1;

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new Dialog(ct1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_profile_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        dialog.setCancelable(false);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        ll_price_comparison_hidden=dialog.findViewById(R.id.ll_price_comparison_hidden);
        down_to_bottom=dialog.findViewById(R.id.down_to_bottom);
        LinearLayout ll_alreay_added_user=dialog.findViewById(R.id.ll_alreay_added_user);
        TextView txt_username=dialog.findViewById(R.id.txt_username);
        LinearLayout ll_businessprofile_edit=dialog.findViewById(R.id.ll_businessprofile_edit);
        TextView txt_buisnessname=dialog.findViewById(R.id.txt_buisnessname);
        View view_lineboth=dialog.findViewById(R.id.view_lineboth);
        View view_linelast=dialog.findViewById(R.id.view_linelast);

        txt_username.setText(accountFragment1.usernameStr);
        if(accountFragment1.isUserPersonalProfileDOne)
        {
            dialog.findViewById(R.id.ll_my_profile).setVisibility(View.GONE);
            ll_alreay_added_user.setVisibility(View.VISIBLE);
            ll_alreay_added_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountFragment.editProfile(0);
                    hidePanel();
                }
            });

        }
        else
        {
            dialog.findViewById(R.id.ll_my_profile).setVisibility(View.VISIBLE);
            ll_alreay_added_user.setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_my_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountFragment.editProfile(0);
                    hidePanel();
                }
            });
        }

        if(accountFragment1.businessprofileName.length()==0)
        {
            ll_businessprofile_edit.setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_business_profile).setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_business_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountFragment. editProfile(1);
                    hidePanel();

                }
            });
        }
        else
        {
            ll_businessprofile_edit.setVisibility(View.GONE);
            dialog.findViewById(R.id.ll_business_profile).setVisibility(View.GONE);
            txt_buisnessname.setText(accountFragment1.businessprofileName);
            ll_businessprofile_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountFragment. editProfile(1);
                    hidePanel();

                }
            });
            view_lineboth.setVisibility(View.GONE);//todo
            view_linelast.setVisibility(View.GONE);
        }


        down_to_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUpDown();
            }
        });
        slideUpDown();
    }
    private void slideUpDown()
    {
        if (!isPanelShown())
        {
        Animation bottomUp = AnimationUtils.loadAnimation(ct, R.anim.bottom_up);
        ll_price_comparison_hidden.startAnimation(bottomUp);
        ll_price_comparison_hidden.setVisibility(View.VISIBLE);

        }
        else {
        // Hide the Panel
          hidePanel();

    }
    }
    private boolean isPanelShown() {
        return ll_price_comparison_hidden.getVisibility() == View.VISIBLE;
    }

    private void hidePanel()
    {
        Animation bottomDown = AnimationUtils.loadAnimation(ct,
                R.anim.bottom_down);

        ll_price_comparison_hidden.startAnimation(bottomDown);
        ll_price_comparison_hidden.setVisibility(View.GONE);
        bottomDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
