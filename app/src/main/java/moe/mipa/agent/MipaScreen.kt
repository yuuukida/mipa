package moe.mipa.agent

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.media.Image
import com.eclipsesource.v8.V8Object
import io.alicorn.v8.V8JavaAdapter.getCacheForRuntime
import io.alicorn.v8.V8JavaObjectUtils
import moe.mipa.annotation.ContextFunction
import moe.mipa.annotation.GlobalFunction
import moe.mipa.types.MipaImage
import moe.mipa.utils.Mipa


class MipaScreen(private val ctx:Context) {

    @GlobalFunction
    fun capture(): MipaImage {
        val landscape = ctx.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val m = getCapture(landscape)
        return MipaImage(m)
    }
    fun getPixel(x: Int, y: Int): String {
        val pixel = getCapture().getPixel(x, y)
        return "#%06X".format(pixel)
    }

    private fun getCapture(landscape: Boolean = false): Bitmap {
        val newImg = if (landscape) {
            Mipa.imageReader2.acquireLatestImage()
        } else {
            Mipa.imageReader.acquireLatestImage()
        }
        if (newImg != null) {
            val bmp = img2Bmp(newImg)
            Mipa.cachedBmp?.recycle()
            Mipa.cachedBmp = bmp
            newImg.close()
        }
        return Mipa.cachedBmp!!
    }
    private fun img2Bmp(image: Image): Bitmap {
        val width = image.width
        val height = image.height
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        var bitmap =
            Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
        image.close()
        return bitmap
    }
}


