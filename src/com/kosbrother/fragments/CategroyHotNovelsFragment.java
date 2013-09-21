package com.kosbrother.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.novel.reader.CategoryActivity;
import com.novel.reader.R;
import com.novel.reader.adapter.GridViewAdapter;
import com.novel.reader.api.NovelAPI;
import com.novel.reader.entity.Novel;
import com.taiwan.imageload.LoadMoreGridView;

public class CategroyHotNovelsFragment extends Fragment {

    private ArrayList<Novel> novels     = new ArrayList<Novel>();
    private ArrayList<Novel> moreNovels = new ArrayList<Novel>();
    private static int       myPage     = 0;
    private LoadMoreGridView myGrid;
    private GridViewAdapter  myGridViewAdapter;
    private Boolean          checkLoad  = true;
    private LinearLayout     progressLayout;
    private LinearLayout     loadmoreLayout;
    private LinearLayout     noDataLayout;
    private LinearLayout     layoutReload;
    private static int       id;
    private Button           buttonReload;
	private Activity mActivity;
    
    @Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    mActivity= activity;
	  }

    public static CategroyHotNovelsFragment newInstance() {

        // id = categoryId;
        CategroyHotNovelsFragment fragment = new CategroyHotNovelsFragment();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.loadmore_grid, container, false);
        progressLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_progress);
        loadmoreLayout = (LinearLayout) myFragmentView.findViewById(R.id.load_more_grid);
        noDataLayout = (LinearLayout) myFragmentView.findViewById(R.id.layout_no_data);
        layoutReload = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
        buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
        myGrid = (LoadMoreGridView) myFragmentView.findViewById(R.id.news_list);
        myGrid.setOnLoadMoreListener(new LoadMoreGridView.OnLoadMoreListener() {
            public void onLoadMore() {
            }
        });

        buttonReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                progressLayout.setVisibility(View.VISIBLE);
                layoutReload.setVisibility(View.GONE);
                new DownloadChannelsTask().execute();
            }
        });

        if (myGridViewAdapter != null) {
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);
            myGrid.setAdapter(myGridViewAdapter);
        } else {
            new DownloadChannelsTask().execute();
        }

        return myFragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private class DownloadChannelsTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

            novels = NovelAPI.getCategoryHotNovels(CategoryActivity.categoryId);
            // moreNovels = NovelAPI.getHotNovels();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);

            if (novels != null && novels.size() != 0) {
                try {
                    layoutReload.setVisibility(View.GONE);
                    myGridViewAdapter = new GridViewAdapter(mActivity, novels);
                    myGrid.setAdapter(myGridViewAdapter);
                } catch (Exception e) {

                }
            } else {
                layoutReload.setVisibility(View.VISIBLE);
            }

        }
    }

    private class LoadMoreTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

            moreNovels = NovelAPI.getThisMonthHotNovels();
            if (moreNovels != null) {
                for (int i = 0; i < moreNovels.size(); i++) {
                    novels.add(moreNovels.get(i));
                }
                for (int i = 0; i < moreNovels.size(); i++) {
                    novels.add(moreNovels.get(i));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            loadmoreLayout.setVisibility(View.GONE);

            if (moreNovels != null) {
                myGridViewAdapter.notifyDataSetChanged();
            } else {
                checkLoad = false;
                Toast.makeText(mActivity, "no more data", Toast.LENGTH_SHORT).show();
            }
            myGrid.onLoadMoreComplete();

        }
    }

}
