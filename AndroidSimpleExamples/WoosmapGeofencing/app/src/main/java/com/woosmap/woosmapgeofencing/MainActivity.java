package com.woosmap.woosmapgeofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.webgeoservices.woosmap_mobile_sdk.Woosmap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    public Woosmap woosmap;

    public class WoosLocationReadyListener implements Woosmap.LocationReadyListener
    {
        public void LocationReadyCallback(Location location)
        {
            onLocationCallback(location);
        }
    }

    protected void onLocationCallback(Location currentLocation) {
        Log.d("Exemple", currentLocation.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView mainText = (TextView)findViewById(R.id.main_text);
        if (checkPermissions()) {
            Log.d("Woozies", "Permission OK");
            this.woosmap.onResume();
            mainText.setText(this.woosmap.AdId);
        } else {
            Log.d("Woozies", "Permission NOK");
            requestPermissions();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Woozies", "BackGround");
        if (checkPermissions()) {
            this.woosmap.onPause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciate woosmap object
        this.woosmap = Woosmap.getInstance().initializeWoosmap(this);
        this.woosmap.setLocationReadyListener(new WoosLocationReadyListener());

        // Call trackNotificationOpen if you want to get some information about wich notifications are opened by your customers
        this.woosmap.trackNotificationOpen(this);

        // For android version >= 8 you have to create a channel or use the woosmap's channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.woosmap.createWoosmapNotifChannel();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("woozies", "Displaying permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i("Woozies", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("Woozies", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("Woozies", "User interaction was cancelled.");
                this.woosmap.updateUserTracking(false);
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Woozies", "Permission granted, updates requested, starting location updates");
                this.woosmap.updateUserTracking(true);
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
                this.woosmap.updateUserTracking(false);
            }
        }
    }
}
