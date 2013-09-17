package com.novel.reader;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

public class AdViewUtil {
	
	private static final String MY_INTERSTITIAL_UNIT_ID = "d875d0cc47ef403b";
	private static String admobKey = "292fbab7f4ea4848";
	private static InterstitialAd interstitial;
	
	private static void getBannerAdRequest(LinearLayout adBannerLayout, Context ctx){

        final AdRequest adReq = new AdRequest();
        AdView adMobAdView = new AdView((Activity) ctx, AdSize.SMART_BANNER, admobKey);
        adMobAdView.setAdListener(new AdListener() {
			@Override
			public void onDismissScreen(Ad arg0) {
				Log.d("admob_banner", "onDismissScreen");
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				Log.d("admob_banner", "onFailedToReceiveAd");
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				Log.d("admob_banner", "onLeaveApplication");
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				Log.d("admob_banner", "onPresentScreen");
			}

			@Override
			public void onReceiveAd(Ad ad) {
				Log.d("admob_banner", "onReceiveAd ad:" + ad.getClass());
			}

		});
		adMobAdView.loadAd(adReq);
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
		interstitial = new InterstitialAd(mActivity, MY_INTERSTITIAL_UNIT_ID);
	    AdRequest adRequest = new AdRequest();
	    interstitial.loadAd(adRequest);
	    interstitial.setAdListener(new AdListener() {
			@Override
			public void onDismissScreen(Ad arg0) {
				Log.d("admob_interstitial", "onDismissScreen");
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				Log.d("admob_interstitial", "onFailedToReceiveAd");
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				Log.d("admob_interstitial", "onLeaveApplication");
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				Log.d("admob_interstitial", "onPresentScreen");
			}

			@Override
			public void onReceiveAd(Ad ad) {
				Log.d("admob_interstitial", "onReceiveAd ad:" + ad.getClass());
				if (ad == interstitial) {
				      interstitial.show();
				    }
			}

		});
		
	}

}
