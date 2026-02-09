package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationRegionBinding
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.RegionState
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.FiltrationRegionViewModel
import ru.practicum.android.diploma.ui.filtration.models.AreaAdapter
import kotlin.text.isNullOrEmpty

class FiltrationRegionFragment : Fragment() {
    private var _binding: FragmentFiltrationRegionBinding? = null
    private val binding get() = _binding!!
    private var _adapter: AreaAdapter? = null
    private val adapter get() = _adapter!!
    private val viewModel by viewModel<FiltrationRegionViewModel>()
    private var country: String? = null
    private var countryWasSelected = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFiltrationRegionBinding.inflate(inflater, container, false)
        _adapter = AreaAdapter { area ->
            viewModel.saveRegion(area as Region, countryWasSelected)
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeRegions().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        // region Получение информции с другого экрана
        country = getArgsCountry()
        countryWasSelected = checkCountryState()
        // endregion

        initListeners()
        configureRecyclerView()
        checkCountryAndSearchRegions(country!!)

    }

    private fun initListeners() {
        binding.editText.setOnClickListener { asd ->
            asd.requestFocus()
        }

        binding.editText.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ -> },
            onTextChanged = { text, _, _, _ ->
                binding.clearTextButton.isVisible = !text.isNullOrEmpty()
                binding.iconLens.isVisible = text.isNullOrEmpty()

                if (text?.isNotEmpty() == true) {
                    viewModel.searchDebounce(text.toString())
                }
            },
            afterTextChanged = { text: Editable? -> }
        )

        binding.clearTextButton.setOnClickListener {
            binding.editText.text.clear()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun configureRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
        country = null
    }

    private fun render(state: RegionState) {
        when (state) {
            is RegionState.Loading -> showLoading()
            is RegionState.Content -> showContent(state.regionList)
            is RegionState.Error -> showError(state.errorMessage)
            is RegionState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.recyclerView.isVisible = false
        binding.placeholder.isVisible = false
    }

    private fun showContent(regions: List<Region>) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        binding.placeholder.isVisible = false
        adapter.updateAreas(regions)
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.placeholder.isVisible = true
        setPlaceholder(errorMessage)
    }

    private fun showEmpty() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = false
        binding.placeholder.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.image_wrong_query_placeholder)
        binding.placeholderText.text = requireContext().resources.getString(R.string.region_is_not_found)
    }

    private fun setPlaceholder(errorMessage: String) {
        when (errorMessage) {
            Resource.CONNECTION_PROBLEM -> {
                binding.placeholderImage.setImageResource(R.drawable.image_nothing_found)
                binding.placeholderText.text = requireContext().resources.getString(R.string.failed_getting_list)
            }
            Resource.SERVER_ERROR -> {
                binding.placeholderImage.setImageResource(R.drawable.image_nothing_found)
                binding.placeholderText.text = requireContext().resources.getString(R.string.failed_getting_list)
            }
        }
    }

    private fun getArgsCountry(): String? {
        val country = arguments?.getString(COUNTRY_KEY)
        return country
    }

    private fun checkCountryAndSearchRegions(countryName: String) {
        if (!countryName.isNullOrEmpty()) {
            viewModel.searchCountryRegions(countryName)
        } else {
            viewModel.searchAllRegions()
        }
    }

    private fun checkCountryState(): Boolean {
        return if (country.isNullOrEmpty()) {
            false
        } else {
            true
        }
    }

    companion object {
        const val COUNTRY_KEY = "COUNTRY_KEY"
        fun createArgsCountry(country: String): Bundle = bundleOf(COUNTRY_KEY to country)
    }
}
