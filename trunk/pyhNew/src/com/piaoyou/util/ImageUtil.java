package com.piaoyou.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

public abstract class ImageUtil {
	
	public static void cutMyPicture(InputStream in, OutputStream out,Rectangle rect){
		if(in==null){
			throw new RuntimeException("ImageUtil->cutPicture->is stream is null");
		}
		if(out==null){
			throw new RuntimeException("ImageUtil->cutPicture->iis stream is null");
		}
		if(rect==null){
			throw new RuntimeException("ImageUtil->cutPicture->rect is null");
		}
		try{
			 	byte[] srcByte = FileUtil.getBytes(in);
	            InputStream is = new ByteArrayInputStream(srcByte);
	            BufferedImage srcImage = ImageIO.read(is);
	            if (srcImage == null) {
	                throw new RuntimeException("Cannot decode image. Please check your file again.");
	            }
	            ImageIO.write(srcImage.getSubimage(rect.x, rect.y, rect.width,rect.height), "jpeg", out);
		}catch(Exception ee){
			ee.printStackTrace();
		}
		finally{
		  	try {
		  		if(in!=null){
		  			in.close();	
		  		}
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
            	if(out!=null){
            		 out.close();
            	}
            } catch (IOException e) { 
                e.printStackTrace();
            }
		}
		
	}
	/**
	 * 切图片
	 */
	public static void cutPicture(InputStream in, OutputStream out,Rectangle rect){
		if(in==null){
			throw new RuntimeException("ImageUtil->cutPicture->is stream is null");
		}
		if(out==null){
			throw new RuntimeException("ImageUtil->cutPicture->iis stream is null");
		}
		if(rect==null){
			throw new RuntimeException("ImageUtil->cutPicture->rect is null");
		}
		try{
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
			ImageReader reader = it.next();
			ImageInputStream iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg",out);
		}catch(Exception ee){
			ee.printStackTrace();
		}
		finally{
		  	try {
		  		if(in!=null){
		  			in.close();	
		  		}
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
            	if(out!=null){
            		 out.close();
            	}
            } catch (IOException e) { 
                e.printStackTrace();
            }
		}
		
	}
	/**
	 * 获得图片的高度
	 * @param inputStream
	 * @return
	 */
	public static int getHeight(InputStream inputStream){
 	   int imgHeight=0;
 	   if(inputStream==null){
 		   return 0;
 	   }
 	   try{
 		   byte[] srcByte = FileUtil.getBytes(inputStream);
 	       InputStream is = new ByteArrayInputStream(srcByte);
 	       BufferedImage srcImage = ImageIO.read(is);
 	       if (srcImage == null) {
 	          return 0;
 	       }
 	       imgHeight = srcImage.getHeight();
 	   }catch(Exception e){
 		   return 0;
 	   }
 	   finally{
 		   if(inputStream!=null){
 			   try{
 				   inputStream.close();
 			   }catch(Exception ee){
 			   }
 		   }
 	   }
 	   return imgHeight;
	}
	
	/**
     * 获取图像的宽度
     * @param inputStream
     * @return
     */
    public static int getWidth(InputStream inputStream){
 	   int imgWidth=0;
 	   if(inputStream==null){
 		   return 0;
 	   }
 	   try{
 		   byte[] srcByte = FileUtil.getBytes(inputStream);
 	       InputStream is = new ByteArrayInputStream(srcByte);
 	       BufferedImage srcImage = ImageIO.read(is);
 	       if (srcImage == null) {
 	          return 0;
 	       }
 	       imgWidth  = srcImage.getWidth();
 	   }catch(Exception e){
 		   return 0;
 	   }
 	   finally{
 		  if(inputStream!=null){
			   try{
				   inputStream.close();
			   }catch(Exception ee){
				   ee.printStackTrace();
			   }
		   }
 	   }
 	   return imgWidth;
    }
    /***
     * 宽和高同时按K倍比例压缩图片
     * @param in
     * @param out
     * @param k
     */
	public static void tarPicture(InputStream inputStream, OutputStream outputStream,double k){
		if(inputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->inputStream stream is null");
		}
		if(outputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->outputStream stream is null");
		}
		try {
	            byte[] srcByte = FileUtil.getBytes(inputStream);
	            InputStream is = new ByteArrayInputStream(srcByte);
	            BufferedImage srcImage = ImageIO.read(is);
	            if (srcImage == null) {
	                throw new RuntimeException("Cannot decode image. Please check your file again.");
	            }
	            int orgHeight  = srcImage.getHeight();
	            int orgWidth = srcImage.getWidth();
	        	int newHeight=(int)Math.round(orgHeight/k);
	    		int newWidth=(int)Math.round(orgWidth/k);
	            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	            Graphics2D g = bufferedImage.createGraphics();
	            Image scaleImage = getScaledInstance(srcImage, newWidth, newHeight);
                g.drawImage(scaleImage, 0, 0, null);
                g.dispose();
	            ImageIO.write(bufferedImage, "jpeg", outputStream);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            try {
	                inputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            try {
	                outputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		
	}
	
	/****
	 * 将图片压缩到指定的像素
	 * @param inputStream
	 * @param outputStream
	 * @param maxHeight
	 * @param maxWidth
	 */
	public static void tarPicture(InputStream inputStream, OutputStream outputStream,int maxWidth,int maxHeight){
		if(inputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture-inputStream is null");
		}
		if(outputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->outputStream is null");
		}
		if (maxWidth <= 0) {
	            throw new IllegalArgumentException("maxWidth must >= 0");
	    }
	    if (maxHeight <= 0) {
	            throw new IllegalArgumentException("maxHeight must >= 0");
	    }
	    try {
            byte[] srcByte = FileUtil.getBytes(inputStream);
            InputStream is = new ByteArrayInputStream(srcByte);
            BufferedImage srcImage = ImageIO.read(is);
            if (srcImage == null) {
                throw new RuntimeException("Cannot decode image. Please check your file again.");
            }
            int imgWidth  = srcImage.getWidth();
            int imgHeight = srcImage.getHeight();
            double realRate=(double)imgWidth/(double)imgHeight;
            double targetRate=(double)maxWidth/(double)maxHeight;
            if(imgWidth>maxWidth || imgHeight>maxHeight){
            	 if(realRate>targetRate){
            		 if(imgWidth>maxWidth){
	                     double temp=(double)maxWidth/(double)imgWidth;
	                     imgWidth=maxWidth;
	                     imgHeight=(int)(imgHeight*(temp));
            		 }
                 }else{
                	 if(imgHeight>maxHeight){
                		 double temp=(double)maxHeight/(double)imgHeight;
                		 imgHeight=maxHeight;
                		 imgWidth=(int)(imgWidth*(temp));
                	 }
                 }
            }
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
            g.drawImage(scaleImage, 0, 0, null);
            g.dispose();
            ImageIO.write(bufferedImage, "jpeg", outputStream);
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	/***
	 * 先按照宽或者高等比压缩图片然后再裁剪图片
	 * @param inputStream
	 * @param outputStream
	 * @param maxHeight
	 * @param maxWidth
	 * @param rect
	 */
	public static void tarAndCutPicture(InputStream inputStream, OutputStream outputStream,int maxWidth,int maxHeight,Rectangle rect){
		if(inputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture-inputStream is null");
		}
		if(outputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->outputStream is null");
		}
		if (maxWidth <= 0) {
	            throw new IllegalArgumentException("maxWidth must >= 0");
	    }
	    if (maxHeight <= 0) {
	            throw new IllegalArgumentException("maxHeight must >= 0");
	    }
	    try {
            byte[] srcByte = FileUtil.getBytes(inputStream);
            InputStream is = new ByteArrayInputStream(srcByte);
            BufferedImage srcImage = ImageIO.read(is);
            if (srcImage == null) {
                throw new IOException("Cannot decode image. Please check your file again.");
            }
            int imgWidth  = srcImage.getWidth();
            int imgHeight = srcImage.getHeight();
            double realRate=(double)imgWidth/(double)imgHeight;
            double targetRate=(double)maxWidth/(double)maxHeight;
            if(imgWidth>maxWidth || imgHeight>maxHeight){
            	 if(realRate>targetRate){
            		 if(imgWidth>maxWidth){
	                     double temp=(double)maxWidth/(double)imgWidth;
	                     imgWidth=maxWidth;
	                     imgHeight=(int)(imgHeight*(temp));
            		 }
                 }else{
                	 if(imgHeight>maxHeight){
                		 double temp=(double)maxHeight/(double)imgHeight;
                		 imgHeight=maxHeight;
                		 imgWidth=(int)(imgWidth*(temp));
                	 }
                 }
            }
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
            Rectangle newRect=new Rectangle();
            //从左边开始切图
           /* if(imgWidth<(rect.x+rect.width)){
            	newRect.setRect(0, rect.y, imgWidth, rect.height);
            }else{
            	newRect.setRect(rect.x , rect.y, rect.width, rect.height);
            }
            if(imgHeight<(rect.y+rect.height)){
            	newRect.setRect(newRect.x, 0, newRect.width, imgHeight);
            }else{
            	newRect.setRect(newRect.x, rect.y, newRect.width, rect.height);
            }*/
            //从中间开始切图
            if(imgWidth<rect.width){
            	newRect.setRect(0, rect.y, imgWidth, rect.height);
            }else{
            	int left=(imgWidth-rect.width)/2;
            	newRect.setRect(left, rect.y, rect.width, rect.height);
            }
            if(imgHeight<rect.height){
            	newRect.setRect(newRect.x, 0, newRect.width, imgHeight);
            }else{
            	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
            }
            g.drawImage(scaleImage, 0, 0, null);
            g.dispose();
            ImageIO.write(bufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height), "jpeg", outputStream);
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	/**
	 * 根据图片的大小填充图片的内容
	 * @param srcImage
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image getScaledInstance(Image srcImage, int width, int height) {
        ImageFilter filter;
        filter = new AreaAveragingScaleFilter(width, height);
        ImageProducer prod = new FilteredImageSource(srcImage.getSource(), filter);
        Image newImage = Toolkit.getDefaultToolkit().createImage(prod);
        ImageIcon imageIcon = new ImageIcon(newImage);
        return imageIcon.getImage();
    }
	/**
	 * 处理用户图像
	 * @param inputStream
	 * @param outputStream
	 * @param maxWidth
	 * @param maxHeight
	 * @param rect
	 */
	public static void processUserImage(InputStream inputStream, OutputStream outputStream){
		if(inputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture-inputStream is null");
		}
		if(outputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->outputStream is null");
		}
	    try {
            byte[] srcByte = FileUtil.getBytes(inputStream);
            InputStream is = new ByteArrayInputStream(srcByte);
            BufferedImage srcImage = ImageIO.read(is);
            if (srcImage == null) {
                throw new IOException("Cannot decode image. Please check your file again.");
            }
            int imgWidth  = srcImage.getWidth();
            int imgHeight = srcImage.getHeight();
            if(imgWidth>300){
            	double rate=(double)imgWidth/(double)300;
            	imgWidth=300;
            	imgHeight=(int)(imgHeight/rate);
	        	 BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
	             Graphics2D g = bufferedImage.createGraphics();
	             Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
	             Rectangle newRect=new Rectangle();
	             if(imgHeight>300){
	             	newRect.setRect(0, 0, 300, 300);
	             }
	             if(imgHeight<=300){
	             	newRect.setRect(0, 0, 300, imgHeight);
	             }
	             g.drawImage(scaleImage, 0, 0, null);
	             g.dispose();
	             ImageIO.write(bufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height), "jpeg", outputStream);
            }else if(imgWidth<300){
            	 double rate =(double)300/(double)imgWidth;
            	 imgWidth=300;
            	 imgHeight=(int)(imgHeight*rate);
            	 BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
                 Graphics2D g = bufferedImage.createGraphics();
                 Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
                 Rectangle newRect=new Rectangle();
                 if(imgHeight>300){
                 	newRect.setRect(0, 0, 300, 300);
                 }
                 if(imgHeight<=300){
                 	newRect.setRect(0, 0, 300, imgHeight);
                 }
                 g.drawImage(scaleImage, 0, 0, null);
                 g.dispose();
                 ImageIO.write(bufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height), "jpeg", outputStream);
            }else{
            	/* if(imgHeight!=300){
	            	 BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		             Graphics2D g = bufferedImage.createGraphics();
		             Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
		             Rectangle newRect=new Rectangle();
		             if(imgHeight>300){
		             	newRect.setRect(0, 0, 300, 300);
		             }
		             if(imgHeight<=300){
		             	newRect.setRect(0, 0, 300, imgHeight);
		             }
		             g.drawImage(scaleImage, 0, 0, null);
		             g.dispose();
		             ImageIO.write(bufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height), "jpeg", outputStream);
            	 }else{
            		FileUtil.copyFile(inputStream, outputStream);
            	 }*/
            }
           
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public static void tarAndCutPicture(InputStream inputStream, OutputStream outputStream,int maxWidth,Rectangle rect){
		if(inputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture-inputStream is null");
		}
		if(outputStream==null){
			throw new RuntimeException("ImageUtil->tarPicture->outputStream is null");
		}
		if (maxWidth <= 0) {
	            throw new IllegalArgumentException("maxWidth must >= 0");
	    }
	    try {
            byte[] srcByte = FileUtil.getBytes(inputStream);
            InputStream is = new ByteArrayInputStream(srcByte);
            BufferedImage srcImage = ImageIO.read(is);
            if (srcImage == null) {
                throw new IOException("Cannot decode image. Please check your file again.");
            }
            int imgWidth  = srcImage.getWidth();
            int imgHeight = srcImage.getHeight();
            if(imgWidth<imgHeight){
            	double rate=(double)imgWidth/(double)maxWidth;
            	imgHeight=(int)((double)imgHeight/rate);
        		imgWidth=maxWidth;
            }else{
            	double rate=(double)imgHeight/(double)maxWidth;
            	imgHeight=maxWidth;
        		imgWidth=(int)((double)imgWidth/rate);
            }
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            Image scaleImage = getScaledInstance(srcImage, imgWidth, imgHeight);
            Rectangle newRect=new Rectangle();
            //从左边开始切图
           /* if(imgWidth<(rect.x+rect.width)){
            	newRect.setRect(0, rect.y, imgWidth, rect.height);
            }else{
            	newRect.setRect(rect.x , rect.y, rect.width, rect.height);
            }
            if(imgHeight<(rect.y+rect.height)){
            	newRect.setRect(newRect.x, 0, newRect.width, imgHeight);
            }else{
            	newRect.setRect(newRect.x, rect.y, newRect.width, rect.height);
            }*/
            //从中间开始切图
            if(imgWidth<rect.width){
            	newRect.setRect(0, rect.y, imgWidth, rect.height);
            }else{
            	int left=(imgWidth-rect.width)/2;
            	newRect.setRect(left, rect.y, rect.width, rect.height);
            }
            if(imgHeight<rect.height){
            	newRect.setRect(newRect.x, 0, newRect.width, imgHeight);
            }else{
            	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
            }
            g.drawImage(scaleImage, 0, 0, null);
            g.dispose();
            ImageIO.write(bufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height), "jpeg", outputStream);
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
}
