package moe.mipa.agent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.eclipsesource.v8.V8Array
import moe.mipa.service.MipaService

fun randPos(x: Int, y: Int, rand: Int): Array<Int> {
    var newX = x + (-rand..rand).random()
    var newY = y + (-rand..rand).random()
    newX = if (newX < 0) 0 else newX
    newY = if (newY < 0) 0 else newY
    return arrayOf(newX, newY)
}


class MipaAction(private val ctx: Context) {
    fun tap(x: Any?, y: Int?, duration: Int?, lag: Int?, rand: Int?): String {
        val _x: Int?
        val _y: Int?
        var (_duration, _lag, _rand) = arrayOf(200, 1000, 5)
        if (x is V8Array) {
            _x = x[0] as Int
            _y = x[1] as Int
            _duration = y ?: _duration
            _lag = duration ?: _lag
            _rand = lag ?: _rand
        } else {
            _x = x as Int
            _y = y as Int
            _duration = duration ?: _duration
            _lag = lag ?: _lag
            _rand = rand ?: _rand
        }
        val intent = Intent(ctx, MipaService::class.java)

        val (newX, newY) = randPos(_x, _y, _rand)

        intent.action = MipaService.ACTION_CLICK
        intent.putExtra("x", newX)
        intent.putExtra("y", newY)
        intent.putExtra("duration", _duration)
        ctx.startService(intent)
        Thread.sleep((_duration).toLong())

        if ((_lag) > 0) {
            Thread.sleep((_lag).toLong())
        }
        Log.d("mipa.logger", "tap($newX,$newY) for $_duration lag=$_lag")
        return "$newX,$newY"
    }

    fun swipe(x1: Any, y1: Any, x2: Int?, y2: Int?, duration: Int?): String {
        val _x1: Int?
        val _y1: Int?
        val _x2: Int?
        val _y2: Int?
        var _duration = 1000
        if (x1 is V8Array && y1 is V8Array) {
            _x1 = x1[0] as Int
            _y1 = x1[1] as Int
            _x2 = y1[0] as Int
            _y2 = y1[1] as Int
            _duration = x2 ?: _duration
        } else {
            _x1 = x1 as Int
            _y1 = y1 as Int
            _x2 = x2 as Int
            _y2 = y2 as Int
            _duration = duration ?: _duration
        }
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_SWIPE
        intent.putExtra("x1", _x1)
        intent.putExtra("y1", _y1)
        intent.putExtra("x2", _x2)
        intent.putExtra("y2", _y2)
        intent.putExtra("duration", _duration)
        ctx.startService(intent)
        Thread.sleep((_duration).toLong())
        Log.d("mipa.logger", "swipe($_x1,$_y1) to ($_x2,$_y2) for $_duration")
        return "$_x1,$_y1,$_x2,$_y2"
    }

    fun launch(pkg: String): String {
        val packageManager: PackageManager = ctx.packageManager
        ctx.startActivity(
            packageManager.getLaunchIntentForPackage(pkg)?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        return "launch($pkg)"
    }

    fun text(str: String): String {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_INPUT_TEXT
        intent.putExtra("str", str)
        ctx.startService(intent)
        return "text($str)"
    }
}