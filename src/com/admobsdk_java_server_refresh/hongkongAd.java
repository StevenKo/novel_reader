package com.admobsdk_java_server_refresh;

import android.app.Activity;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class hongkongAd implements CustomEventBanner{
	AdView adMobAdView;
	private static String admobKey = "a700a228c55c4c7e";
	@Override
	public void destroy() {
		if(adMobAdView!=null){
			adMobAdView.destroy();
			adMobAdView = null;
		}
	}
	@Override
	public void requestBannerAd(final CustomEventBannerListener listener, Activity activity,
			String label, String serverParameter, AdSize adSize, MediationAdRequest request,
			Object customEventExtra) {
		
		final AdRequest adReq = new AdRequest();
		final AdView adMobAdView = new AdView(activity, AdSize.SMART_BANNER, admobKey);
		adMobAdView.setAdListener(new AdListener() {
			@Override
			public void onDismissScreen(Ad arg0) {
				Log.d("hongkong_admob_banner", "onDismissScreen");
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				Log.d("hongkong_admob_banner", "onFailedToReceiveAd");
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				Log.d("hongkong_admob_banner", "onLeaveApplication");
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				Log.d("hongkong_admob_banner", "onPresentScreen");
			}

			@Override
			public void onReceiveAd(Ad ad) {
				Log.d("hongkong_admob_banner", "onReceiveAd ad:" + ad.getClass());
				listener.onReceivedAd(adMobAdView);
			}

		});
		adMobAdView.loadAd(adReq);
		
		
		
	}

}
