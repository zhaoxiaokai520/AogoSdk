package com.tencent.mars.xlog

import com.tencent.mars.xlog.Log.logLevel

class Xlog : Log.LogImp {

    override var logLevel: Int
        external get
        external set

    class XLoggerInfo {
        var level: Int = 0
        var tag: String? = null
        var filename: String? = null
        var funcname: String? = null
        var line: Int = 0
        var pid: Long = 0
        var tid: Long = 0
        var maintid: Long = 0
    }

    override fun logV(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_VERBOSE, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override fun logD(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_DEBUG, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override fun logI(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_INFO, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override fun logW(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_WARNING, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override fun logE(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_ERROR, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override fun logF(tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String) {
        logWrite2(LEVEL_FATAL, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log)
    }

    override external fun appenderClose()

    override external fun appenderFlush(isSync: Boolean)

    companion object {

        val LEVEL_ALL = 0
        val LEVEL_VERBOSE = 0
        val LEVEL_DEBUG = 1
        val LEVEL_INFO = 2
        val LEVEL_WARNING = 3
        val LEVEL_ERROR = 4
        val LEVEL_FATAL = 5
        val LEVEL_NONE = 6

        val AppednerModeAsync = 0
        val AppednerModeSync = 1

        fun open(isLoadLib: Boolean, level: Int, mode: Int, cacheDir: String, logDir: String, nameprefix: String, pubkey: String) {
            if (isLoadLib) {
                System.loadLibrary("stlport_shared")
                System.loadLibrary("xlog")
            }

            appenderOpen(level, mode, cacheDir, logDir, nameprefix, pubkey)
        }

        private fun decryptTag(tag: String): String {
            return tag
        }

        public fun setLogLevel(level: Int)
        {
            //logLevel = level
        }

        external fun logWrite(logInfo: XLoggerInfo, log: String)

        external fun logWrite2(level: Int, tag: String, filename: String, funcname: String, line: Int, pid: Int, tid: Long, maintid: Long, log: String)

        external fun setAppenderMode(mode: Int)

        external fun setConsoleLogOpen(isOpen: Boolean)    //set whether the console prints log

        external fun setErrLogOpen(isOpen: Boolean)    //set whether the  prints err log into a separate file

        external fun appenderOpen(level: Int, mode: Int, cacheDir: String, logDir: String, nameprefix: String, pubkey: String)
    }

}
