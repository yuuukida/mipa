package moe.mipa.utils

import android.graphics.Bitmap
import android.media.ImageReader
import com.eclipsesource.v8.V8

class Mipa private constructor() {
    companion object {
        lateinit var imageReader: ImageReader
        lateinit var imageReader2: ImageReader
         var cachedBmp: Bitmap ? = null
    }
}



