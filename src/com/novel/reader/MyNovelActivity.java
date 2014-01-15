package com.novel.reader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.vending.billing.InAppBillingForNovel;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AdRequest.ErrorCode;
import com.google.analytics.tracking.android.EasyTracker;
import com.kosbrother.fragments.MyBookcaseFragment;
import com.kosbrother.fragments.MyDownloadFragment;
import com.kosbrother.tool.Report;
import com.novel.reader.util.Setting;
import com.viewpagerindicator.TitlePageIndicator;

public class MyNovelActivity extends SherlockFragmentActivity {

    private static final int          ID_SETTING  = 0;
    private static final int          ID_RESPONSE = 1;
    private static final int          ID_ABOUT_US = 2;
    private static final int          ID_GRADE    = 3;
    private static final int          ID_Report   = 6;
    private String[]                  CONTENT;
    private AlertDialog.Builder       aboutUsDialog;
    
    private ViewPager                 pager;
    private FragmentStatePagerAdapter adapter;
    
    private InAppBillingForNovel iap;
	private LinearLayout bannerAdView;

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.setApplicationActionBarTheme(this);
        setContentView(R.layout.simple_titles);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(getResources().getString(R.string.title_my_bookcase));
        ab.setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.collections);

        adapter = new NovelPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("noti")) {
                pager.setCurrentItem(1);
            }
        }

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        setAboutUsDialog();
       
        bannerAdView = (LinearLayout) findViewById(R.id.adonView);
        if(Setting.getSetting(Setting.keyYearSubscription, this) ==  0)
        	iap = new InAppBillingForNovel(this, bannerAdView);

    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (iap != null && iap.mHelper != null) {
        	try {
        		iap.mHelper.dispose();
        	}catch (IllegalArgumentException ex){
                ex.printStackTrace();
            }finally{}
        	
        	iap.mHelper = null;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iap.mHelper == null) return;

        if (!iap.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);

        menu.add(0, ID_SETTING, 0, getResources().getString(R.string.menu_settings)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_RESPONSE, 1, getResources().getString(R.string.menu_respond)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_ABOUT_US, 2, getResources().getString(R.string.menu_aboutus)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_GRADE, 3, getResources().getString(R.string.menu_recommend)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_Report, 6, getResources().getString(R.string.menu_report)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        if(Setting.getSetting(Setting.keyYearSubscription, this) ==  0)
        	menu.add(0, 7, 7, getResources().getString(R.string.buy_year_subscription)).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            finish();
            // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
            break;
        case ID_SETTING: // setting
            Intent intent = new Intent(MyNovelActivity.this, SettingActivity.class);
            startActivity(intent);
            break;
        case ID_RESPONSE: // response
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.respond_mail_address) });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.respond_mail_title));
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            break;
        case ID_ABOUT_US:
            aboutUsDialog.show();
            break;
        case ID_GRADE:
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.recommend_url)));
            startActivity(browserIntent);
            break;
        case ID_Report:
        	Report.createReportDialog(this,getResources().getString(R.string.report_not_novel_problem),getResources().getString(R.string.report_not_article_problem));
            break;
        case 7:
        	iap.launchSubscriptionFlow();
        	break;
        }
        return true;
    }

    class NovelPagerAdapter extends FragmentStatePagerAdapter {
        public NovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // 這邊要寫兩個不同的Fragment, 分別取書櫃資 & 下載資料
        @Override
        public Fragment getItem(int position) {
            Fragment kk = new Fragment();
            if (position == 0) {
                kk = MyBookcaseFragment.newInstance();
            } else if (position == 1) {
                kk = MyDownloadFragment.newInstance();
            }
            return kk;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

    private void setAboutUsDialog() {
        // TODO Auto-generated method stub
        aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string)).setIcon(R.drawable.play_store_icon)
                .setMessage(getResources().getString(R.string.about_us))
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int position = pager.getCurrentItem();
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
    }

    public void rotationHoriztion(int beganDegree, int endDegree, AdView view) {
        final float centerX = 320 / 2.0f;
        final float centerY = 48 / 2.0f;
        final float zDepth = -0.50f * view.getHeight();

        Rotate3dAnimation rotation = new Rotate3dAnimation(beganDegree, endDegree, centerX, centerY, zDepth, true);
        rotation.setDuration(1000);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(rotation);
    }
    
    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance().activityStop(this);
    }

}
