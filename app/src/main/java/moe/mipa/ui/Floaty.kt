package moe.mipa.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_PHONE
import android.widget.Button
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import moe.mipa.MipaAgent
import moe.mipa.R
import moe.mipa.agent.MipaAction
import moe.mipa.agent.MipaConsole
import moe.mipa.agent.MipaScreen
import moe.mipa.agent.MipaThread

class FloatWindow(private val ctx: Context) {
    private  var isRunning = false

    private class OnFloatingTouchListener(
        val layoutParams: WindowManager.LayoutParams,
        val windowManager: WindowManager
    ) : View.OnTouchListener {
        private var x = 0
        private var y = 0
        private var flagX = 0
        private var flagY = 0

        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = motionEvent.rawX.toInt()
                    y = motionEvent.rawY.toInt()
                    flagX = x
                    flagY = y
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = motionEvent.rawX.toInt()
                    val nowY = motionEvent.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.apply {
                        x += movedX
                        y += movedY
                    }
                    windowManager.updateViewLayout(view, layoutParams)
                }
                MotionEvent.ACTION_UP -> {
                    if (motionEvent.rawX.toInt() == flagX && motionEvent.rawY.toInt() == flagY) {
                        view.performClick()
                    }
                }
                else -> {

                }
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun init() {
        val btn = Button(ctx)
        val wm = ctx.getSystemService(WINDOW_SERVICE) as WindowManager
        btn.text = ctx.resources.getString(R.string.start)
        val layoutParams = WindowManager.LayoutParams().apply {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            type = TYPE_PHONE
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        btn.setOnTouchListener(OnFloatingTouchListener(layoutParams, wm))
        btn.setOnClickListener {
            if(!isRunning) {
                MainScope().launch {
                    isRunning = true
                    btn.text = ctx.resources.getString(R.string.stop)
                    MipaAgent(
                        ctx = ctx,
                        clzList = listOf(
                            MipaConsole::class.java,
                            MipaScreen::class.java,
                            MipaThread::class.java,
                            MipaAction::class.java,
                        )
                    ).runScript()
                    isRunning = false
                    btn.text = ctx.resources.getString(R.string.start)
                }
            }
        }

        wm.addView(btn, layoutParams)
    }
}