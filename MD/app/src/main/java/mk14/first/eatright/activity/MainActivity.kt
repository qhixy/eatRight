package mk14.first.eatright.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import mk14.first.eatright.R
import mk14.first.eatright.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val motionLayout = findViewById<MotionLayout>(R.id.main)
        val btnSelanjutnya = findViewById<Button>(R.id.btn_selanjutnya)
        val btndaftar = findViewById<Button>(R.id.btn_masuk)
        val txtlewat = findViewById<TextView>(R.id.txtlewat)
        val txtmasuk = findViewById<TextView>(R.id.txtmasuk)
        val main1 :ImageFilterView = findViewById(R.id.menu1)
        val main2 :ImageFilterView = findViewById(R.id.menu2)
        val main3 :ImageFilterView = findViewById(R.id.menu3)


        btnSelanjutnya.setOnClickListener {
            when (motionLayout.currentState) {
                R.id.end -> motionLayout.transitionToState(R.id.menu2)
                R.id.menu2 -> motionLayout.transitionToState(R.id.menu3)
            }
        }

        btndaftar.setOnClickListener{
            Toast.makeText(this, "Navigate to Sign-Up", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BioActivity::class.java)
            startActivity(intent)
        }

        txtmasuk.setOnClickListener {
            Toast.makeText(this, "Navigate to Log-In", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        main1.setOnClickListener{
            when(motionLayout.currentState){
                R.id.end -> motionLayout.transitionToState(R.id.end)
                R.id.menu2 -> motionLayout.transitionToState(R.id.end)
                R.id.menu3 -> motionLayout.transitionToState(R.id.end)
            }
        }

        main2.setOnClickListener{
            when(motionLayout.currentState){
                R.id.end -> motionLayout.transitionToState(R.id.menu2)
                R.id.menu2 -> motionLayout.transitionToState(R.id.menu2)
                R.id.menu3 -> motionLayout.transitionToState(R.id.menu2)
            }
        }

        main3.setOnClickListener{
            when(motionLayout.currentState){
                R.id.end -> motionLayout.transitionToState(R.id.menu3)
                R.id.menu2 -> motionLayout.transitionToState(R.id.menu3)
                R.id.menu3 -> motionLayout.transitionToState(R.id.menu3)
            }
        }

        txtlewat.setOnClickListener{
            when(motionLayout.currentState){
                R.id.end -> motionLayout.transitionToState(R.id.menu3)
                R.id.menu2 -> motionLayout.transitionToState(R.id.menu3)
            }
        }

        // Membuat Notification Channel jika Android 8.0 ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel for reminder notifications"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

    }

}