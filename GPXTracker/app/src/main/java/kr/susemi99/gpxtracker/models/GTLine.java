package kr.susemi99.gpxtracker.models;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class GTLine {
  public ArrayList<LatLng> locations = new ArrayList<>();
  public PolylineOptions polyline;
  public GTPin startPin;
  public GTPin endPin;

  public GTLine(ArrayList<LatLng> locations) {
    this.locations.addAll(locations);
    polyline = new PolylineOptions().addAll(locations).width(10).color(Color.BLUE);
    startPin = new GTPin("start", locations.get(0));
    endPin = new GTPin("end", locations.get(locations.size() - 1));
  }
}
