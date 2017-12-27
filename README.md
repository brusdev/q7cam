Q7Cam is a sample project to control the Q7 camera P2P WIFI. To make a new project you can use the following tutorial.
 
### Start a new project
I create a new project with application name "Q7Camera". During the wizard i add a basic activity.


### Import native libraries and sources
To control the camera you need the libraries at this link: https://github.com/brusdev/q7cam/tree/master/app/src/main/jniLibs/armeabi. Copy the previous library in the path "app/src/main/jniLibs/armeabi" of your project. The packages to import are x1.Studio.Core at https://github.com/brusdev/q7cam/tree/master/app/src/main/java/x1/Studio/Core and com.microembed.sccodec at https://github.com/brusdev/q7cam/tree/master/app/src/main/java/com/microembed/sccodec.

 
### Edit the manifest
To use the native libraries you need to add the following permissions to the manifest:
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

 

### Edit the activity layout
I add the following sv to the activity layout to show the camera:
<SurfaceView android:id="@+id/surfaceView_video" android:layout_width="match_parent" android:layout_height="match_parent" />


### Edit the activity source
To show the camera in an activity, it needs to implements the interfaces SurfaceHolder.Callback and IVideoDataCallBack, you can find an example here: https://github.com/brusdev/q7cam/blob/master/app/src/main/java/com/brusdev/q7cam/MainActivity.java.
