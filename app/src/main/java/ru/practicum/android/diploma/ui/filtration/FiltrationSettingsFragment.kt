package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFiltrationSettingsBinding
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Location
import ru.practicum.android.diploma.ui.filtration.FiltrationSettingsViewModel.Companion.SP_ERROR_CLEAR
import ru.practicum.android.diploma.ui.filtration.FiltrationSettingsViewModel.Companion.SP_ERROR_INPUT
import ru.practicum.android.diploma.ui.filtration.FiltrationSettingsViewModel.Companion.SP_ERROR_OUTPUT
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

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.saveFilter(f)
        viewModel.showFilter()

        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

    }

    private fun render(state: FiltrationSettingsState) {
        when (state) {
            is FiltrationSettingsState.Content -> showContent(state.filter)
            is FiltrationSettingsState.Error -> showError(state.message)
        }
    }

    private fun showContent(filter: Filter) {
        binding.textInputEditText.setText(filter.salary.toString())
        // ЗАГРУЗКА ДАННЫХ В СВОБОДНЫЕ ПОЛЯ
    }

    private fun showError(errorMessage: String) {
        when (errorMessage) {
            SP_ERROR_INPUT -> Toast.makeText(
                requireContext(),
                SP_ERROR_INPUT,
                Toast.LENGTH_SHORT
            )
                .show()

            SP_ERROR_OUTPUT -> Toast.makeText(
                requireContext(),
                SP_ERROR_OUTPUT,
                Toast.LENGTH_SHORT
            )
                .show()

            SP_ERROR_CLEAR -> Toast.makeText(
                requireContext(),
                SP_ERROR_CLEAR,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // companion object {
    //      fun newInstance() = FiltrationSettingsFragment()
    //  }
}
