package com.goka.kenburnsview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import com.goka.kenburnsview.*;

public class MainActivity extends ActionBarActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        //Get urls
        List<String> urls = Arrays.asList(SampleImages.IMAGES20);

        //KenBurnsView
        final KenBurnsView kenBurnsView = (KenBurnsView)findViewById(R.id.ken_burns_view);
        //kenBurnsView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        kenBurnsView.initUrls(urls);


        //LoopViewListener
        LoopViewPager.LoopViewPagerListener listener = new LoopViewPager.LoopViewPagerListener() {
            @Override
            public View OnInstantiateItem(int page) {
                TextView counterText = new TextView(getApplicationContext());
                counterText.setText(page + "");
                return counterText;
            }
            @Override
            public void onPageScroll(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                kenBurnsView.forceSelected(position);
            }
            @Override
            public void onPageScrollChanged(int page){}
        };

        //LoopView
        LoopViewPager loopViewPager = new LoopViewPager(this, urls.size(), listener);
        FrameLayout viewPagerFrame = (FrameLayout)findViewById(R.id.view_pager_frame);
        viewPagerFrame.addView(loopViewPager);

        kenBurnsView.setPager(loopViewPager);

    }

}
