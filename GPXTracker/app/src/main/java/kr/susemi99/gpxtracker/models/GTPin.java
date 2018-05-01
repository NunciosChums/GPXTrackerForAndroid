package kr.susemi99.gpxtracker.models;

import com.google.android.gms.maps.model.LatLng;

public class GTPin {
  public String title;
  public LatLng location;
  public int color;
  public String iconUrl;

  public GTPin(String title, LatLng location) {
    this.title = title;
    this.location = location;
  }
}
