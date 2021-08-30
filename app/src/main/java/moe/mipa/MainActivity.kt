package moe.mipa


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat.RGBA_8888
import android.media.ImageReader
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import moe.mipa.service.CaptureService
import moe.mipa.service.MipaService
import moe.mipa.ui.FloatWindow
import moe.mipa.utils.Mipa


class MainActivity : AppCompatActivity() {
    private var captureEnable = false
    private val registers =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    private val captureRegister =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val captureServiceIntent = Intent(this, CaptureService::class.java)
                captureServiceIntent.putExtra("data", it.data)
                captureServiceIntent.action = CaptureService.CAPTURE_START
                startService(captureServiceIntent)
                captureEnable = true
            } else {
                captureEnable = false
                //  TODO Toast
            }
        }

    /**
     * 检查 无障碍、悬浮窗、截图权限、绑定onClickListener
     */
    private fun checkPermissions() {
        accessibility_switch.isChecked = MipaService.isActivated
        float_switch.isChecked = Settings.canDrawOverlays(this)
        capture_switch.isChecked = captureEnable
    }


    private fun initFloatWindow(){
        val fw = FloatWindow(this)
        fw.init()
    }


    private fun initListeners(){
        float_switch.setOnClickListener {
            registers.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }
        accessibility_switch.setOnClickListener {
            registers.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        capture_switch.setOnClickListener {
            captureRegister.launch((getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent())
        }
    }

    private fun initUI() {
        setContentView(R.layout.activity_main)
        initFloatWindow()
    }

    @SuppressLint("WrongConstant")
    private fun initData(){
        // TODO coroutine
        Mipa.imageReader = ImageReader.newInstance(
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
            RGBA_8888,
            2
        )
        Mipa.imageReader2 = ImageReader.newInstance(
            resources.displayMetrics.heightPixels,
            resources.displayMetrics.widthPixels,
            RGBA_8888,
            2
        )
        captureRegister.launch((getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent())
    }


    private fun initialize() {
        initUI()
        initData()
        initListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

}