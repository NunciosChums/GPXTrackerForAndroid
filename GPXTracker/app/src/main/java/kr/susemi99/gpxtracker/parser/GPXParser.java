package kr.susemi99.gpxtracker.parser;

import org.jsoup.nodes.Document;

public final class GPXParser implements IParser {
  private Document document;

  public GPXParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("trk name").first().text();
  }
}
