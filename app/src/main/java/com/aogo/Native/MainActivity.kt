package com.aogo.Native

import android.os.Bundle
import android.util.Log
import com.unity3d.player.UnityPlayerActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity

class MainActivity : UnityPlayerActivity() {
    var mInstance: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Example of a call to a native method

        mInstance = this
        val a = Thread.currentThread()
        Log.d("MainActivity", "[ThreadId:" + a.id + "] onCreate")
        SdkBase.init(this)
        SdkBase.xlogWrite("MainActivity.onCreate")
    }

    override fun onStart() {
        super.onStart()

        SdkBase.xlogWrite("test log MainAct onStart");
    }

    override fun onDestroy() {
        super.onDestroy()

        SdkBase.uninit()
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {

        }
    }
}
