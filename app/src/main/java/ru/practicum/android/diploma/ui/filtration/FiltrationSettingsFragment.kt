package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFiltrationSettingsBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location
import kotlin.getValue

class FiltrationSettingsFragment : Fragment() {
    private var _binding: FragmentFiltrationSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFiltrationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val f = Filter( // ТЕСТОВЫЙ РУЧНОЙ ФИЛЬТР
            location = Location(null, null),
            sector = null,
            salary = 5000,
            onlyWithSalary = true
        )

        val viewModel by viewModel<FiltrationSettingsViewModel>()

        viewModel.saveFilter(f)
        viewModel.showFilter()
        viewModel.clearFilter()

        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // companion object {
    //      fun newInstance() = FiltrationSettingsFragment()
    //  }
}
