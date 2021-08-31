package moe.mipa.agent

import android.util.Log
import moe.mipa.annotation.GlobalFunction
import moe.mipa.annotation.OverloadFunction


class MipaConsole {

    @OverloadFunction
    @GlobalFunction
    fun debug(msg1: Any, msg2: Any, msg3: Any) = Unit
    @GlobalFunction
    fun debugOverload(msg1: Any, msg2: Any, msg3: Any): String {
        val msg = msg1.toString() + msg2.toString() + msg3.toString()
        Log.d("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun info(msg1: Any, msg2: Any, msg3: Any) = Unit
    @GlobalFunction
    fun infoOverload(msg1: Any, msg2: Any, msg3: Any): String {
        val msg = msg1.toString() + msg2.toString() + msg3.toString()
        Log.i("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun error(msg1: Any, msg2: Any, msg3: Any) = Unit
    @GlobalFunction
    fun errorOverload(msg1: Any, msg2: Any, msg3: Any): String {
        val msg = msg1.toString() + msg2.toString() + msg3.toString()
        Log.e("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun warn(msg: Any, msg2: Any, msg3: Any) = Unit
    @GlobalFunction
    fun warnOverload(msg1: Any, msg2: Any, msg3: Any): String {
        val msg = msg1.toString() + msg2.toString() + msg3.toString()
        Log.w("mipa.console", msg)
        return msg
    }

    @OverloadFunction
    @GlobalFunction
    fun log(msg: Any, msg2: Any, msg3: Any) = Unit
    @GlobalFunction
    fun logOverload(msg1: Any, msg2: Any, msg3: Any): String {
        val msg = msg1.toString() + msg2.toString() + msg3.toString()
        Log.w("mipa.console", msg)
        return msg
    }

}