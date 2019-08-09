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
    setContentView(R.layout.activity_main);
    ...
}
```
* You must initialize the WoosmapLocalities before SetContaintView.


# Embed an LocalitiesSupportFragment.
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

Add LocalitiesSupportFragment to an activity
To add LocalitiesSupportFragment to an activity, add a new fragment to an XML layout. For example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment android:id="@+id/localities_fragment"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:name="com.webgeoservices.woosmaplocalities.LocalitiesSupportFragment" />
</LinearLayout>
```

* By default, the fragment has no border or background. To provide a consistent visual appearance, nest the fragment within another layout element such as a <a href='http://developer.android.com/training/material/lists-cards.html'>CardView</a>.

Add a LocalitiesSupportListener to an activity
The LocalitiesSupportListener handles returning a place in response to the user's selection. The following code shows creating a reference to the fragment and adding a listener to your LocalitiesSupportFragment:

```java
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
```

