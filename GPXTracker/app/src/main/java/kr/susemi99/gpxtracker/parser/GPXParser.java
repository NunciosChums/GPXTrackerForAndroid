package kr.susemi99.gpxtracker.parser;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import kr.susemi99.gpxtracker.models.GTLine;
import kr.susemi99.gpxtracker.models.GTPin;

final class GPXParser implements IParser {
  private Document document;

  GPXParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("trk name").first().text();
  }

  @Override
  public ArrayList<GTPin> places() {
    ArrayList<GTPin> result = new ArrayList<>();

    for (Element wpt : document.select("wpt")) {
      double lon = Double.parseDouble(wpt.attr("lon"));
      double lat = Double.parseDouble(wpt.attr("lat"));
      String name = wpt.select("name").first().text();

      result.add(new GTPin(name, new LatLng(lat, lon)));
    }

    return result;
  }

  @Override
  public ArrayList<GTLine> lines() {
    ArrayList<LatLng> locations = new ArrayList<>();
    for (Element trkpt : document.select("trkpt")) {
      double lon = Double.parseDouble(trkpt.attr("lon"));
      double lat = Double.parseDouble(trkpt.attr("lat"));
      locations.add(new LatLng(lat, lon));
    }

    ArrayList<GTLine> result = new ArrayList<>();
    result.add(new GTLine(locations));
    return result;
  }
}
