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

import com.novel.reader.NovelIntroduceActivity;
import com.novel.reader.R;
import com.novel.reader.entity.Novel;
import com.novel.reader.util.NovelReaderUtil;
import com.taiwan.imageload.ImageLoader;

public class GridViewAdapter extends BaseAdapter {

    private final Activity         activity;
    private final ArrayList<Novel> data;
    private static LayoutInflater  inflater = null;
    public ImageLoader             imageLoader;

    public GridViewAdapter(Activity a, ArrayList<Novel> d) {
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
            vi = inflater.inflate(R.layout.item_gridview_novel, null);
        } else {
            vi = inflater.inflate(R.layout.item_gridview_novel_small, null);
        }

        vi.setClickable(true);
        vi.setFocusable(true);
        // vi.setBackgroundResource(android.R.drawable.menuitem_background);
        vi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, NovelIntroduceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("NovelId", data.get(position).getId());
                bundle.putString("NovelName", data.get(position).getName());
                bundle.putString("NovelAuthor", data.get(position).getAuthor());
                bundle.putString("NovelDescription", data.get(position).getDescription());
                bundle.putString("NovelUpdate", data.get(position).getLastUpdate());
                bundle.putString("NovelPicUrl", data.get(position).getPic());
                bundle.putString("NovelArticleNum", data.get(position).getArticleNum());
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }

        });

        TextView textName = (TextView) vi.findViewById(R.id.grid_item_name);
        ImageView image = (ImageView) vi.findViewById(R.id.grid_item_image);
        TextView textAuthor = (TextView) vi.findViewById(R.id.grid_item_author);
        TextView textCounts = (TextView) vi.findViewById(R.id.grid_item_counts);
        TextView textFinish = (TextView) vi.findViewById(R.id.grid_item_finish);
        TextView textSerialize = (TextView) vi.findViewById(R.id.serializing);

        textName.setText(data.get(position).getName());
        if (data.get(position).getName().length() > 6)
            textName.setTextSize(12);
        textAuthor.setText(data.get(position).getAuthor());
        if (data.get(position).getAuthor().length() > 14) {
            textAuthor.setTextSize(8);
        }
        textCounts.setText(data.get(position).getArticleNum());
        textFinish.setText(data.get(position).getLastUpdate());

        if (NovelReaderUtil.isDisplayDefaultBookCover(data.get(position).getPic())) {
            image.setImageResource(R.drawable.bookcover_default);
        } else {
            imageLoader.DisplayImage(data.get(position).getPic(), image);
        }

        if (data.get(position).isSerializing()) {
            textSerialize.setText("連載中...");
        } else {
            textSerialize.setText("全本");
        }

        return vi;
    }
}
