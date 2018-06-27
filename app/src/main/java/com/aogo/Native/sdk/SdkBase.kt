package com.aogo.Native

import android.app.Activity

import android.util.Log
import android.os.Environment

import com.tencent.mars.xlog.Xlog

class SdkBase {
    internal var sdkInited = false
    internal var sdkLogined = false
    var sdkUserId = "1"


    fun loadDefaultLibrary() {
        try {
            init(sContext)
        } catch (e: Throwable) {
            Log.e("SdkBase", "", e)
        }
    }

    init {
        loadDefaultLibrary()
    }

    companion object
    {
        val TAG = "sdk"
        val XLOG_PUBKEY = "fd8ed32b0e26df3037ef22d482c6f242f2a09d81080c2e4fd08c8ee0ab8587d4202618380e1872d6d1f8e098e35b2e68cada09532dea33da7806dff04cd46fed"
        val logName = "cp";

        var sContext: Activity? = null
        internal var sInstance: SdkBase? = null

        fun init(act: Activity?) {
            sContext = act;
            _init(act)
        }

        fun uninit()
        {
            com.tencent.mars.xlog.Log.appenderFlush(false);
            com.tencent.mars.xlog.Log.appenderClose();
        }

        fun _init(act: Activity?)
        {
            System.loadLibrary("stlport_shared")
            System.loadLibrary("marsxlog")

            com.tencent.mars.xlog.Log.setLogImp(com.tencent.mars.xlog.Xlog())
        }

        @JvmStatic
        fun xlogOpen(fileName: String)
        {
            if (null != sContext) {
                sContext?.runOnUiThread {
                    try {
                        var SDCARD: String  = Environment.getExternalStorageDirectory().absolutePath;
                        var logPath: String  = SDCARD + "/cp/log";

                        // this is necessary, or may cash for SIGBUS
                        var cachePath: String  = sContext?.filesDir?.absolutePath + "/xlog"

                        //init xlog
                        if (BuildConfig.DEBUG) {
                            Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, cachePath, logPath, fileName, XLOG_PUBKEY);
                            Xlog.setConsoleLogOpen(false);
                        } else {
                            Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, cachePath, logPath, fileName, XLOG_PUBKEY);
                            Xlog.setConsoleLogOpen(false);
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @JvmStatic
        fun xlogClose()
        {
            if (null != sContext) {
                sContext?.runOnUiThread {
                    try {
                        com.tencent.mars.xlog.Log.appenderFlush(false);
                        com.tencent.mars.xlog.Log.appenderClose();
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @JvmStatic
        fun xlogWrite(msg: String) {
            //android.util.Log.d(SdkBase.TAG, "==xlogWrite== " + msg);
            if (null != sContext) {
                sContext?.runOnUiThread {
                    try {
                        com.tencent.mars.xlog.Log.w(SdkBase.TAG, msg)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @JvmStatic
        fun xlogFrush()
        {
            //android.util.Log.d(SdkBase.TAG, "==xlogFrush==");
            if (null != sContext) {
                sContext?.runOnUiThread {
                    try {
                        com.tencent.mars.xlog.Log.appenderFlush(false);
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @JvmStatic
        fun xlogGetSavePath() : String?
        {
            //android.util.Log.d(SdkBase.TAG, "==xlogGetSavePath==");
            if (null != sContext) {
                var SDCARD: String  = Environment.getExternalStorageDirectory().absolutePath;
                var logPath: String  = SDCARD + "/cp/log";
                return logPath
            }

            return null;
        }
    }
}