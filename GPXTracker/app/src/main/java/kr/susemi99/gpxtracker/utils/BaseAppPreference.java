package kr.susemi99.gpxtracker.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.util.Set;

import kr.susemi99.gpxtracker.application.App;

class BaseAppPreference {
  private static SharedPreferences preferences;

  protected BaseAppPreference() {
    preferences = PreferenceManager.getDefaultSharedPreferences(App.instance().context());
  }

  protected void put(String key, String value) {
    preferences.edit().putString(key, value).apply();
  }

  protected String get(String key) {
    return preferences.getString(key, "");
  }


  protected void put(String key, boolean value) {
    preferences.edit().putBoolean(key, value).apply();
  }

  protected boolean get(String key, boolean defaultValue) {
    return preferences.getBoolean(key, defaultValue);
  }


  protected void put(String key, int value) {
    preferences.edit().putInt(key, value).apply();
  }

  protected int get(String key, int defaultValue) {
    return preferences.getInt(key, defaultValue);
  }


  protected void put(String key, long value) {
    preferences.edit().putLong(key, value).apply();
  }

  protected long get(String key, long defaultValue) {
    return preferences.getLong(key, defaultValue);
  }

  protected void put(String key, Set<String> value) {
    preferences.edit().putStringSet(key, value).apply();
  }

  protected Set<String> get(String key, @Nullable Set<String> defaultValue) {
    return preferences.getStringSet(key, defaultValue);
  }

  protected void remove(String key) {
    preferences.edit().remove(key).apply();
  }
}
