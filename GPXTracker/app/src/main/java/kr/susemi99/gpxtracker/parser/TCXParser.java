package kr.susemi99.gpxtracker.parser;

import org.jsoup.nodes.Document;

public final class TCXParser implements IParser {
  private Document document;

  public TCXParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("Course Name").first().text();
  }
}
