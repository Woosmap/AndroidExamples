package com.woosmap.localitiesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.webgeoservices.woosmaplocalities.LocalitiesActivity;
import com.webgeoservices.woosmaplocalities.WoosmapLocalities;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String private_key = "your_private_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Specify the types of Locality data to return.
        WoosmapLocalities.initialize(this, private_key);

        Intent intent = new LocalitiesActivity.IntentBuilder()
                .setCountry ("country:fr")
                .setType ("locality")
                .setData ("")
                .setInitialQuery ("Paris")
                .withMinChar (3)
                .build(this);
        startActivityForResult(intent, LocalitiesActivity.LOCALITIES_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LocalitiesActivity.LOCALITIES_REQUEST_CODE) {
            if (resultCode == LocalitiesActivity.RESULT_OK) {
                Log.e(TAG, " **Locality = ** " + LocalitiesActivity.getLocality ().getDescription ());
            } else if (resultCode == LocalitiesActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                String status = LocalitiesActivity.getStatus ();
                Log.e(TAG, " **Error = ** " + status);

            } else if (resultCode == LocalitiesActivity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.e(TAG, " **he user canceled the operation. ** " );
            }
        }

    }
}
