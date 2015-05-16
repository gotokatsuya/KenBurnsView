KenBurnsView
============

Android ImageViews are animated by Ken Burns Effect.

![](https://img.shields.io/badge/Android%20Arsenal-KenBurnsView-brightgreen.svg?style=flat)


## Demo
![Demo](https://github.com/gotokatsuya/KenBurnsView/blob/master/demo.gif)


## How to use
```java
private void initializeKenBurnsView(){
    // KenBurnsView
    final KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.ken_burns_view);
    // kenBurnsView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    // File path, or a uri or url
    List<String> urls = Arrays.asList(SampleImages.IMAGES_URL);
    kenBurnsView.initStrings(urls);

    // ResourceID
    //List<Integer> resourceIDs = Arrays.asList(SampleImages.IMAGES_RESOURCE);
    //kenBurnsView.initResourceIDs(resourceIDs);

    // MIX (String & Integer)
    //List<Object> mixingList = Arrays.asList(SampleImages.IMAGES_MIX);
    //kenBurnsView.initMixing(mixingList);

    // LoopViewListener
    LoopViewPager.LoopViewPagerListener listener = new LoopViewPager.LoopViewPagerListener() {
        @Override
        public View OnInstantiateItem(int page) {
            TextView counterText = new TextView(getApplicationContext());
            counterText.setText(page + "");
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
    LoopViewPager loopViewPager = new LoopViewPager(this, urls.size(), listener);

    //LoopViewPager loopViewPager = new LoopViewPager(this, resourceIDs.size(), listener);

    //LoopViewPager loopViewPager = new LoopViewPager(this, mixingList.size(), listener);


    FrameLayout viewPagerFrame = (FrameLayout) findViewById(R.id.view_pager_frame);
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
    compile 'com.github.goka.kenburnsview:library:1.0.2'
}
```

## Release
### 1.0.1
Enable to load images from res.  
Enable to mix. (Enable to load images from url & res)

### 1.0.2
Fix a little issue. (Forgot to initialize a size)


## iOS
[CPKenburnsSlideshowView](https://github.com/muukii0803/CPKenburnsSlideshowView)


## Detail Demo Movie
[youtube](http://youtu.be/G2gJfT4tdnw)


## Library
Thanks for 
[Glide](https://github.com/bumptech/glide)

