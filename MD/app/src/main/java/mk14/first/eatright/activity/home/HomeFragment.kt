package mk14.first.eatright.activity.home

//import androidx.navigation.fragment.findNavController


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import mk14.first.eatright.R
import mk14.first.eatright.activity.ScanActivity
import mk14.first.eatright.databinding.FragmentHomeBinding
import mk14.first.eatright.utils.UserSessionManager

class HomeFragment : Fragment() {

    private lateinit var userSessionManager: UserSessionManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.reminder.setOnClickListener {
            // Pindah ke ReminderPageFragment
            findNavController().navigate(R.id.action_homeFragment_to_reminderPageFragment)
        }
        // Menambahkan OnClickListener untuk layoutatas
        binding.layoutatas.setOnClickListener {
            // Menggunakan NavController untuk berpindah ke ProfileFragment
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        binding.barcodescan.setOnClickListener {
            val intent = Intent(requireContext(), ScanActivity::class.java)
            startActivity(intent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
