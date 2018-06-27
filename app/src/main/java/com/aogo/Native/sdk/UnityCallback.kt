package com.aogo.Native

import org.json.JSONObject

import android.util.Log

import com.unity3d.player.UnityPlayer

object UnityCallback {
    private val TAG = "UnityCallback"

    /* result flag start */
    val SUCCESS = 1

    val FAIL = 2

    val CANCEL = 3
    /* result flag end */

    fun callbackUnity(cbType: String, code: Int, data: String) {
        val formatStr = String.format("%s;%d;%s", cbType, code, data)
        val a = Thread.currentThread()
        Log.d(TAG, "[ThreadId:" + a.id + "] send message to , message data=" + formatStr)
        SdkBase.xlogWrite("send message to Unity, message data=$formatStr")
        UnityPlayer.UnitySendMessage("NativeHelper", "SdkCallback", formatStr)
    }
}
