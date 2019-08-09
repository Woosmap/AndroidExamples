package com.woosmap.localitiesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.webgeoservices.woosmaplocalities.LocalitiesTextField;
import com.webgeoservices.woosmaplocalities.WoosmapLocalities;

import org.json.JSONObject;

import static com.woosmap.localitiesexample.R.id.Input;

public class MainActivity extends AppCompatActivity {

    private String private_key = "de0a05ba-6018-4a5b-ad90-04ca427671d9";

    private Context mContext = MainActivity.this;
    private final String TAG = MainActivity.class.getSimpleName();

    private LocalitiesTextField localitiesTextField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize WoosmapLocalities service
        WoosmapLocalities.initialize(this, private_key);

        setContentView(R.layout.activity_main);

        JSONObject queryParams = new JSONObject();
        try {
            // Query params
            queryParams.put("components","country:fr" );
            queryParams.put("language","fr" );
            queryParams.put("data", "standard");
            queryParams.put("types", "locality");
        }
        catch(Exception e){
            Log.e(TAG,("Exception: " + e.getMessage()));
        }

        localitiesTextField = findViewById(Input);
        localitiesTextField.setContext (mContext);

        // Set option parameters
        localitiesTextField.setParams (queryParams);

        // Set the threshold of the number of character to enhance the search
        localitiesTextField.setMinCharRequest (3);

    }

}
