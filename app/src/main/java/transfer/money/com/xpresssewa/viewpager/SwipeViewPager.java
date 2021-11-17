package transfer.money.com.xpresssewa.viewpager;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import me.anwarshahriar.calligrapher.Calligrapher;
import transfer.money.com.xpresssewa.R;

public class SwipeViewPager extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view_pager);
        Calligrapher calligrapher = new Calligrapher(getApplicationContext());
        calligrapher.setFont(this, "MontserratRegular.ttf", true);
        init();
    }

    private void init() {
        final SkipPagerAdapter skipPagerAdapter = new SkipPagerAdapter(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(skipPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                skipPagerAdapter.animateView(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setCurrentItem(int pos) {
        viewPager.setCurrentItem(pos);
    }
}
