package com.piaoyou.img;

/**
 * ImageCompress 提供用户将大图片按比例压缩为小图片，支持JPG
 * Please refer to: <BR>
 * http://blog.csdn.net/feng_sundy/archive/2008/08/05/2769572.aspx
 * <P>
 * @author feng_sunddy <sundysea@hotmail.com>
 * @version 1.0
 * @see java.awt.image.BufferedImage
**/

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageCompress extends Frame {
	private static final long serialVersionUID = 48L;
	public static void main(String[] args){
		String fileName = "d:/11.jpg";
		String gui = "";
    if (args.length > 0) fileName = args[0];
    if (args.length > 1) gui = "gui";
    if (gui.equals("gui")){
      new ImageCompress(fileName);
    }else{
			long c = System.currentTimeMillis();
			ImageCompress.ImageScale(getFilePath(fileName), getFileFullName(fileName), getFileName(fileName) + "-s." + getFileExt(fileName).toLowerCase());
			System.out.println("elapse time:" + (System.currentTimeMillis() - c)/1000.0f + "s");
	  }
	}
	
	private static final String version = "ImageCompress v1.0";
	
  public ImageCompress(String fileName) {
    super(version);
    file = fileName;
    createUI();
    loadImage(fileName);
    setVisible(true);
  }
  /**
   * A Hashtable member variable holds the image processing
   * operations, keyed by their names.
  **/
  
	private Panel mControlPanel;
	
	private BufferedImage mBufferedImage;
	
	private Label labelWidth = new Label("width:");    
	private TextField textWidth = new TextField(7);
  
	private Label labelHeight = new Label("height:");    
	private TextField textHeight = new TextField(7);
	
	private String file;

  /**
   * createUI() creates the user controls and lays out the window.
   * It also creates the event handlers (as inner classes) for
   * the user controls.
  **/
  private void createUI() {
    setFont(new Font("Serif", Font.PLAIN, 12));
    // Use a BorderLayout. The image will occupy most of the window,
    //   with the user controls at the bottom.
    setLayout(new BorderLayout());
  
    // Use a Label to display status messages to the user.
    final Label statusLabel = new Label("Welcome to " + version + ".");
    
    textWidth.setText("160");
    textHeight.setText("160");
    // Create a Button for loading a new image.
    Button loadButton = new Button("Load...");
    // Add a listener for the button. It pops up a file dialog
    //   and loads the selected image file.
    loadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        FileDialog fd = new FileDialog(ImageCompress.this);
        fd.setVisible(true);
        if (fd.getFile() == null) return;
        String path = fd.getDirectory() + fd.getFile();
        file = path;
        loadImage(path);
      }
    });
    
    Button buttonResize = new Button("Resize");
    buttonResize.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent ae){
    		resizeImage(file);
    		
    	}
    });

    // Add the user controls at the bottom of the window.
    mControlPanel = new Panel();
    mControlPanel.add(loadButton);
    mControlPanel.add(labelWidth);
    mControlPanel.add(textWidth);
    mControlPanel.add(labelHeight);
    mControlPanel.add(textHeight);
    mControlPanel.add(buttonResize);
    //mControlPanel.add(processChoice);
    mControlPanel.add(statusLabel);
    add(mControlPanel, BorderLayout.SOUTH);

    // Terminate the application if the user closes the window.
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0);
      }
    });
  }
  
  private void resizeImage(String fileName){
    try{
	  	Image image = javax.imageio.ImageIO.read(new File(file));
	    int imageWidth = image.getWidth(null);
	  	int imageHeight = image.getHeight(null);
	  	
	    float scale = getRatio(imageWidth,imageHeight,Integer.parseInt(textWidth.getText()),Integer.parseInt(textWidth.getText()));
	    imageWidth = (int)(scale*imageWidth);
	    imageHeight = (int)(scale*imageHeight);
	    
	    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
	    // Make a BufferedImage from the Image.
	    mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = mBufferedImage.createGraphics();
			
	    //Map readeringHint = new HashMap();
			//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
	    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //g.setRenderingHints(readeringHint);
			
	    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
	
			float[] kernelData2 = { 
					-0.125f, -0.125f, -0.125f,
	        -0.125f,2, -0.125f,
	        -0.125f,-0.125f, -0.125f };
	    Kernel kernel = new Kernel(3, 3, kernelData2);
	    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    mBufferedImage = cOp.filter(mBufferedImage, null);

	    repaint();

	    //file = ImageCompress.getFilePath(file) + ImageCompress.getFileName(file) + "-s." + ImageCompress.getFileExt(file).toLowerCase(); 

	    //FileOutputStream out = new FileOutputStream(file);
	    //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
	    //param.setQuality(0.9f, true);
	    //encoder.setJPEGEncodeParam(param);
	    //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    //encoder.encode(mBufferedImage);
	    //out.close();	    
	  }catch (IOException e){
	  }
  }
  
  private void loadImage(String fileName) {
    // Use a MediaTracker to fully load the image.
    Image image = Toolkit.getDefaultToolkit().getImage(fileName);
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);
    try { mt.waitForID(0); }
    catch (InterruptedException ie) { return; }
    if (mt.isErrorID(0)) return;
    // Make a BufferedImage from the Image.
    mBufferedImage = new BufferedImage(
        image.getWidth(null), image.getHeight(null),
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = mBufferedImage.createGraphics();
    g2.drawImage(image, null, null);

    adjustToImageSize();
    center();
    validate();
    repaint();
    setTitle(version + ": " + fileName);
  }
  
  private void adjustToImageSize() {
    if (!isDisplayable()) addNotify(); // Do this to get valid Insets.
    Insets insets = getInsets();
    int imageWidth = mBufferedImage.getWidth(); 
    int imageHeight = mBufferedImage.getHeight();
    imageWidth = 600;
    imageHeight = 550;
    int w = imageWidth + insets.left + insets.right;
    int h = imageHeight + insets.top + insets.bottom;
    h += mControlPanel.getPreferredSize().height;
    setSize(w, h);
  }
  
  /**
   * Center this window in the user's desktop.
  **/
  private void center() {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension d = getSize();
    int x = (screen.width - d.width) / 2;
    int y = (screen.height - d.height) / 2;
    setLocation(x, y);
  }
  
  /**
   * All paint() has to do is show the current image.
  **/
  public void paint(Graphics g) {
    if (mBufferedImage == null) return;
    Insets insets = getInsets();
    g.drawImage(mBufferedImage, insets.left, insets.top, null);
  }
  
  public static void ImageScale(String path,String fileName,String toFileName){
    try {
	    Image image = javax.imageio.ImageIO.read(new File(path + fileName));
	    int imageWidth = image.getWidth(null);
    	int imageHeight = image.getHeight(null);
	    float scale = getRatio(imageWidth,imageHeight,272,272);
	    imageWidth = (int)(scale*imageWidth);
	    imageHeight = (int)(scale*imageHeight);
	    
	    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
	    // Make a BufferedImage from the Image.
	    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = mBufferedImage.createGraphics();
			
	    //Map readeringHint = new HashMap();
			//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
	    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //g.setRenderingHints(readeringHint);
			
	    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
	    g2.dispose();
	    
			float[] kernelData2 = { 
					-0.125f, -0.125f, -0.125f,
          -0.125f,2, -0.125f,
          -0.125f,-0.125f, -0.125f };
	    Kernel kernel = new Kernel(3, 3, kernelData2);
	    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    mBufferedImage = cOp.filter(mBufferedImage, null);
	    
	   
	    //================================
	//    Rectangle newRect=new Rectangle();
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
//        if(imageWidth<rect.width){
//        	newRect.setRect(0, rect.y, imgWidth, rect.height);
//        }else{
//        	int left=(imgWidth-rect.width)/2;
//        	newRect.setRect(left, rect.y, rect.width, rect.height);
//        }
//        if(imageHeight<rect.height){
//        	newRect.setRect(newRect.x, 0, newRect.width, imgHeight);
//        }else{
//        	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
//        }
	    //mBufferedImage = mBufferedImage.getSubimage(0, 0, 272, 272);
	    //================================
	    
	    

	    FileOutputStream out = new FileOutputStream(path + toFileName);
      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
      //param.setQuality(0.9f, true);
      //encoder.setJPEGEncodeParam(param);
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    encoder.encode(mBufferedImage);
	    out.close();
    }catch (FileNotFoundException fnf){
    }catch (IOException ioe){
    }finally{
    
    }
  }
  
  public static float getRatio(int width,int height,int maxWidth,int maxHeight){
  	float Ratio = 1.0f;
  	float widthRatio ;
  	float heightRatio ;
  	//
  	if(width<height){
  		widthRatio = (float)maxWidth/width;
  	    heightRatio = (float)maxHeight/width;
  	}else if(width>height){
  		widthRatio = (float)maxWidth/height;
  	    heightRatio = (float)maxHeight/height;
  	}else{
  	    widthRatio = (float)maxWidth/width;
  	    heightRatio = (float)maxHeight/height;
  	}
  	//

    if(widthRatio<1.0 || heightRatio<1.0){
      Ratio = widthRatio<=heightRatio?widthRatio:heightRatio;
    }
    return Ratio;
  }
  
  public static String getFileExt(String filePath) {
    String tmp = filePath.substring(filePath.lastIndexOf(".") + 1);
    return tmp.toUpperCase();
  }

  public static String getFileName(String filePath) {
    int pos = -1;
    int endPos = -1;
    if (!filePath.equals("")) {
      if (filePath.lastIndexOf("/") != -1) {
        pos = filePath.lastIndexOf("/") + 1;
      } else if (filePath.lastIndexOf("\\") != -1) {
        pos = filePath.lastIndexOf("\\") + 1;
      }
      if (pos == -1)
        pos = 0;
      filePath = filePath.substring(pos);
      endPos = filePath.lastIndexOf(".");
      if (endPos == -1) {
        return filePath;
      } else {
        return filePath.substring(0, endPos);
      }
    } else {
      return "";
    }
  }

  public static String getFileFullName(String filePath) {
    int pos = -1;
    if (!filePath.equals("")) {
      if (filePath.lastIndexOf("/") != -1) {
        pos = filePath.lastIndexOf("/") + 1;
      } else if (filePath.lastIndexOf("\\") != -1) {
        pos = filePath.lastIndexOf("\\") + 1;
      }
      if (pos == -1)
        pos = 0;
      return filePath.substring(pos);
    } else {
      return "";
    }
  }
  
  public static String getFilePath(String filePath) {
    int pos = -1;
    if (!filePath.equals("")) {
      if (filePath.lastIndexOf("/") != -1) {
        pos = filePath.lastIndexOf("/") + 1;
      } else if (filePath.lastIndexOf("\\") != -1) {
        pos = filePath.lastIndexOf("\\") + 1;
      }
      if (pos != -1) {
        return filePath.substring(0, pos);
      } else {
        return "";
      }
    } else {
      return "";
    }
  }
  /**
   * 先压缩后截取
   * @param directory
   * @param fileName
   * @param toFileName
   * @param standard
   * @param rect
   */
  
  public static void ImageScale(File directory,String fileName,String toFileName,int standard,Rectangle rect){
	    try {
		    Image image = javax.imageio.ImageIO.read(new File(directory,fileName));
		    int imageWidth = image.getWidth(null);
	    	int imageHeight = image.getHeight(null);
		    float scale = getRatio(imageWidth,imageHeight,standard,standard);
		    imageWidth = (int)(scale*imageWidth);
		    imageHeight = (int)(scale*imageHeight);
		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
		    // Make a BufferedImage from the Image.
		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
		    Graphics2D g2 = mBufferedImage.createGraphics();
		    //Map readeringHint = new HashMap();
				//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
		    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    //g.setRenderingHints(readeringHint);
		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
		    g2.dispose();
				float[] kernelData2 = { 
						-0.125f, -0.125f, -0.125f,
	          -0.125f,2, -0.125f,
	          -0.125f,-0.125f, -0.125f };
		    Kernel kernel = new Kernel(3, 3, kernelData2);
		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		    mBufferedImage = cOp.filter(mBufferedImage, null);
		    
		   
		    //================================
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
	        if(imageWidth<rect.width){
	        	newRect.setRect(0, rect.y, imageWidth, rect.height);
	        }else{
	        	int left=(imageWidth-rect.width)/2;
	        	newRect.setRect(left, rect.y, rect.width, rect.height);
	        }
	        if(imageHeight<rect.height){
	        	newRect.setRect(newRect.x, 0, newRect.width, imageHeight);
	        }else{
	        	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
	        }
		    mBufferedImage = mBufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height);
		    FileOutputStream out = new FileOutputStream(new File(directory,toFileName));
	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
	      //param.setQuality(0.9f, true);
	      //encoder.setJPEGEncodeParam(param);
		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		    encoder.encode(mBufferedImage);
		    out.close();
	    }catch (FileNotFoundException fnf){
	    }catch (IOException ioe){
	    }finally{
	    
	    }
	  }
  
  public static void ImageScale(File orgFile,File desFile,int standard,Rectangle rect){
	    try {
		    Image image = javax.imageio.ImageIO.read(orgFile);
		    int imageWidth = image.getWidth(null);
	    	int imageHeight = image.getHeight(null);
		    float scale = getRatio(imageWidth,imageHeight,standard,standard);
		    imageWidth = (int)(scale*imageWidth);
		    imageHeight = (int)(scale*imageHeight);
		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
		    // Make a BufferedImage from the Image.
		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
		    Graphics2D g2 = mBufferedImage.createGraphics();
		    //Map readeringHint = new HashMap();
				//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
		    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    //g.setRenderingHints(readeringHint);
		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
		    g2.dispose();
				float[] kernelData2 = { 
						-0.125f, -0.125f, -0.125f,
	          -0.125f,2, -0.125f,
	          -0.125f,-0.125f, -0.125f };
		    Kernel kernel = new Kernel(3, 3, kernelData2);
		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		    mBufferedImage = cOp.filter(mBufferedImage, null);
		    
		   
		    //================================
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
	        if(imageWidth<rect.width){
	        	newRect.setRect(0, rect.y, imageWidth, rect.height);
	        }else{
	        	int left=(imageWidth-rect.width)/2;
	        	newRect.setRect(left, rect.y, rect.width, rect.height);
	        }
	        if(imageHeight<rect.height){
	        	newRect.setRect(newRect.x, 0, newRect.width, imageHeight);
	        }else{
	        	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
	        }
		    mBufferedImage = mBufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height);
		    FileOutputStream out = new FileOutputStream(desFile);
	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
	      //param.setQuality(0.9f, true);
	      //encoder.setJPEGEncodeParam(param);
		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		    encoder.encode(mBufferedImage);
		    out.close();
	    }catch (FileNotFoundException fnf){
	    }catch (IOException ioe){
	    }finally{
	    
	    }
	  }
       /**
        * 等比压缩图片（k)
        * @param directory
        * @param fileName
        * @param toFileName
        * @param k
        */
  	  public static void tarPicture(File directory,String fileName,String toFileName,double k){
  		 try {
 		    Image image = javax.imageio.ImageIO.read(new File(directory,fileName));
 		    int imageWidth = image.getWidth(null);
 	    	int imageHeight = image.getHeight(null);
 		    imageWidth = (int)((double)imageWidth/k);
 		    imageHeight = (int)((double)imageHeight/k);
 		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
 		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
 		    Graphics2D g2 = mBufferedImage.createGraphics();
 		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
 		    g2.dispose();
 			float[] kernelData2 = { 
 						-0.125f, -0.125f, -0.125f,
 	          -0.125f,2, -0.125f,
 	          -0.125f,-0.125f, -0.125f };
 		    Kernel kernel = new Kernel(3, 3, kernelData2);
 		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
 		    mBufferedImage = cOp.filter(mBufferedImage, null);
 		    FileOutputStream out = new FileOutputStream(new File(directory,toFileName));
 		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
 		    encoder.encode(mBufferedImage);
 		    out.close();
 	    }catch (FileNotFoundException fnf){
 	    }catch (IOException ioe){
 	    }finally{
 	    
 	    }
  	  }
  	  /**
  	   * 上传用户图像
  	   * @param directory
  	   * @param fileName
  	   * @param toFileName
  	   */
  	  public static void processUserImage(File directory,String fileName,String toFileName){
  		  try {
  		    Image image = javax.imageio.ImageIO.read(new File(directory,fileName));
  		    int imageWidth = image.getWidth(null);
  	    	int imageHeight = image.getHeight(null);
  	    	if(imageWidth>300){
  	    		double rate=(double)imageWidth/(double)300;
  	    		imageWidth=300;
  	    		imageHeight=(int)(imageHeight/rate);
  	    		image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
  	  		    // Make a BufferedImage from the Image.
  	  		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
  	  		    Graphics2D g2 = mBufferedImage.createGraphics();
  	  		    //Map readeringHint = new HashMap();
  	  				//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
  	  		    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  	  		    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
  	  		    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
  	  		    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
  	  		    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  	  		    //g.setRenderingHints(readeringHint);
  	  		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
  	  		    g2.dispose();
  	  				float[] kernelData2 = { 
  	  						-0.125f, -0.125f, -0.125f,
  	  	          -0.125f,2, -0.125f,
  	  	          -0.125f,-0.125f, -0.125f };
  	  		    Kernel kernel = new Kernel(3, 3, kernelData2);
  	  		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
  	  		    mBufferedImage = cOp.filter(mBufferedImage, null);
  	  		    
  	  		   
  	  		    //================================
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
             if(imageHeight>300){
             	newRect.setRect(0, 0, 300, 300);
             }
             if(imageHeight<=300){
             	newRect.setRect(0, 0, 300, imageHeight);
             }
 		     mBufferedImage = mBufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height);
 		     FileOutputStream out = new FileOutputStream(new File(directory,toFileName));
  	  	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
  	  	      //param.setQuality(0.9f, true);
  	  	      //encoder.setJPEGEncodeParam(param);
  	  		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
  	  		    encoder.encode(mBufferedImage);
  	  		    out.close();
  	    	}else if(imageWidth<300){
  	    		double rate =(double)300/(double)imageWidth;
  	    		imageWidth=300;
            	 imageHeight=(int)(imageHeight*rate);
 	    		 image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
 	  		    // Make a BufferedImage from the Image.
 	  		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
 	  		    Graphics2D g2 = mBufferedImage.createGraphics();
 	  		    //Map readeringHint = new HashMap();
 	  				//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
 	  		    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 	  		    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
 	  		    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
 	  		    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
 	  		    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
 	  		    //g.setRenderingHints(readeringHint);
 	  		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
 	  		    g2.dispose();
 	  				float[] kernelData2 = { 
 	  						-0.125f, -0.125f, -0.125f,
 	  	          -0.125f,2, -0.125f,
 	  	          -0.125f,-0.125f, -0.125f };
 	  		    Kernel kernel = new Kernel(3, 3, kernelData2);
 	  		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
 	  		    mBufferedImage = cOp.filter(mBufferedImage, null);
 	  		    //================================
 	  		    Rectangle newRect=new Rectangle();
 	  		    if(imageHeight>300){
 	  		    	newRect.setRect(0, 0, 300, 300);
 	  		    }
 	  		    if(imageHeight<=300){
 	  		    	newRect.setRect(0, 0, 300, imageWidth);
 	  		    }
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
 	  		    mBufferedImage = mBufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height);
 	  		    FileOutputStream out = new FileOutputStream(new File(directory,toFileName));
 	  	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
 	  	      //param.setQuality(0.9f, true);
 	  	      //encoder.setJPEGEncodeParam(param);
 	  		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
 	  		    encoder.encode(mBufferedImage);
 	  		    out.close();
 	    	
  	    		
  	    	}
  	    }catch (FileNotFoundException fnf){
  	    }catch (IOException ioe){
  	    }finally{
  	    
  	    }
  		  
  	  }
  	  /**
  	   * 截图
  	   * @param orgFile
  	   * @param desFile
  	   * @param standard
  	   * @param rect
  	   */
  	  public static void cutPicture(File orgFile,File desFile,Rectangle rect){
  		  try {
  		    Image image = javax.imageio.ImageIO.read(orgFile);
  		    int imageWidth = image.getWidth(null);
  	    	int imageHeight = image.getHeight(null);
  		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
  		    // Make a BufferedImage from the Image.
  		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
  		    Graphics2D g2 = mBufferedImage.createGraphics();
  		    //Map readeringHint = new HashMap();
  				//readeringHint.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
  		    //readeringHint.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  		    //readeringHint.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
  		    //readeringHint.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
  		    //readeringHint.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);//VALUE_INTERPOLATION_BICUBIC
  		    //readeringHint.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  		    //g.setRenderingHints(readeringHint);
  		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
  		    g2.dispose();
  				float[] kernelData2 = { 
  						-0.125f, -0.125f, -0.125f,
  	          -0.125f,2, -0.125f,
  	          -0.125f,-0.125f, -0.125f };
  		    Kernel kernel = new Kernel(3, 3, kernelData2);
  		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
  		    mBufferedImage = cOp.filter(mBufferedImage, null);
  		    
  		   
  		    //================================
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
  	        if(imageWidth<rect.width){
  	        	newRect.setRect(0, rect.y, imageWidth, rect.height);
  	        }else{
  	        	int left=(imageWidth-rect.width)/2;
  	        	newRect.setRect(left, rect.y, rect.width, rect.height);
  	        }
  	        if(imageHeight<rect.height){
  	        	newRect.setRect(newRect.x, 0, newRect.width, imageHeight);
  	        }else{
  	        	newRect.setRect(newRect.x, 0, newRect.width, rect.height);
  	        }
  		    mBufferedImage = mBufferedImage.getSubimage(newRect.x, newRect.y, newRect.width,newRect.height);
  		    FileOutputStream out = new FileOutputStream(desFile);
  	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
  	      //param.setQuality(0.9f, true);
  	      //encoder.setJPEGEncodeParam(param);
  		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
  		    encoder.encode(mBufferedImage);
  		    out.close();
  	    }catch (FileNotFoundException fnf){
  	    }catch (IOException ioe){
  	    }finally{
  	    
  	    }
  	  }
  
    

}
