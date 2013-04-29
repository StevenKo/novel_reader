package com.novel.reader.entity;

public class Article {
    int     id;
    int     novelId;
    String  text;
    String  title;
    String  subject;
    boolean isDownloaded;
    int     num;

    public Article() {
        this(1, 1, "", "", "", false);
    }

    public Article(int id, int novelId, String text, String title, String subject, boolean isDownloaded) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.text = text;
        this.subject = subject;
        this.isDownloaded = isDownloaded;
    }

    public Article(int id, int novelId, String text, String title, String subject, boolean isDownloaded, int num) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.text = text;
        this.subject = subject;
        this.isDownloaded = isDownloaded;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsDownloaded(boolean b) {
        this.isDownloaded = b;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getNovelId() {
        return novelId;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isDownload() {
        return isDownloaded;
    }

    public void setNovelId(int id) {
        novelId = id;
    }
}
