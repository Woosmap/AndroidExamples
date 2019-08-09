# Introduction
Here is an example to show how to create an Autocomplete Widget which request the Woosmap <a href='https://developers.woosmap.com/products/localities/search-city-postcode/'>Localities service</a> and display the results.

This example use an LocalitiesTextField.

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


# Embed an LocalitiesTextField.
## Passing an object with (optinal) parameters
The Localities (optinal) parameters are defined throw a JSONObject. These parameters are the same as the ones of the <a href='https://developers.woosmap.com/products/localities/search-city-postcode/#optional-parameters'>server's API point</a>

```java
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
```

Add LocalitiesTextField to an activity
To add LocalitiesTextField to an activity, add a new TextField to an XML layout. For example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context=".MainActivity">

    <com.webgeoservices.woosmaplocalities.LocalitiesTextField
        android:id="@+id/Input"
        android:layout_width="268dp"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_marginStart="20dp"
        android:layout_marginTop="0dp"
        android:layout_marginEnd="20dp"
        android:ems="10"
        android:hint="Input"
        android:inputType="textPersonName"
        android:selectAllOnFocus="true" />
</RelativeLayout>

```

Add a LocalitiesTextField to an activity
See the code below:

```java
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
```

