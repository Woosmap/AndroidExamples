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

# Create the Widget
## Add Activity
Declare the `LocalitiesActivity` in the Manifest.xml file

```xml
<application
    ...
    <activity
        android:name="com.webgeoservices.woosmaplocalities.LocalitiesActivity"
        android:label="LocalitiesActivity" />
    ...
</application>
```
## Create an Intent
Then create an Intent object. The Localities (optinal) parameters are defined throw a JSONObject. These parameters are the same as the ones of the <a href='https://developers.woosmap.com/products/localities/search-city-postcode/#optional-parameters'>server's API point</a>

```java
protected void onCreate(Bundle savedInstanceState) {
    JSONObject queryParams = new JSONObject();
    try {
        // Query params
        queryParams.put("components","country:fr" );
        queryParams.put("language","fr" );
        queryParams.put("data", "standard");
        queryParams.put("types", "locality");
    }
    catch(Exception e){
        return new String("Exception: " + e.getMessage());
    }

    Intent intent = new LocalitiesActivity.IntentBuilder()
            .withQueryParams (queryParams)
            .withMinChar (3)
            .build(this);
    startActivityForResult(intent, LocalitiesActivity.LOCALITIES_REQUEST_CODE);
}
```

Finally implement the onActivityResult method
```java
@Override
protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == LocalitiesActivity.LOCALITIES_REQUEST_CODE) {
        if (resultCode == LocalitiesActivity.RESULT_OK) {
            Log.d(TAG, " **Locality = ** " + LocalitiesActivity.getLocality ().getDescription ());
        } else if (resultCode == LocalitiesActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            String status = LocalitiesActivity.getStatus ();
            Log.d(TAG, " **Error = ** " + status);

        } else if (resultCode == LocalitiesActivity.RESULT_CANCELED) {
            // The user canceled the operation.
            Log.d(TAG, " **he user canceled the operation. ** " );
        }
    }

}
```
