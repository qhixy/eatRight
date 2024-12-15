package mk14.first.eatright.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk14.first.eatright.R
import mk14.first.eatright.api.RegisterUser
import mk14.first.eatright.retrofit.RetrofitClient
import mk14.first.eatright.utils.UserSessionManager

class BioActivity : AppCompatActivity() {
    private lateinit var userSessionManager: UserSessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bio)
        userSessionManager = UserSessionManager(this)


        findViewById<Button>(R.id.next_button).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email_input).text.toString()
            val username = findViewById<TextInputEditText>(R.id.name_input).text.toString()
            val password = findViewById<TextInputEditText>(R.id.password_input).text.toString()
            val passc = findViewById<TextInputEditText>(R.id.confirm_input_password).text.toString()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || passc.isEmpty()) {
                Toast.makeText(this, "Lengkapi Semua data.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (isValidEmail(email) && password == passc) {
                    registerUser(username,email,password)
                } else {
                    if (!isValidEmail(email)) {
                        Toast.makeText(this, "Format email salah", Toast.LENGTH_SHORT).show()
                    }
                    if (password != passc) {
                        Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        findViewById<TextView>(R.id.Masuk).setOnClickListener{
            Toast.makeText(this, "Navigate to Log-In", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val registerRequest = RegisterUser(username, email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.registerUser(registerRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        userSessionManager.saveUser(email, username)
                        Toast.makeText(this@BioActivity, "Sign-Up Successful: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BioActivity, QuestionActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        if (errorBody.contains("email already exists", ignoreCase = true)) {
                            Toast.makeText(this@BioActivity, "Email is already in use", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@BioActivity, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BioActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        return emailRegex.matches(email)
    }
}