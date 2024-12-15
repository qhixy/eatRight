//// BarcodeScannerFragment.kt
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.camera.view.PreviewView
//import mk14.first.eatright.R
//import mk14.first.eatright.api.Product
//import mk14.first.eatright.utils.CameraUtils
//import mk14.first.eatright.utils.ApiUtils
//
//class MainFragment : Fragment() {
//
//    private lateinit var previewView: PreviewView
//    private lateinit var scanButton: Button
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_main, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        previewView = view.findViewById(R.id.previewView)
//        scanButton = view.findViewById(R.id.btnScan) // Assuming a button with ID `btnScan` exists in your layout
//
//        scanButton.setOnClickListener {
//            startBarcodeScanning()
//        }
//    }
//
//    private fun startBarcodeScanning() {
//        CameraUtils.startCamera(requireContext(), viewLifecycleOwner, previewView) { barcode ->
//            ApiUtils.fetchProductDetails(barcode, requireContext()) { product ->
//                displayProductDetails(product)
//            }
//        }
//    }
//
//    private fun displayProductDetails(product: Product) {
//        val productNameTextView = view?.findViewById<TextView>(R.id.tvProductName)
//        val brandTextView = view?.findViewById<TextView>(R.id.tvBrand)
//        val nutritionalInfoTextView = view?.findViewById<TextView>(R.id.tvNutritionalInfo)
//
//        productNameTextView?.apply {
//            text = "Product Name: ${product.product_name}"
//            visibility = View.VISIBLE
//        }
//
//        brandTextView?.apply {
//            text = "Brand: ${product.brand}"
//            visibility = View.VISIBLE
//        }
//
//        nutritionalInfoTextView?.apply {
//            text = buildString {
//                append("Calories: ${product.nutritional_info.calories}\n")
//                append("Fat: ${product.nutritional_info.fat}g\n")
//                append("Carbohydrates: ${product.nutritional_info.carbohydrates}g\n")
//                append("Protein: ${product.nutritional_info.protein}g\n")
//                append("Sugar: ${product.nutritional_info.sugar}g")
//            }
//            visibility = View.VISIBLE
//        }
//    }
//}