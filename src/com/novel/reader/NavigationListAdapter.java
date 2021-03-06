package com.novel.reader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationListAdapter extends BaseAdapter {

    private Activity            activity;
    private ArrayList<NavigationItem> data;
    private static LayoutInflater     inflater = null;

    public NavigationListAdapter(Activity a) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = new ArrayList<NavigationItem>();
        data.add(new NavigationItem(activity.getResources().getString(R.string.my_read_setting), R.drawable.navigation_setting));
        data.add(new NavigationItem(activity.getResources().getString(R.string.my_bookmark), R.drawable.navigation_bookmark));
        data.add(new NavigationItem(activity.getResources().getString(R.string.my_recent_reading), R.drawable.navigation_recent));
        data.add(new NavigationItem(activity.getResources().getString(R.string.my_collect), R.drawable.navigation_collect));
        data.add(new NavigationItem(activity.getResources().getString(R.string.my_download), R.drawable.navigation_download));

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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.item_drawer_list, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        ImageView icon = (ImageView) vi.findViewById(R.id.imgIcon);
        text.setText(data.get(position).title);
        icon.setImageResource(data.get(position).iconRecourceId);


        return vi;
    }
}