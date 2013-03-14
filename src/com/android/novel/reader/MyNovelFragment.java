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
    private LinearLayout myBookcase;
    private LinearLayout mySetting;
    private LinearLayout classicKongFu;
    private LinearLayout classicNovel;

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
        
        myBookcase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), MyNovelActivity.class);
            	startActivity(intent);
            }
        });
        
        mySetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), SettingActivity.class);
            	startActivity(intent);
            }
        });
        
        classicKongFu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), ClassicNovelsActivity.class);
            	Bundle bundle = new Bundle();
                bundle.putString("ClassTitle", "經典武俠");
                bundle.putInt("ClassicId", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
        classicNovel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), ClassicNovelsActivity.class);
            	Bundle bundle = new Bundle();
                bundle.putString("ClassTitle", "經典小說");
                bundle.putInt("ClassicId", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
        

    }

    private void findViews() {
        myBookmarks = (LinearLayout) myFragmentView.findViewById(R.id.my_bookmarks);
        recentRead = (LinearLayout) myFragmentView.findViewById(R.id.my_recent_read_bookmarks);
        myBookcase = (LinearLayout) myFragmentView.findViewById(R.id.my_bookcase);
        mySetting = (LinearLayout) myFragmentView.findViewById(R.id.my_setting);
        classicKongFu = (LinearLayout) myFragmentView.findViewById(R.id.classic_kongfu);
        classicNovel = (LinearLayout) myFragmentView.findViewById(R.id.classic_novel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
