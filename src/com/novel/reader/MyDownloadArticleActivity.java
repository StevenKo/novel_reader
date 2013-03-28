package com.novel.reader;

import java.util.ArrayList;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.adwhirl.AdWhirlManager;
import com.adwhirl.AdWhirlTargeting;
import com.google.ads.AdView;
import com.kosbrother.tool.ChildArticle;
import com.kosbrother.tool.Group;
import com.novel.reader.api.NovelAPI;
import com.novel.reader.entity.Article;
import com.novel.reader.entity.Novel;
import com.taiwan.imageload.ExpandListAdapter;
import com.taiwan.imageload.ImageLoader;

public class MyDownloadArticleActivity extends SherlockFragmentActivity implements AdWhirlInterface {

    private static final int                          ID_SETTING         = 0;
    private static final int                          ID_RESPONSE        = 1;
    private static final int                          ID_ABOUT_US        = 2;
    private static final int                          ID_GRADE           = 3;
    private static final int                          ID_DELETE_DOWNLOAD = 4;

    private Bundle                                    mBundle;
    private String                                    novelName;
    private int                                       novelId;
    private String                                    novelAuthor;
    private String                                    novelPicUrl;
    private String                                    novelArticleNum;
    private ImageView                                 novelImageView;
    private TextView                                  novelTextName;
    private TextView                                  novelTextAuthor;
    private TextView                                  downloadedCount;
    private ImageLoader                               mImageLoader;
    private LinearLayout                              novelLayoutProgress;
    private ArrayList<Article>                        articleList        = new ArrayList<Article>();
    private ExpandableListView                        novelListView;
    private Novel                                     theNovel;

    private final TreeMap<String, ArrayList<Article>> myData             = new TreeMap<String, ArrayList<Article>>();
    private final ArrayList<Group>                    mGroups            = new ArrayList<Group>();
    private AlertDialog.Builder                       deleteDialog;
    private AlertDialog.Builder                       aboutUsDialog;
    private final String                              adWhirlKey         = "215f895eb71748e7ba4cb3a5f20b061e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_novel_downloaded);

        final ActionBar ab = getSupportActionBar();
        mBundle = this.getIntent().getExtras();
        novelName = mBundle.getString("NovelName");
        novelId = mBundle.getInt("NovelId");
        novelAuthor = mBundle.getString("NovelAuthor");
        novelPicUrl = mBundle.getString("NovelPicUrl");
        novelArticleNum = mBundle.getString("NovelArticleNum");

        theNovel = new Novel(novelId, novelName, novelAuthor, "", novelPicUrl, 0, novelArticleNum, "", false, false, false);

        ab.setTitle(getResources().getString(R.string.title_my_downloading));
        ab.setDisplayHomeAsUpEnabled(true);

        setViews();
        setAboutUsDialog();

        new DownloadArticlesTask().execute();

        try {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // deprecated
            int height = display.getHeight(); // deprecated

            if (width > 320) {
                setAdAdwhirl();
            }
        } catch (Exception e) {

        }

    }

    private void setViews() {
        novelImageView = (ImageView) findViewById(R.id.novel_image);
        novelTextName = (TextView) findViewById(R.id.novel_name);
        novelTextAuthor = (TextView) findViewById(R.id.novel_author);
        novelListView = (ExpandableListView) findViewById(R.id.novel_download_artiles_list);
        downloadedCount = (TextView) findViewById(R.id.text_downloaded_count);
        novelLayoutProgress = (LinearLayout) findViewById(R.id.novel_layout_progress);

        novelTextName.setText(novelName + "(" + novelArticleNum + ")");
        novelTextAuthor.setText(getResources().getString(R.string.novel_author) + novelAuthor);

        mImageLoader = new ImageLoader(MyDownloadArticleActivity.this, 70);
        mImageLoader.DisplayImage(novelPicUrl, novelImageView);

        deleteDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.delete_title))
                .setMessage(getResources().getString(R.string.delete_message))
                .setPositiveButton(getResources().getString(R.string.delete_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Novel myNovel = new Novel(novelId, novelName, novelAuthor, "", novelPicUrl, 0, novelArticleNum, "", false, false, true);
                        NovelAPI.removeNovelFromDownload(myNovel, MyDownloadArticleActivity.this);
                        MyDownloadArticleActivity.this.finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.delete_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_main, menu);

        menu.add(0, ID_SETTING, 0, "設定").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_RESPONSE, 1, "意見回餽").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_ABOUT_US, 2, "關於我們").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_GRADE, 3, "為App評分").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, ID_DELETE_DOWNLOAD, 5, "刪除").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
        case ID_SETTING: // setting
            Intent intent = new Intent(MyDownloadArticleActivity.this, SettingActivity.class);
            startActivity(intent);
            break;
        case ID_RESPONSE: // response
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "brotherkos@gmail.com" });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "意見回餽 from 小說王");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            break;
        case ID_ABOUT_US: // response
            aboutUsDialog.show();
            break;
        case ID_GRADE: // response
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=KosBrother"));
            startActivity(browserIntent);
            break;
        case ID_DELETE_DOWNLOAD: // response
            deleteDialog.show();
            break;

        }
        return true;
    }

    private class DownloadArticlesTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            articleList = NovelAPI.getDownloadedNovelArticles(novelId, false, MyDownloadArticleActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            super.onPostExecute(result);
            novelLayoutProgress.setVisibility(View.GONE);
            // if(articleList!= null && articleList.size()!= 0){
            //
            // ExpandListAdapter mAdapter = new ExpandListAdapter( MyDownloadArticleActivity.this, mGroups, theNovel);
            // // ListArticleAdapter mAdapter = new ListArticleAdapter( MyDownloadArticleActivity.this, articleList, novelName);
            // novelListView.setAdapter(mAdapter);
            //
            // }

            if (articleList != null && articleList.size() != 0) {

                // use HashMap || TreeMap to make a parent key
                for (int i = 0; i < articleList.size(); i++) {
                    if (myData.containsKey(articleList.get(i).getSubject())) {
                        myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
                    } else {
                        // groupTitleList.add(articleList.get(i).getSubject());
                        mGroups.add(new Group(articleList.get(i).getSubject()));
                        myData.put(articleList.get(i).getSubject(), new ArrayList<Article>());
                        myData.get(articleList.get(i).getSubject()).add(articleList.get(i));
                    }
                }

                for (int i = 0; i < mGroups.size(); i++) {
                    ArrayList<Article> articles = myData.get(mGroups.get(i).getTitle());
                    for (int j = 0; j < articles.size(); j++) {
                        mGroups.get(i).addChildrenItem(
                                new ChildArticle(articles.get(j).getId(), articles.get(j).getNovelId(), "", articles.get(j).getTitle(), articles.get(j)
                                        .getSubject(), articles.get(j).isDownload()));
                    }
                }

                ExpandListAdapter mAdapter = new ExpandListAdapter(MyDownloadArticleActivity.this, mGroups, theNovel, -1);
                novelListView.setAdapter(mAdapter);

            }

            String txtCount = Integer.toString(articleList.size());
            downloadedCount.setText("共 " + txtCount + " 個下載");

        }
    }

    private void setAboutUsDialog() {
        // TODO Auto-generated method stub
        aboutUsDialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.about_us_string)).setIcon(R.drawable.play_store_icon)
                .setMessage(getResources().getString(R.string.about_us))
                .setPositiveButton(getResources().getString(R.string.yes_string), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    private void setAdAdwhirl() {
        // TODO Auto-generated method stub
        AdWhirlManager.setConfigExpireTimeout(1000 * 60);
        AdWhirlTargeting.setAge(23);
        AdWhirlTargeting.setGender(AdWhirlTargeting.Gender.MALE);
        AdWhirlTargeting.setKeywords("online games gaming");
        AdWhirlTargeting.setPostalCode("94123");
        AdWhirlTargeting.setTestMode(false);

        AdWhirlLayout adwhirlLayout = new AdWhirlLayout(this, adWhirlKey);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.adonView);

        adwhirlLayout.setAdWhirlInterface(this);

        mainLayout.addView(adwhirlLayout);

        mainLayout.invalidate();
    }

    @Override
    public void adWhirlGeneric() {
        // TODO Auto-generated method stub

    }

    public void rotationHoriztion(int beganDegree, int endDegree, AdView view) {
        final float centerX = 320 / 2.0f;
        final float centerY = 48 / 2.0f;
        final float zDepth = -0.50f * view.getHeight();

        Rotate3dAnimation rotation = new Rotate3dAnimation(beganDegree, endDegree, centerX, centerY, zDepth, true);
        rotation.setDuration(1000);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(rotation);
    }

}