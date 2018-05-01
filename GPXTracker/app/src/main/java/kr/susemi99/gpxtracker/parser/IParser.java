package kr.susemi99.gpxtracker.parser;

import java.util.ArrayList;

import kr.susemi99.gpxtracker.models.GTLine;
import kr.susemi99.gpxtracker.models.GTPin;

public interface IParser {
  String title();

  ArrayList<GTPin> places();

  ArrayList<GTLine> lines();
}
