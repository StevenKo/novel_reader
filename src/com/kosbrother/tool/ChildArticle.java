package com.kosbrother.tool;

public class ChildArticle {
	int     id;
    int     novelId;
    String  text;
    String  title;
    String  subject;
    private boolean isChecked;
    boolean isDownloaded;

//    public ChildArticle() {
//        this(1, 1, "", "", "", false);
//    }

    public ChildArticle(int id, int novelId, String text, String title, String subject, boolean isDownloaded) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.text = text;
        this.subject = subject;
        this.isDownloaded = isDownloaded;
        this.isChecked = false;
    }
    
    public void toggle() {
        this.isChecked = !this.isChecked;
    }
    
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    
    public boolean getChecked() {
        return this.isChecked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsDownloaded(boolean b) {
        this.isDownloaded = b;
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
}

