package com.android.novel.reader;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;

public class BookmarkActivity extends SherlockListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_article);

        final ActionBar ab = getSupportActionBar();

        ab.setTitle("我的書籤");
        ab.setDisplayHomeAsUpEnabled(true);

    }

}
