package mk14.first.eatright.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginUser(
    val email: String,
    val password: String
)

data class RegisterUser(
    val username : String,
    val email: String,
    val password: String
)
data class RegisterUserResponse(
    val success: Boolean,
    val message: String
)

data class UserLifestyle(
    val user: User,
    val lifestyle: List<Any>
)

data class User(
    val id: Int,
    val username: String,
    val email: String
)

data class UserResponse(
    val success: Boolean,
    val message: String,
)

data class TestDB(
    val check: Boolean
)

data class ApiResponse(
    val barcodes: List<Barcode>
)

data class Barcode(
    val type: String,
    val data: String,
    val product_name: String,
    val brand: String,
    val nutritional_info: NutritionalInfo
)

data class NutritionalInfo(
    val calories: String,
    val fat: Int,
    val carbohydrates: Int,
    val protein: Int,
    val sugar: Int
)

data class DBResponse(
    val checky : Boolean
)

data class PredictRequest(
    val Age: Int,
    val Gender: String,
    val Height: Int,
    val Weight: Int,
    val CALC: String,
    val FAVC: String,
    val FCVC: Int,
    val NCP: Int,
    val SCC: String,
    val SMOKE: String,
    val CH2O: Int,
    val family_history_with_overweight: String,
    val FAF: Int,
    val TUE: Int,
    val CAEC: String,
    val MTRANS: String
)

data class PredictResponse(
    val prediction: String
)

data class ReminderRequest(
    val reminderTime: String
)

interface ApiService{

    @GET("user_database/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): Response<UserLifestyle>

    @POST("predict_obesity/{user_id}")
    suspend fun predictObesity(
        @Path("user_id") userId: Int,
        @Body predictionData: PredictRequest
    ): Response<PredictResponse>

    @Headers("Content-Type: application/json", "accept: application/json")
    @POST("user/login")
    fun loginUser(
        @Body loginUser: LoginUser
    ): Call<UserResponse>

    @Headers("Content-Type: application/json", "accept: application/json")
    @POST("user/register")
    suspend fun registerUser(
        @Body request: RegisterUser
    ): Response<RegisterUserResponse>

    @POST("barcode/")
    fun getProductDetails(@Query("barcode") barcode: String ): Call<ApiResponse>

    @Multipart
    @POST("barcode/")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ApiResponse>

    @GET("ingredients/test-db-connection")
    suspend fun checking(
        @Body request : TestDB
    ): Response<DBResponse>

    @POST("setReminder")  // Ganti dengan endpoint API yang sesuai
    fun setReminder(@Body reminderRequest: ReminderRequest): Call<Void>
}