package com.goka.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.goka.kenburnsview.KenBurnsView;
import com.goka.kenburnsview.LoopViewPager;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initializeKenBurnsView();
    }


    private void initializeKenBurnsView() {
        // KenBurnsView
        final KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.ken_burns_view);
        kenBurnsView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        kenBurnsView.setSwapMs(3750);
        kenBurnsView.setFadeInOutMs(750);

        // ResourceIDs
        List<Integer> resourceIDs = Arrays.asList(SampleImages.IMAGES_RESOURCE);
        kenBurnsView.loadResourceIDs(resourceIDs);

        // LoopViewListener
        LoopViewPager.LoopViewPagerListener listener = new LoopViewPager.LoopViewPagerListener() {
            @Override
            public View OnInstantiateItem(int page) {
                TextView counterText = new TextView(getApplicationContext());
                counterText.setText(String.valueOf(page));
                return counterText;
            }

            @Override
            public void onPageScroll(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                kenBurnsView.forceSelected(position);
            }

            @Override
            public void onPageScrollChanged(int page) {
            }
        };

        // LoopView
        LoopViewPager loopViewPager = new LoopViewPager(this, resourceIDs.size(), listener);

        FrameLayout viewPagerFrame = (FrameLayout) findViewById(R.id.view_pager_frame);
        viewPagerFrame.addView(loopViewPager);

        kenBurnsView.setPager(loopViewPager);
    }

}