package com.novel.reader;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.kosbrother.fragments.MyBookmarkFragment;
import com.novel.reader.util.Setting;
import com.viewpagerindicator.TitlePageIndicator;

public class BookmarkActivity extends SherlockFragmentActivity{

   
    private boolean                              alertDeleteBookmark;
    SharedPreferences                            settings;
    private final String                         alertKey   = "alertDeleteBookmark";
    private String[]                  CONTENT;
    private ViewPager                 pager;
    private FragmentStatePagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Setting.setApplicationActionBarTheme(this);
        setContentView(R.layout.simple_titles);
        
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(getResources().getString(R.string.my_bookmark));
        ab.setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.bookmarks);

        adapter = new NovelPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("IS_RECNET")) {
                pager.setCurrentItem(1);
            }
        }
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        settings = getSharedPreferences(Setting.keyPref, 0);
        alertDeleteBookmark = settings.getBoolean(alertKey, true);
        AdViewUtil.setAdView((LinearLayout) findViewById(R.id.adonView), this);
        if (alertDeleteBookmark)
            showArticleDeleteDialog();
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
                kk = MyBookmarkFragment.newInstance(MyBookmarkFragment.BOOKMARK_VIEW);
            } else if (position == 1) {
                kk = MyBookmarkFragment.newInstance(MyBookmarkFragment.RECENT_READ_VIEW);
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

    @Override
    protected void onResume() {
        super.onResume();
//        new LoadDataTask().execute();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            finish();
            // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
            break;
        }
        return true;
    }


    

    private void showArticleDeleteDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.reminder)).setIcon(R.drawable.ic_stat_notify)
                .setMessage(getResources().getString(R.string.delete_bookmark_reminder))
                .setPositiveButton(getResources().getString(R.string.do_not_reminder), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        settings.edit().putBoolean(alertKey, false).commit();

                    }

                }).setNegativeButton(getResources().getString(R.string.reminder_next), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }

                }).show();

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
