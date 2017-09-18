package org.tensorflow.demo;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCaptureSession.StateCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.tensorflow.demo.env.Logger;

public class CameraConnectionFragment
  extends Fragment
{
  private static final String FRAGMENT_DIALOG = "dialog";
  private static final Logger LOGGER;
  private static final int MINIMUM_PREVIEW_SIZE = 320;
  private static final SparseIntArray ORIENTATIONS;
  private Handler backgroundHandler;
  private HandlerThread backgroundThread;
  private CameraDevice cameraDevice;
  private String cameraId;
  private final Semaphore cameraOpenCloseLock = new Semaphore(1);
  private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback()
  {
    public void onCaptureCompleted(CameraCaptureSession paramAnonymousCameraCaptureSession, CaptureRequest paramAnonymousCaptureRequest, TotalCaptureResult paramAnonymousTotalCaptureResult) {}
    
    public void onCaptureProgressed(CameraCaptureSession paramAnonymousCameraCaptureSession, CaptureRequest paramAnonymousCaptureRequest, CaptureResult paramAnonymousCaptureResult) {}
  };
  private CameraCaptureSession captureSession;
  private ImageReader previewReader;
  private CaptureRequest previewRequest;
  private CaptureRequest.Builder previewRequestBuilder;
  private Size previewSize;
  private RecognitionScoreView scoreView;
  private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback()
  {
    public void onDisconnected(CameraDevice paramAnonymousCameraDevice)
    {
      CameraConnectionFragment.this.cameraOpenCloseLock.release();
      paramAnonymousCameraDevice.close();
      CameraConnectionFragment.access$302(CameraConnectionFragment.this, null);
    }
    
    public void onError(CameraDevice paramAnonymousCameraDevice, int paramAnonymousInt)
    {
      CameraConnectionFragment.this.cameraOpenCloseLock.release();
      paramAnonymousCameraDevice.close();
      CameraConnectionFragment.access$302(CameraConnectionFragment.this, null);
      Activity localActivity = CameraConnectionFragment.this.getActivity();
      if (localActivity != null) {
        localActivity.finish();
      }
    }
    
    public void onOpened(CameraDevice paramAnonymousCameraDevice)
    {
      CameraConnectionFragment.this.cameraOpenCloseLock.release();
      CameraConnectionFragment.access$302(CameraConnectionFragment.this, paramAnonymousCameraDevice);
      CameraConnectionFragment.this.createCameraPreviewSession();
    }
  };
  private final TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener()
  {
    public void onSurfaceTextureAvailable(SurfaceTexture paramAnonymousSurfaceTexture, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      CameraConnectionFragment.this.openCamera(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public boolean onSurfaceTextureDestroyed(SurfaceTexture paramAnonymousSurfaceTexture)
    {
      return true;
    }
    
    public void onSurfaceTextureSizeChanged(SurfaceTexture paramAnonymousSurfaceTexture, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      CameraConnectionFragment.this.configureTransform(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onSurfaceTextureUpdated(SurfaceTexture paramAnonymousSurfaceTexture) {}
  };
  private AutoFitTextureView textureView;
  private final TensorflowImageListener tfPreviewListener = new TensorflowImageListener();
  
  static
  {
    if (!CameraConnectionFragment.class.desiredAssertionStatus()) {}
    for (boolean bool = true;; bool = false)
    {
      $assertionsDisabled = bool;
      LOGGER = new Logger();
      ORIENTATIONS = new SparseIntArray();
      ORIENTATIONS.append(0, 90);
      ORIENTATIONS.append(1, 0);
      ORIENTATIONS.append(2, 270);
      ORIENTATIONS.append(3, 180);
      return;
    }
  }
  
  private static Size chooseOptimalSize(Size[] paramArrayOfSize, int paramInt1, int paramInt2, Size paramSize)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayOfSize.length;
    int j = 0;
    if (j < i)
    {
      Size localSize2 = paramArrayOfSize[j];
      if ((localSize2.getHeight() >= 320) && (localSize2.getWidth() >= 320))
      {
        LOGGER.i("Adding size: " + localSize2.getWidth() + "x" + localSize2.getHeight(), new Object[0]);
        localArrayList.add(localSize2);
      }
      for (;;)
      {
        j++;
        break;
        LOGGER.i("Not adding size: " + localSize2.getWidth() + "x" + localSize2.getHeight(), new Object[0]);
      }
    }
    if (localArrayList.size() > 0)
    {
      Size localSize1 = (Size)Collections.min(localArrayList, new CompareSizesByArea());
      LOGGER.i("Chosen size: " + localSize1.getWidth() + "x" + localSize1.getHeight(), new Object[0]);
      return localSize1;
    }
    LOGGER.e("Couldn't find any suitable preview size", new Object[0]);
    return paramArrayOfSize[0];
  }
  
  private void closeCamera()
  {
    try
    {
      this.cameraOpenCloseLock.acquire();
      if (this.captureSession != null)
      {
        this.captureSession.close();
        this.captureSession = null;
      }
      if (this.cameraDevice != null)
      {
        this.cameraDevice.close();
        this.cameraDevice = null;
      }
      if (this.previewReader != null)
      {
        this.previewReader.close();
        this.previewReader = null;
      }
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new RuntimeException("Interrupted while trying to lock camera closing.", localInterruptedException);
    }
    finally
    {
      this.cameraOpenCloseLock.release();
    }
  }
  
  private void configureTransform(int paramInt1, int paramInt2)
  {
    Activity localActivity = getActivity();
    if ((this.textureView == null) || (this.previewSize == null) || (localActivity == null)) {
      return;
    }
    int i = localActivity.getWindowManager().getDefaultDisplay().getRotation();
    Matrix localMatrix = new Matrix();
    RectF localRectF1 = new RectF(0.0F, 0.0F, paramInt1, paramInt2);
    RectF localRectF2 = new RectF(0.0F, 0.0F, this.previewSize.getHeight(), this.previewSize.getWidth());
    float f1 = localRectF1.centerX();
    float f2 = localRectF1.centerY();
    if ((1 == i) || (3 == i))
    {
      localRectF2.offset(f1 - localRectF2.centerX(), f2 - localRectF2.centerY());
      localMatrix.setRectToRect(localRectF1, localRectF2, Matrix.ScaleToFit.FILL);
      float f3 = Math.max(paramInt2 / this.previewSize.getHeight(), paramInt1 / this.previewSize.getWidth());
      localMatrix.postScale(f3, f3, f1, f2);
      localMatrix.postRotate(90 * (i - 2), f1, f2);
    }
    for (;;)
    {
      this.textureView.setTransform(localMatrix);
      return;
      if (2 == i) {
        localMatrix.postRotate(180.0F, f1, f2);
      }
    }
  }
  
  private void createCameraPreviewSession()
  {
    SurfaceTexture localSurfaceTexture;
    try
    {
      localSurfaceTexture = this.textureView.getSurfaceTexture();
      if ((!$assertionsDisabled) && (localSurfaceTexture == null)) {
        throw new AssertionError();
      }
    }
    catch (CameraAccessException localCameraAccessException)
    {
      LOGGER.e(localCameraAccessException, "Exception!", new Object[0]);
    }
    for (;;)
    {
      LOGGER.i("Getting assets.", new Object[0]);
      this.tfPreviewListener.initialize(getActivity().getAssets(), this.scoreView);
      LOGGER.i("Tensorflow initialized.", new Object[0]);
      return;
      localSurfaceTexture.setDefaultBufferSize(this.previewSize.getWidth(), this.previewSize.getHeight());
      Surface localSurface = new Surface(localSurfaceTexture);
      this.previewRequestBuilder = this.cameraDevice.createCaptureRequest(1);
      this.previewRequestBuilder.addTarget(localSurface);
      LOGGER.i("Opening camera preview: " + this.previewSize.getWidth() + "x" + this.previewSize.getHeight(), new Object[0]);
      this.previewReader = ImageReader.newInstance(this.previewSize.getWidth(), this.previewSize.getHeight(), 35, 2);
      this.previewReader.setOnImageAvailableListener(this.tfPreviewListener, this.backgroundHandler);
      this.previewRequestBuilder.addTarget(this.previewReader.getSurface());
      CameraDevice localCameraDevice = this.cameraDevice;
      Surface[] arrayOfSurface = new Surface[2];
      arrayOfSurface[0] = localSurface;
      arrayOfSurface[1] = this.previewReader.getSurface();
      localCameraDevice.createCaptureSession(Arrays.asList(arrayOfSurface), new CameraCaptureSession.StateCallback()
      {
        public void onConfigureFailed(CameraCaptureSession paramAnonymousCameraCaptureSession)
        {
          CameraConnectionFragment.this.showToast("Failed");
        }
        
        public void onConfigured(CameraCaptureSession paramAnonymousCameraCaptureSession)
        {
          if (CameraConnectionFragment.this.cameraDevice == null) {
            return;
          }
          CameraConnectionFragment.access$502(CameraConnectionFragment.this, paramAnonymousCameraCaptureSession);
          try
          {
            CameraConnectionFragment.this.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(4));
            CameraConnectionFragment.this.previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(2));
            CameraConnectionFragment.access$702(CameraConnectionFragment.this, CameraConnectionFragment.this.previewRequestBuilder.build());
            CameraConnectionFragment.this.captureSession.setRepeatingRequest(CameraConnectionFragment.this.previewRequest, CameraConnectionFragment.this.captureCallback, CameraConnectionFragment.this.backgroundHandler);
            return;
          }
          catch (CameraAccessException localCameraAccessException)
          {
            CameraConnectionFragment.LOGGER.e(localCameraAccessException, "Exception!", new Object[0]);
          }
        }
      }, null);
    }
  }
  
  public static CameraConnectionFragment newInstance()
  {
    return new CameraConnectionFragment();
  }
  
  private void openCamera(int paramInt1, int paramInt2)
  {
    setUpCameraOutputs(paramInt1, paramInt2);
    configureTransform(paramInt1, paramInt2);
    CameraManager localCameraManager = (CameraManager)getActivity().getSystemService("camera");
    try
    {
      if (!this.cameraOpenCloseLock.tryAcquire(2500L, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
      }
    }
    catch (CameraAccessException localCameraAccessException)
    {
      LOGGER.e(localCameraAccessException, "Exception!", new Object[0]);
      return;
      localCameraManager.openCamera(this.cameraId, this.stateCallback, this.backgroundHandler);
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new RuntimeException("Interrupted while trying to lock camera opening.", localInterruptedException);
    }
  }
  
  private void setUpCameraOutputs(int paramInt1, int paramInt2)
  {
    CameraManager localCameraManager = (CameraManager)getActivity().getSystemService("camera");
    for (;;)
    {
      try
      {
        String[] arrayOfString = localCameraManager.getCameraIdList();
        int i = arrayOfString.length;
        int j = 0;
        if (j >= i) {
          break;
        }
        String str = arrayOfString[j];
        CameraCharacteristics localCameraCharacteristics = localCameraManager.getCameraCharacteristics(str);
        Integer localInteger = (Integer)localCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
        if ((localInteger == null) || (localInteger.intValue() != 0))
        {
          StreamConfigurationMap localStreamConfigurationMap = (StreamConfigurationMap)localCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
          if (localStreamConfigurationMap != null)
          {
            Size localSize = (Size)Collections.max(Arrays.asList(localStreamConfigurationMap.getOutputSizes(35)), new CompareSizesByArea());
            this.previewSize = chooseOptimalSize(localStreamConfigurationMap.getOutputSizes(SurfaceTexture.class), paramInt1, paramInt2, localSize);
            if (getResources().getConfiguration().orientation == 2)
            {
              this.textureView.setAspectRatio(this.previewSize.getWidth(), this.previewSize.getHeight());
              this.cameraId = str;
              return;
            }
            this.textureView.setAspectRatio(this.previewSize.getHeight(), this.previewSize.getWidth());
            continue;
          }
        }
        j++;
      }
      catch (CameraAccessException localCameraAccessException)
      {
        LOGGER.e(localCameraAccessException, "Exception!", new Object[0]);
        return;
      }
      catch (NullPointerException localNullPointerException)
      {
        ErrorDialog.newInstance(getString(2131099649)).show(getChildFragmentManager(), "dialog");
        return;
      }
    }
  }
  
  private void showToast(final String paramString)
  {
    final Activity localActivity = getActivity();
    if (localActivity != null) {
      localActivity.runOnUiThread(new Runnable()
      {
        public void run()
        {
          Toast.makeText(localActivity, paramString, 0).show();
        }
      });
    }
  }
  
  private void startBackgroundThread()
  {
    this.backgroundThread = new HandlerThread("CameraBackground");
    this.backgroundThread.start();
    this.backgroundHandler = new Handler(this.backgroundThread.getLooper());
  }
  
  private void stopBackgroundThread()
  {
    this.backgroundThread.quitSafely();
    try
    {
      this.backgroundThread.join();
      this.backgroundThread = null;
      this.backgroundHandler = null;
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      LOGGER.e(localInterruptedException, "Exception!", new Object[0]);
    }
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(2130903041, paramViewGroup, false);
  }
  
  public void onPause()
  {
    closeCamera();
    stopBackgroundThread();
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
    startBackgroundThread();
    if (this.textureView.isAvailable())
    {
      openCamera(this.textureView.getWidth(), this.textureView.getHeight());
      return;
    }
    this.textureView.setSurfaceTextureListener(this.surfaceTextureListener);
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    this.textureView = ((AutoFitTextureView)paramView.findViewById(2131230721));
    this.scoreView = ((RecognitionScoreView)paramView.findViewById(2131230722));
  }
  
  static class CompareSizesByArea
    implements Comparator<Size>
  {
    public int compare(Size paramSize1, Size paramSize2)
    {
      return Long.signum(paramSize1.getWidth() * paramSize1.getHeight() - paramSize2.getWidth() * paramSize2.getHeight());
    }
  }
  
  public static class ErrorDialog
    extends DialogFragment
  {
    private static final String ARG_MESSAGE = "message";
    
    public static ErrorDialog newInstance(String paramString)
    {
      ErrorDialog localErrorDialog = new ErrorDialog();
      Bundle localBundle = new Bundle();
      localBundle.putString("message", paramString);
      localErrorDialog.setArguments(localBundle);
      return localErrorDialog;
    }
    
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      final Activity localActivity = getActivity();
      new AlertDialog.Builder(localActivity).setMessage(getArguments().getString("message")).setPositiveButton(17039370, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          localActivity.finish();
        }
      }).create();
    }
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\CameraConnectionFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */