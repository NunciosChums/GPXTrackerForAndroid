package kr.susemi99.gpxtracker.parser;

import org.jsoup.nodes.Document;

public final class KMLParser implements IParser {
  private Document document;

  public KMLParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("Document name").first().text();
  }
}
