package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
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
            location = Location(null, null), // Region(2, "Москва", 1)),
            sector = null,
            salary = null,
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
        showRegion(filter)
        showIndustry(filter)
        showSalary(filter)
        binding.checkbox.isChecked = filter.onlyWithSalary
        showButtons(filter)
    }

    private fun showRegion(filter: Filter) {
        if ((filter.location.country != null) && (filter.location.region != null)) {
            val workplace = filter.location.country.name + ", " + filter.location.region.name
            binding.apply {
                workplaceEdit.setText(workplace)
                workplaceLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
                workplaceArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
            }
        } else {
            if (filter.location.country != null) {
                val workplace = filter.location.country.name
                binding.apply {
                    workplaceEdit.setText(workplace)
                    workplaceLayout.defaultHintTextColor =
                        ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
                    workplaceArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
                }
            } else {
                if (filter.location.region != null) {
                    val workplace = filter.location.region.name
                    binding.apply {
                        workplaceEdit.setText(workplace)
                        workplaceLayout.defaultHintTextColor =
                            ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
                        workplaceArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
                    }
                }
            }
        }
        if ((filter.location.country == null) && (filter.location.region == null)) {
            binding.apply {
                workplaceEdit.text?.clear()
                workplaceLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
                workplaceArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_right_filter))
            }
        }
    }

    private fun showIndustry(filter: Filter) {
        if (filter.sector != null) {
            val industry = filter.sector.name
            binding.apply {
                industryEdit.setText(industry)
                industryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.black_to_white)
                industryArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
            }
        } else {
            binding.apply {
                industryEdit.text?.clear()
                industryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
                industryArrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_right_filter))
            }
        }
    }

    private fun showSalary(filter: Filter) {
        if (filter.salary != null) {
            binding.apply {
                salaryEdit.setText(filter.salary.toString())
                salaryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.blue)
                clear.isVisible = true
            }
        } else {
            binding.apply {
                salaryEdit.text?.clear()
                salaryLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
                clear.isVisible = false
            }
        }
    }

    private fun showButtons(filter: Filter) {
        var flag = false
        if (filter.location.country != null || filter.location.region != null) {
            binding.apply {
                applyButton.isVisible = true
                resetButton.isVisible = true
            }
            flag = true
        }
        if (filter.sector != null || filter.salary != null) {
            binding.apply {
                applyButton.isVisible = true
                resetButton.isVisible = true
            }
            flag = true
        }
        if (filter.onlyWithSalary) {
            binding.apply {
                applyButton.isVisible = true
                resetButton.isVisible = true
            }
            flag = true
        }

        if (!flag) {
            binding.apply {
                applyButton.isVisible = false
                resetButton.isVisible = false
            }
        }
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
