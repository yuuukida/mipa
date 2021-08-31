package moe.mipa.agent

import android.util.Log
import moe.mipa.annotation.GlobalFunction
import moe.mipa.annotation.OverloadFunction


class MipaConsole {


    @OverloadFunction
    @GlobalFunction
    fun debug(msg1: Any?, msg2: Any?, msg3: Any?) = Unit

    @GlobalFunction
    fun debugOverload(msg1: Any?, msg2: Any?, msg3: Any?): String {
        val msg = checkMsg(msg1, msg2, msg3)
        Log.d("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun info(msg1: Any?, msg2: Any?, msg3: Any?) = Unit

    @GlobalFunction
    fun infoOverload(msg1: Any?, msg2: Any?, msg3: Any?): String {
        val msg = checkMsg(msg1, msg2, msg3)
        Log.i("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun error(msg1: Any?, msg2: Any?, msg3: Any?) = Unit

    @GlobalFunction
    fun errorOverload(msg1: Any?, msg2: Any?, msg3: Any?): String {
        val msg = checkMsg(msg1, msg2, msg3)
        Log.e("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun warn(msg1: Any?, msg2: Any?, msg3: Any?) = Unit

    @GlobalFunction
    fun warnOverload(msg1: Any?, msg2: Any?, msg3: Any?): String {
        val msg = checkMsg(msg1, msg2, msg3)
        Log.w("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun log(msg1: Any?, msg2: Any?, msg3: Any?) = Unit

    @GlobalFunction
    fun logOverload(msg1: Any?, msg2: Any?, msg3: Any?): String {
        val msg = checkMsg(msg1, msg2, msg3)
        Log.w("mipa.console", msg)
        return msg
    }

    private fun checkMsg(msg1: Any?, msg2: Any?, msg3: Any?): String {
        var msg = ""
        if (msg1 is String) {
            msg += msg1
        } else if (msg1 is Any) {
            msg += msg1.toString()
        }
        if (msg2 is String) {
            msg += msg2
        } else if (msg2 is Any) {
            msg += msg2.toString()
        }
        if (msg3 is String) {
            msg += msg3
        } else if (msg3 is Any) {
            msg += msg3.toString()
        }
        return msg
    }
}
