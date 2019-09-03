# Introduction
Woosmap Geofencing SDK collects the geolocation of users (with their consent) to receive and display location based notifications sent by our servers.

To use this SDK you need an account. If you haven't got one, please contact us.
# Android Woosmap Geofencing Basic integration
## Use our Maven repository
* Add our Maven repository in the build.gradle of the project
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
* Then import our package in the build.gralde of the application
```gradle
dependencies {
    implementation 'com.webgeoservices:woosmap-mobile-sdk:0.3.1'
}
``` 

## AndroidManifest.xml Configuration
First you have to add permissions to use the device geolocation and network
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.location.GPS_ENABLED_CHANGE" />
```

Then you have to set the Woosmap Private Key in the xml tag `application`
```xml
<application ... >
    ...
    <meta-data android:name="woosmap_private_key" android:value="Your_UUID_Private_key" />
    ...
</application>
```
## Connect your application to the Firebase service
A Firebase connexion is required by Woosmap's library and is used to receive and display notifications.

To connect your application to Firebase, follow this tutorial https://firebase.google.com/docs/android/setup

If you see the following error: 
```
Make sure to call FirebaseApp.initializeApp(Context) first 
``` 
It means that your application is not correctly connected to your Firebase project.


## Import and instanciate Woosmap library
```java
import com.webgeoservices.woosmap_mobile_sdk.Woosmap;

public class MainActivity extends AppCompatActivity {
    public Woosmap woosmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The woosmap instance is a singleton that should be initialize once using initializeWoosmap
        this.woosmap = Woosmap.getInstance().initializeWoosmap(this);
    }
        
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            this.woosmap.onResume();
        } else {
            Log.d("Woozies", "Permission NOK");
            requestPermissions();
        }
    }
    
    @Override
    public void onPause(){
        super.onPause();
        if (checkPermissions()) {
            this.woosmap.onPause();
        }
    }
}
```
To work properly, you have to instanciate the Woosmap object in the onCreate function and call Woosmap's onResume and onPause functions.
  
## Geolocation permissions
You have to grant geolocation permissions on the MainActivity. To so you must to implement the functions `checkPermissions()` and `requestPermissions()`

You can find a full example here: https://github.com/WebGeoServices/AndroidExemples/blob/master/AndroidExemples/WoosmapGeofencing/app/src/main/java/com/woosmap/woosmapgeofencing/MainActivity.java

and the official documentation here: https://developers.google.com/maps/documentation/android-sdk/location

To authorize Woosmap to use geolocation, you have to call the function `updateUserTracking()` with parameter **true**
```java
this.woosmap.updateUserTracking(true);
```

## Setup notifications (Example with Firebase)
### AndroidManifest
You have to connect Woosmap services to the Firebase service
```xml
<service android:name="com.webgeoservices.woosmap_mobile_sdk.WoosmapInstanceIDService">
    <intent-filter>
        <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
    </intent-filter>
</service>
<service android:name="com.webgeoservices.woosmap_mobile_sdk.WoosmapMessagingService">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```
# Advanced integration

## For Android version >= 8
You have to declare a channel in the MainActivity https://developer.android.com/training/notify-user/channels.html

Or create it by calling `createWoosmapNotifChannel()` from the Woosmap object in the onCreate function
```java
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.woosmap = Woosmap.getInstance().initializeWoosmap(
                this,
                FirebaseInstanceId.getInstance().getToken(),
        );
        this.woosmap.createWoosmapNotifChannel();
    }
```

## Customize notifications

If you want to define the Activity which will be opened we a user clicks on the notification, you first have to set an Uri to this Activity in the Manifest.xml (example for the uri: `wooziesexemple://notif` on the MainActivity)

```xml
<activity android:name=".MainActivity">
    ...
    <intent-filter>
        ...
        <data android:scheme="wooziesexemple"
            android:host="notif" />
    </intent-filter>
</activity>
```

Finally you have to define the default Uri which will be opened when the user clicks on the notification in the tag `application` 

```xml
<application ... >
...
    <meta-data android:name="woosmap_notification_defautl_uri" android:value="wooziesexemple://notif" />
...
</application>
```

### Define the notification's icon
If you want to customize the small icon displayed in the notification, you must add the icon file in the directory `res/drawable` and add the following meta-data in the `AndroidManifest.xml``

```xml
<application ...>
...
    <meta-data
android:name="woosmap.messaging.default_notification_icon"
android:resource="@drawable/your_custom_icon_24dp" />
...
</application>
```

## Track if notifications are opened
If you want to track if notifications are opened, you have to call `trackNotificationOpen()` in the Activity opened by the notification

```java
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  this.woosmap = Woosmap.getInstance().initializeWoosmap(this);
  this.woosmap.trackNotificationOpen(this);
```

## Implement your own notifications service
### AndroidManifest
If you don't want to use the Woosmap notification service, you can implement your own.

You have to declare the Firebase service in the Manifest
```xml
<service android:name=".ExampleInstanceIdService">
    <intent-filter>
        <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
    </intent-filter>
</service>
<service android:name=".ExampleMessagingService">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```
`ExampleInstanceIdService` and `ExampleMessagingService` are your own services which have to inherit from `FirebaseInstanceIdService` and `FirebaseMessagingService`
 
### Notification implementation
If you ever use the Firebase messaging service in your application, you can add the fcm token in the Woosmap initialization. Even if you give the fcmToken in the Woosmap initialization, you have to set it in the FirebaseInstanceIdService as explained in the next section.
```java
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  this.woosmap = Woosmap.getInstance().initializeWoosmap(
          this,
          <The_fcm_Token>
  );
```

#### ExampleInstanceIdService
```java
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.webgeoservices.woosmap_mobile_sdk.Woosmap;

public class ExampleInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Woosmap.setMessageToken(this, FirebaseInstanceId.getInstance().getToken());
    }
}

```
You have to set the token in the Woosmap library when FCM refreshes it

#### ExampleMessagingService
```java
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webgeoservices.woosmap_mobile_sdk.MessageDatas;
import com.webgeoservices.woosmap_mobile_sdk.WoosmapMessageBuilder;

public class ExampleMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        WoosmapMessageBuilder messageBuilder = new WoosmapMessageBuilder(this, MainActivity.class);
        MessageDatas messageDatas = new MessageDatas(remoteMessage.getData());
        if (messageDatas.isFromWoosmap()) {
            messageBuilder.sendWoosmapNotification(messageDatas);
        }

    }
}
```

### Enable location after a device reboot
#### Create the BroadcasReceiver
To collect location after a device reboot without having to relaunch the application, you have to create a Broadcast which launches the jobInstantService `WoosmapRebootJobService` when it receives the BOOT_COMPLETED event.

```java
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.webgeoservices.woosmap_mobile_sdk.WoosmapRebootJobService;

public class woosmapRunOnStartup extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.d("woozExemple", intent.toString());
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            WoosmapRebootJobService.enqueueWork(context, new Intent());
        }
    }

}
```

#### Add the BroadcastReceiver to the Manifest
Add the permission `android.permission.RECEIVE_BOOT_COMPLETED`
```
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```
Then, just declare your receiver in the Manifest.xml in the application bloc

```
<receiver android:name=".woosmapRunOnStartup">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</receiver>
```
