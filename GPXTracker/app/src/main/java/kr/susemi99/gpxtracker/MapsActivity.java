package kr.susemi99.gpxtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import kr.susemi99.gpxtracker.constants.AppConstant;
import kr.susemi99.gpxtracker.models.GTLine;
import kr.susemi99.gpxtracker.models.GTPin;
import kr.susemi99.gpxtracker.parser.ParseHelper;
import kr.susemi99.gpxtracker.utils.AppPreference;
import kr.susemi99.gpxtracker.utils.FileUtil;

public class MapsActivity extends AppCompatActivity {
  private static final int REQUEST_CODE_FILE_LIST = 11;

  private GoogleMap map;
  private Disposable permissionDisposable;
  private Disposable myLocationDisposable;
  private RxLocation rxLocation;
  private MenuItem shareFileItem;
  private ImageView greenPin, redPin, zoomToFitIcon, mapTypeIcon;
  private LatLngBounds zoomToFitBound;
  private KmlLayer kmlLayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.maps_activity);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    rxLocation = new RxLocation(this);
    mapFragment.getMapAsync(mapReadyCallback);

    greenPin = findViewById(R.id.greenPin);
    greenPin.setOnClickListener(__ -> goToStartLocation());
    greenPin.setEnabled(false);

    redPin = findViewById(R.id.redPin);
    redPin.setOnClickListener(__ -> goToEndLocation());
    redPin.setEnabled(false);

    zoomToFitIcon = findViewById(R.id.zoomToFitIcon);
    zoomToFitIcon.setOnClickListener(__ -> zoomToFit());
    zoomToFitIcon.setEnabled(false);

    mapTypeIcon = findViewById(R.id.mapTypeIcon);
    mapTypeIcon.setOnClickListener(__ -> toggleMapType());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_map, menu);
    shareFileItem = menu.findItem(R.id.menu_share);
    shareFileItem.setEnabled(false);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_select_file) {
      startActivityForResult(new Intent(getApplicationContext(), FileListActivity.class), REQUEST_CODE_FILE_LIST);
    }
    else if (item.getItemId() == shareFileItem.getItemId()) {
      share();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    try {
      permissionDisposable.dispose();
    } catch (Exception ignore) { }

    try {
      myLocationDisposable.dispose();
    } catch (Exception ignore) { }
  }

  @SuppressLint("MissingPermission")
  public void setupMyLocation() {
    Log.i("APP# MapsActivity | setupMyLocation", "|" + "=====================");
    permissionDisposable = new RxPermissions(this)
      .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
      .subscribe(granted -> {
        map.setMyLocationEnabled(true);

//        map.setOnMyLocationButtonClickListener(() -> {
//          startReceiveMyLocation();
//          return false;
//        });
      });
  }

  @SuppressLint("MissingPermission")
  private void startReceiveMyLocation() {
//    LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    LocationRequest locationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setInterval(TimeUnit.SECONDS.toMillis(10));

    myLocationDisposable = rxLocation.location()
      .updates(locationRequest)
      .subscribe(location -> {
        Log.i("APP# MapsActivity | startReceiveMyLocation", "|" + location);
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
      });
  }

  private void stopReceiveMyLocation() {
    try {
      myLocationDisposable.dispose();
    } catch (Exception ignore) {}
  }

  private void goToStartLocation() {

  }

  private void goToEndLocation() {

  }

  private void zoomToFit() {
    int padding = 50;
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(zoomToFitBound, padding);
    map.animateCamera(cu);
  }

  private void toggleMapType() {
    boolean isNormal = map.getMapType() == GoogleMap.MAP_TYPE_NORMAL;
    mapTypeIcon.setSelected(!isNormal);
    int mapType = isNormal ? GoogleMap.MAP_TYPE_HYBRID : GoogleMap.MAP_TYPE_NORMAL;
    map.setMapType(mapType);
    AppPreference.instance().setMapType(mapType);
  }

  private void restoreMapType() {
    int savedMapType = AppPreference.instance().getMapType();
    map.setMapType(savedMapType);

    boolean isNormal = savedMapType == GoogleMap.MAP_TYPE_NORMAL;
    mapTypeIcon.setSelected(isNormal);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (REQUEST_CODE_FILE_LIST != requestCode || resultCode != RESULT_OK) {
      return;
    }

    fileSelected(data.getStringExtra(AppConstant.SELECTED_FILE_PATH));
  }

  private void fileSelected(String filePath) {
    zoomToFitBound = null;
    LatLngBounds.Builder zoomToFitBuilder = new LatLngBounds.Builder();
    if (kmlLayer != null) { kmlLayer.removeLayerFromMap(); }
    kmlLayer = null;

    ParseHelper parseHelper = new ParseHelper(filePath);
    setTitle(parseHelper.title());

    if (".kml".equals(FileUtil.getExtension(filePath))) {
      try {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        kmlLayer = new KmlLayer(map, fileInputStream, this);
//        for (KmlPlacemark placemark : kmlLayer.li()) {
//          Log.i("APP# MapsActivity | fileSelected", "|" + placemark.toString());
//        }
        kmlLayer.addLayerToMap();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      for (GTPin place : parseHelper.places()) {
        map.addMarker(new MarkerOptions().position(place.location).title(place.title));
        zoomToFitBuilder.include(place.location);
      }

      for (GTLine line : parseHelper.lines()) {
        map.addPolyline(line.polyline);

        for (LatLng location : line.locations) {
          zoomToFitBuilder.include(location);
        }
      }

      zoomToFitBound = zoomToFitBuilder.build();
      zoomToFitIcon.setEnabled(true);

      zoomToFit();
    }
  }

  private void share() {
  }

  /***********************************
   * listener
   ***********************************/
  private OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
    @Override
    public void onMapReady(GoogleMap googleMap) {
      map = googleMap;
      map.setBuildingsEnabled(true);

      map.getUiSettings().setZoomControlsEnabled(true);
      map.getUiSettings().setCompassEnabled(true);
//      map.getUiSettings().setMyLocationButtonEnabled(true);
      map.getUiSettings().setMapToolbarEnabled(true);
      map.getUiSettings().setZoomGesturesEnabled(true);
      map.getUiSettings().setScrollGesturesEnabled(true);
      map.getUiSettings().setTiltGesturesEnabled(true);
      map.getUiSettings().setRotateGesturesEnabled(true);

      setupMyLocation();
      restoreMapType();
    }
  };

}
