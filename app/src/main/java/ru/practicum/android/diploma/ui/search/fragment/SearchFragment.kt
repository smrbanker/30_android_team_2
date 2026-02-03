package ru.practicum.android.diploma.ui.search.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.ui.search.SearchState
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.search.VacancyAdapter
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: VacancyAdapter

    private var debouncedSearch: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        setupObservers()

        binding.clearTextButton.setOnClickListener {
            binding.editText.text?.clear()
        }
    }

    private fun setupRecyclerView() {
        adapter = VacancyAdapter { vacancyId ->
            // TODO: Navigate to vacancy details
        }

        binding.vacanciesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.vacanciesRecyclerView.adapter = adapter

        binding.vacanciesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisiblePosition >= totalItemCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun setupSearchInput() {
        debouncedSearch = debounce(
            delayMillis = 2000L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query: String ->
            viewModel.search(query)
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                binding.clearTextButton.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE

                if (query.isNotEmpty()) {
                    debouncedSearch?.invoke(query)
                } else {
                    viewModel.search("")
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Default -> {
                    showDefaultState()
                }

                is SearchState.Loading -> {
                    showLoadingState()
                }

                is SearchState.Content -> {
                    showContentState(state.vacancies, state.found, state.isLoadingMore)
                }

                is SearchState.Error -> {
                    showErrorState(state.message)
                }

                is SearchState.Empty -> {
                    showEmptyState()
                }
            }
        }
    }

    private fun showDefaultState() {
        binding.startImage.isVisible = true
        binding.progressBar.isVisible = false
        binding.vacanciesRecyclerView.isVisible = false
        binding.placeholder.isVisible = false
        binding.searchResultCount.isVisible = false
        binding.loadingMoreProgress.isVisible = false
    }

    private fun showLoadingState() {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = true
        binding.vacanciesRecyclerView.isVisible = false
        binding.placeholder.isVisible = false
        binding.searchResultCount.isVisible = false
        binding.loadingMoreProgress.isVisible = false
    }

    private fun showContentState(vacancies: List<ru.practicum.android.diploma.domain.models.Vacancy>, found: Int, isLoadingMore: Boolean) {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = false
        binding.vacanciesRecyclerView.isVisible = true
        binding.placeholder.isVisible = false
        binding.searchResultCount.isVisible = true
        binding.loadingMoreProgress.isVisible = isLoadingMore

        adapter.submitList(vacancies)
        binding.searchResultCount.text = getString(R.string.found_vacancies, found)
    }

    private fun showErrorState(message: String) {
        val currentState = viewModel.state.value
        if (currentState is SearchState.Content) {
            // Если есть контент, показываем Toast и возвращаемся к контенту
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            binding.loadingMoreProgress.isVisible = false
            showContentState(currentState.vacancies, currentState.found, false)
        } else {
            // Если это первая страница, показываем placeholder
            binding.startImage.isVisible = false
            binding.progressBar.isVisible = false
            binding.vacanciesRecyclerView.isVisible = false
            binding.placeholder.isVisible = true
            binding.searchResultCount.isVisible = false
            binding.loadingMoreProgress.isVisible = false

            binding.placeholderText.text = message
        }
    }

    private fun showEmptyState() {
        binding.startImage.isVisible = false
        binding.progressBar.isVisible = false
        binding.vacanciesRecyclerView.isVisible = false
        binding.placeholder.isVisible = true
        binding.searchResultCount.isVisible = false
        binding.loadingMoreProgress.isVisible = false

        binding.placeholderText.text = getString(R.string.nothing_found)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
