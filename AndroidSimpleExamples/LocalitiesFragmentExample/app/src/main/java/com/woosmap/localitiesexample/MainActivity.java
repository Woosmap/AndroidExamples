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

    private String private_key = "your_private_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize WoosmapLocalities service
        WoosmapLocalities.initialize(this, private_key);

        // Initialize the LocalitiesSupportFragment.
        LocalitiesSupportFragment localitiesFragment = (LocalitiesSupportFragment)
                getSupportFragmentManager ().findFragmentById (R.id.localities_fragment);

        // Specify the types of Locality data to return.
        JSONObject queryParams = new JSONObject();
        try {
            // Query params
            queryParams.put("components","country:fr" );
            queryParams.put("language","fr" );
            queryParams.put("data", "standard");
            queryParams.put("types", "locality");
        }
        catch(Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        // Set option parameters
        localitiesFragment.queryParams = queryParams;

        // Set the threshold of the number of character to enhance the search
        localitiesFragment.minCharRequest = 3;

        // Set up a PlaceSelectionListener to handle the response.
        localitiesFragment.setOnLocalitiesSelectedListener (new LocalitiesSupportListener () {
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
