package moe.mipa.agent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.eclipsesource.v8.V8Function
import moe.mipa.annotation.GlobalFunction
import moe.mipa.annotation.OverloadFunction
import moe.mipa.service.MipaService

class MipaAction(private val ctx: Context) {


    @GlobalFunction
    @OverloadFunction
    fun tap(x: Int, y: Int, millis: Int? = 1000) = Unit

    @GlobalFunction
    fun tapOverload(x: Int, y: Int, millis: Int? = 1000): String {
        val defaultMillis = millis ?: 1000
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_CLICK
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        ctx.startService(intent)
        if (defaultMillis > 0) {
            Thread.sleep(defaultMillis.toLong())
        }
        return "tap($x,$y,$defaultMillis)"
    }


    @GlobalFunction
    @OverloadFunction
    fun tapUntil(condition: Any?, x: Int, y: Int, millis: Int?) = Unit

    @GlobalFunction
    fun tapUntilOverload(condition: Any?, x: Int, y: Int, millis: Int?): String {
        val defaultMillis = millis ?: 1000
        var count = 0
        var res = ""
        if (condition is V8Function) {
            while (!(condition.call(null, null) as Boolean)) {
                count++
                res = tapOverload(x, y, defaultMillis)
            }
        } else if (condition is String) {
            Log.d(condition, condition)
        }
        return  res + "x$count"
    }

    @GlobalFunction
    @OverloadFunction
    fun tapAfter(condition: Any?, x: Int, y: Int, millis: Int?) = Unit

    @GlobalFunction
    fun tapAfterOverload(condition: Any?, x: Int, y: Int, millis: Int?): String {
        val defaultMillis = millis ?: 1000
        var count = 0
        if (condition is V8Function) {
            while (!(condition.call(null, null) as Boolean)) {
                count++
                Thread.sleep(defaultMillis.toLong())
            }
        } else if (condition is String) {
            Log.d(condition, condition)
        }
        return tapOverload(x, y, 0) + "...$count"
    }

    @Deprecated("replace with tap")
    @GlobalFunction
    fun click(x: Int, y: Int) {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_CLICK
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        ctx.startService(intent)
    }

    @GlobalFunction
    fun swipe(x1: Int, y1: Int, x2: Int, y2: Int) {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_SWIPE
        intent.putExtra("x1", x1)
        intent.putExtra("y1", y1)
        intent.putExtra("x2", x2)
        intent.putExtra("y2", y2)
        ctx.startService(intent)
    }

    @GlobalFunction
    fun launch(pkg: String) {
        val packageManager: PackageManager = ctx.packageManager
        ctx.startActivity(
            packageManager.getLaunchIntentForPackage(pkg)?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    @GlobalFunction
    fun text(str: String) {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_INPUT_TEXT
        intent.putExtra("str", str)
        ctx.startService(intent)
    }
}