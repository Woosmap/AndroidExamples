# Introduction
Here is an example to show how to create an Autocomplete Widget which request the Woosmap <a href='https://developers.woosmap.com/products/localities/search-city-postcode/'>Localities service</a> and display the results.

This example use an Intent to launch the WoosmapAutocomplete activity

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

# Get Localities programmatically
## Passing an object with (optinal) parameters
You can create a custom search UI as an alternative to the UI provided by the Localities Search widget. 
To do this, your app must get localities predictions programmatically. Your app can get a list of predicted city or PostCode from the Localities API by calling WoosmapLocalities.findAutocompletePredictions, 
passing a FindAutocompletePredictionsRequest object with the following parameters:
* Required: A query string containing the text typed by the user.
* Optional: A grouping of places to which you would like to restrict your results. Currently, you can use components to filter over countries. Countries must be passed as a two character, ISO 3166-1 Alpha-2 compatible country code. For example: components=country:fr would restrict your results to places within France. Multiple countries must be passed as multiple country:XX filters, with the pipe character (|) as a separator. For example: components=country:gb|country:fr|country:be|country:sp|country:it would restrict your results to city name or postcodes within the United Kingdom, France, Benelux, Spain and Italy.
* Optional: A Types, The types of predictions to return. Two types are available locality or postal_code. No specifying the expected types will query all of them.
* Optional: A language, The language code, indicating in which language the results should be returned, if possible. Searches are also biased to the selected language; results in the selected language may be given a higher ranking. If language is not supplied, the Localities service will use the default language of each country. No language necessary for postal_code request.
* Optional: A data, wo values for this parameter: standard or advanced. By default, if the parameter is not defined, value is set as standard. The advanced value opens suggestions to worldwide postcodes in addition to postcodes for Western Europe. A dedicated option subject to specific billing on your license is needed to use this parameter. Please contact us if you are interested in using this parameter and you do not have subscribed the proper option yet.

These parameters are the same as the ones of the <a href='https://developers.woosmap.com/products/localities/search-city-postcode/#optional-parameters'>server's API point</a>

The example below shows a complete call to WoosmapLocalities.findAutocompletePredictions.
```java
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
```
The API returns an List<Locality> in a callback onDataReceived. The List<Locality> contains a list of Locality objects representing predicted localities. 
The list may be empty, if there is no known place corresponding to the query and the filter criteria.
