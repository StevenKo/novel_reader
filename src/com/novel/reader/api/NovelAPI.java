package com.novel.reader.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.novel.db.SQLiteNovel;
import com.novel.reader.entity.Article;
import com.novel.reader.entity.Bookmark;
import com.novel.reader.entity.Category;
import com.novel.reader.entity.Novel;

public class NovelAPI {

    final static String         HOST  = "http://106.187.40.42";
    public static final String  TAG   = "NOVEL_API";
    public static final boolean DEBUG = true;

    // public static ArrayList<Bookmark> getNovelBookmarks(int novelId, Context context) {
    // SQLiteNovel db = new SQLiteNovel(context);
    // return db.getNovelBookmarks(novelId);
    // }

    public static ArrayList<Bookmark> getAllRecentReadBookmarks(Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getAllRecentReadBookmarks();
    }

    public static ArrayList<Bookmark> getAllBookmarks(Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getAllBookmarks();
    }

    public static Bookmark getNovelBookmark(int novel_id, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getNovelBookmark(novel_id);
    }

    public static boolean updateBookmark(Bookmark bookmark, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.updateBookmark(bookmark);
    }

    public static boolean deleteBookmark(Bookmark bookmark, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        db.deleteBookmark(bookmark);
        return true;
    }

    public static Bookmark insertBookmark(Bookmark bookmark, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        int id = (int) db.insertBookmark(bookmark);
        bookmark.setId(id);
        return bookmark;
    }

    public static Bookmark createRecentBookmark(Bookmark bookmark, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        Bookmark lastNovelBookmark = db.getNovelBookmark(bookmark.getNovelId());
        if (lastNovelBookmark != null)
            db.deleteBookmark(lastNovelBookmark);
        int id = (int) db.insertBookmark(bookmark);
        bookmark.setId(id);
        return bookmark;
    }

    public static ArrayList<Novel> getCollectedNovels(Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getCollectedNovels();
    }

    public static Boolean isNovelCollected(Context context, int novel_id) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.isNovelCollected(novel_id);
    }

    public static Boolean isNovelDownloaded(Context context, int novel_id) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.isNovelDownloaded(novel_id);
    }

    public static ArrayList<Novel> getDownloadedNovels(Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getDownloadNovels();
    }

    public static boolean downloadArticles(int novelId, ArrayList<Article> articles, Context context) {
        for (int i = 0; i < articles.size(); i++)
            downloadArticle(novelId, articles.get(i), context);
        return true;
    }

    public static boolean downloadArticle(int novelId, Article article, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);

        if (!db.isNovelExists(novelId)) {
            downloadOrUpdateNovelInfo(article.getNovelId(), context, false, true);
        } else if (!db.isNovelDownloaded(novelId)) {
            Novel novel = getNovel(novelId, context);
            novel.setIsDownload(true);
            db.updateNovel(novel);
        }

        String message = getMessageFromServer("GET", "/api/v1/articles/" + article.getId() + ".json", null);
        if (message == null) {
            return false;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String text = nObject.getString("text");
                article.setText(text);
                article.setIsDownloaded(true);
                nObject = null;
                text = null;

            } catch (JSONException e) {

                e.printStackTrace();
                return false;
            }
        }
        message = null;

        if (db.isArticleExists(article.getId()))
            db.updateArticle(article);
        else
            db.insertArticle(article);

        article.setText("");

        return true;
    }

    public static boolean removeNovelFromCollected(Novel novel, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.removeNovelFromCollected(novel);
    }

    public static boolean removeNovelFromDownload(Novel novel, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        ArrayList<Article> articles = db.getNovelArticles(novel.getId(), true);
        db.deleteArticles(articles);
        return db.removeNovelFromDownload(novel);
    }

    public static boolean removeArticle(Article article, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.deleteArticle(article);
    }

    public static boolean removeArticles(ArrayList<Article> articles, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.deleteArticles(articles);
    }

    public static boolean collecNovel(final Novel novel, final Context context) {
        novel.setIsCollected(true);
        SQLiteNovel db = new SQLiteNovel(context);
        if (db.isNovelExists(novel.getId()))
            db.updateNovel(novel);
        else
            db.insertNovel(novel);

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                downloadOrUpdateNovelInfo(novel.getId(), context, true, false);
                return params;
            }
        }.execute();

        return true;
    }

    public static boolean downloadOrUpdateNovelInfo(int novelId, Context context, boolean isCollected, boolean isDownload) {

        Novel n = null;
        // ArrayList<Article> articles = new ArrayList<Article>();

        String message = getMessageFromServer("GET", "/api/v1/novels/" + novelId + "/detail_for_save.json", null);
        if (message == null) {
            return false;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                nObject = nObject.getJSONObject("novel");

                int novel_id = nObject.getInt("id");
                String articleNum = nObject.getString("article_num");
                String author = nObject.getString("author");
                boolean isSerializing = nObject.getBoolean("is_serializing");
                String lastUpdate = nObject.getString("last_update");
                String name = nObject.getString("name");
                String pic = nObject.getString("pic");
                String description = nObject.getString("description");
                int category_id = nObject.getInt("category_id");

                n = new Novel(novel_id, name, author, description, pic, category_id, articleNum, lastUpdate, isSerializing, isCollected, isDownload);

                // JSONArray articlesArray = nObject.getJSONArray("articles");
                // for (int i = 0; i < articlesArray.length(); i++) {
                //
                // int article_id = articlesArray.getJSONObject(i).getInt("id");
                // String subject = articlesArray.getJSONObject(i).getString("subject");
                // String title = articlesArray.getJSONObject(i).getString("title");
                //
                // Article a = new Article(article_id, novelId, "", title, subject, false);
                // articles.add(a);
                // }

            } catch (JSONException e) {

                e.printStackTrace();
                return false;
            }
        }

        SQLiteNovel db = new SQLiteNovel(context);
        if (db.isNovelExists(novelId))
            db.updateNovel(n);
        else
            db.insertNovel(n);

        // for (int i = 0; i < articles.size(); i++) {
        // if (db.isArticleExists(articles.get(i).getId()))
        // db.updateArticle(articles.get(i));
        // else
        // db.insertArticle(articles.get(i));
        // }

        return true;
    }

    public static ArrayList<Novel> searchNovels(String keyword) {
        String query;
        try {
            query = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/search.json?search=" + query, null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
        // try {
        // JSONArray novelsArray;
        // novelsArray = new JSONArray(message.toString());
        // for (int i = 0; i < novelsArray.length(); i++) {
        //
        // int id = novelsArray.getJSONObject(i).getInt("id");
        // String author = novelsArray.getJSONObject(i).getString("author");
        // String name = novelsArray.getJSONObject(i).getString("name");
        // String pic = novelsArray.getJSONObject(i).getString("pic");
        //
        // Novel novel = new Novel(id, name, author, "", pic, 0, "", "", false, false);
        // novels.add(novel);
        // }
        //
        // } catch (JSONException e) {
        // e.printStackTrace();
        // return null;
        // }
        // }
        // return novels;
    }

    public static Article getArticle(Article article, Context context) {

        SQLiteNovel db = new SQLiteNovel(context);
        if (db.isArticleExists(article.getId())) {
            Article articleFromDB = db.getArticle(article.getId());
            if (articleFromDB.getText().length() > 0)
                articleFromDB.setText(articleFromDB.getText() + "\n");
            return articleFromDB;
        }

        String message = getMessageFromServer("GET", "/api/v1/articles/" + article.getId() + ".json", null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String text = nObject.getString("text") + "\n";
                String title = nObject.getString("title");
                article.setText(text);
                article.setTitle(title);

            } catch (JSONException e) {

                e.printStackTrace();
                return null;
            }
        }
        return article;
    }

    public static Article getPreviousArticle(Article orginArticle, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        ArrayList<Article> articles = db.getNovelArticles(orginArticle.getNovelId(), true);
        for (int i = 1; i < articles.size(); i++) {
            if (articles.get(i).getId() == orginArticle.getId()) {
                return getArticle(articles.get(i - 1), context);
            }
        }

        String message = getMessageFromServer("GET", "/api/v1/articles/previous_article_by_num.json?novel_id=" + orginArticle.getNovelId() + "&article_id="
                + orginArticle.getId() + "&num=" + orginArticle.getNum(), null);
        if (message == null) {
            return null;
        } else {
            try {

                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String text = nObject.getString("text");
                int id = nObject.getInt("id");
                int novelId = nObject.getInt("novel_id");
                String title = nObject.getString("title");
                int num = nObject.getInt("num");
                return new Article(id, novelId, text, title, "", false, num);

            } catch (JSONException e) {

                e.printStackTrace();
                return null;
            }
        }
    }

    public static Article getNextArticle(Article orginArticle, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        ArrayList<Article> articles = db.getNovelArticles(orginArticle.getNovelId(), true);
        for (int i = 0; i < articles.size() - 1; i++) {
            if (articles.get(i).getId() == orginArticle.getId()) {
                return getArticle(articles.get(i + 1), context);
            }
        }

        String message = getMessageFromServer("GET", "/api/v1/articles/next_article_by_num.json?novel_id=" + orginArticle.getNovelId() + "&article_id="
                + orginArticle.getId() + "&num=" + orginArticle.getNum(), null);
        if (message == null) {
            return null;
        } else {
            try {

                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String text = nObject.getString("text");
                int id = nObject.getInt("id");
                int novelId = nObject.getInt("novel_id");
                String title = nObject.getString("title");
                int num = nObject.getInt("num");
                return new Article(id, novelId, text, title, "", false, num);

            } catch (JSONException e) {

                e.printStackTrace();
                return null;
            }
        }
    }

    public static ArrayList<Article> getDownloadedNovelArticles(int novelId, boolean isOrderUp, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        return db.getNovelArticles(novelId, isOrderUp);
    }

    public static ArrayList<Article> getNovelArticles(int novelId, boolean isOrderUp, Context context) {
        ArrayList<Article> articles = new ArrayList<Article>();
        String message = getMessageFromServer("GET", "/api/v1/articles/articles_by_num.json?novel_id=" + novelId + "&order=" + isOrderUp, null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONArray novelsArray;
                novelsArray = new JSONArray(message.toString());
                for (int i = 0; i < novelsArray.length(); i++) {

                    int id = novelsArray.getJSONObject(i).getInt("id");
                    String subject = novelsArray.getJSONObject(i).getString("subject");
                    String title = novelsArray.getJSONObject(i).getString("title");
                    int num = novelsArray.getJSONObject(i).getInt("num");

                    Article a = new Article(id, novelId, "", title, subject, false, num);
                    articles.add(a);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        SQLiteNovel db = new SQLiteNovel(context);
        articles = db.getArticleDownloadInfo(articles);

        return articles;
    }

    public static ArrayList<Novel> getThisMonthHotNovels() {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/this_month_hot.json", null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }

    }

    public static ArrayList<Novel> getThisWeekHotNovels() {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/this_week_hot.json", null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static ArrayList<Novel> getHotNovels() {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/hot.json", null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    // 經典小說
    public static ArrayList<Novel> getClassicNovels() {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/classic.json", null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    // 經典武俠
    public static ArrayList<Novel> getClassicActionNovels() {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/classic_action.json", null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static ArrayList<Novel> getCategoryRecommendNovels(int category_id) {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/category_recommend.json?category_id=" + category_id, null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static ArrayList<Novel> getCategoryThisWeekHotNovels(int category_id) {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/category_this_week_hot.json?category_id=" + category_id, null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static ArrayList<Novel> getCategoryHotNovels(int category_id) {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels/category_hot.json?category_id=" + category_id, null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static Novel getNovel(int novelId, Context context) {

        SQLiteNovel db = new SQLiteNovel(context);
        if (db.isNovelExists(novelId)) {
            return db.getNovel(novelId);
        }

        Novel n = null;
        String message = getMessageFromServer("GET", "/api/v1/novels/" + novelId + ".json", null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                int id = nObject.getInt("id");
                String articleNum = nObject.getString("article_num");
                String author = nObject.getString("author");
                boolean isSerializing = nObject.getBoolean("is_serializing");
                String lastUpdate = nObject.getString("last_update");
                String name = nObject.getString("name");
                String pic = nObject.getString("pic");
                String description = nObject.getString("description");
                int category_id = nObject.getInt("category_id");

                n = new Novel(id, name, author, description, pic, category_id, articleNum, lastUpdate, isSerializing, false, false);

            } catch (JSONException e) {

                e.printStackTrace();
                return null;
            }
        }
        return n;
    }

    public static ArrayList<Novel> getCategoryNovels(int category_id, int page) {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        String message = getMessageFromServer("GET", "/api/v1/novels.json?category_id=" + category_id + "&page=" + page, null);
        if (message == null) {
            return null;
        } else {
            return parseNovel(message, novels);
        }
    }

    public static ArrayList<Category> getCategories() {
        return Category.getCategories();
    }

    public static String getMessageFromServer(String requestMethod, String apiPath, JSONObject json) {
        URL url;
        try {
            url = new URL(HOST + apiPath);
            if (DEBUG)
                Log.d(TAG, "URL: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            if (requestMethod.equalsIgnoreCase("POST"))
                connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            if (requestMethod.equalsIgnoreCase("POST")) {
                OutputStream outputStream;

                outputStream = connection.getOutputStream();
                if (DEBUG)
                    Log.d("post message", json.toString());

                outputStream.write(json.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder lines = new StringBuilder();
            ;
            String tempStr;

            while ((tempStr = reader.readLine()) != null) {
                lines = lines.append(tempStr);
            }
            if (DEBUG)
                Log.d("MOVIE_API", lines.toString());

            reader.close();
            connection.disconnect();

            return lines.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Novel> parseNovel(String message, ArrayList<Novel> novels) {
        try {
            JSONArray novelsArray;
            novelsArray = new JSONArray(message.toString());
            for (int i = 0; i < novelsArray.length(); i++) {

                int id = novelsArray.getJSONObject(i).getInt("id");
                if (novelsArray.getJSONObject(i).isNull("article_num"))
                    continue;
                String articleNum = novelsArray.getJSONObject(i).getString("article_num");
                String author = novelsArray.getJSONObject(i).getString("author");
                boolean isSerializing = novelsArray.getJSONObject(i).getBoolean("is_serializing");
                String lastUpdate = novelsArray.getJSONObject(i).getString("last_update");
                String name = novelsArray.getJSONObject(i).getString("name");
                String pic = novelsArray.getJSONObject(i).getString("pic");

                Novel novel = new Novel(id, name, author, "", pic, 0, articleNum, lastUpdate, isSerializing, false, false);
                novels.add(novel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return novels;
    }
}
