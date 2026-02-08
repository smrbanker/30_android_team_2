package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFiltrationCountryBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.FiltrationCountryViewModel
import ru.practicum.android.diploma.ui.filtration.models.AreaAdapter

class FiltrationCountryFragment : Fragment() {
    private var _binding: FragmentFiltrationCountryBinding? = null
    private val binding get() = _binding!!
    private var _adapter: AreaAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel by viewModel<FiltrationCountryViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFiltrationCountryBinding.inflate(inflater, container, false)
        _adapter = AreaAdapter { area ->
            viewModel.saveCountry(area as Country)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.observeCountries().observe(viewLifecycleOwner) {
            if (it.first != null) {
                showContent(it.first!!)
            } else {
                showError(it.second!!)
            }
        }
    }

    private fun showContent(countries: List<Country>) {
        adapter.updateAreas(countries)
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            recyclerView.isVisible = false
            placeholderText.text = errorMessage
            placeholder.isVisible = true
        }
    }

    companion object {
        fun newInstance() = FiltrationCountryFragment()
    }
}
