package kr.susemi99.gpxtracker.application;

import android.app.Application;
import android.content.Context;

public class App extends Application {
  private static App instance;

  public static App instance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }

  public Context context() {
    return getApplicationContext();
  }
}
