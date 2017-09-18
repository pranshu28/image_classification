package org.tensorflow.demo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class CameraActivity
  extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    if (paramBundle == null) {
      getFragmentManager().beginTransaction().replace(2131230720, CameraConnectionFragment.newInstance()).commit();
    }
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\CameraActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */