package com.goka.kenburnsview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

/**
 * Created by katsuyagoto on 2015/04/17.
 */
public class KenBurnsView extends FrameLayout {

    private enum LoadType {
        String,      // A file path, or a uri or url (default)
        ResourceID,  // The id of the resource containing the image
        MIXING       // String & Resource
    }

    private LoadType mLoadType = LoadType.String;

    private static final int NUM_OF_IMAGE_VIEWS = 3;

    private static final int FIRST_IMAGE_VIEW_INDEX = 0;
    private static final int SECOND_IMAGE_VIEW_INDEX = 1;
    private static final int THIRD_IMAGE_VIEW_INDEX = 2;

    private static final String PROPERTY_ALPHA = "alpha";

    private final Handler mHandler;

    private Context mContext;

    private ImageView[] mImageViews;

    private FrameLayout mRootLayout;

    private final Random mRandom = new Random();

    private int mSwapMs = 5500;

    private int mFadeInOutMs = 500;

    private float mMaxScaleFactor = 1.5F;

    private float mMinScaleFactor = 1.0F;

    private int mPosition = 0;

    private int mPreviousPosition = 0;

    private int mActiveImageIndex = -1;

    private List<String> mStrings;

    private List<Integer> mResourceIDs;

    private List<Object> mMixingList;

    private LoopViewPager mLoopViewPager;

    private ImageView.ScaleType mScaleType = null;

    private static int sCachedSizeForLoadType;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
        mContext = context;
    }

    public void setScaleType(ImageView.ScaleType mScaleType) {
        this.mScaleType = mScaleType;
    }

    public void setPager(LoopViewPager mPager) {
        this.mLoopViewPager = mPager;
    }

    public void forceSelected(int position) {
        mPreviousPosition = mPosition;
        if (mHandler != null) {
            stopKenBurnsAnimation();
            startForceKenBurnsAnimation();
        }
        mPosition = position;
    }


    private void forceSwapImage() {

        if (mImageViews.length <= 0) {
            return;
        }

        if (mActiveImageIndex == -1) {
            mActiveImageIndex = FIRST_IMAGE_VIEW_INDEX;
            animate(mImageViews[mActiveImageIndex]);
            return;
        }

        int inactiveIndex = mActiveImageIndex;

        if (mPreviousPosition >= mPosition) {
            mActiveImageIndex = swapDirection(mActiveImageIndex, true);
        } else {
            mActiveImageIndex = swapDirection(mActiveImageIndex, false);
        }

        if (mPreviousPosition == 0 && mPosition == getSizeByLoadType() - 1) {
            mActiveImageIndex = swapDirection(mActiveImageIndex, true);
        }

        if (mPreviousPosition == getSizeByLoadType() - 1 && mPosition == 0) {
            mActiveImageIndex = swapDirection(mActiveImageIndex, false);
        }

        if (mActiveImageIndex >= mImageViews.length) {
            mActiveImageIndex = FIRST_IMAGE_VIEW_INDEX;
        }

        final ImageView activeImageView = mImageViews[mActiveImageIndex];
        loadImages(mPosition, mActiveImageIndex);
        activeImageView.setAlpha(0.0f);

        ImageView inactiveImageView = mImageViews[inactiveIndex];

        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(inactiveImageView, PROPERTY_ALPHA, 1.0f, 0.0f),
                ObjectAnimator.ofFloat(activeImageView, PROPERTY_ALPHA, 0.0f, 1.0f)
        );
        animatorSet.start();
    }

    private int swapDirection(final int activeIndex, boolean isPrevious) {
        if (activeIndex == FIRST_IMAGE_VIEW_INDEX) {
            if (isPrevious) {
                return THIRD_IMAGE_VIEW_INDEX;
            } else {
                return SECOND_IMAGE_VIEW_INDEX;
            }
        } else if (activeIndex == SECOND_IMAGE_VIEW_INDEX) {
            if (isPrevious) {
                return FIRST_IMAGE_VIEW_INDEX;
            } else {
                return THIRD_IMAGE_VIEW_INDEX;
            }

        } else if (activeIndex == THIRD_IMAGE_VIEW_INDEX) {
            if (isPrevious) {
                return SECOND_IMAGE_VIEW_INDEX;
            } else {
                return FIRST_IMAGE_VIEW_INDEX;
            }
        }
        return FIRST_IMAGE_VIEW_INDEX;
    }


    private void autoSwapImage() {

        if (mImageViews.length <= 0) {
            return;
        }

        if (mActiveImageIndex == -1) {
            mActiveImageIndex = FIRST_IMAGE_VIEW_INDEX;
            animate(mImageViews[mActiveImageIndex]);
            return;
        }

        int inactiveIndex = mActiveImageIndex;
        mActiveImageIndex = (1 + mActiveImageIndex);

        if (mActiveImageIndex >= mImageViews.length) {
            mActiveImageIndex = FIRST_IMAGE_VIEW_INDEX;
        }

        if (mLoopViewPager != null) {
            mPosition++;

            if (mPosition >= getSizeByLoadType()) {
                mPosition = 0;
            }

            mLoopViewPager.setCurrentItemAfterCancelListener(mPosition, false);
        }

        final ImageView activeImageView = mImageViews[mActiveImageIndex];
        loadImages(mPosition, mActiveImageIndex);
        activeImageView.setAlpha(0.0f);

        ImageView inactiveImageView = mImageViews[inactiveIndex];

        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(inactiveImageView, PROPERTY_ALPHA, 1.0f, 0.0f),
                ObjectAnimator.ofFloat(activeImageView, PROPERTY_ALPHA, 0.0f, 1.0f)
        );
        animatorSet.start();
    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX,
            float fromTranslationY, float toTranslationX, float toTranslationY) {
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        ViewPropertyAnimator propertyAnimator = view.animate().
                translationX(toTranslationX).
                translationY(toTranslationY).
                scaleX(toScale).
                scaleY(toScale).
                setDuration(duration);
        propertyAnimator.start();
    }

    private float pickScale() {
        return this.mMinScaleFactor + this.mRandom.nextFloat() * (this.mMaxScaleFactor - this.mMinScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.mRandom.nextFloat() - 0.5f);
    }

    public void animate(ImageView view) {
        float fromScale = pickScale();
        float toScale = pickScale();
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX,
                toTranslationY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopKenBurnsAnimation();
    }

    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            autoSwapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
        }
    };

    private Runnable mForceSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            forceSwapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
        }
    };

    private void stopKenBurnsAnimation() {
        mHandler.removeCallbacks(mSwapImageRunnable);
        mHandler.removeCallbacks(mForceSwapImageRunnable);
    }

    private void startKenBurnsAnimation() {
        mHandler.post(mSwapImageRunnable);
    }

    private void startForceKenBurnsAnimation() {
        mHandler.post(mForceSwapImageRunnable);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.ken_burns_view, this);
        mRootLayout = (FrameLayout) view.findViewById(R.id.ken_burns_root);
    }

    public void loadStrings(List<String> strings) {
        mLoadType = LoadType.String;
        sCachedSizeForLoadType = 0;
        mStrings = strings;
        if (mRootLayout != null) {
            initImageViews(mRootLayout);
        }
    }

    public void loadResourceIDs(List<Integer> resourceIDs) {
        mLoadType = LoadType.ResourceID;
        sCachedSizeForLoadType = 0;
        mResourceIDs = resourceIDs;
        if (mRootLayout != null) {
            initImageViews(mRootLayout);
        }
    }

    public void loadMixing(List<Object> mixingList) {
        mLoadType = LoadType.MIXING;
        sCachedSizeForLoadType = 0;
        mMixingList = mixingList;
        if (mRootLayout != null) {
            initImageViews(mRootLayout);
        }
    }

    private void initImageViews(FrameLayout root) {

        mImageViews = new ImageView[NUM_OF_IMAGE_VIEWS];

        for (int i = NUM_OF_IMAGE_VIEWS - 1; i >= 0; i--) {
            mImageViews[i] = new ImageView(mContext);

            if (i != 0) {
                mImageViews[i].setAlpha(0.0f);
            }

            if (mScaleType != null) {
                mImageViews[i].setScaleType(mScaleType);
            }

            mImageViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            root.addView(mImageViews[i]);
        }

        loadImages(0, FIRST_IMAGE_VIEW_INDEX);
    }

    private void loadImages(final int position, final int activeIndex) {
        loadImage(position, activeIndex);

        int prePosition  = position - 1;
        int nextPosition = position + 1;

        if (prePosition < 0) {
            prePosition = getSizeByLoadType() - 1;
        }

        if (nextPosition > getSizeByLoadType() - 1) {
            nextPosition = 0;
        }

        switch (activeIndex) {
            case FIRST_IMAGE_VIEW_INDEX:
                if (position != prePosition) {
                    loadImage(prePosition, THIRD_IMAGE_VIEW_INDEX);
                }
                if (position != nextPosition) {
                    loadImage(nextPosition, SECOND_IMAGE_VIEW_INDEX);
                }
                break;
            case SECOND_IMAGE_VIEW_INDEX:
                if (position != prePosition) {
                    loadImage(prePosition, FIRST_IMAGE_VIEW_INDEX);
                }
                if (position != nextPosition) {
                    loadImage(nextPosition, THIRD_IMAGE_VIEW_INDEX);
                }
                break;
            case THIRD_IMAGE_VIEW_INDEX:
                if (position != prePosition) {
                    loadImage(prePosition, SECOND_IMAGE_VIEW_INDEX);
                }
                if (position != nextPosition) {
                    loadImage(nextPosition, FIRST_IMAGE_VIEW_INDEX);
                }
                break;
        }
    }

    private int getSizeByLoadType() {
        if (sCachedSizeForLoadType > 0) {
            return sCachedSizeForLoadType;
        }
        switch (mLoadType) {
            case String:
                sCachedSizeForLoadType = mStrings.size();
                break;
            case ResourceID:
                sCachedSizeForLoadType = mResourceIDs.size();
                break;
            case MIXING:
                sCachedSizeForLoadType = mMixingList.size();
                break;
        }
        return sCachedSizeForLoadType;
    }

    private void loadImage(final int position, final int imageIndex) {
        switch (mLoadType) {
            case String:
                Glide.with(mContext).load(mStrings.get(position)).into(mImageViews[imageIndex]);
                break;
            case ResourceID:
                Glide.with(mContext).load(mResourceIDs.get(position)).into(mImageViews[imageIndex]);
                break;
            case MIXING:
                Object object = mMixingList.get(position);
                if (object.getClass() == String.class) {
                    String string = (String) object;
                    Glide.with(mContext).load(string).into(mImageViews[imageIndex]);
                } else if (object.getClass() == Integer.class) {
                    Integer integer = (Integer) object;
                    Glide.with(mContext).load(integer).into(mImageViews[imageIndex]);
                }
                break;
        }
    }

    public void setSwapMs(int mSwapMs) {
        this.mSwapMs = mSwapMs;
    }

    public void setFadeInOutMs(int mFadeInOutMs) {
        this.mFadeInOutMs = mFadeInOutMs;
    }

    public void setMaxScaleFactor(float mMaxScaleFactor) {
        this.mMaxScaleFactor = mMaxScaleFactor;
    }

    public void setMinScaleFactor(float mMinScaleFactor) {
        this.mMinScaleFactor = mMinScaleFactor;
    }
}
