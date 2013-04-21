package com.kosbrother.fragments;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.novel.db.SQLiteNovel;
import com.novel.reader.BookmarkActivity;
import com.novel.reader.ClassicNovelsActivity;
import com.novel.reader.MyNovelActivity;
import com.novel.reader.R;
import com.novel.reader.SettingActivity;

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

    @Override
    public void onStart() {
        super.onStart();
        checkDB();
    }

    void checkDB() {
        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "kosnovel");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        File sdcardDB = new File(cacheDir, SQLiteNovel.DB_NAME);
        if (!sdcardDB.exists())
            new InitDBTask().execute();
    }

    class InitDBTask extends AsyncTask<Integer, Integer, String> {

        private ProgressDialog progressdialogInit;

        @Override
        protected void onPreExecute() {
            progressdialogInit = ProgressDialog.show(getActivity(), "Load", "Loading…");
            progressdialogInit.setTitle("初始化DB");
            progressdialogInit.setMessage("初始化DB中…(原先下載過小說的用戶，會將資料轉至 SD卡）");
            progressdialogInit.setCanceledOnTouchOutside(false);
            progressdialogInit.setCancelable(false);
            progressdialogInit.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            SQLiteNovel db = new SQLiteNovel(getActivity());
            return "progress end";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            progressdialogInit.dismiss();
        }

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
