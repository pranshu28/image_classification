package org.tensorflow.demo;

import android.graphics.Bitmap;
import android.graphics.RectF;
import java.util.List;

public abstract interface Classifier
{
  public abstract void close();
  
  public abstract List<Recognition> recognizeImage(Bitmap paramBitmap);
  
  public static class Recognition
  {
    private final Float confidence;
    private final String id;
    private final RectF location;
    private final String title;
    
    public Recognition(String paramString1, String paramString2, Float paramFloat, RectF paramRectF)
    {
      this.id = paramString1;
      this.title = paramString2;
      this.confidence = paramFloat;
      this.location = paramRectF;
    }
    
    public Float getConfidence()
    {
      return this.confidence;
    }
    
    public String getId()
    {
      return this.id;
    }
    
    public RectF getLocation()
    {
      return new RectF(this.location);
    }
    
    public String getTitle()
    {
      return this.title;
    }
    
    public String toString()
    {
      String str = "";
      if (this.id != null) {
        str = str + "[" + this.id + "] ";
      }
      if (this.title != null) {
        str = str + this.title + " ";
      }
      if (this.confidence != null)
      {
        StringBuilder localStringBuilder = new StringBuilder().append(str);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Float.valueOf(100.0F * this.confidence.floatValue());
        str = String.format("(%.1f%%) ", arrayOfObject);
      }
      if (this.location != null) {
        str = str + this.location + " ";
      }
      return str.trim();
    }
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\Classifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */