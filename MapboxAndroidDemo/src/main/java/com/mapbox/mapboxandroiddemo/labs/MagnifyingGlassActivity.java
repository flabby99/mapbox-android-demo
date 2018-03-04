package com.mapbox.mapboxandroiddemo.labs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxandroiddemo.utils.OnMapMovedFragmentInterface;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.utils.MapFragmentUtils;

/**
 * Explore a map's details with a fun magnifying glass effect
 */

public class MagnifyingGlassActivity extends AppCompatActivity implements
    OnMapReadyCallback, MapboxMap.OnCameraMoveListener, View.OnDragListener {

  private MapView mainMapMapView;
  private MapboxMap mainLargeMapboxMap;
  private OnMapMovedFragmentInterface onMapMovedFragmentInterfaceListener;
  private int MAGNIFIYING_GLASS_ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS = 7;
  private Bundle savedInstanceState;
  private String TAG = "MagnifyingGlassActivity2";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.savedInstanceState = savedInstanceState;

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_magnifying_glass);

    mainMapMapView = findViewById(R.id.magnifying_glass_base_mapview);
    mainMapMapView.onCreate(savedInstanceState);
    mainMapMapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    MagnifyingGlassActivity.this.mainLargeMapboxMap = mapboxMap;
    /* Custom version of the regular Mapbox SupportMapFragment class. A custom one is being built here
    so that the interface call backs can be used in the appropriate places so that the example eventually
    works*/
    CustomSupportMapFragment customSupportMapFragment;
    if (savedInstanceState == null) {

      // Create fragment
      final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

      // Build map fragment options
      MapboxMapOptions options = new MapboxMapOptions();
      options.styleUrl(Style.SATELLITE_STREETS);
      options.attributionEnabled(false);
      options.logoEnabled(false);
      options.compassEnabled(false);
      options.scrollGesturesEnabled(false);
      options.tiltGesturesEnabled(false);
      options.rotateGesturesEnabled(false);
      options.camera(new CameraPosition.Builder()
          .target(new LatLng(40.73581, -73.99155))
          .zoom(mainLargeMapboxMap.getCameraPosition().zoom +
              MAGNIFIYING_GLASS_ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS)
          .build());
      // Create map fragment and pass through map options
      customSupportMapFragment = CustomSupportMapFragment.newInstance(options);

      // Add fragmentMap fragment to parent container
      transaction.add(R.id.magnifying_glass_fragment_container, customSupportMapFragment, "com.mapbox.fragmentMap");
      transaction.commit();

      findViewById(R.id.magnifying_glass_fragment_container).setOnDragListener(this);

    } else {
      customSupportMapFragment = (CustomSupportMapFragment)
          getSupportFragmentManager().findFragmentByTag("com.mapbox.fragmentMap");
    }
    mainLargeMapboxMap.addOnCameraMoveListener(this);
  }

  @Override
  public boolean onDrag(View view, DragEvent event) {

    Log.d(TAG, "onDrag: view = " + view.getTag().toString());

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view1) {
        Log.d(TAG, "onClick: Clicked on view1. view1 = " + view1.getTag().toString());

      }
    });



    return false;
  }

  @Override
  public void onCameraMove() {
    onMapMovedFragmentInterfaceListener.onMapMoved(mainLargeMapboxMap.getCameraPosition());
  }

  private void setOnDataListener(OnMapMovedFragmentInterface onMapMovedFragmentInterface) {
    onMapMovedFragmentInterfaceListener = onMapMovedFragmentInterface;
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

  /**
   * Custom version of the regular Mapbox SupportMapFragment class. A custom one is being built here
   * so that the interface call backs can be used in the appropriate places so that the example eventually
   * works
   *
   * @see #getMapAsync(OnMapReadyCallback)
   */
  public static class CustomSupportMapFragment extends Fragment implements
      OnMapMovedFragmentInterface {

    private MapView fragmentMap;
    private OnMapReadyCallback onMapReadyCallback;
    private CameraPosition cameraPositionForFragmentMap;
    private static final int ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS = 8;

    /**
     * Creates a CustomSupportMapFragment instance
     *
     * @param mapboxMapOptions The configuration options to be used.
     * @return CustomSupportMapFragment created.
     */
    public static CustomSupportMapFragment newInstance(@Nullable MapboxMapOptions mapboxMapOptions) {
      CustomSupportMapFragment mapFragment = new CustomSupportMapFragment();
      mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(mapboxMapOptions));
      return mapFragment;
    }

    // Override method for OnMapMovedFragmentInterface
    @Override
    public void onMapMoved(final CameraPosition mainMapCameraPosition) {

      cameraPositionForFragmentMap = new CameraPosition.Builder()
          .target(mainMapCameraPosition.target)
          .zoom(mainMapCameraPosition.zoom - ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS)
          .bearing(mainMapCameraPosition.bearing)
          .tilt(mainMapCameraPosition.tilt)
          .build();

      fragmentMap.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(final MapboxMap mapInFragment) {
          mapInFragment.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionForFragmentMap));
        }
      });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ((MagnifyingGlassActivity) getActivity()).setOnDataListener(this);
    }

    /**
     * Creates the fragment view hierarchy.
     *
     * @param inflater           Inflater used to inflate content.
     * @param container          The parent layout for the fragmentMap fragment.
     * @param savedInstanceState The saved instance state for the fragmentMap fragment.
     * @return The view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      Context context = inflater.getContext();
      return fragmentMap = new MapView(context, MapFragmentUtils.resolveArgs(context, getArguments()));
    }

    /**
     * Called when the fragment view hierarchy is created.
     *
     * @param view               The content view of the fragment
     * @param savedInstanceState THe saved instance state of the framgnt
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      fragmentMap.onCreate(savedInstanceState);
    }

    /**
     * Called when the fragment is visible for the users.
     */
    @Override
    public void onStart() {
      super.onStart();
      fragmentMap.onStart();
      fragmentMap.getMapAsync(onMapReadyCallback);
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    @Override
    public void onResume() {
      super.onResume();
      fragmentMap.onResume();
    }

    /**
     * Called when the fragment is pausing.
     */
    @Override
    public void onPause() {
      super.onPause();
      fragmentMap.onPause();
    }

    /**
     * Called when the fragment state needs to be saved.
     *
     * @param outState The saved state
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
      super.onSaveInstanceState(outState);
      fragmentMap.onSaveInstanceState(outState);
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    @Override
    public void onStop() {
      super.onStop();
      fragmentMap.onStop();
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    @Override
    public void onLowMemory() {
      super.onLowMemory();
      fragmentMap.onLowMemory();
    }

    /**
     * Called when the fragment is view hierarchy is being destroyed.
     */
    @Override
    public void onDestroyView() {
      super.onDestroyView();
      fragmentMap.onDestroy();
    }

    /**
     * Sets a callback object which will be triggered when the MapboxMap instance is ready to be used.
     *
     * @param onMapReadyCallback The callback to be invoked.
     */
    public void getMapAsync(@NonNull final OnMapReadyCallback onMapReadyCallback) {
      this.onMapReadyCallback = onMapReadyCallback;
    }
  }
}