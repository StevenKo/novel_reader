package com.taiwan.imageload;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.novel.reader.ArticleActivity;
import com.android.novel.reader.NovelIntroduceActivity;
import com.android.novel.reader.R;
import com.android.novel.reader.entity.Article;

public class ListArticleAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<Article> data;
    private static LayoutInflater inflater=null;
    private String novelName;
   
    
    public ListArticleAdapter(Activity a, ArrayList<Article> d, String theNovelName) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        novelName = theNovelName;
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
        View vi=convertView;
        vi = inflater.inflate(R.layout.item_expandible_child, null);
        TextView text=(TextView)vi.findViewById(R.id.expandlist_child);
	    text.setText(data.get(position).getTitle());
	        
	    vi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(activity, "tt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, ArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ArticleId", data.get(position).getId());
                bundle.putString("NovelName", novelName);
                bundle.putString("ArticleTitle", data.get(position).getTitle());
                bundle.putBoolean("ArticleDownloadBoolean", data.get(position).isDownload());
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }

        });
	        
	        
        return vi;
    }
}
