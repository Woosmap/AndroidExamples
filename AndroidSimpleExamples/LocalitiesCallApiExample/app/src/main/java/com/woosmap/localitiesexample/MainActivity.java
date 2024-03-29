package com.woosmap.localitiesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.webgeoservices.woosmaplocalities.FindAutocompletePredictionsRequest;
import com.webgeoservices.woosmaplocalities.GetResponseCallback;
import com.webgeoservices.woosmaplocalities.LocalitiesApiData;
import com.webgeoservices.woosmaplocalities.Locality;
import com.webgeoservices.woosmaplocalities.WoosmapLocalities;

import org.json.JSONObject;
import com.google.gson.Gson;
import com.webgeoservices.woosmaplocalities.WoosmapSettings;

import java.util.List;

import static com.woosmap.localitiesexample.R.id.Input;

public class MainActivity extends AppCompatActivity {

    private String private_key = "your_private_key";

    private Context mContext = MainActivity.this;
    private final String TAG = MainActivity.class.getSimpleName();

    private ListView listView = null;

    private ArrayAdapter arrayAdapter = null;

    private String[] description;

    public WoosmapLocalities woosmapLocalities = WoosmapLocalities.getInstance(this, private_key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Specify the types of Locality data to return.
        WoosmapLocalities.initialize(this, private_key);

        listView = findViewById(R.id.result);

        TextView input = findViewById(Input);
        input.addTextChangedListener(new TextWatcher () {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    callAnAPI();
                }
            }
        });

    }

    /*
     * Here we are using a woosmap API to see how we can integrate rest API
     * */

    // This is a GET type of rest API.
    private void callAnAPI() {

        FindAutocompletePredictionsRequest.Builder requestBuilder =
                FindAutocompletePredictionsRequest.builder ()
                        .setQuery (((TextView) findViewById (Input)).getText ().toString ())
                        .setCountry ("country:fr")
                        .setType ("locality")
                        .setData ("");


        woosmapLocalities.findAutocompletePredictions (requestBuilder.build (), new GetResponseCallback () {
            @Override
            public void onDataReceived(List<Locality> response) {
                if (response == null)
                    return;

                description = new String[response.size ()];
                for (int i = 0; i < response.size (); i++) {
                    Locality locality = response.get (i);
                    String title = locality.getDescription ().replace (" ", "");
                    Log.e (TAG, " **title** " + title);
                    description[i] = title;
                }

                arrayAdapter = new ArrayAdapter (MainActivity.this, android.R.layout.simple_list_item_1, description);
                listView.setAdapter (arrayAdapter);

            }

            @Override
            public void onFailure(String result) {
                AlertDialog.Builder builder = new AlertDialog.Builder (mContext);

                // Set up the input
                final TextView input = new TextView (mContext);
                builder.setView (input);
                input.setMovementMethod (new ScrollingMovementMethod ());
                input.setText (result);

                // Set up the buttons
                builder.setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel ();
                    }
                });

                builder.show ();
            }

        });

    }
}
