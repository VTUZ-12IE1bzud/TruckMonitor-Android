package ru.annin.truckmonitor.presentation.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.google.zxing.BarcodeFormat
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Экран QR сканера.
 *
 * @author Pavel Annin.
 */
class QrScannerActivity : MvpAppCompatActivity() {

    companion object {
        // Parameter's
        private const val REQUEST_CODE_PERMISSION_CAMERA = 1

        @JvmStatic fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, QrScannerActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    // Component's
    private lateinit var viewDelegate: ViewDelegate

    // Data's
    private var isCameraEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDelegate = ViewDelegate(this).apply {
            onQrScan = { navigate2FinishOnSuccess(it) }
        }
        setContentView(viewDelegate.vScanner)
    }

    override fun onResume() {
        super.onResume()
        // Проверка permission на камеру
        val check = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (check == PackageManager.PERMISSION_GRANTED) {
            // Разрешение выдано
            viewDelegate.startCamera()
            isCameraEnabled = true
        } else {
            // Разрешение не выдано, запрашиваем разрешение у пользователя.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION_CAMERA)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isCameraEnabled) {
            viewDelegate.stopCamera()
            isCameraEnabled = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            permissions
                    .filterIndexed { index, permission ->
                        permission == Manifest.permission.CAMERA
                                && grantResults[index] == PackageManager.PERMISSION_GRANTED
                    }
                    .forEach {
                        viewDelegate.startCamera()
                        isCameraEnabled = true
                    }
            if (!isCameraEnabled) {
                // Если пользователь отказался от разрешения на камеру, закрываем экран
                navigate2FinishOnCancel()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /** Закрыть экран, и сообщеть о удачной попытке сканирования QR кода. */
    private fun navigate2FinishOnSuccess(qr: String) {
        val intent = Intent().apply { data = Uri.parse(qr) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /** Закрыть экран, и сообщеть о неудачной попытке сканирования QR кода. */
    private fun navigate2FinishOnCancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    /** View Delegate экрана "QR сканера".
     *
     * @property onQrScan Событие, QR отсканирован.
     */
    private class ViewDelegate(private val context: Context) {

        // View'
        val vScanner by lazy { ZXingScannerView(context) }

        // Listener's
        var onQrScan: ((String) -> Unit)? = null

        init {
            // Setup
            vScanner.run {
                setFormats(arrayListOf(BarcodeFormat.QR_CODE))
                setResultHandler { onQrScan?.invoke(it.text) }
            }
        }

        fun startCamera() = vScanner.startCamera()

        fun stopCamera() = vScanner.stopCamera()
    }
}