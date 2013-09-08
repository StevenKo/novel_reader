package com.novel.reader.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.novel.reader.ArticleActivity;
import com.novel.reader.R;
import com.novel.reader.entity.Bookmark;
import com.novel.reader.util.NovelReaderUtil;
import com.taiwan.imageload.ImageLoader;

public class GridViewIndexBookmarkAdapter extends BaseAdapter {

    private final Activity         activity;
    private final ArrayList<Bookmark> data;
    private static LayoutInflater  inflater = null;
    public ImageLoader             imageLoader;

    public GridViewIndexBookmarkAdapter(Activity a, ArrayList<Bookmark> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext(), 70);

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        // if (convertView == null)
        // vi = inflater.inflate(R.layout.item_gridview_novel, null);

        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated

        if (width > 480) {
            vi = inflater.inflate(R.layout.item_gridview_index_bookmark, null);
        } else {
            vi = inflater.inflate(R.layout.item_gridview_index_bookmark, null);
        }

        vi.setClickable(true);
        vi.setFocusable(true);
        // vi.setBackgroundResource(android.R.drawable.menuitem_background);
        vi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleActivity.class);
                intent.putExtra("NovelName", data.get(position).getNovelName());
                intent.putExtra("ArticleTitle", data.get(position).getArticleTitle());
                intent.putExtra("ArticleId", data.get(position).getArticleId());
                intent.putExtra("ReadingRate", data.get(position).getReadRate());
                intent.putExtra("NovelPic", data.get(position).getNovelPic());
                intent.putExtra("NovelId", data.get(position).getNovelId());
                activity.startActivity(intent);

            }

        });

        TextView textName = (TextView) vi.findViewById(R.id.grid_item_name);
        ImageView image = (ImageView) vi.findViewById(R.id.grid_item_image);
        TextView textArticleTitle = (TextView) vi.findViewById(R.id.grid_item_article_title);
        TextView textSerialize = (TextView) vi.findViewById(R.id.bookmark);

        textName.setText(data.get(position).getNovelName());
        if (data.get(position).getNovelName().length() > 6)
            textName.setTextSize(12);
        textArticleTitle.setText(data.get(position).getArticleTitle());
        if (data.get(position).getArticleTitle().length() > 14) {
        	textArticleTitle.setTextSize(8);
        }

        if (NovelReaderUtil.isDisplayDefaultBookCover(data.get(position).getNovelPic())) {
            image.setImageResource(R.drawable.bookcover_default);
        } else {
            imageLoader.DisplayImage(data.get(position).getNovelPic(), image);
        }

        if (data.get(position).isRecentRead()) {
            textSerialize.setText("最近閱讀");
        } else {
            textSerialize.setText("書籤");
        }

        return vi;
    }
}
