package kr.susemi99.gpxtracker.parser;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import kr.susemi99.gpxtracker.models.GTLine;
import kr.susemi99.gpxtracker.models.GTPin;

final class TCXParser implements IParser {
  private Document document;

  TCXParser(Document document) {
    this.document = document;
  }

  @Override
  public String title() {
    return document.select("Course Name").first().text();
  }

  @Override
  public ArrayList<GTPin> places() {
    return new ArrayList<>();
  }

  @Override
  public ArrayList<GTLine> lines() {
    return  new ArrayList<>();
  }
}
