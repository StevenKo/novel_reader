package com.kosbrother.fragments;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.novel.reader.R;
import com.novel.reader.api.NovelAPI;
import com.novel.reader.entity.Novel;
import com.taiwan.imageload.GridViewAdapter;
import com.taiwan.imageload.LoadMoreGridView;

public class MonthFragment extends Fragment {

    private ArrayList<Novel> novels = new ArrayList<Novel>();
    private LoadMoreGridView myGrid;
    private GridViewAdapter  myGridViewAdapter;
    private LinearLayout     progressLayout;
    private LinearLayout     loadmoreLayout;
    private LinearLayout     layoutReload;
    private Button           buttonReload;

    public static MonthFragment newInstance() {

        MonthFragment fragment = new MonthFragment();

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
        layoutReload = (LinearLayout) myFragmentView.findViewById(R.id.layout_reload);
        buttonReload = (Button) myFragmentView.findViewById(R.id.button_reload);
        myGrid = (LoadMoreGridView) myFragmentView.findViewById(R.id.news_list);
        myGrid.setOnLoadMoreListener(new LoadMoreGridView.OnLoadMoreListener() {
            public void onLoadMore() {
                // Do the work to load more items at the end of list
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

            novels = NovelAPI.getThisMonthHotNovels();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressLayout.setVisibility(View.GONE);
            loadmoreLayout.setVisibility(View.GONE);

            if (novels != null) {
                try {
                    layoutReload.setVisibility(View.GONE);
                    myGridViewAdapter = new GridViewAdapter(getActivity(), novels);
                    myGrid.setAdapter(myGridViewAdapter);
                } catch (Exception e) {

                }
            } else {
                layoutReload.setVisibility(View.VISIBLE);
            }

        }
    }

}
