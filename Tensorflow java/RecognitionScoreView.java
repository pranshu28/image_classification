package org.tensorflow.demo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import java.util.Iterator;
import java.util.List;

public class RecognitionScoreView
  extends View
{
  private static final float TEXT_SIZE_DIP = 24.0F;
  private final Paint bgPaint;
  private final Paint fgPaint = new Paint();
  private List<Classifier.Recognition> results;
  private final float textSizePx = TypedValue.applyDimension(1, 24.0F, getResources().getDisplayMetrics());
  
  public RecognitionScoreView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.fgPaint.setTextSize(this.textSizePx);
    this.bgPaint = new Paint();
    this.bgPaint.setColor(-868055564);
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    int i = (int)(1.5F * this.fgPaint.getTextSize());
    paramCanvas.drawPaint(this.bgPaint);
    if (this.results != null)
    {
      Iterator localIterator = this.results.iterator();
      for (int j = i; localIterator.hasNext(); j = (int)(j + 1.5F * this.fgPaint.getTextSize()))
      {
        Classifier.Recognition localRecognition = (Classifier.Recognition)localIterator.next();
        paramCanvas.drawText(localRecognition.getTitle() + ": " + localRecognition.getConfidence(), 10.0F, j, this.fgPaint);
      }
    }
  }
  
  public void setResults(List<Classifier.Recognition> paramList)
  {
    this.results = paramList;
    postInvalidate();
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\RecognitionScoreView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */