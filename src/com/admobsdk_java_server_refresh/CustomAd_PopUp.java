package com.admobsdk_java_server_refresh;

import android.app.Activity;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class CustomAd_PopUp implements CustomEventInterstitial, AdListener {
	private CustomEventInterstitialListener interstitialListener;
	private InterstitialAd interstitial;

	@Override
	public void requestInterstitialAd(
			final CustomEventInterstitialListener listener,
			final Activity activity, String label, String serverParameter,
			MediationAdRequest mediationAdRequest, Object extra) {
		// Keep the custom event listener for use later.
		this.interstitialListener = listener;

		// Initialize an InterstitialAd, using the publisher ID provided from
		// the
		// serverParameter you gave when creating the custom event.
		this.interstitial = new InterstitialAd(activity, serverParameter);

		// Set the listener to register for events.
		this.interstitial.setAdListener(this);

		// Generate an ad request using custom targeting values provided in the
		// MediationAdRequest.
		AdRequest adRequest = new AdRequest();		

		// Load the interstitial with the ad request.
		this.interstitial.loadAd(adRequest);
	}

	@Override
	public void showInterstitial() {
		// This method should only be called after the custom event notifies
		// mediation that it has received an ad. Therefore, it is safe to assume
		// that the interstitial is ready to be displayed.
	    Log.d("mobmax","showInterstitial() from CustomAd_PopUp");
	    this.interstitial.show();
	}

	@Override
	public void destroy() {
		// The destroy method gets called when the mediation framework refreshes
		// and removes the custom event. Perform any necessary cleanup here.
	    Log.d("mobmax","destroy() from CustomAd_PopUp");
	}

	@Override
	public void onDismissScreen(Ad ad) {
		Log.d("mobmax", "onDismissScreen() from CustomAd_PopUp");
		this.interstitialListener.onDismissScreen();
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {
		Log.d("mobmax", "onFailedToReceiveAd() from CustomAd_PopUp");
		this.interstitialListener.onFailedToReceiveAd();
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		Log.d("mobmax", "onLeaveApplication() from CustomAd_PopUp");
		this.interstitialListener.onLeaveApplication();
	}

	@Override
	public void onPresentScreen(Ad ad) {
		Log.d("mobmax", "onPresentScreen() from CustomAd_PopUp");
		this.interstitialListener.onPresentScreen();
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("mobmax", "onReceiveAd() from CustomAd_PopUp");
		this.interstitialListener.onReceivedAd();
	}
}
