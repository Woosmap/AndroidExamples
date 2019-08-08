# Introduction
Here is an example to show how to create an Autocomplete Widget which request the Woosmap <a href='https://developers.woosmap.com/products/localities/search-city-postcode/'>Localities service</a> and display the results.

This example use an Intent to launch the WoosmapAutocomplete activity

# Get Started
## Import the WoosmapLocalities library:
### Add our maven's repo:
In the project's gradle file add our maven's reporsitory `https://wgs-android-maven.s3-eu-west-1.amazonaws.com`
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://wgs-android-maven.s3-eu-west-1.amazonaws.com"
        }
    }
}
```

### Import our library:
In the application's gradle configuration add the dependecie `com.webgeoservices:woosmaplocalities:0.0.2`

```gradle
dependencies {
    ...
    implementation 'com.webgeoservices:woosmaplocalities:0.0.2'
}
```
## Initialize the WoosmapLocalities Service
To initialize the WoosmapLocalities object you must have an apiKey. 

Then in the onCreate call the static method `WoosmapLocalities.initialize(Context, apiKey)`

```java
protected void onCreate(Bundle savedInstanceState) {
    ...
    WoosmapLocalities.initialize(this, private_key);
    ...
}
```

# Get Localities programmatically
## Passing an object with (optinal) parameters
You can create a custom search UI as an alternative to the UI provided by the Localities Search widget. To do this, your app must get localities predictions programmatically. Your app can get a list of predicted city or PostCode from the Localities API by calling WoosmapLocalities.getInstanceIfExists ().getPredictions. The Localities (optinal) parameters are defined throw a JSONObject. These parameters are the same as the ones of the <a href='https://developers.woosmap.com/products/localities/search-city-postcode/#optional-parameters'>server's API point</a>

```java
// This is a GET type of rest API.
    private void callAnAPI() {
        JSONObject queryParams = new JSONObject();
        try {
            // Query params
            queryParams.put("input", ((TextView) findViewById(Input)).getText().toString());
            queryParams.put("components","country:fr" );
            queryParams.put("language","fr" );
            queryParams.put("data", "standard");
            queryParams.put("types", "locality");
        }
        catch(Exception e){
            Log.e(TAG,("Exception: " + e.getMessage()));
        }

```

The example below shows a complete call to WoosmapLocalities.getInstanceIfExists().getPredictions().
```java
private void callAnAPI() {
        JSONObject queryParams = new JSONObject();
        try {
            // Query params
            queryParams.put("input", ((TextView) findViewById(Input)).getText().toString());
            queryParams.put("components","country:fr" );
            queryParams.put("language","fr" );
            queryParams.put("data", "standard");
            queryParams.put("types", "locality");
        }
        catch(Exception e){
            Log.e(TAG,("Exception: " + e.getMessage()));
        }

        WoosmapLocalities.getInstanceIfExists ().getPredictions(queryParams, new GetResponseCallback () {
            @Override
            public void onDataReceived(String result) {
                if (result == null)
                    return;
                LocalitiesApiData data;
                Gson gson = new Gson();
                data = gson.fromJson(WoosmapLocalities.getInstanceIfExists ().result, LocalitiesApiData.class);
                description = new String[data.getLocalities().length];

                for(int i = 0; i < data.getLocalities().length; i++ ){
                    Locality locality = data.getLocalities()[i];
                    String title = locality.getDescription().replace(" ", "");
                    Log.e(TAG, " **title** " + title);
                    description[i] = title;
                }

                arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, description);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onFailure(String result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                // Set up the input
                final TextView input = new TextView(mContext);
                builder.setView(input);
                input.setMovementMethod(new ScrollingMovementMethod());
                input.setText(result);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });

    }
```
