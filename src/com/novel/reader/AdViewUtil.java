package com.novel.reader;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;


public class AdViewUtil {
	
	private static final String MY_INTERSTITIAL_UNIT_ID = "d875d0cc47ef403b";
	private static String admobKey = "292fbab7f4ea4848";
	private static InterstitialAd interstitial;
	
	private static void getBannerAdRequest(LinearLayout adBannerLayout, Context ctx){

        AdView adMobAdView = new AdView((Activity) ctx);
        adMobAdView.setAdUnitId(admobKey);
        adMobAdView.setAdSize(AdSize.SMART_BANNER);
        adMobAdView.setAdListener(new LogAdListener(ctx));
		adMobAdView.loadAd(new AdRequest.Builder().build());
		adBannerLayout.addView(adMobAdView);
		
	}
	
	public static void setBannerAdView(LinearLayout adBannerLayout, Context ctx){
		try {
            Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // deprecated

            if (width > 320) {
            	getBannerAdRequest(adBannerLayout, ctx);
            }
        } catch (Exception e) {

        }
	}
	
	public static void requestInterstitialAd(Activity mActivity){
		interstitial = new InterstitialAd(mActivity);
		interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);
		interstitial.setAdListener(new LogAdListener(mActivity){
			@Override
		    public void onAdLoaded() {
				interstitial.show();
			}
		});
		interstitial.loadAd(new AdRequest.Builder().build());	    
		
	}

}
