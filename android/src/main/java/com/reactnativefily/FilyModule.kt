package com.reactnativefily

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray;
import android.content.Intent
import android.app.Activity;

class FilyModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    val IMAGE_PICKER_REQUEST = 1;
    val ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    val PICKER_CANCELLED = "E_PICKER_CANCELLED";
    val FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    val NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";

    var mPickerPromise: Promise? = null;
  
    override fun getName(): String {
        return "Fily"
    }

    override fun getConstants(): Map<String, Any> {
    val constants= HashMap<String, String>();
    constants.put(ACTIVITY_DOES_NOT_EXIST, "E_ACTIVITY_DOES_NOT_EXIST");
    constants.put(PICKER_CANCELLED, "E_PICKER_CANCELLED");
    constants.put(FAILED_TO_SHOW_PICKER, "E_FAILED_TO_SHOW_PICKER");
    constants.put(NO_IMAGE_DATA_FOUND, "E_NO_IMAGE_DATA_FOUND");

    return constants;
    }


    val mActivityEventListener: ActivityEventListener = object: BaseActivityEventListener() {
      override fun onActivityResult(activity: Activity,requestCode: Int,  resultCode: Int, intent: Intent) {
        if (requestCode == IMAGE_PICKER_REQUEST) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mPickerPromise?.reject(PICKER_CANCELLED, "Picker was cancelled by user")
          } else if (resultCode == Activity.RESULT_OK) {
            val uri = intent.getData();

            if (uri == null) {
              mPickerPromise?.reject(NO_IMAGE_DATA_FOUND, "No image data found")
            } else {
              mPickerPromise?.resolve(uri.toString())
            }
          }

        }

        mPickerPromise = null;
      }
    }

    init {
      reactContext.addActivityEventListener(mActivityEventListener);
    }
    
    @ReactMethod
    fun open(promise: Promise) {
      val currentActivity = getCurrentActivity();

      if (currentActivity == null) {
        promise.reject(ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
        return;
      }

      mPickerPromise = promise;

      try {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
  
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        currentActivity.startActivityForResult(intent, IMAGE_PICKER_REQUEST)
  
      } catch (e: Exception) {
        promise.reject(FAILED_TO_SHOW_PICKER, e);
        mPickerPromise = null;
      }



    
    }
}
