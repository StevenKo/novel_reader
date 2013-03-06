package com.android.novel.reader.entity;

public class Bookmark {
    int id;
    int novelId;
    int articleId;
    int rate;

    public Bookmark() {

    }

    public Bookmark(int id, int novelId, int articleId, int rate) {
        this.id = id;
        this.novelId = novelId;
        this.articleId = articleId;
        this.rate = rate;
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
