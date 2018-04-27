package kr.susemi99.gpxtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class MapsActivity extends FragmentActivity {

  private GoogleMap map;
  private Disposable permissionDisposable;
  private Disposable myLocationDisposable;
  private RxLocation rxLocation;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.maps_activity);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    rxLocation = new RxLocation(this);
    mapFragment.getMapAsync(mapReadyCallback);
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
    LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    myLocationDisposable = rxLocation.location()
      .updates(locationRequest)
      .subscribe(location -> {
        Log.i("APP# MapsActivity | startReceiveMyLocation", "|" + location);
      });
  }

  private void stopReceiveMyLocation() {

  }


  /***********************************
   * listener
   ***********************************/
  private OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
    @Override
    public void onMapReady(GoogleMap googleMap) {
      map = googleMap;

      setupMyLocation();

      LatLng sydney = new LatLng(-34, 151);
      map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
  };
}
