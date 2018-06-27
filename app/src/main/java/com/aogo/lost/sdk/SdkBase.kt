package com.aogo.lost.sdk

import java.lang.reflect.InvocationTargetException
import java.math.BigDecimal
import java.util.HashMap
import java.util.UUID
import java.io.InputStream
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.lang.reflect.Method
import java.io.FileNotFoundException
import java.io.IOException
import java.io.FileOutputStream
import java.io.File

import android.content.SharedPreferences
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.ActivityManager

import android.app.ActivityManager.MemoryInfo
import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.StatFs
import android.util.Log
import android.util.Pair
import android.widget.Toast
import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.media.ExifInterface
import android.database.Cursor
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.net.Uri
import android.content.DialogInterface
import android.content.IntentFilter
import android.content.DialogInterface.OnClickListener
import android.content.Context
import android.os.Environment
import android.app.AlertDialog
import android.net.ConnectivityManager

import android.text.TextUtils
import com.unity3d.player.UnityPlayer
import android.provider.ContactsContract

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException

object SdkBase {

    val TAG = "SdkBase"
    internal var sdkInited = false
    internal var sdkLogined = false
    var sdkUserId = "1"
    var sContext: Activity? = null
    internal var sInstance: SdkBase? = null

    fun loadDefaultLibrary() {
        try {
            System.loadLibrary("stlport_shared")
            System.loadLibrary("xlog")
        } catch (e: Throwable) {
            Log.e("SdkBase", "", e)
        }

    }

    init {
        loadDefaultLibrary()
    }

    fun init() {
        System.loadLibrary("stlport_shared")
        System.loadLibrary("xlog")

        com.tencent.mars.xlog.Log.setLogImp(com.tencent.mars.xlog.Xlog())
    }

    fun uploadLog(msg: String) {
        Log.d(TAG, "SdkBase.uploadLog() msg = $msg")
        if (null != sContext) {
            sContext!!.runOnUiThread {
                try {
                    com.tencent.mars.xlog.Log.w(TAG, msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}