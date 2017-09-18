package org.tensorflow.demo.env;

import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public final class Logger
{
  private static final int DEFAULT_MIN_LOG_LEVEL = 3;
  private static final String DEFAULT_TAG = "tensorflow";
  private static final Set<String> IGNORED_CLASS_NAMES = new HashSet(3);
  private final String messagePrefix;
  private int minLogLevel = 3;
  private final String tag;
  
  static
  {
    IGNORED_CLASS_NAMES.add("dalvik.system.VMStack");
    IGNORED_CLASS_NAMES.add("java.lang.Thread");
    IGNORED_CLASS_NAMES.add(Logger.class.getCanonicalName());
  }
  
  public Logger()
  {
    this("tensorflow", null);
  }
  
  public Logger(int paramInt)
  {
    this("tensorflow", null);
  }
  
  public Logger(Class<?> paramClass)
  {
    this(paramClass.getSimpleName());
  }
  
  public Logger(String paramString)
  {
    this("tensorflow", paramString);
  }
  
  public Logger(String paramString1, String paramString2)
  {
    this.tag = paramString1;
    if (paramString2 == null) {
      paramString2 = getCallerSimpleName();
    }
    if (paramString2.length() > 0) {
      paramString2 = paramString2 + ": ";
    }
    this.messagePrefix = paramString2;
  }
  
  private static String getCallerSimpleName()
  {
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    int i = arrayOfStackTraceElement.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfStackTraceElement[j].getClassName();
      if (!IGNORED_CLASS_NAMES.contains(str))
      {
        String[] arrayOfString = str.split("\\.");
        return arrayOfString[(-1 + arrayOfString.length)];
      }
    }
    return Logger.class.getSimpleName();
  }
  
  private String toMessage(String paramString, Object... paramVarArgs)
  {
    StringBuilder localStringBuilder = new StringBuilder().append(this.messagePrefix);
    if (paramVarArgs.length > 0) {
      paramString = String.format(paramString, paramVarArgs);
    }
    return paramString;
  }
  
  public void d(String paramString, Object... paramVarArgs)
  {
    if (isLoggable(3)) {
      Log.d(this.tag, toMessage(paramString, paramVarArgs));
    }
  }
  
  public void d(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (isLoggable(3)) {
      Log.d(this.tag, toMessage(paramString, paramVarArgs), paramThrowable);
    }
  }
  
  public void e(String paramString, Object... paramVarArgs)
  {
    if (isLoggable(6)) {
      Log.e(this.tag, toMessage(paramString, paramVarArgs));
    }
  }
  
  public void e(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (isLoggable(6)) {
      Log.e(this.tag, toMessage(paramString, paramVarArgs), paramThrowable);
    }
  }
  
  public void i(String paramString, Object... paramVarArgs)
  {
    if (isLoggable(4)) {
      Log.i(this.tag, toMessage(paramString, paramVarArgs));
    }
  }
  
  public void i(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (isLoggable(4)) {
      Log.i(this.tag, toMessage(paramString, paramVarArgs), paramThrowable);
    }
  }
  
  public boolean isLoggable(int paramInt)
  {
    return (paramInt >= this.minLogLevel) || (Log.isLoggable(this.tag, paramInt));
  }
  
  public void setMinLogLevel(int paramInt)
  {
    this.minLogLevel = paramInt;
  }
  
  public void v(String paramString, Object... paramVarArgs)
  {
    if (isLoggable(2)) {
      Log.v(this.tag, toMessage(paramString, paramVarArgs));
    }
  }
  
  public void v(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (isLoggable(2)) {
      Log.v(this.tag, toMessage(paramString, paramVarArgs), paramThrowable);
    }
  }
  
  public void w(String paramString, Object... paramVarArgs)
  {
    if (isLoggable(5)) {
      Log.w(this.tag, toMessage(paramString, paramVarArgs));
    }
  }
  
  public void w(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (isLoggable(5)) {
      Log.w(this.tag, toMessage(paramString, paramVarArgs), paramThrowable);
    }
  }
}


/* Location:              C:\Users\pcsahu.2011\Desktop\classes-dex2jar.jar!\org\tensorflow\demo\env\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */