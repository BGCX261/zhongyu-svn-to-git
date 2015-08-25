package com.piaoyou.img;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import magick.ImageInfo;
import magick.MagickImage;

public class Img {

	static {
		System.setProperty("jmagick.systemclassloader", "no");
	}

	/**
	 * 压缩和切图
	 * 
	 * @param sourcePath
	 *            源图片路径
	 * @param compressPath
	 *            压缩图片路径
	 * @param w
	 *            压缩图片宽度
	 * @param h
	 *            压缩图片高度
	 * @param x
	 *            切图x坐标
	 * @param y
	 *            切图y坐标
	 * @param isCut
	 *            是否启用切图0不启用1启用
	 * @param cutPath
	 *            切图图片路径
	 * @param long_or_short
	 *            压缩时是以长边为准还是以短边为准"wideth"长边"height"短边
	 *            
	 *  flag==0 :表示删除压缩的图片          
	 */
	public static void createThumbnail(String sourcePath, String compressPath,int w, int h, int x, int y, int isCut, String cutPath,String wideth_or_height,int flag,Rectangle paramRect) {
		ImageInfo info = null;
		MagickImage image = null;
		Dimension imageDim = null;
		MagickImage scaled = null;
		int wideth = 0;
		int height = 0;
		try {
			info = new ImageInfo(sourcePath);
			image = new MagickImage(info);
			imageDim = image.getDimension();
			wideth = imageDim.width;
			height = imageDim.height;

			// 得到压缩后的图片宽度和高度
			int[] w_h = getW_H(wideth, height, w, h, wideth_or_height);
			wideth = w_h[0];
			height = w_h[1];

			scaled = image.scaleImage(wideth, height);

			scaled.setFileName(compressPath);
			scaled.writeImage(info);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (scaled != null) {
				scaled.destroyImages();
			}
		}
		// 切图
		if (isCut == 1) {
			cutImg(compressPath, cutPath, paramRect.width, paramRect.height, x, y);
			 if(flag==0){
				   File compressFile=new File(compressPath);
				   if(compressFile.exists()){
					   compressFile.delete();
				   }
		     }
		}

	}

	/**
	 * 切图
	 * 
	 * @param imgPath
	 *            源图路径
	 * @param toPath
	 *            修改图路径
	 * @param w
	 *            宽度
	 * @param h
	 *            高度
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	public static void cutImg(String imgPath, String toPath, int w, int h,
			int x, int y) {
		ImageInfo infoS = null;
		MagickImage image = null;
		MagickImage cropped = null;
		Rectangle rect = null;
		try {
			infoS = new ImageInfo(imgPath);
			image = new MagickImage(infoS);
			rect = new Rectangle(x, y, w, h);
			cropped = image.cropImage(rect);
			cropped.setFileName(toPath);
			cropped.writeImage(infoS);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cropped != null) {
				cropped.destroyImages();
			}
		}
	}

	/**
	 * 等比压缩的宽、高 计算方法
	 * 
	 * @param wideth
	 *            原图宽度
	 * @param height
	 *            原图高度
	 * @param w
	 *            缩略图宽度
	 * @param h
	 *            缩略图高度
	 * @return int数组 {等比压缩的宽度，等比压缩高度}
	 */
	private static int[] getW_H(int wideth, int height, int w, int h,
			String wideth_or_height) {

		double w_ = Double.valueOf(w);
		double h_ = Double.valueOf(h);
		double wideth_ = Double.valueOf(wideth);
		double height_ = Double.valueOf(height);

		double b1 = w_ / h_;
		double b2 = wideth_ / height_;

		// System.out.println(b1 + "--" + b2);
		if ("height".equals(wideth_or_height)) {

			

				wideth_ = h_ * wideth_ / height_;
				height_ = h_;

			
		} else if ("wideth".equals(wideth_or_height)) {

			

				height_ = w_ * height_ / wideth_;
				wideth_ = w_;

			
		} else if ("auto".equals(wideth_or_height)) {
			if (wideth_ < height_) {

				height_ = w_ * height_ / wideth_;
				wideth_ = w_;

			} else {

				wideth_ = h_ * wideth_ / height_;
				height_ = h_;

			}
		}

		// System.out.println("wideth:" + wideth_);
		// System.out.println("height:" + height_);
		return new int[] { (int) wideth_, (int) height_ };

	}
	
	

}
