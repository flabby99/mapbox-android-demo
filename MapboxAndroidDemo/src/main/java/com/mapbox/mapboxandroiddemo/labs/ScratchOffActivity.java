package com.mapbox.mapboxandroiddemo.labs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Reveal a second map by "scratching" off the first one
 */

public class ScratchOffActivity extends AppCompatActivity {

  private MapView mapViewOne;
  private MapView mapViewTwo;
  private String TAG = "ScratchOffActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_scratch_off);

    mapViewOne = findViewById(R.id.scratch_off_mapview_one);
    mapViewTwo = findViewById(R.id.scratch_off_mapview_two);
    mapViewOne.onCreate(savedInstanceState);
    mapViewTwo.onCreate(savedInstanceState);
    mapViewOne.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        Log.d(TAG, "onMapReady: mapview one ready");
      }
    });
    mapViewTwo.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        Log.d(TAG, "onMapReady: mapview two ready");
      }
    });
  }


  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapViewOne.onResume();
    mapViewTwo.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapViewOne.onStart();
    mapViewTwo.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapViewOne.onStop();
    mapViewTwo.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapViewOne.onPause();
    mapViewTwo.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapViewOne.onLowMemory();
    mapViewTwo.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapViewOne.onDestroy();
    mapViewTwo.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapViewOne.onSaveInstanceState(outState);
    mapViewTwo.onSaveInstanceState(outState);
  }
}