package com.android.novel.reader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

    // API:
    // 搜索: searchNovels(String),
    // 取某篇章節: getArticle(Article),
    // 取所有章節: getNovelArticles(int novelId, int page, boolean isOrderUp),

    // 熱門排行
    // getThisMonthHotNovels(),
    // getThisWeekHotNovels(),
    // getHotNovels(),
    // getCategoryRecommendNovels(int category_id),
    // getCategoryThisWeekHotNovels(int category_id),
    // getCategoryHotNovels(int category_id)

    // 取小說:
    // getNovel(int novelId)
    // getCategoryNovels(int category_id, int page)

    // 取cagegory: getCategories()

    private String[] CONTENT;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_titles);

        Resources res = getResources();
        CONTENT = res.getStringArray(R.array.sections);

        FragmentStatePagerAdapter adapter = new NovelPagerAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        pager.setCurrentItem(1);
       

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);

        menu.add("設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        menu.add("Search").setIcon(R.drawable.ic_search_inverse).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                search = (EditText) item.getActionView();
                search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                search.setInputType(InputType.TYPE_CLASS_TEXT);
                search.requestFocus();
                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            Bundle bundle = new Bundle();
                            bundle.putString("SearchKeyword", v.getText().toString());
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return true;
                        }

                        return false;
                    }
                });
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Auto-generated method stub
                search.setText("");
                return true;
            }
        }).setActionView(R.layout.collapsible_edittext).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    class NovelPagerAdapter extends FragmentStatePagerAdapter {
        public NovelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment kk = new Fragment();
            if (position == 0) {
                kk = CategoryListFragment.newInstance(MainActivity.this);
            } else if (position == 1) {
                kk = MyNovelFragment.newInstance();
            } else if (position == 2) {
                kk = WeekFragment.newInstance();
            } else if (position == 3) {
                kk = MonthFragment.newInstance();
            } else if (position == 4) {
                kk = HotNovelsFragment.newInstance();
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

}
