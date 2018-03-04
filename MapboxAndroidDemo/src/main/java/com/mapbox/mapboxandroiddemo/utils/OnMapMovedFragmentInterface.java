package com.mapbox.mapboxandroiddemo.utils;

import com.mapbox.mapboxsdk.camera.CameraPosition;

/**
 * Interface passes map camera position
 */
public interface OnMapMovedFragmentInterface {
  void onMapMoved(CameraPosition mainMapCameraPosition);
}