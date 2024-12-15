package mk14.first.eatright.utils

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    // Save user email and message (e.g., "Welcome vincent!")
    fun saveUser(email: String, message: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("message", message)
        editor.apply()
    }

    // Retrieve saved email
    fun getUserEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    // Retrieve saved message
    fun getUserMessage(): String? {
        return sharedPreferences.getString("message", null)
    }

    // Clear session data
    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
