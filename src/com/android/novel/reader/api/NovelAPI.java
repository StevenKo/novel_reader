package com.android.novel.reader.api;

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
import android.util.Log;

import com.android.novel.db.SQLiteNovel;
import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Category;
import com.android.novel.reader.entity.Novel;

public class NovelAPI {

    final static String         HOST  = "http://106.187.103.131";
    public static final String  TAG   = "NOVEL_API";
    public static final boolean DEBUG = true;

    public static boolean downloadArticle(int novelId, Article article, Context context) {
        SQLiteNovel db = new SQLiteNovel(context);
        Novel n = db.getNovel(novelId);
        if (n == null) {
            downloadNovelInfo(novelId, context);
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

            } catch (JSONException e) {

                e.printStackTrace();
                return false;
            }
        }

        if (db.getArticle(article.getId()) != null)
            db.updateArticle(article);
        else
            db.insertArticle(article);

        return true;
    }

    private static boolean downloadNovelInfo(int novelId, Context context) {

        Novel n = null;
        ArrayList<Article> articles = new ArrayList<Article>();

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

                n = new Novel(novel_id, name, author, description, pic, category_id, articleNum, lastUpdate, isSerializing);

                JSONArray articlesArray = nObject.getJSONArray("articles");
                for (int i = 0; i < articlesArray.length(); i++) {

                    int article_id = articlesArray.getJSONObject(i).getInt("id");
                    String subject = articlesArray.getJSONObject(i).getString("subject");
                    String title = articlesArray.getJSONObject(i).getString("title");

                    Article a = new Article(article_id, novelId, "", title, subject, false);
                    articles.add(a);
                }

            } catch (JSONException e) {

                e.printStackTrace();
                return false;
            }
        }

        SQLiteNovel db = new SQLiteNovel(context);
        db.insertNovel(n);

        for (int i = 0; i < articles.size(); i++) {
            db.insertArticle(articles.get(i));
        }

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
            try {
                JSONArray novelsArray;
                novelsArray = new JSONArray(message.toString());
                for (int i = 0; i < novelsArray.length(); i++) {

                    int id = novelsArray.getJSONObject(i).getInt("id");
                    String author = novelsArray.getJSONObject(i).getString("author");
                    String name = novelsArray.getJSONObject(i).getString("name");

                    Novel novel = new Novel(id, name, author, "", "", 0, "", "", false);
                    novels.add(novel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return novels;
    }

    public static Article getArticle(Article article) {
        String message = getMessageFromServer("GET", "/api/v1/articles/" + article.getId() + ".json", null);
        if (message == null) {
            return null;
        } else {
            try {
                JSONObject nObject;
                nObject = new JSONObject(message.toString());
                String text = nObject.getString("text");
                article.setText(text);

            } catch (JSONException e) {

                e.printStackTrace();
                return null;
            }
        }
        return article;
    }

    public static ArrayList<Article> getNovelArticles(int novelId, int page, boolean isOrderUp, Context context) {
        ArrayList<Article> articles = new ArrayList<Article>();
        String message = getMessageFromServer("GET", "/api/v1/articles.json?novel_id=" + novelId + "&page=" + page + "&order=" + isOrderUp, null);
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

                    Article a = new Article(id, novelId, "", title, subject, false);
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

    public static Novel getNovel(int novelId) {
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

                n = new Novel(id, name, author, description, pic, category_id, articleNum, lastUpdate, isSerializing);

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
                String articleNum = novelsArray.getJSONObject(i).getString("article_num");
                String author = novelsArray.getJSONObject(i).getString("author");
                boolean isSerializing = novelsArray.getJSONObject(i).getBoolean("is_serializing");
                String lastUpdate = novelsArray.getJSONObject(i).getString("last_update");
                String name = novelsArray.getJSONObject(i).getString("name");
                String pic = novelsArray.getJSONObject(i).getString("pic");

                Novel novel = new Novel(id, name, author, "", pic, 0, articleNum, lastUpdate, isSerializing);
                novels.add(novel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return novels;
    }
}
