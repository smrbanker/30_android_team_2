package ru.practicum.android.diploma.ui.filtration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
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

        viewModel.observeCountry().observe(viewLifecycleOwner) { country ->
            binding.countryEditText.setText(country)
            saveButtonEnableState(country)
            clearCountryButtonState(country)
        }
        viewModel.observeRegion().observe(viewLifecycleOwner) { region ->
            binding.regionEditText.setText(region)
            clearRegionButtonState(region)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            getViewLifecycleOwner(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.clearCountryAndRegion()
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun setupUi() {
        viewModel.fetchCountry()
        viewModel.fetchRegion()
    }

    private fun initListeners() {
        binding.countryEditText.setOnClickListener {
            findNavController().navigate(R.id.action_filtrationWorkplaceFragment_to_filtrationCountryFragment)
        }
        binding.regionEditText.setOnClickListener {
            findNavController().navigate(
                R.id.action_filtrationWorkplaceFragment_to_filtrationRegionFragment,
                FiltrationRegionFragment.createArgsCountry(binding.countryEditText.text.toString())
            )
        }
        binding.toolbar.setNavigationOnClickListener {
            viewModel.clearCountryAndRegion()
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
        binding.chooseButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.stateCountryButton.setOnClickListener {
            viewModel.clearCountryAndRegion()
        }
        binding.stateRegionButton.setOnClickListener {
            viewModel.clearRegion()
        }
    }

    private fun saveButtonEnableState(country: String?) {
        binding.chooseButton.isVisible = if (country.isNullOrEmpty()) false else true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clearCountryButtonState(country: String?) {
        if (country.isNullOrEmpty()) {
            binding.stateCountryButton.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_arrow_right)
            )
            binding.stateCountryButton.isEnabled = false
        } else {
            binding.stateCountryButton.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_clear)
            )
            binding.stateCountryButton.isEnabled = true
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clearRegionButtonState(country: String?) {
        if (country.isNullOrEmpty()) {
            binding.stateRegionButton.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_arrow_right)
            )
            binding.stateRegionButton.isEnabled = false
        } else {
            binding.stateRegionButton.setImageDrawable(
                requireContext().getDrawable(R.drawable.ic_clear)
            )
            binding.stateRegionButton.isEnabled = true
        }
    }
}
