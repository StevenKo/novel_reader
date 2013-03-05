package com.android.novel.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.novel.reader.entity.Article;
import com.android.novel.reader.entity.Novel;

public class SQLiteNovel extends SQLiteOpenHelper {

    private static final String  DB_NAME          = "kosnovel.sqlite"; // 資料庫名稱
    private static final int     DATABASE_VERSION = 1;                // 資料庫版本
    private final SQLiteDatabase db;
    private final Context        ctx;

    // Define database schema
    public interface NovelSchema {
        String TABLE_NAME     = "novels";
        String ID             = "id";
        String NAME           = "name";
        String AUTHOR         = "author";
        String DESCRIPTION    = "description";
        String PIC            = "pic";
        String CATEGORY_ID    = "category_id";
        String ARTICLE_NUM    = "article_num";
        String LAST_UPDATE    = "last_update";
        String IS_SERIALIZING = "is_serializing";
    }

    public interface ArtcileSchema {
        String TABLE_NAME    = "articles";
        String ID            = "id";
        String NOVEL_ID      = "novel_id";
        String TEXT          = "text";
        String TITLE         = "title";
        String SUBJECT       = "subject";
        String IS_DOWNLOADED = "is_downloaded";
    }

    public SQLiteNovel(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        ctx = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NovelSchema.TABLE_NAME + " (" + NovelSchema.ID + " INTEGER PRIMARY KEY" + "," + NovelSchema.NAME
                + " TEXT NOT NULL" + "," + NovelSchema.AUTHOR + " TEXT NOT NULL" + "," + NovelSchema.DESCRIPTION + " TEXT NOT NULL" + "," + NovelSchema.PIC
                + " TEXT NOT NULL" + "," + NovelSchema.CATEGORY_ID + " INTEGER NOT NULL" + "," + NovelSchema.ARTICLE_NUM + " TEXT NOT NULL" + ","
                + NovelSchema.LAST_UPDATE + " TEXT NOT NULL" + "," + NovelSchema.IS_SERIALIZING + " INTEGER NOT NULL" + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ArtcileSchema.TABLE_NAME + " (" + ArtcileSchema.ID + " INTEGER PRIMARY KEY" + "," + ArtcileSchema.NOVEL_ID
                + " INTEGER NOT NULL" + "," + ArtcileSchema.TEXT + " TEXT NOT NULL" + "," + ArtcileSchema.TITLE + " TEXT NOT NULL" + ","
                + ArtcileSchema.SUBJECT + " TEXT NOT NULL" + "," + ArtcileSchema.IS_DOWNLOADED + " INTEGER NOT NULL" + "," + "FOREIGN KEY("
                + ArtcileSchema.NOVEL_ID + ") REFERENCES " + NovelSchema.TABLE_NAME + "(" + NovelSchema.ID + ") ON UPDATE CASCADE" + ");");

    }

    public boolean updateNovel(Novel novel) {
        Cursor cursor = db.rawQuery("UPDATE novels SET `article_num` = ?, `last_update` = ?, `is_serializing` = ? WHERE `novels`.`id` = ?", new String[] {
                novel.getArticleNum(), novel.getLastUpdate(), getSQLiteBoolean(novel.isSerializing()) + "", novel.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public boolean updateArticle(Article article) {
        Cursor cursor = db.rawQuery("UPDATE articles SET `text` = ?, `is_downloaded` = ? WHERE `articles`.`id` = ?", new String[] { article.getText(),
                getSQLiteBoolean(article.isDownload()) + "", article.getId() + "" });
        cursor.moveToFirst();
        cursor.close();
        return true;
    }

    public Novel getNovel(int novel_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + NovelSchema.TABLE_NAME + " WHERE id = \'" + novel_id + "\'", null);
        Novel novel = null;
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            String NAME = cursor.getString(1);
            String AUTHOR = cursor.getString(2);
            String DESCRIPTION = cursor.getString(3);
            String PIC = cursor.getString(4);
            int CATEGORY_ID = cursor.getInt(5);
            String ARTICLE_NUM = cursor.getString(6);
            String LAST_UPDATE = cursor.getString(7);
            Boolean IS_SERIALIZING = cursor.getInt(8) > 0;
            novel = new Novel(ID, NAME, AUTHOR, DESCRIPTION, PIC, CATEGORY_ID, ARTICLE_NUM, LAST_UPDATE, IS_SERIALIZING);
        }
        cursor.close();
        return novel;
    }

    public ArrayList<Article> getNovelArticles(int novel_id) {
        Cursor cursor = null;
        ArrayList<Article> articles = new ArrayList<Article>();
        cursor = db.rawQuery("SELECT id,novel_id,title,subject,is_downloaded FROM " + ArtcileSchema.TABLE_NAME + " WHERE id = \'" + novel_id + "\'", null);
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            int NOVEL_ID = cursor.getInt(1);
            String TITLE = cursor.getString(2);
            String SUBJECT = cursor.getString(3);
            boolean IS_DOWNLOADED = cursor.getInt(4) > 0;
            Article article = new Article(ID, NOVEL_ID, "", TITLE, SUBJECT, IS_DOWNLOADED);
            articles.add(article);
        }
        return articles;
    }

    public Article getArticle(int article_id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + ArtcileSchema.TABLE_NAME + " WHERE id = \'" + article_id + "\'", null);
        Article article = null;
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            int NOVEL_ID = cursor.getInt(1);
            String TEXT = cursor.getString(2);
            String TITLE = cursor.getString(3);
            String SUBJECT = cursor.getString(4);
            boolean IS_DOWNLOADED = cursor.getInt(5) > 0;
            article = new Article(ID, NOVEL_ID, TEXT, TITLE, SUBJECT, IS_DOWNLOADED);
        }
        cursor.close();
        return article;
    }

    public long insertArticle(Article article) {

        ContentValues args = new ContentValues();
        args.put(ArtcileSchema.ID, article.getId());
        args.put(ArtcileSchema.NOVEL_ID, article.getNovelId());
        args.put(ArtcileSchema.TEXT, article.getText());
        args.put(ArtcileSchema.TITLE, article.getTitle());
        args.put(ArtcileSchema.SUBJECT, article.getSubject());
        args.put(ArtcileSchema.IS_DOWNLOADED, getSQLiteBoolean(article.isDownload()));
        return db.insert(ArtcileSchema.TABLE_NAME, null, args);
    }

    public ArrayList<Novel> getNovels() {
        Cursor cursor = null;
        ArrayList<Novel> novels = new ArrayList<Novel>();
        cursor = db.rawQuery("SELECT * FROM " + NovelSchema.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            String NAME = cursor.getString(1);
            String AUTHOR = cursor.getString(2);
            String DESCRIPTION = cursor.getString(3);
            String PIC = cursor.getString(4);
            int CATEGORY_ID = cursor.getInt(5);
            String ARTICLE_NUM = cursor.getString(6);
            String LAST_UPDATE = cursor.getString(7);
            Boolean IS_SERIALIZING = cursor.getInt(8) > 0;
            Novel novel = new Novel(ID, NAME, AUTHOR, DESCRIPTION, PIC, CATEGORY_ID, ARTICLE_NUM, LAST_UPDATE, IS_SERIALIZING);
            novels.add(novel);
        }
        return novels;
    }

    public long insertNovel(Novel novel) {

        ContentValues args = new ContentValues();
        args.put(NovelSchema.ID, novel.getId());
        args.put(NovelSchema.NAME, novel.getName());
        args.put(NovelSchema.AUTHOR, novel.getAuthor());
        args.put(NovelSchema.DESCRIPTION, novel.getDescription());
        args.put(NovelSchema.PIC, novel.getPic());
        args.put(NovelSchema.CATEGORY_ID, novel.getCategoryId());
        args.put(NovelSchema.ARTICLE_NUM, novel.getArticleNum());
        args.put(NovelSchema.LAST_UPDATE, novel.getLastUpdate());
        args.put(NovelSchema.IS_SERIALIZING, getSQLiteBoolean(novel.isSerializing()));

        return db.insert(NovelSchema.TABLE_NAME, null, args);
    }

    static int getSQLiteBoolean(boolean b) {
        if (b)
            return 1;
        else
            return 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
