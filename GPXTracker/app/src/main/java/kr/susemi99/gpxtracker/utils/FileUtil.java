package kr.susemi99.gpxtracker.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtil {
  public static ArrayList<File> getFileList(String... extension) {
    return extensionFiltering(Environment.getExternalStorageDirectory(), extension);
  }

  private static ArrayList<File> extensionFiltering(File folder, String... extension) {
    ArrayList<String> extensions = new ArrayList<>(Arrays.asList(extension));
    ArrayList<File> result = new ArrayList<>();

    File[] files = folder.listFiles();

    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          result.addAll(extensionFiltering(file, extension));
        }
        else {
          if (extensions.contains(file.getName().substring(file.getName().lastIndexOf(".")))) {
            result.add(file);
          }
        }
      }
    }

    return result;
  }
}
