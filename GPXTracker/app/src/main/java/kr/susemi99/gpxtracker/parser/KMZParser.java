package kr.susemi99.gpxtracker.parser;

import org.jsoup.nodes.Document;

public final class KMZParser implements IParser {
  private Document document;

  public KMZParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("trk name").first().text();
  }
}
