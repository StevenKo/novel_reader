package com.android.novel.reader.entity;

public class Novel {
    int     id;
    String  name;
    String  author;
    String  description;
    String  pic;
    int     categoryId;
    String  articleNum;
    String  lastUpdate;
    boolean isSearializing;

    public Novel() {
        this(1, "", "", "", "", 1, "", "", true);
    }

    public Novel(int id, String name, String author, String description, String pic, int categoryId, String articleNum, String lastUpdate,
            boolean isSearializing) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.pic = pic;
        this.categoryId = categoryId;
        this.articleNum = articleNum;
        this.lastUpdate = lastUpdate;
        this.isSearializing = isSearializing;

    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public boolean isSearializing() {
        return isSearializing;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPic() {
        return pic;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

}
