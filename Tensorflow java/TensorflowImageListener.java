package org.tensorflow.demo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import org.tensorflow.demo.env.ImageUtils;
import org.tensorflow.demo.env.Logger;

public class TensorflowImageListener
  implements ImageReader.OnImageAvailableListener
{
  private static final int IMAGE_MEAN = 117;
  private static final int INPUT_SIZE = 224;
  private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";
  private static final Logger LOGGER = new Logger();
  private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
  private static final int NUM_CLASSES = 1001;
  private static final boolean SAVE_PREVIEW_BITMAP = true;
  private Bitmap croppedBitmap = null;
  private int previewHeight = 0;
  private int previewWidth = 0;
  private int[] rgbBytes = null;
  private Bitmap rgbFrameBitmap = null;
  private RecognitionScoreView scoreView;
  private final int screenRotation = 90;
  private final TensorflowClassifier tensorflow = new TensorflowClassifier();
  private byte[][] yuvBytes;
  
  private void drawResizedBitmap(Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    Assert.assertEquals(paramBitmap2.getWidth(), paramBitmap2.getHeight());
    float f1 = Math.min(paramBitmap1.getWidth(), paramBitmap1.getHeight());
    Matrix localMatrix = new Matrix();
    localMatrix.preTranslate(-Math.max(0.0F, (paramBitmap1.getWidth() - f1) / 2.0F), -Math.max(0.0F, (paramBitmap1.getHeight() - f1) / 2.0F));
    float f2 = paramBitmap2.getHeight() / f1;
    localMatrix.postScale(f2, f2);
    localMatrix.postTranslate(-paramBitmap2.getWidth() / 2.0F, -paramBitmap2.getHeight() / 2.0F);
    localMatrix.postRotate(90.0F);
    localMatrix.postTranslate(paramBitmap2.getWidth() / 2.0F, paramBitmap2.getHeight() / 2.0F);
    new Canvas(paramBitmap2).drawBitmap(paramBitmap1, localMatrix, null);
  }
  
  public void initialize(AssetManager paramAssetManager, RecognitionScoreView paramRecognitionScoreView)
  {
    this.tensorflow.initializeTensorflow(paramAssetManager, "file:///android_asset/tensorflow_inception_graph.pb", "file:///android_asset/imagenet_comp_graph_label_strings.txt", 1001, 224, 117);
    this.scoreView = paramRecognitionScoreView;
  }
  
  public void onImageAvailable(ImageReader paramImageReader)
  {
    localObject = null;
    for (;;)
    {
      try
      {
        localImage = paramImageReader.acquireLatestImage();
        if (localImage == null) {
          return;
        }
      }
      catch (Exception localException1)
      {
        Image.Plane[] arrayOfPlane;
        Logger localLogger1;
        Object[] arrayOfObject1;
        int i;
        int k;
        int m;
        int n;
        Logger localLogger2;
        Object[] arrayOfObject2;
        Iterator localIterator;
        if (localObject != null) {
          ((Image)localObject).close();
        }
        LOGGER.e(localException1, "Exception!", new Object[0]);
        return;
      }
      try
      {
        arrayOfPlane = localImage.getPlanes();
        if ((this.previewWidth == localImage.getWidth()) && (this.previewHeight == localImage.getHeight())) {
          break label506;
        }
        this.previewWidth = localImage.getWidth();
        this.previewHeight = localImage.getHeight();
        localLogger1 = LOGGER;
        arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Integer.valueOf(this.previewWidth);
        arrayOfObject1[1] = Integer.valueOf(this.previewHeight);
        localLogger1.i("Initializing at size %dx%d", arrayOfObject1);
        this.rgbBytes = new int[this.previewWidth * this.previewHeight];
        this.rgbFrameBitmap = Bitmap.createBitmap(this.previewWidth, this.previewHeight, Bitmap.Config.ARGB_8888);
        this.croppedBitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888);
        this.yuvBytes = new byte[arrayOfPlane.length][];
        i = 0;
        if (i >= arrayOfPlane.length) {
          break label506;
        }
        this.yuvBytes[i] = new byte[arrayOfPlane[i].getBuffer().capacity()];
        i++;
        continue;
      }
      catch (Exception localException2)
      {
        localObject = localImage;
        continue;
        j = 0;
        continue;
      }
      if (j >= arrayOfPlane.length) {
        continue;
      }
      arrayOfPlane[j].getBuffer().get(this.yuvBytes[j]);
      j++;
    }
    
    k = arrayOfPlane[0].getRowStride();
    m = arrayOfPlane[1].getRowStride();
    n = arrayOfPlane[1].getPixelStride();
    ImageUtils.convertYUV420ToARGB8888(this.yuvBytes[0], this.yuvBytes[1], this.yuvBytes[2], this.rgbBytes, this.previewWidth, this.previewHeight, k, m, n, false);
    localImage.close();
    this.rgbFrameBitmap.setPixels(this.rgbBytes, 0, this.previewWidth, 0, 0, this.previewWidth, this.previewHeight);
    drawResizedBitmap(this.rgbFrameBitmap, this.croppedBitmap);
    ImageUtils.saveBitmap(this.croppedBitmap);
    List localList = this.tensorflow.recognizeImage(this.croppedBitmap);
    localLogger2 = LOGGER;
    arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = Integer.valueOf(localList.size());
    localLogger2.v("%d results", arrayOfObject2);
    localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Classifier.Recognition localRecognition = (Classifier.Recognition)localIterator.next();
      LOGGER.v("Result: " + localRecognition.getTitle(), new Object[0]);
    }
    this.scoreView.setResults(localList);
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\TensorflowImageListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */