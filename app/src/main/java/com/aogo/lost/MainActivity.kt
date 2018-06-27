package com.aogo.lost

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.unity3d.player.UnityPlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.aogo.lost.sdk.SdkBase
import android.app.Activity

class MainActivity : UnityPlayerActivity() {
    var mInstance: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        SdkBase.init()
        super.onCreate(savedInstanceState)

        mInstance = this
        val a = Thread.currentThread()
        Log.d("MainActivity", "[ThreadId:" + a.id + "] onCreate")
        SdkBase.uploadLog("testtesttest")
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
