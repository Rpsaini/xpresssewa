package transfer.money.com.xpresssewa.viewpager;
import android.content.Intent;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import transfer.money.com.xpresssewa.R;
import transfer.money.com.xpresssewa.registration.SignInActivity;
import transfer.money.com.xpresssewa.savePrefrences.SaveImpPrefrences;
import transfer.money.com.xpresssewa.util.DefaultConstatnts;

public class SkipPagerAdapter extends PagerAdapter {

    private SwipeViewPager mContext;

    private ViewGroup layout;


    public SkipPagerAdapter(SwipeViewPager context) {
        mContext = context;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);

        TextView tv_title=layout.findViewById(R.id.tv_title);
        TextView tv_desc=layout.findViewById(R.id.tv_desc);

        Typeface face=Typeface.createFromAsset(mContext.getAssets(), "MontserratRegular.ttf");
        tv_title.setTypeface(face);
        tv_desc.setTypeface(face);

//        layout.findViewById(R.id.ll_skip).setTag(position);
//        layout.findViewById(R.id.ll_skip).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (position < ModelObject.values().length - 1)
//                {
//                    mContext.setCurrentItem(Integer.parseInt(v.getTag() + "") + 1);
//                } else {
////                    Intent intent = new Intent(mContext, HomeScreen.class);
////                    mContext.startActivity(intent);
////                    mContext.finishAffinity();
//                }
//            }
//        });



        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    TextView txt_description ;
    boolean isAni_in_progress=false;
    public void animateView(int pos)
    {

//        System.out.println("layout ===="+layout);
        if(pos==0) {

        }
        else if(pos==1)
        {

        }
        else
        {

           TextView getStart= layout.findViewById(R.id.getSTart);
           if(getStart!=null) {
               getStart.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Intent i=new Intent(mContext,SignInActivity.class);
                       new SaveImpPrefrences().savePrefrencesData(mContext,"Yes",DefaultConstatnts.IsTutorialDone);
                       i.putExtra(DefaultConstatnts.IsShowPin,"yes");
                       mContext.startActivity(i);
                       mContext.finishAffinity();
                   }
               });
           }
        }

//        if(!isAni_in_progress)
//        {
//
//            animationForViews.handleAnimation(mContext, txt_description, 500, -300, 00, IsAnimationEndedCallback.translationX, new IsAnimationEndedCallback() {
//                @Override
//                public void getAnimationStatus(String status) {
//
//                    switch (status) {
//                        case IsAnimationEndedCallback.animationCancel: {
//                            break;
//                        }
//                        case IsAnimationEndedCallback.animationEnd: {
//                            isAni_in_progress = false;
//                            break;
//                        }
//
//                        case IsAnimationEndedCallback.animationRepeat: {
//                            break;
//                        }
//
//                        case IsAnimationEndedCallback.animationStart: {
//                            isAni_in_progress = true;
//                            break;
//                        }
//
//                    }
//                }
//            });
//        }


    }

}
