package com.tencent.mars.xlog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.widget.Toast

/**
 * @author zhaoyuan zhangweizang
 */
object Log {
    private val TAG = "mars.xlog.log"

    val LEVEL_VERBOSE = 0
    val LEVEL_DEBUG = 1
    val LEVEL_INFO = 2
    val LEVEL_WARNING = 3
    val LEVEL_ERROR = 4
    val LEVEL_FATAL = 5
    val LEVEL_NONE = 6

    // defaults to LEVEL_NONE
    private var level = LEVEL_NONE
    var toastSupportContext: Context? = null

    private val debugLog: LogImp = object : LogImp {
        private val handler = Handler(Looper.getMainLooper())

        override val logLevel: Int
            get() = level

        override fun logV(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level <= LEVEL_VERBOSE) {
                android.util.Log.v(tag, log)
            }
        }

        override fun logI(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level <= LEVEL_INFO) {
                android.util.Log.i(tag, log)
            }
        }

        override fun logD(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level <= LEVEL_DEBUG) {
                android.util.Log.d(tag, log)
            }

        }

        override fun logW(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level <= LEVEL_WARNING) {
                android.util.Log.w(tag, log)
            }

        }

        override fun logE(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level <= LEVEL_ERROR) {
                android.util.Log.e(tag, log)
            }
        }

        override fun logF(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
            if (level > LEVEL_FATAL) {
                return
            }
            android.util.Log.e(tag, log)

            if (toastSupportContext != null) {
                handler.post { Toast.makeText(toastSupportContext, log, Toast.LENGTH_LONG).show() }
            }
        }

        override fun appenderClose() {

        }

        override fun appenderFlush(isSync: Boolean) {}

    }

    var impl: LogImp? = debugLog
        private set

    val logLevel: Int
        get() = if (impl != null) {
            impl!!.logLevel
        } else LEVEL_NONE

    val sysInfo: String

    interface LogImp {

        val logLevel: Int

        fun logV(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun logI(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun logD(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun logW(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun logE(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun logF(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        fun appenderClose()

        fun appenderFlush(isSync: Boolean)

    }

    fun setLogImp(imp: LogImp) {
        impl = imp
    }

    fun appenderClose() {
        if (impl != null) {
            impl!!.appenderClose()
        }
    }

    fun appenderFlush(isSync: Boolean) {
        if (impl != null) {
            impl!!.appenderFlush(isSync)
        }
    }

    fun setLevel(level: Int, jni: Boolean) {
        Log.level = level
        android.util.Log.w(TAG, "new log level: $level")

        if (jni) {
            Xlog.setLogLevel(level)
            //android.util.Log.e(TAG, "no jni log level support");
        }
    }

    /**
     * use f(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun f(tag: String, msg: String) {
        f(tag, msg, *(null as Array<Any>?)!!)
    }

    /**
     * use e(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun e(tag: String, msg: String) {
        e(tag, msg, *(null as Array<Any>?)!!)
    }

    /**
     * use w(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun w(tag: String, msg: String) {
        w(tag, msg, *(null as Array<Any>?)!!)
    }

    /**
     * use i(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun i(tag: String, msg: String) {
        i(tag, msg, *(null as Array<Any>?)!!)
    }

    /**
     * use d(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun d(tag: String, msg: String) {
        d(tag, msg, *(null as Array<Any>?)!!)
    }

    /**
     * use v(tag, format, obj) instead
     *
     * @param tag
     * @param msg
     */
    fun v(tag: String, msg: String) {
        v(tag, msg, *(null as Array<Any>?)!!)
    }

    fun f(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            val log = if (obj == null) format else String.format(format, *obj)
            impl!!.logF(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun e(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            impl!!.logE(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun w(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            impl!!.logW(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun i(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            impl!!.logI(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun d(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            impl!!.logD(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun v(tag: String, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            impl!!.logV(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
        if (impl != null) {
            var log: String? = if (obj == null) format else String.format(format, *obj)
            if (log == null) {
                log = ""
            }
            log += "  " + android.util.Log.getStackTraceString(tr)
            impl!!.logE(tag, "", "", 0, Process.myPid(), Thread.currentThread().id, Looper.getMainLooper().thread.id, log)
        }
    }

    init {
        val sb = StringBuilder()
        try {
            sb.append("VERSION.RELEASE:[" + android.os.Build.VERSION.RELEASE)
            sb.append("] VERSION.CODENAME:[" + android.os.Build.VERSION.CODENAME)
            sb.append("] VERSION.INCREMENTAL:[" + android.os.Build.VERSION.INCREMENTAL)
            sb.append("] BOARD:[" + android.os.Build.BOARD)
            sb.append("] DEVICE:[" + android.os.Build.DEVICE)
            sb.append("] DISPLAY:[" + android.os.Build.DISPLAY)
            sb.append("] FINGERPRINT:[" + android.os.Build.FINGERPRINT)
            sb.append("] HOST:[" + android.os.Build.HOST)
            sb.append("] MANUFACTURER:[" + android.os.Build.MANUFACTURER)
            sb.append("] MODEL:[" + android.os.Build.MODEL)
            sb.append("] PRODUCT:[" + android.os.Build.PRODUCT)
            sb.append("] TAGS:[" + android.os.Build.TAGS)
            sb.append("] TYPE:[" + android.os.Build.TYPE)
            sb.append("] USER:[" + android.os.Build.USER + "]")
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        sysInfo = sb.toString()
    }
}
