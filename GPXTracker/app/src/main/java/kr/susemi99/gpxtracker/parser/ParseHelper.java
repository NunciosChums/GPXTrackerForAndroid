package kr.susemi99.gpxtracker.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

import kr.susemi99.gpxtracker.utils.FileUtil;

public class ParseHelper implements IParser {
  private String filePath;
  private Document document;
  private IParser parser;

  public ParseHelper(String filePath) {
    this.filePath = filePath;

    try {
      document = Jsoup.parse(new File(filePath), "utf-8");
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (isGPX()) { parser = new GPXParser(document); }
    else if (isTCX()) { parser = new TCXParser(document); }
    else if (isKML()) { parser = new KMLParser(document); }
    else if (isKMZ()) { parser = new KMZParser(document); }
  }

  private boolean isGPX() {
    return ".gpx".equals(FileUtil.getExtension(filePath));
  }

  private boolean isTCX() {
    return ".tcx".equals(FileUtil.getExtension(filePath));
  }

  private boolean isKML() {
    return ".kml".equals(FileUtil.getExtension(filePath));
  }

  private boolean isKMZ() {
    return ".kmz".equals(FileUtil.getExtension(filePath));
  }

  @Override
  public String title() {
    return parser.title();
  }
}
