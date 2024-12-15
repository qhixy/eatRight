package mk14.first.eatright.activity.reminder

import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import mk14.first.eatright.R
import mk14.first.eatright.activity.MainActivity
import mk14.first.eatright.databinding.FragmentReminderPageBinding


import java.text.SimpleDateFormat
import java.util.*

class ReminderPageFragment : Fragment() {

    private var _binding: FragmentReminderPageBinding? = null
    private val binding get() = _binding!!
    private var reminderTime: String? = null // Variabel untuk menyimpan waktu reminder
    private var isReminderActive = false // Flag untuk menandakan apakah reminder aktif

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReminderPageBinding.inflate(inflater, container, false)

        // Tampilkan dialog time picker untuk memilih waktu reminder
        binding.btnSetReminder.setOnClickListener {
            showTimePickerDialog()
        }

        // Set reminder ketika tombol submit di klik
        binding.btnSubmitReminder.setOnClickListener {
            if (reminderTime != null && reminderTime != "0") {  // Cek jika reminderTime valid
                // Menyimpan waktu reminder dan mengecek waktu secara periodik
                isReminderActive = true // Mengaktifkan reminder
                checkReminderTime(requireContext(), reminderTime!!)
            } else {
                // Memberi tahu pengguna bahwa waktu belum dipilih
                showErrorDialog("Please set a reminder time first.")
            }
        }

        return binding.root
    }

    // Fungsi untuk memeriksa waktu saat ini dengan waktu yang dipilih
    fun checkReminderTime(context: Context, reminderTime: String) {
        val handler = Handler(Looper.getMainLooper()) // Pastikan Handler berjalan di main thread
        val runnable = object : Runnable {
            override fun run() {
                try {
                    // Ambil waktu saat ini
                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                    // Jika waktu saat ini sama dengan waktu reminder yang ditentukan dan reminder aktif
                    if (currentTime == reminderTime && isReminderActive) {
                        showNotification(context, reminderTime)
                        showReminderDialog(context)
                        isReminderActive = false // Nonaktifkan reminder setelah dialog ditampilkan
                    }
                } catch (e: Exception) {
                    e.printStackTrace() // Pastikan jika terjadi kesalahan, tidak menyebabkan crash
                }

                // Cek waktu lagi dalam 1 detik jika reminder masih aktif
                if (isReminderActive) {
                    handler.postDelayed(this, 1000)
                }
            }
        }

        // Mulai pengecekan
        handler.post(runnable)
    }

    // Fungsi untuk menampilkan notifikasi
    private fun showNotification(context: Context, reminderTime: String) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = 1

            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notifications_none_24)
                .setContentTitle("Reminder Time!")
                .setContentText("Reminder: $reminderTime")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fungsi untuk menampilkan alert dialog ketika waktu reminder tercapai
    private fun showReminderDialog(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Reminder!")
            builder.setMessage("It's time for your reminder: $reminderTime")

            // Menambahkan aksi untuk tombol "OK"
            builder.setPositiveButton("OK") { dialog, _ ->
                // Menutup dialog ketika tombol "OK" diklik
                reminderTime = "0" // Reset reminderTime ke "0" agar dialog tidak muncul lagi
                binding.tvReminderTime.text = "Reminder not set" // Mengubah tampilan text menjadi "Reminder not set"
                dialog.dismiss()
            }

            builder.show()

        } catch (e: Exception) {
            e.printStackTrace() // Cegah aplikasi keluar karena kesalahan saat menunjukkan dialog
        }
    }

    // Fungsi untuk menampilkan TimePickerDialog dan menyimpan waktu yang dipilih
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = android.app.TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                reminderTime = String.format("%02d:%02d", selectedHour, selectedMinute) // Set reminderTime
                binding.tvReminderTime.text = "Reminder set for: $reminderTime"
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    // Fungsi untuk menampilkan error dialog jika waktu belum dipilih
    private fun showErrorDialog(message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
