package com.android.novel.reader.entity;

public class Bookmark {
    int    id;
    int    novelId;
    int    articleId;
    int    rate;
    String novelName;
    String articleTitle;

    public Bookmark() {

    }

    public Bookmark(int id, int novelId, int articleId, int rate, String novelName, String articleTitle) {
        this.id = id;
        this.novelId = novelId;
        this.articleId = articleId;
        this.rate = rate;
        this.novelName = novelName;
        this.articleTitle = articleTitle;
    }

    public String getArticleTitle() {
        return this.articleTitle;
    }

    public String getNovelName() {
        return this.novelName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getNovelId() {
        return novelId;
    }

    public int getArticleId() {
        return articleId;
    }

    public int getReadRate() {
        return rate;
    }

}
