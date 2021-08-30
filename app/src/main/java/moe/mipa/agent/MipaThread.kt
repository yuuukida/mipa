package moe.mipa.agent

import com.eclipsesource.v8.V8Function
import kotlinx.coroutines.delay
import moe.mipa.annotation.GlobalFunction
import java.lang.Thread

class MipaThread {
    @GlobalFunction
    fun sleep(millis: Long) {
        Thread.sleep(millis)
    }

    @GlobalFunction
    fun waitFor(foo: V8Function) {
        while (!(foo.call(null, null) as Boolean)) {
            Thread.sleep(1000)
        }
    }
}