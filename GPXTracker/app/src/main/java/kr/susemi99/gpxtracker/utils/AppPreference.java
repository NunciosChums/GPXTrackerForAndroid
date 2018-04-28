package kr.susemi99.gpxtracker.utils;

import com.google.android.gms.maps.GoogleMap;

public class AppPreference extends BaseAppPreference {
  private static AppPreference instance = new AppPreference();

  public static AppPreference instance() {
    return instance;
  }

  private AppPreference() {}

  private static final String MAP_TYPE = "map_type";

  public void setMapType(int mapType) {
    put(MAP_TYPE, mapType);
  }

  public int getMapType() {
    return get(MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
  }
}