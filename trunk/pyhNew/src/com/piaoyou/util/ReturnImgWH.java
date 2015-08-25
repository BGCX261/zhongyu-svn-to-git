package com.piaoyou.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class ReturnImgWH {
	public static int[] getImgSizeWeb(URL url) {
		int[] imgSize = new int[2];
		try {
			Image img = Image.getInstance(url);
			imgSize[0] = Math.round(img.getWidth());
			imgSize[1] = Math.round(img.getHeight());
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgSize;
	}
	
	
	public static void main(String[] args){
		try {
			URL url  =  new URL("http://image.t3.com.cn/images/155861.jpg");
			int[] size = getImgSizeWeb(url);
			System.out.println(size[0]+"   "+size[1]);
			
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
