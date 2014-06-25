package com.ads;

import android.view.Display;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.novel.reader.LogAdListener;
import com.novel.reader.R;
import com.novel.reader.R.string;

public class AdFragmentListActivity extends SherlockListActivity{
	protected AdView mAdView;
	
	@Override
    protected void onPause() {
		if(mAdView != null)
			mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView != null)
        	mAdView.resume();
    }

    @Override
    protected void onDestroy() {
    	if(mAdView != null)
    		mAdView.destroy();
        super.onDestroy();
    }
    
	private static InterstitialAd interstitial;
	
	private AdView getBannerAdRequest(final RelativeLayout adBannerLayout){

		final AdView adMobAdView = new AdView(this);
        adMobAdView.setAdUnitId(getResources().getString(R.string.admobKey));
        adMobAdView.setAdSize(AdSize.SMART_BANNER);
        adMobAdView.setAdListener(new LogAdListener(this){
			@Override
		    public void onAdLoaded() {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				if(adMobAdView.getParent() == null)
					adBannerLayout.addView(adMobAdView,params);
			}
		});
		adMobAdView.loadAd(new AdRequest.Builder().build());
		
		return adMobAdView;
		
	}
	
	public AdView setBannerAdView(RelativeLayout adBannerLayout){
		try {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // deprecated

            if (width > 320) {
            	return getBannerAdRequest(adBannerLayout);
            }
        } catch (Exception e) {

        }
		return null;
	}
	
	public void requestInterstitialAd(){
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(getResources().getString(R.string.InterstitialAd));
		interstitial.setAdListener(new LogAdListener(this){
			@Override
		    public void onAdLoaded() {
				interstitial.show();
			}
		});
		interstitial.loadAd(new AdRequest.Builder().build());	    
		
	}

}
