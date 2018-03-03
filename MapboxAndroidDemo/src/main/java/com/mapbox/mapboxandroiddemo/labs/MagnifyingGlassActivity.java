package com.mapbox.mapboxandroiddemo.labs;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

/**
 * Explore a map's details with a fun magnifying glass effect
 */

public class MagnifyingGlassActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnCameraMoveListener {

  private MapView mainMapMapView;
  private MapboxMap mainLargeMapboxMap;
  private InsetMapActivity.OnMapMovedFragmentInterface onMapMovedFragmentInterfaceListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_magnifying_glass);

    mainMapMapView = findViewById(R.id.magnifying_glass_base_mapview);
    mainMapMapView.onCreate(savedInstanceState);
    mainMapMapView.getMapAsync(this);

    /* Custom version of the regular Mapbox SupportMapFragment class. A custom one is being built here
    so that the interface call backs can be used in the appropriate places so that the example eventually
    works*/
    InsetMapActivity.CustomSupportMapFragment customSupportMapFragment;
    if (savedInstanceState == null) {

      // Create fragment
      final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

      // Build map fragment options
      MapboxMapOptions options = new MapboxMapOptions();
      options.styleUrl(Style.MAPBOX_STREETS);
      options.attributionEnabled(false);
      options.logoEnabled(false);
      options.compassEnabled(false);
      options.scrollGesturesEnabled(false);
      options.tiltGesturesEnabled(false);
      options.rotateGesturesEnabled(false);
      options.camera(new CameraPosition.Builder()
        .target(new LatLng(11.302318, 106.025839))
        .zoom(2)
        .build());

      // Create map fragment and pass through map options
      customSupportMapFragment = InsetMapActivity.CustomSupportMapFragment.newInstance(options);

      // Add fragmentMap fragment to parent container
      transaction.add(R.id.magnifying_glass_fragment_container, customSupportMapFragment, "com.mapbox.fragmentMap");
      transaction.commit();

    } else {
      customSupportMapFragment = (InsetMapActivity.CustomSupportMapFragment)
        getSupportFragmentManager().findFragmentByTag("com.mapbox.fragmentMap");
    }
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    MagnifyingGlassActivity.this.mainLargeMapboxMap = mapboxMap;
    mainLargeMapboxMap.addOnCameraMoveListener(this);
  }

  @Override
  public void onCameraMove() {
    onMapMovedFragmentInterfaceListener.onMapMoved(mainLargeMapboxMap.getCameraPosition());
  }

  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mainMapMapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mainMapMapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mainMapMapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mainMapMapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mainMapMapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mainMapMapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mainMapMapView.onSaveInstanceState(outState);
  }
}