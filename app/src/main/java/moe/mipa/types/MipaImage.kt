package moe.mipa.types

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MipaImage(private val bitmap: Bitmap) {
    fun getPixel(x: Int, y: Int): String {
        try {
            val color = bitmap.getPixel(x, y)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return "#%02X%02X%02X".format(r,g,b)
        } catch (e :Exception) {
            Log.e("ERROR",e.message?:"Unknown ERROR")
        }
        return "#000000"
    }

    fun close() {
        bitmap.recycle()
    }

    fun save(path:String, fileName: String) {
        if (!File(path).exists()) File(path).mkdirs()
        try {
            val os = FileOutputStream (File(path,fileName))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,os)
            os.flush()
            os.close()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

}