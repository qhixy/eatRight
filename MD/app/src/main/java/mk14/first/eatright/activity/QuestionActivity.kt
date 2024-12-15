package mk14.first.eatright.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mk14.first.eatright.R
import mk14.first.eatright.api.ApiService
import mk14.first.eatright.api.PredictRequest
import mk14.first.eatright.api.PredictResponse

import mk14.first.eatright.retrofit.RetrofitClient
import mk14.first.eatright.utils.UserSessionManager
import retrofit2.Response

class QuestionActivity : AppCompatActivity() {

    private lateinit var userSessionManager: UserSessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        userSessionManager = UserSessionManager(this)
        apiService = RetrofitClient.apiService

        findViewById<Button>(R.id.submit_button).setOnClickListener {
            handlePrediction()
        }
    }

    private fun handlePrediction() {
        val username = userSessionManager.getUserMessage()
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Fetch user data by username
                val userResponse = apiService.getUserByUsername(username)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()?.user
                    if (user != null) {
                        val userId = user.id
                        val predictionRequest = collectPredictionData()
                        if (predictionRequest != null) {
                            sendPredictionRequest(userId, predictionRequest)
                        }
                    } else {
                        Toast.makeText(this@QuestionActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@QuestionActivity, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@QuestionActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectPredictionData(): PredictRequest? {
        val age = findViewById<EditText>(R.id.age_input).text.toString().toIntOrNull()
        val gender = when (findViewById<RadioGroup>(R.id.rgGender).checkedRadioButtonId) {
            R.id.roPria -> "Male"
            R.id.roWanita -> "Female"
            else -> null
        }
        val height = findViewById<EditText>(R.id.height_input).text.toString().toIntOrNull()
        val weight = findViewById<EditText>(R.id.weight_input).text.toString().toIntOrNull()
        val calc = when (findViewById<RadioGroup>(R.id.rgCALC).checkedRadioButtonId) {
            R.id.roNever -> "no"
            R.id.roRarely -> "Sometimes"
            R.id.roOften -> "Frequently"
            R.id.roAlways -> "Always"
            else -> null
        }
        val favc = when (findViewById<RadioGroup>(R.id.rgFAVC).checkedRadioButtonId) {
            R.id.roYesFAVC -> "yes"
            R.id.roNoFAVC -> "no"
            else -> null
        }
        val fcvc = when (findViewById<RadioGroup>(R.id.rgFCVC).checkedRadioButtonId) {
            R.id.ro1 -> 1
            R.id.ro2 -> 2
            R.id.ro3 -> 3
            else -> null
        }
        val ncp = findViewById<EditText>(R.id.ncp_input).text.toString().toIntOrNull() ?: 1
        val scc = when (findViewById<RadioGroup>(R.id.rgSCC).checkedRadioButtonId) {
            R.id.roYes -> "yes"
            R.id.roNo -> "no"
            else -> null
        }
        val smoke = when (findViewById<RadioGroup>(R.id.rgSmoke).checkedRadioButtonId) {
            R.id.roSmokeYes -> "yes"
            R.id.roSmokeNo -> "no"
            else -> null
        }
        val ch2o = findViewById<EditText>(R.id.CH20_input).text.toString().toIntOrNull() ?: 1
        val familyHistory = when (findViewById<RadioGroup>(R.id.rgOBS).checkedRadioButtonId) {
            R.id.roOBSYes -> "yes"
            R.id.roOBSNo -> "no"
            else -> null
        }
        val faf = findViewById<EditText>(R.id.FAF_input).text.toString().toIntOrNull() ?: 1
        val tue = findViewById<EditText>(R.id.TUE_input).text.toString().toIntOrNull() ?: 1
        val caec = when (findViewById<RadioGroup>(R.id.rgCAEC).checkedRadioButtonId) {
            R.id.roCAECNever -> "no"
            R.id.roCAECRarely -> "Sometimes"
            R.id.roCAECOften -> "Frequently"
            R.id.roCAECAlways -> "Always"
            else -> null
        }
        val mtrans = when (findViewById<RadioGroup>(R.id.rgMTRANS).checkedRadioButtonId) {
            R.id.roWalking -> "Walking"
            R.id.roBike -> "Bike"
            R.id.roMotorbike -> "Motorbike"
            R.id.roCar -> "Car"
            R.id.roPT -> "Public Transportation"
            R.id.roOther -> "Other"
            else -> null
        }

        // Validate inputs
        if (age == null || gender == null || height == null || weight == null || calc == null || favc == null ||
            fcvc == null || scc == null || smoke == null || familyHistory == null || caec == null || mtrans == null
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return null
        }

        return PredictRequest(
            Age = age,
            Gender = gender,
            Height = height,
            Weight = weight,
            CALC = calc,
            FAVC = favc,
            FCVC = fcvc,
            NCP = ncp,
            SCC = scc,
            SMOKE = smoke,
            CH2O = ch2o,
            family_history_with_overweight = familyHistory,
            FAF = faf,
            TUE = tue,
            CAEC = caec,
            MTRANS = mtrans
        )
    }

    private suspend fun sendPredictionRequest(userId: Int, predictionRequest: PredictRequest) {
        try {
            val response: Response<PredictResponse> = apiService.predictObesity(userId, predictionRequest)
            if (response.isSuccessful) {
                val prediction = response.body()?.prediction
                if (prediction != null) {
                    Toast.makeText(this, "Prediction: $prediction", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@QuestionActivity, ScanActivity::class.java)
                    startActivity(intent)
//                    finish()
                } else {
                    Toast.makeText(this, "No prediction data returned", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Prediction request failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
