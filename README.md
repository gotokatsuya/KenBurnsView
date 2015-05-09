KenBurnsView
============

Android ImageViews animated by Ken Burns Effect.

![](https://img.shields.io/badge/Android%20Arsenal-KenBurnsView-brightgreen.svg?style=flat)


## Demo
![Demo](https://github.com/gotokatsuya/KenBurnsView/blob/master/demo.gif)


## How to use
```java
private void kenburnsviewInitialize(){
    //urls
    List<String> urls = Arrays.asList(SampleImages.IMAGES20);

    //KenBurnsView
    final KenBurnsView kenBurnsView = (KenBurnsView)findViewById(R.id.ken_burns_view);
    kenBurnsView.initUrls(urls);

    //LoopViewPagerListener
    LoopViewPager.LoopViewPagerListener listener = new LoopViewPager.LoopViewPagerListener() {
        @Override
        public View OnInstantiateItem(int page) {
            // If you need page counter
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

    //LoopViewPager
    LoopViewPager loopViewPager = new LoopViewPager(this, urls.size(), listener);
    FrameLayout viewPagerFrame = (FrameLayout)findViewById(R.id.view_pager_frame);
    viewPagerFrame.addView(loopViewPager);

    kenBurnsView.setPager(loopViewPager);
}
```
You should check [sample code](https://github.com/gotokatsuya/KenBurnsView/blob/master/app/src/main/java/com/goka/sample/MainActivity.java).



## Gradle
```java
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.goka.kenburnsview:library:1.0.0'
}
```

## iOS
[CPKenburnsSlideshowView](https://github.com/muukii0803/CPKenburnsSlideshowView)


## Detail Demo Movie
[youtube](http://youtu.be/G2gJfT4tdnw)


## Library
Thanks for 
[Glide](https://github.com/bumptech/glide)

