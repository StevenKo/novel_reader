package com.admobsdk_java_server_refresh;

import android.app.Activity;
import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.doubleclick.*;
import com.google.ads.mediation.*;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

public class CustomAd extends Activity implements CustomEventBanner, AdListener {
	private CustomEventBannerListener bannerListener;
	private DfpAdView adView;

	@Override
	public void requestBannerAd(final CustomEventBannerListener listener,
			final Activity activity, String label, String serverParameter,
			AdSize adSize, MediationAdRequest mediationAdRequest, Object extra) {
		
		// Keep the custom event listener for use later.
		this.bannerListener = listener;
		
		// Determine the best ad format to use given the adSize. If the adSize
		// isn't appropriate for any format, an ad will not fill.	
		AdSize bestAdSize = adSize.findBestSize(AdSize.BANNER);
		if (bestAdSize == null) {
			listener.onFailedToReceiveAd();
			return;
		}
		
		//AdSize bestAdSize = adSize.findBestSize(AdSize.BANNER);
		//if (bestAdSize == null) {
		//	listener.onFailedToReceiveAd();
		//	return;
		//}
		
		// Initialize an AdView with the bestAdSize and the publisher ID.
		// The publisher ID is the server parameter that you gave when creating
		// the custom event.
		
		this.adView = new DfpAdView(activity, AdSize.BANNER, serverParameter);
		
		// Set the listener to register for events.
		this.adView.setAdListener(this);
		
		// Generate an ad request using custom targeting values provided in the
		// MediationAdRequest.
		AdRequest adRequest = new AdRequest();
		
		// Load the ad with the ad request.
		this.adView.loadAd(adRequest);
	}

	@Override
	public void destroy() {
		
		// The destroy method gets called when the mediation framework refreshes
		// and removes the custom event. Perform any necessary cleanup here.
		if (this.adView != null) {
			this.adView.destroy();
		}
	}

	@Override
	public void onDismissScreen(Ad ad) {
		this.bannerListener.onDismissScreen();
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
		this.bannerListener.onFailedToReceiveAd();
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		this.bannerListener.onLeaveApplication();
	}

	@Override
	public void onPresentScreen(Ad ad) {
		this.bannerListener.onClick();
		this.bannerListener.onPresentScreen();
	}

	@Override
	public void onReceiveAd(Ad ad) {
		this.bannerListener.onReceivedAd(this.adView);
	}
}