package com.example.callbackflowapinetworkcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.callbackflowapinetworkcheck.databinding.FragmentFirstBinding
import com.example.callbackflowapinetworkcheck.util.checkEtTextChange
import com.example.callbackflowapinetworkcheck.util.checkNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@FlowPreview
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
                        "Connected with Internet"
                    }
                    false -> {
                        "Not connected"
                    }
                }
                binding.tvNetworkStatus.text = check
            }
        }

        lifecycleScope.launchWhenStarted {
            binding.etTextChange.checkEtTextChange()
                .debounce(500L) //500 millis ma jetlu text type thashe, eeetlo text add thashe
                .collect { newText->
                binding.tvNetworkStatus.text = "${binding.tvNetworkStatus.text} $newText"
                    Log.d("VRAJTEST", "onViewCreated: $newText")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}