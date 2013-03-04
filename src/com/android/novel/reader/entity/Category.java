package com.android.novel.reader.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class Category {

    static String message = "[{\"cat_link\":\"http://www.bestory.com/category/1.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":1,\"link\":\"http://www.bestory.com/category/1-1.html\",\"name\":\"\u9b54\u6cd5\u7570\u754c\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/2.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":2,\"link\":\"http://www.bestory.com/category/2-1.html\",\"name\":\"\u4ed9\u6b66\u7570\u80fd\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/3.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":3,\"link\":\"http://www.bestory.com/category/3-1.html\",\"name\":\"\u8a00\u60c5\u6558\u4e8b\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/4.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":4,\"link\":\"http://www.bestory.com/category/4-1.html\",\"name\":\"\u6642\u5149\u7a7f\u8d8a\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/8.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":5,\"link\":\"http://www.bestory.com/category/8-1.html\",\"name\":\"\u79d1\u5e7b\u592a\u7a7a\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/5.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":6,\"link\":\"http://www.bestory.com/category/5-1.html\",\"name\":\"\u9748\u7570\u8ecd\u4e8b\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/6.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":7,\"link\":\"http://www.bestory.com/category/6-1.html\",\"name\":\"\u6e38\u6232\u9ad4\u80b2\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/9.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":8,\"link\":\"http://www.bestory.com/category/9-1.html\",\"name\":\"\u52d5\u6f2b\u65e5\u8f15\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/10.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":9,\"link\":\"http://www.bestory.com/category/10-1.html\",\"name\":\"\u66c6\u53f2\u7d00\u5be6\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/11.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":10,\"link\":\"http://www.bestory.com/category/11-1.html\",\"name\":\"\u540d\u8457\u53e4\u5178\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/14.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":11,\"link\":\"http://www.bestory.com/category/14-1.html\",\"name\":\"\u79d1\u666e\u5176\u5b83\",\"updated_at\":\"2013-03-03T15:05:05Z\"},{\"cat_link\":\"http://www.bestory.com/category/15.html\",\"created_at\":\"2013-03-03T15:05:05Z\",\"id\":12,\"link\":\"http://www.bestory.com/category/15-1.html\",\"name\":\"\u539f\u5275\",\"updated_at\":\"2013-03-03T15:05:05Z\"}]";

    int           id;
    String        name;

    public Category() {
        this(-1, "");
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCateName() {
        return name;
    }

    public void setCateName(String name) {
        this.name = name;
    }

    public static String getCategoryName(int id) {
        HashMap hash = new HashMap();
        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(message.toString());
            for (int i = 0; i < categoryArray.length(); i++) {
                int category_id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                hash.put(category_id, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (String) hash.get(id);
    }

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> cateogries = new ArrayList<Category>();
        JSONArray categoryArray;
        try {
            categoryArray = new JSONArray(message.toString());
            for (int i = 0; i < categoryArray.length(); i++) {
                int category_id = categoryArray.getJSONObject(i).getInt("id");
                String name = categoryArray.getJSONObject(i).getString("name");
                Category cat = new Category(category_id, name);
                cateogries.add(cat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cateogries;
    }
}
