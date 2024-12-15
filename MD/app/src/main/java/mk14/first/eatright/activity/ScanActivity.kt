package mk14.first.eatright.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import mk14.first.eatright.R
import mk14.first.eatright.retrofit.RetrofitClient
import mk14.first.eatright.api.ApiResponse
import mk14.first.eatright.api.Barcode
import mk14.first.eatright.utils.ApiUtils
import mk14.first.eatright.utils.CameraUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var btnScan: Button
    private lateinit var tvProductName: TextView
    private lateinit var tvBrand: TextView
    private lateinit var tvNutritionalInfo: TextView

    private val REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        previewView = findViewById(R.id.previewView)
        btnScan = findViewById(R.id.btnScan)
        tvProductName = findViewById(R.id.tvProductName)
        tvBrand = findViewById(R.id.tvBrand)
        tvNutritionalInfo = findViewById(R.id.tvNutritionalInfo)

        // Request permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
        }

        // Start the camera using CameraUtils
        CameraUtils.startCamera(
            context = this,
            lifecycleOwner = this,
            previewView = previewView,
            onBarcodeScanned = { barcode ->
                // Handle barcode scanned result (for example, show barcode info)
                showProductDetails(barcode)
            }
        )

        // Set up the scan button to capture the image and upload it
        btnScan.setOnClickListener {
            captureImageAndSendToApi()
        }
    }

    private fun showProductDetails(barcode: String) {
        // Update the UI based on the barcode data
        tvProductName.text = "Product: $barcode"
        tvProductName.visibility = View.VISIBLE
        tvBrand.visibility = View.VISIBLE
        tvNutritionalInfo.visibility = View.VISIBLE
    }

    private fun captureImageAndSendToApi() {
        val imageCapture = CameraUtils.imageCapture ?: return

        val file = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        // Capture the image
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("ScanActivity", "Image saved at: ${file.absolutePath}")
                    uploadImageToApi(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("ScanActivity", "Image capture failed", exception)
                }
            })
    }

    private fun uploadImageToApi(file: File) {
        ApiUtils.uploadImageFile(file,
            onSuccess = { apiResponse ->
                // Handle success (barcode scanned and details fetched)
                val barcode = apiResponse?.barcodes?.firstOrNull()
                if (barcode != null) {
                    displayProductDetails(barcode)
                } else {
                    showToast("No barcode found!")
                }
            },
            onError = { errorMessage ->
                // Show the error message
                showToast(errorMessage)
            }
        )
    }

    private fun displayProductDetails(barcode: Barcode) {
        // Update UI with the product details from barcode
        tvProductName.text = "Product Name: ${barcode.product_name}"
        tvBrand.text = "Brand: ${barcode.brand}"
        tvNutritionalInfo.text = """
            Nutritional Info:
            Calories: ${barcode.nutritional_info.calories}
            Fat: ${barcode.nutritional_info.fat}g
            Carbs: ${barcode.nutritional_info.carbohydrates}g
            Protein: ${barcode.nutritional_info.protein}g
            Sugar: ${barcode.nutritional_info.sugar}g
        """.trimIndent()

        tvProductName.visibility = View.VISIBLE
        tvBrand.visibility = View.VISIBLE
        tvNutritionalInfo.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Shut down the camera when the activity is destroyed
        CameraUtils.shutdownCameraExecutor()
    }

}
