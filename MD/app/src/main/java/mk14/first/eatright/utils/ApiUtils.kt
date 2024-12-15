package mk14.first.eatright.utils

import mk14.first.eatright.api.ApiResponse
import mk14.first.eatright.api.ApiService
import mk14.first.eatright.retrofit.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object ApiUtils {

    private val apiService: ApiService = RetrofitClient.apiService
    fun uploadImageFile(
        imageFile: File,
        onSuccess: (ApiResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        // Create a RequestBody from the image file
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val multipartBody = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)

        // Make the API call
        apiService.uploadImage(multipartBody).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body())  // Pass the response body on success
                } else {
                    onError("API error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Pass the error message in case of network failure
                onError("Network error: ${t.message}")
            }
        })
    }
}

