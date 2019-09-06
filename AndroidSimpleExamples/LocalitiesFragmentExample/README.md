# Introduction
Here is an example to show how to create an Autocomplete Widget which request the Woosmap <a href='https://developers.woosmap.com/products/localities/search-city-postcode/'>Localities service</a> and display the results.

This example use an embedded an LocalitiesSupportFragment.

# Get Started
## Import the WoosmapLocalities library:
### Add our maven's repo:
In the project's gradle file add our maven's reporsitory `https://android-maven.woosmap.com`
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://android-maven.woosmap.com"
        }
    }
}
```

### Import our library:
In the application's gradle configuration add the dependecie `com.webgeoservices:woosmaplocalities:0.0.3`

```gradle
dependencies {
    ...
    implementation 'com.webgeoservices:woosmaplocalities:0.0.3'
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


# Embed an AutocompleteSupportFragment.

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
```

