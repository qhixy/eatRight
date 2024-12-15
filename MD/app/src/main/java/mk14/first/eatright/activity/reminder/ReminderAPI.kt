package mk14.first.eatright.activity.reminder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class ReminderRequest(
    val reminderTime: String
)

interface ReminderApi {
    @POST("setReminder")  // Ganti dengan endpoint API yang sesuai
    fun setReminder(@Body reminderRequest: ReminderRequest): Call<Void>
}
