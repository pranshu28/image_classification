package org.tensorflow.demo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TensorflowClassifier
  implements Classifier
{
  private static final String TAG = "TensorflowClassifier";
  
  static
  {
    System.loadLibrary("tensorflow_demo");
  }
  
  private native String classifyImageBmp(Bitmap paramBitmap);
  
  private native String classifyImageRgb(int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  public void close() {}
  
  public native int initializeTensorflow(AssetManager paramAssetManager, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3);
  
  public List<Classifier.Recognition> recognizeImage(Bitmap paramBitmap)
  {
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = classifyImageBmp(paramBitmap).split("\n");
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str1 = arrayOfString[j];
      Log.i("TensorflowClassifier", "Parsing [" + str1 + "]");
      StringTokenizer localStringTokenizer = new StringTokenizer(str1);
      if (!localStringTokenizer.hasMoreTokens()) {}
      for (;;)
      {
        j++;
        break;
        String str2 = localStringTokenizer.nextToken();
        String str3 = localStringTokenizer.nextToken();
        float f = Float.parseFloat(str3);
        String str4 = str1.substring(2 + (str2.length() + str3.length()), str1.length());
        if (!str4.isEmpty()) {
          localArrayList.add(new Classifier.Recognition(str2, str4, Float.valueOf(f), null));
        }
      }
    }
    return localArrayList;
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\TensorflowClassifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */