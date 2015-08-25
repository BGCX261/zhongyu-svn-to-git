package com.piaoyou.util;

import java.awt.image.ColorModel;
import java.awt.image.ImageFilter;

public class AreaAveragingScaleFilter extends ImageFilter {

    private ColorModel rgbModel;
    private long []    alphas;
    private long []    reds;
    private long []    greens;
    private long []    blues;

    protected int      srcWidth;
    protected int      srcHeight;
    private   int []   srcPixels;
    protected int      destWidth;
    protected int      destHeight;
    protected int []   destPixels;

    {
      // Test if the class java.awt.image.ColorModel can be loaded
      //boolean classColorModelAccessible = PJAGraphicsManager.getDefaultGraphicsManager ().isClassAccessible ("java.awt.image.ColorModel");
      // modified by minhnn
      boolean classColorModelAccessible = isClassAccessible ("java.awt.image.ColorModel");
      if (classColorModelAccessible)
        rgbModel = ColorModel.getRGBdefault ();
    }

    /**
     * Constructs an AreaAveragingScaleFilter that scales the pixels from
     * its source Image as specified by the width and height parameters.
     * @param width  the target width to scale the image
     * @param height the target height to scale the image
     */
    public AreaAveragingScaleFilter(int width, int height) {
        destWidth = width;
        destHeight = height;
    }

    public void setDimensions (int w, int h)
    {
      srcWidth = w;
      srcHeight = h;
      if (destWidth < 0)
      {
        if (destHeight < 0)
        {
          destWidth = srcWidth;
          destHeight = srcHeight;
        }
        else
          destWidth = srcWidth * destHeight / srcHeight;
      }
      else if (destHeight < 0)
        destHeight = srcHeight * destWidth / srcWidth;

      consumer.setDimensions (destWidth, destHeight);
    }

    public void setHints (int hints)
    {
      // Images are sent entire frame by entire frame
      consumer.setHints (  (hints & (SINGLEPASS | SINGLEFRAME))
                         | TOPDOWNLEFTRIGHT);
    }

    public void imageComplete (int status)
    {
      if (   status == STATICIMAGEDONE
          || status == SINGLEFRAMEDONE)
        accumPixels (0, 0, srcWidth, srcHeight, rgbModel, srcPixels, 0, srcWidth);
      consumer.imageComplete (status);
    }

    public void setPixels (int x, int y, int width, int height,
                           ColorModel model, byte pixels [], int offset, int scansize)
    {
      // Store pixels in srcPixels array
      if (srcPixels == null)
        srcPixels = new int [srcWidth * srcHeight];
      for (int row = 0, destRow = y * srcWidth;
           row < height;
           row++, destRow += srcWidth)
      {
        int rowOff = offset + row * scansize;
        for (int col = 0; col < width; col++)
          // v1.2 : Added & 0xFF to disable sign bit
          srcPixels [destRow + x + col] = model.getRGB (pixels [rowOff + col] & 0xFF);
      }
    }

    public void setPixels (int x, int y, int width, int height,
                   ColorModel model, int pixels[], int offset, int scansize)
    {
      // Store pixels in srcPixels array
      if (srcPixels == null)
        srcPixels = new int [srcWidth * srcHeight];
      for (int row = 0, destRow = y * srcWidth;
           row < height;
           row++, destRow += srcWidth)
      {
        int rowOff = offset + row * scansize;
        for (int col = 0; col < width; col++)
          // If model == null, consider it's the default RGB model
          srcPixels [destRow + x + col] = model == null
                                                     ? pixels [rowOff + col]
                                                     : model.getRGB (pixels [rowOff + col]);
      }
    }

    private int [] calcRow ()
    {
      long mult = (srcWidth * srcHeight) << 32;
      if (destPixels == null)
        destPixels = new int [destWidth];

      for (int x = 0; x < destWidth; x++)
      {
        int a = (int)roundDiv (alphas [x], mult);
        int r = (int)roundDiv (reds   [x], mult);
        int g = (int)roundDiv (greens [x], mult);
        int b = (int)roundDiv (blues  [x], mult);
        a = Math.max (Math.min (a, 255), 0);
        r = Math.max (Math.min (r, 255), 0);
        g = Math.max (Math.min (g, 255), 0);
        b = Math.max (Math.min (b, 255), 0);
        destPixels [x] = (a << 24 | r << 16 | g << 8 | b);
      }

      return destPixels;
    }

    private void accumPixels (int x, int y, int w, int h,
                              ColorModel model, int [] pixels, int off,
                              int scansize)
    {
      reds   = new long [destWidth];
      greens = new long [destWidth];
      blues  = new long [destWidth];
      alphas = new long [destWidth];

      int sy = y;
      int syrem = destHeight;
      int dy = 0;
      int dyrem = 0;
      while (sy < y + h)
      {
        if (dyrem == 0)
        {
          for (int i = 0; i < destWidth; i++)
            alphas [i] =
            reds   [i] =
            greens [i] =
            blues  [i] = 0;

          dyrem = srcHeight;
        }

        int amty = Math.min (syrem, dyrem);
        int sx = 0;
        int dx = 0;
        int sxrem = 0;
        int dxrem = srcWidth;
        int a = 0,
            r = 0,
            g = 0,
            b = 0;
        while (sx < w)
        {
          if (sxrem == 0)
          {
            sxrem = destWidth;
            int rgb = pixels [off + sx];
            a = rgb >>> 24;
            r = (rgb >> 16) & 0xFF;
            g = (rgb >>  8) & 0xFF;
            b = rgb & 0xFF;
          }

          int  amtx = Math.min (sxrem, dxrem);
          long mult = (amtx * amty) << 32;
          alphas [dx] += mult * a;
          reds   [dx] += mult * r;
          greens [dx] += mult * g;
          blues  [dx] += mult * b;

          if ((sxrem -= amtx) == 0)
            sx++;

          if ((dxrem -= amtx) == 0)
          {
            dx++;
            dxrem = srcWidth;
          }
        }

        if ((dyrem -= amty) == 0)
        {
          int outpix [] = calcRow ();
          do
          {
            consumer.setPixels (0, dy, destWidth, 1,
                                rgbModel, outpix, 0, destWidth);
            dy++;
          }
          while ((syrem -= amty) >= amty && amty == srcHeight);
        }
        else
          syrem -= amty;

        if (syrem == 0)
        {
          syrem = destHeight;
          sy++;
          off += scansize;
        }
      }
    }
    ////////////////
    // util method
    ////////////////
    // util method
    /**
     * Returns the rounded result of <code>dividend / divisor</code>, avoiding the use of floating
     * point operation (returns the same as <code>Math.round((float)dividend / divisor)</code>).
     * @param dividend A <code>int</code> number to divide.
     * @param divisor  A <code>int</code> divisor.
     * @return dividend / divisor rounded to the closest <code>int</code> integer.
     */
    public static int roundDiv(int dividend, int divisor) {
        final int remainder = dividend % divisor;
        if (Math.abs(remainder) * 2 <= Math.abs(divisor))
            return dividend / divisor;
        else
        if (dividend * divisor < 0)
            return dividend / divisor - 1;
        else
            return dividend / divisor + 1;
    }

    /**
     * Returns the rounded result of <code>dividend / divisor</code>, avoiding the use of floating
     * point operation (returns the same as <code>Math.round((double)dividend / divisor)</code>).
     * @param dividend A <code>long</code> number to divide.
     * @param divisor  A <code>long</code> divisor.
     * @return dividend / divisor rounded to the closest <code>long</code> integer.
     */
    public static long roundDiv (long dividend, long divisor) {
        final long remainder = dividend % divisor;
        if (Math.abs (remainder) * 2 <= Math.abs (divisor))
          return dividend / divisor;
        else
          if (dividend * divisor < 0)
            return dividend / divisor - 1;
          else
            return dividend / divisor + 1;
    }

    /**
     * Returns <code>true</code> if it successes to load the class <code>className</code>.
     * If security manager is too restictive, it is possible that the classes <code>java.awt.Color</code>,
     * <code>java.awt.Rectangle</code>, <code>java.awt.Font</code>, <code>java.awt.FontMetrics</code>
     * and <code>java.awt.image.ColorModel</code> (and also <code>java.awt.Dimension</code> and other classes
     * not required by PJA classes) can't be loaded because they need either the class <code>java.awt.Toolkit</code>
     * or the library awt to be accessible to call their <code>initIDs ()</code> native method.
     * @param  className  the fully qualified class name.
     * @return <code>true</code> if <code>java.awt.Toolkit</code> class could be loaded.
     */
   public static boolean isClassAccessible(String className) {
       try {
           Class.forName(className);
           return true;
       } catch (ClassNotFoundException e) {} catch (LinkageError error) {} // Thrown by some AWT classes which require awt library in static initializer.

       return false;
   }
 
  

}
