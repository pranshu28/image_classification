package org.tensorflow.demo.env;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils
{
  private static final Logger LOGGER = new Logger();
  
  public static native void convertARGB8888ToYUV420SP(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public static native void convertRGB565ToYUV420SP(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2);
  
  public static native void convertYUV420SPToARGB8888(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public static native void convertYUV420SPToRGB565(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2);
  
  public static native void convertYUV420ToARGB8888(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean);
  
  public static int getYUVByteSize(int paramInt1, int paramInt2)
  {
    return paramInt1 * paramInt2 + 2 * ((paramInt1 + 1) / 2 * ((paramInt2 + 1) / 2));
  }
  
  public static void saveBitmap(Bitmap paramBitmap)
  {
    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "tensorflow";
    Logger localLogger = LOGGER;
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(paramBitmap.getWidth());
    arrayOfObject[1] = Integer.valueOf(paramBitmap.getHeight());
    arrayOfObject[2] = str;
    localLogger.i("Saving %dx%d bitmap to %s.", arrayOfObject);
    File localFile1 = new File(str);
    if (!localFile1.mkdirs()) {
      LOGGER.i("Make dir failed", new Object[0]);
    }
    File localFile2 = new File(localFile1, "preview.png");
    if (localFile2.exists()) {
      localFile2.delete();
    }
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 99, localFileOutputStream);
      localFileOutputStream.flush();
      localFileOutputStream.close();
      return;
    }
    catch (Exception localException)
    {
      LOGGER.e(localException, "Exception!", new Object[0]);
    }
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\env\ImageUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */