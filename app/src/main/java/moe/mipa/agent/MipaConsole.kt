package moe.mipa.agent

import android.util.Log
import moe.mipa.annotation.GlobalFunction


class MipaConsole {
    fun debug(msg:Any) {
        Log.d("mipa.console", msg.toString())
    }
    fun info(msg:Any) {
        Log.i("mipa.console", msg.toString())
    }
    fun warn(msg:Any) {
        Log.w("mipa.console", msg.toString())
    }
    fun error(msg:Any) {
        Log.e("mipa.console", msg.toString())
    }
    @GlobalFunction
    fun log(msg:Any) {
        Log.v("mipa.console", msg.toString())
    }
}