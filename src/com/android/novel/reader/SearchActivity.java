package com.android.novel.reader;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.novel.reader.api.NovelAPI;
import com.android.novel.reader.entity.Novel;
import com.taiwan.imageload.ImageLoader;

public class SearchActivity extends SherlockListActivity {

    private Bundle           mBundle;
    private String           keyword;
    private ArrayList<Novel> novels;
    private ListView         novelListView;
    private MenuItem         item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);

        final ActionBar ab = getSupportActionBar();
        mBundle = this.getIntent().getExtras();
        keyword = mBundle.getString("SearchKeyword");
        novelListView = this.getListView();

        ab.setTitle("小說王");
        ab.setDisplayHomeAsUpEnabled(true);
        new LoadDataTask().execute();
    }

    private void fetchData() {
        novels = NovelAPI.searchNovels(keyword);
    }

    public class SearchAdapter extends BaseAdapter {

        private final Context          mContext;
        private final ArrayList<Novel> novels;
        private final ImageLoader      imageLoader;

        public SearchAdapter(Context mContext, ArrayList<Novel> novels) {
            this.novels = novels;
            this.mContext = mContext;
            imageLoader = new ImageLoader(mContext);
        }

        @Override
        public int getCount() {
            return novels.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater myInflater = LayoutInflater.from(mContext);
            View converView = myInflater.inflate(R.layout.item_novel_search, null);
            ImageView pic = (ImageView) converView.findViewById(R.id.grid_item_image);
            TextView name = (TextView) converView.findViewById(R.id.grid_item_name);
            TextView author = (TextView) converView.findViewById(R.id.grid_item_author);
            TextView articleNum = (TextView) converView.findViewById(R.id.grid_item_counts);
            TextView textFinish = (TextView) converView.findViewById(R.id.grid_item_finish);
            TextView textSerialize = (TextView) converView.findViewById(R.id.serializing);

            imageLoader.DisplayImage(novels.get(position).getPic(), pic);
            name.setText(novels.get(position).getName());
            author.setText(novels.get(position).getAuthor());
            articleNum.setText(novels.get(position).getArticleNum());
            textFinish.setText(novels.get(position).getLastUpdate());

            if (novels.get(position).isSerializing()) {
                textSerialize.setText("連載中...");
            } else {
                textSerialize.setText("全本");
            }

            return converView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_main, menu);

        menu.add("設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add("為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        item = menu.add("Search").setIcon(R.drawable.ic_search_inverse).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            private EditText search;

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                search = (EditText) item.getActionView();
                search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                search.setInputType(InputType.TYPE_CLASS_TEXT);
                search.requestFocus();
                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            keyword = v.getText().toString();
                            new LoadDataTask().execute();

                            return true;
                        }

                        return false;
                    }
                });
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // TODO Auto-generated method stub
                search.setText("");
                return true;
            }
        }).setActionView(R.layout.collapsible_edittext);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            finish();
            // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
            break;
        }
        return true;
    }

    class LoadDataTask extends AsyncTask<Integer, Integer, String> {

        private ProgressDialog         progressdialogInit;
        private final OnCancelListener cancelListener = new OnCancelListener() {
                                                          public void onCancel(DialogInterface arg0) {
                                                              LoadDataTask.this.cancel(true);
                                                              finish();
                                                          }
                                                      };

        @Override
        protected void onPreExecute() {
            progressdialogInit = ProgressDialog.show(SearchActivity.this, "Load", "Loading…");
            progressdialogInit.setTitle("Load");
            progressdialogInit.setMessage("Loading…");
            progressdialogInit.setOnCancelListener(cancelListener);
            progressdialogInit.setCanceledOnTouchOutside(false);
            progressdialogInit.setCancelable(true);
            progressdialogInit.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            fetchData();
            return "progress end";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            progressdialogInit.dismiss();
            novelListView.setAdapter(new SearchAdapter(SearchActivity.this, novels));
            item.expandActionView();
            EditText search = (EditText) item.getActionView();
            search.setText(keyword);
        }

    }

}
