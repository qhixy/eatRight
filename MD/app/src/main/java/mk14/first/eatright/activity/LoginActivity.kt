package mk14.first.eatright.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import mk14.first.eatright.R
import mk14.first.eatright.activity.home.HomeActivity
import mk14.first.eatright.activity.home.HomeFragment
import mk14.first.eatright.api.LoginUser
import mk14.first.eatright.api.UserResponse
import mk14.first.eatright.retrofit.RetrofitClient
import mk14.first.eatright.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        userSessionManager = UserSessionManager(this)

        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupText = findViewById<TextView>(R.id.txtDaftar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Mohon Lengkapi data anda", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        // Handle Sign-up Text Click
        signupText.setOnClickListener {
            Toast.makeText(this, "Masuk Ke Daftar Akun", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginUser = LoginUser(email, password)

        RetrofitClient.apiService.loginUser(loginUser).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.success) {
                        // Save user data in session
                        userSessionManager.saveUser(email, userResponse.message)

                        // Log response and success message
                        Log.d("LoginActivity", "Login successful: ${userResponse.message}")

                        // Show success message
                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

//                         Navigate to Scan Activity after login successful
//                        val intent = Intent(this@LoginActivity, ScanActivity::class.java)
//                        Log.d("LoginActivity", "Starting ScanActivity")
//                        startActivity(intent)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, HomeFragment()) // R.id.fragmentContainer adalah ID container fragment di layout activity_login.xml
                            .commit()


                        // Finish LoginActivity so user cannot go back to it using back button
//                        finish()
                    } else {

                        Toast.makeText(this@LoginActivity, userResponse?.message ?: "Login Gagal", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        Log.d("LoginActivity", "Starting ScanActivity")
                        startActivity(intent)
                        finish()

                    }
                } else {
                    Log.e("LoginActivity", "Login failed: ${response.message()}")
                    Toast.makeText(this@LoginActivity, "Login Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("LoginActivity", "Login failed: ${t.message}")
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
