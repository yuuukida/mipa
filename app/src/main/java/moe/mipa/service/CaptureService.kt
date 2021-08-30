package moe.mipa.service

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import moe.mipa.utils.Mipa

class CaptureService : Service() {

    companion object {
        const val CAPTURE_START = "moe.mipa.CAPTURE_START"
    }

    private lateinit var resultData: Intent
    private lateinit var mediaProjection: MediaProjection
    private lateinit var virtualDisplay: VirtualDisplay


    // TODO permission should requested in runtime
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            CAPTURE_START -> {
                val mediaProjectionManager =
                    getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                resultData = intent.getParcelableExtra("data")!!
                mediaProjection =
                    mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, resultData)
                virtualDisplay = mediaProjection.createVirtualDisplay(
                    "mipa-display",
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    resources.displayMetrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                    Mipa.imageReader.surface, null, null
                )
                mediaProjection.createVirtualDisplay(
                    "mipa-display",
                    resources.displayMetrics.heightPixels,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                    Mipa.imageReader2.surface, null, null
                )
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // TODO release

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}