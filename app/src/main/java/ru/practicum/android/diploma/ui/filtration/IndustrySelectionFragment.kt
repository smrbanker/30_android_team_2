package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustrySelectionBinding
import ru.practicum.android.diploma.domain.models.IndustryState

class IndustrySelectionFragment : Fragment() {
    private var _binding: FragmentIndustrySelectionBinding? = null
    private val binding get() = _binding!!
    private var _adapter: IndustryAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel by viewModel<IndustrySelectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        configureRecyclerView()
        observeViewModel()

        viewModel.loadIndustries()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configureRecyclerView() {
        _adapter = IndustryAdapter(emptyList()) { industry ->
            // TODO: Сохранить выбранную индустрию и вернуться назад
            findNavController().popBackStack()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observeViewModel() {
        viewModel.observeIndustries().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: IndustryState) {
        when (state) {
            is IndustryState.Loading -> {
                showLoading()
            }
            is IndustryState.Content -> {
                showContent(state.industries)
            }
            is IndustryState.Empty -> {
                showEmpty()
            }
            is IndustryState.Error -> {
                showError(state.message)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            recyclerView.isVisible = false
            progressBar.isVisible = true
            placeholder.isVisible = false
        }
    }

    private fun showContent(industries: List<ru.practicum.android.diploma.domain.models.Industry>) {
        binding.apply {
            recyclerView.isVisible = true
            progressBar.isVisible = false
            placeholder.isVisible = false
        }
        adapter.updateList(industries)
    }

    private fun showEmpty() {
        binding.apply {
            recyclerView.isVisible = false
            progressBar.isVisible = false
            placeholder.isVisible = true
            placeholderImage.setImageResource(R.drawable.image_wrong_query_placeholder)
            placeholderText.text = requireContext().resources.getString(R.string.cannot_get_vacancies_list)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            recyclerView.isVisible = false
            progressBar.isVisible = false
            placeholder.isVisible = true
            when (message) {
                ru.practicum.android.diploma.domain.models.Resource.CONNECTION_PROBLEM -> {
                    placeholderImage.setImageResource(R.drawable.image_no_internet_placeholder)
                    placeholderText.text = requireContext().resources.getString(R.string.no_internet)
                }
                ru.practicum.android.diploma.domain.models.Resource.SERVER_ERROR -> {
                    placeholderImage.setImageResource(R.drawable.image_server_error_placeholder)
                    placeholderText.text = requireContext().resources.getString(R.string.no_internet)
                }
                else -> {
                    placeholderText.text = message
                }
            }
        }
    }

    companion object {
        fun newInstance() = IndustrySelectionFragment()
    }
}
