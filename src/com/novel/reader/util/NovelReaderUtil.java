package com.novel.reader.util;

public class NovelReaderUtil {
	
	public static boolean isDisplayDefaultBookCover(String url){
		if(url.equals("") || url == null || url.equals("http://www.bestory.com/pics/0.jpg"))
			return true;
		
		return false;
	}

}
