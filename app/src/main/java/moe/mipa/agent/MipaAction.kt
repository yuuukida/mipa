package moe.mipa.agent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.eclipsesource.v8.V8Function
import moe.mipa.annotation.GlobalFunction
import moe.mipa.service.MipaService

class MipaAction(private val ctx: Context) {
    @GlobalFunction
    fun tap(x: Int, y: Int) {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_CLICK
        intent.putExtra("x", x)
        intent.putExtra("y", y)
        ctx.startService(intent)
        Thread.sleep(1000)
    }

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
    fun text(str:String) {
        val intent = Intent(ctx, MipaService::class.java)
        intent.action = MipaService.ACTION_INPUT_TEXT
        intent.putExtra("str", str)
        ctx.startService(intent)
    }
}