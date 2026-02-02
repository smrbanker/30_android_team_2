package ru.practicum.android.diploma.ui.search.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyState
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.favorites.fragment.FavouritesAdapter
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var _adapter: FavouritesAdapter? = null
    private val adapter get() = _adapter!!
    private var _vacancyList: MutableList<Vacancy>? = null
    private val vacancyList get() = _vacancyList!!

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _vacancyList = mutableListOf()
        _adapter = FavouritesAdapter(vacancyList) { vacancy ->
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment,
                VacancyFragment.createArgsId(vacancy.id))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _vacancyList = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        configureRecyclerView()

        viewModel.observeVacancy().observe(viewLifecycleOwner) { vacancyState ->
            render(vacancyState)
        }

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

        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filtrationSettingsFragment)
        }
    }

    private fun configureRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
    }

    private fun render(state: VacancyState) {
        when (state) {
            is VacancyState.Loading -> showLoading()
            is VacancyState.Empty -> showEmpty()
            is VacancyState.Content -> showContent(state.vacanciesList, state.itemsFound)
            // is VacancyState.Error -> showError(state.errorMessage)
            else -> return // Тут будет обработка ошибок от Екатерины
        }
    }

    private fun showLoading() {
        binding.apply {
            searchResultCount.isVisible = false
            recyclerView.isVisible = false
            placeholder.isVisible = false
            startImage.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholderImage.setImageResource(R.drawable.image_wrong_query_placeholder)
            placeholderText.text = requireContext()
                .resources.getString(R.string.cannot_get_vacancies_list)

            searchResultCount.isVisible = false
            recyclerView.isVisible = false
            placeholder.isVisible = true
            startImage.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showContent(vacanciesList: List<Vacancy>, itemsFound: Int) {
        vacancyList.clear()
        vacancyList.addAll(vacanciesList)
        adapter.notifyDataSetChanged()
        binding.apply {
            searchResultCount.text = requireContext()
                .resources.getQuantityString(R.plurals.vacancies_found, itemsFound, itemsFound)
            searchResultCount.isVisible = true
            recyclerView.isVisible = true
            placeholder.isVisible = false
            startImage.isVisible = false
            progressBar.isVisible = false
        }
    }

    /*private fun showError(errorMessage: String) {
        setPlaceholder(errorMessage)
        binding.apply {
            searchResultCount.isVisible = false
            recyclerView.isVisible = false
            placeholder.isVisible = true
            startImage.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun setPlaceholder(errorMessage: String) {
        when (errorMessage) {
            Resource.CONNECTION_PROBLEM -> {
                binding.placeholderImage.setImageResource(R.drawable.image_no_internet_placeholder)
                binding.placeholderText.text = requireContext().resources.getString(R.string.no_internet)
            }
            Resource.SERVER_ERROR -> {
                binding.placeholderImage.setImageResource(R.drawable.image_server_error_placeholder)
                binding.placeholderText.text = requireContext().resources.getString(R.string.no_internet)
            }
        }
    }*/
}
