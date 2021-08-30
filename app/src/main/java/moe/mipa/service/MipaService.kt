package moe.mipa.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo


class MipaService : AccessibilityService() {
    companion object {
        const val ACTION_CLICK = "moe.mipa.action.CLICK"
        const val ACTION_SWIPE = "moe.mipa.action.SWIPE"
        const val ACTION_NODES_INFO = "moe.mipa.action.NODES_INFO"
        const val ACTION_INPUT_TEXT = "moe.mipa.action.INPUT_TEXT"
        var isActivated = false
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("service.mipa.action", "start " + intent.action)
        when (intent.action) {
            ACTION_CLICK -> {
                click(
                    intent.getIntExtra("x", 0),
                    intent.getIntExtra("y", 0)
                )
            }
            ACTION_SWIPE -> {
                swipe(
                    intent.getIntExtra("x1", 0),
                    intent.getIntExtra("y1", 0),
                    intent.getIntExtra("x2", 0),
                    intent.getIntExtra("y2", 0),
                )
            }
            ACTION_INPUT_TEXT -> {
                text(
                    intent.getStringExtra("str")
                )
            }

            ACTION_NODES_INFO -> {
                swipe(
                    intent.getIntExtra("x1", 0),
                    intent.getIntExtra("y1", 0),
                    intent.getIntExtra("x2", 0),
                    intent.getIntExtra("y2", 0),
                )
            }
            else -> {
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun click(x: Int, y: Int, duration: Long = 100L) {
        Log.d("service.mipa.action", "click ($x,$y)")
        val clickNode = Path()
        clickNode.moveTo(x.toFloat(), y.toFloat())
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(StrokeDescription(clickNode, 0L, duration))
        dispatchGesture(gestureBuilder.build(), null, null)

    }

    private fun text(str: String? = "") {
        val node = getEditableNode(rootInActiveWindow)
        Log.d("service.mipa.action", "text ($str)")
        val arguments = Bundle()
        arguments.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            str
        )
        if (node !== null) {
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        }
    }

    private fun getEditableNode(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (root.isEditable) {
            return root
        }
        for (i in 0..root.childCount) {
            val child = root.getChild(i)
            if (child !== null) {
                return getEditableNode(child)
            }
        }
        return null
    }

    private fun swipe(x1: Int, y1: Int, x2: Int, y2: Int, duration: Long = 200L) {
        Log.d("service.mipa.action", "swipe ($x1,$y1) to ($x2,$y2)")
        val swipePath = Path()

        swipePath.moveTo(x1.toFloat(), y1.toFloat())
        swipePath.lineTo(x2.toFloat(), y2.toFloat())
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(StrokeDescription(swipePath, 100L, duration))
        dispatchGesture(gestureBuilder.build(), null, null)
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivated = false
        Log.d("service.mipa.status", "onDestroy")
    }

    override fun onInterrupt() {
        Log.d("service.mipa.status", "End")

    }


    override fun onServiceConnected() {
        super.onServiceConnected()
        isActivated = true
        Log.d("service.mipa.status", "Start")
        swipe(600, 200, 600, 600)
    }

}