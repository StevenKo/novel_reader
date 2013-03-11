package com.android.novel.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MyNovelFragment extends Fragment {

    public static MyNovelFragment newInstance() {
        MyNovelFragment fragment = new MyNovelFragment();
        return fragment;
    }

    private View         myFragmentView;
    private LinearLayout myBookmarks;
    private LinearLayout recentRead;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.my_novel, container, false);
        findViews();
        setViews();
        return myFragmentView;
    }

    private void setViews() {

        myBookmarks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_RECNET", false);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });
        recentRead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IS_RECNET", true);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        myBookmarks = (LinearLayout) myFragmentView.findViewById(R.id.my_bookmarks);
        recentRead = (LinearLayout) myFragmentView.findViewById(R.id.my_recent_read_bookmarks);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
