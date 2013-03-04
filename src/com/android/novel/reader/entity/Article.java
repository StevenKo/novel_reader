package com.android.novel.reader.entity;

public class Article {
    int    id;
    int    novelId;
    String text;
    String title;
    String subject;

    public Article() {
        this(1, 1, "", "", "");
    }

    public Article(int id, int novelId, String text, String title, String subject) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.text = text;
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
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
}
