package com.novel.reader.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

public class Category {

    static String message = "[{\"cat_link\":\"http://www.ranwen.net/modules/article/index.php?fullflag=1\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":13,\"link\":null,\"name\":\"\u5168\u672c\u5c0f\u8aaa\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=1\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":14,\"link\":null,\"name\":\"\u7384\u5e7b\u9b54\u6cd5\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=2\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":15,\"link\":null,\"name\":\"\u6b66\u4fe0\u4fee\u771f\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=3\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":16,\"link\":null,\"name\":\"\u90fd\u5e02\u8a00\u60c5\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=4\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":17,\"link\":null,\"name\":\"\u6b77\u53f2\u8ecd\u4e8b\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=5\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":18,\"link\":null,\"name\":\"\u6e38\u6232\u7af6\u6280\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=6\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":19,\"link\":null,\"name\":\"\u79d1\u5e7b\u5c0f\u8aaa\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=7\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":20,\"link\":null,\"name\":\"\u6050\u6016\u9748\u7570\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=8\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":21,\"link\":null,\"name\":\"\u7f8e\u6587\u6563\u6587\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.ranwen.net/modules/article/articlelist.php?class=9\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":22,\"link\":null,\"name\":\"\u540c\u4eba\u5c0f\u8aaa\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.wenku8.cn/top.php\",\"created_at\":\"2013-12-23T04:07:35Z\",\"id\":23,\"link\":null,\"name\":\"\u8f15\u5c0f\u8aaa\",\"updated_at\":\"2013-12-23T04:07:35Z\"},{\"cat_link\":\"http://www.bestory.com/category/11.html\",\"created_at\":\"2013-04-20T14:10:38Z\",\"id\":10,\"link\":\"http://www.bestory.com/category/11-1.html\",\"name\":\"\u540d\u8457\u53e4\u5178\",\"updated_at\":\"2013-04-20T14:10:38Z\"},{\"cat_link\":\"http://www.bestory.com/category/14.html\",\"created_at\":\"2013-04-20T14:10:38Z\",\"id\":11,\"link\":\"http://www.bestory.com/category/14-1.html\",\"name\":\"\u79d1\u666e\u5176\u5b83\",\"updated_at\":\"2013-04-20T14:10:38Z\"}]";
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
