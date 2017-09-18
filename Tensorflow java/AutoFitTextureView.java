package org.tensorflow.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View.MeasureSpec;

public class AutoFitTextureView
  extends TextureView
{
  private int ratioHeight = 0;
  private int ratioWidth = 0;
  
  public AutoFitTextureView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AutoFitTextureView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AutoFitTextureView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    if ((this.ratioWidth == 0) || (this.ratioHeight == 0))
    {
      setMeasuredDimension(i, j);
      return;
    }
    if (i < j * this.ratioWidth / this.ratioHeight)
    {
      setMeasuredDimension(i, i * this.ratioHeight / this.ratioWidth);
      return;
    }
    setMeasuredDimension(j * this.ratioWidth / this.ratioHeight, j);
  }
  
  public void setAspectRatio(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0)) {
      throw new IllegalArgumentException("Size cannot be negative.");
    }
    this.ratioWidth = paramInt1;
    this.ratioHeight = paramInt2;
    requestLayout();
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\AutoFitTextureView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */