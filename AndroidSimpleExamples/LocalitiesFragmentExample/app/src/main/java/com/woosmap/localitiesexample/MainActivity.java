package com.woosmap.localitiesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.webgeoservices.woosmaplocalities.LocalitiesSupportFragment;
import com.webgeoservices.woosmaplocalities.LocalitiesSupportListener;
import com.webgeoservices.woosmaplocalities.Locality;
import com.webgeoservices.woosmaplocalities.WoosmapLocalities;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String private_key = "your_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize WoosmapLocalities service
        WoosmapLocalities.initialize(this, private_key);

        setContentView(R.layout.activity_main);

        // Initialize the LocalitiesSupportFragment.
        LocalitiesSupportFragment autocompleteFragment = (LocalitiesSupportFragment)
                getSupportFragmentManager ().findFragmentById (R.id.localities_fragment);


        // Set option parameters
        // Set Query
        autocompleteFragment.setInitialQuery ("Paris");

        // Set Country
        autocompleteFragment.setCountry ("country:fr");

        // Set type
        autocompleteFragment.setType ("locality");

        // Set Data
        autocompleteFragment.setData ("");

        // Set the threshold of the number of character to enhance the search
        autocompleteFragment.minCharRequest = 3;

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnLocalitiesSelectedListener (new LocalitiesSupportListener () {
            @Override
            public void onLocalitySelected(Locality locality) {
                Log.e(TAG, " **Locality = ** " + locality.getDescription ());
            }

            @Override
            public void onError(String status) {
                Log.e(TAG, " **Error = ** " + status);
            }

            @Override
            public void onUserCancel() {
                Log.e(TAG, " **User Cancel ** ");
            }
        });
    }
}
