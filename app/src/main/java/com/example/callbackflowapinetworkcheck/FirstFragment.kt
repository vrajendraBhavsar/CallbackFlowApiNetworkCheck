package com.example.callbackflowapinetworkcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.callbackflowapinetworkcheck.databinding.FragmentFirstBinding
import com.example.callbackflowapinetworkcheck.util.checkNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@ExperimentalCoroutinesApi
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        lifecycleScope.launchWhenStarted {
            requireContext().checkNetwork().collect { networkStatusBoolean ->
                val check = when (networkStatusBoolean) {
                    true -> {
//                        binding.tvNetworkStatus.setTextColor(
//                            ContextCompat.getColor(
//                                requireContext(),
//                                R.color.green
//                            )
//                        )
                        "Connected with Internet"
                    }
                    false -> {
//                        binding.tvNetworkStatus.setTextColor(
//                            ContextCompat.getColor(
//                                requireContext(),
//                                R.color.red
//                            )
//                        )
                        "Not connected"
                    }
                }
                binding.tvNetworkStatus.text = check
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}