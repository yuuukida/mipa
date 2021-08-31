package moe.mipa.agent

import android.util.Log
import moe.mipa.annotation.GlobalFunction
import moe.mipa.annotation.OverloadFunction


class MipaConsole {

    @OverloadFunction
    fun debug(msg:Any,msg2:Any,msg3:Any) {}
    fun debugOverload(msg:Any,msg2:Any,msg3:Any) {
        Log.d("mipa.console", msg.toString()+msg2.toString()+msg3.toString())
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