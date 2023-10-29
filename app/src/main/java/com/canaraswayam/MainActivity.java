package com.canaraswayam;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
// import android.view.WindowManager;

import org.devio.rn.splashscreen.SplashScreen;

public class MainActivity extends ReactActivity {

  private static final int PERMISSION_REQUEST_CODE = 123; // Replace 123 with any unique request code

  /**
   * Returns the name of the main component registered from JavaScript. This is
   * used to schedule rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "canaraswayam";
  }

  /**
   * Returns the instance of the {@link ReactActivityDelegate}. Here we use a util
   * class {@link DefaultReactActivityDelegate} which allows you to easily enable
   * Fabric and Concurrent React (aka React 18) with two boolean flags.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SplashScreen.show(this); // Show the splash screen
    super.onCreate(savedInstanceState);

    // Check and request notification and location permissions
    String[] permissions = {
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.ACCESS_FINE_LOCATION
    };
    requestMultiplePermissions(permissions, PERMISSION_REQUEST_CODE);

    // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
    // WindowManager.LayoutParams.FLAG_SECURE);
  }

  private void requestMultiplePermissions(String[] permissions, int requestCode) {
    int permissionsNotGrantedCount = 0;
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        permissionsNotGrantedCount++;
      }
    }

    if (permissionsNotGrantedCount > 0) {
      ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    // Handle the result of the permission request
    if (requestCode == PERMISSION_REQUEST_CODE) {
      boolean allPermissionsGranted = true;
      for (int grantResult : grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
          allPermissionsGranted = false;
          break;
        }
      }

      if (allPermissionsGranted) {
        // All permissions granted, handle your logic here
      } else {
        // At least one permission was denied, handle the scenario when permissions are
        // not granted
      }
    }
  }

  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new DefaultReactActivityDelegate(this, getMainComponentName(),
        // If you opted-in for the New Architecture, we enable the Fabric Renderer.
        DefaultNewArchitectureEntryPoint.getFabricEnabled());
  }
}
