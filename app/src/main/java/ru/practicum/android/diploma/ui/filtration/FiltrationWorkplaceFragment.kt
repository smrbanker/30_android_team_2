package ru.practicum.android.diploma.ui.filtration

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.xmlpull.v1.XmlPullParserFactory
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationWorkplaceBinding
import ru.practicum.android.diploma.presentation.FiltrationWorkplaceViewModel

class FiltrationWorkplaceFragment : Fragment() {
    private var _binding: FragmentFiltrationWorkplaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FiltrationWorkplaceViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFiltrationWorkplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        initListeners()

        viewModel.observeCountry().observe(viewLifecycleOwner) { country->
            binding.countryEditText.setText(country)
        }
        viewModel.observeRegion().observe(viewLifecycleOwner) { region->
            binding.regionEditText.setText(region)
        }
    }

    private fun setupUi() {
        viewModel.fetchCountry()
        viewModel.fetchRegion()
    }

    private fun initListeners() {
        binding.countryEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtrationWorkplaceFragment_to_filtrationCountryFragment)
        }

        binding.regionLayout.setOnClickListener {
            // Переход на экран с выбором региона
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryEditText.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.countryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
            }
        }

        binding.regionEditText.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.countryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
            }
        }
    }
}
