package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.presentation.search.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: VacancyAdapter

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

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setupRecyclerView()
        setupSearchInput()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = VacancyAdapter(emptyList())
        binding.vacanciesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.vacanciesRecyclerView.adapter = adapter

        binding.vacanciesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = adapter.itemCount

                    if (lastVisibleItemPosition >= totalItemCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = if (s.isNullOrBlank()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.searchVacancies(s?.toString() ?: "")
            }
        })

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Initial -> {
                    showInitialState()
                }
                is SearchState.Loading -> {
                    showLoadingState()
                }
                is SearchState.Content -> {
                    showContentState(state.vacancies, state.found)
                }
                is SearchState.Error -> {
                    showErrorState(state.message)
                }
                is SearchState.Empty -> {
                    showEmptyState()
                }
            }
        }

        viewModel.isLoadingNextPage.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showInitialState() {
        binding.vacanciesRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.foundTextView.visibility = View.GONE
    }

    private fun showLoadingState() {
        binding.vacanciesRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.foundTextView.visibility = View.GONE
    }

    private fun showContentState(vacancies: List<Vacancy>, found: Int) {
        binding.vacanciesRecyclerView.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.emptyLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.foundTextView.visibility = View.VISIBLE
        binding.foundTextView.text = getString(R.string.found_vacancies, formatNumber(found))
        adapter.updateItems(vacancies)
    }

    private fun showErrorState(message: String) {
        binding.vacanciesRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.emptyLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.foundTextView.visibility = View.GONE
        binding.errorTextView.text = message
        
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyState() {
        binding.vacanciesRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.foundTextView.visibility = View.GONE
    }

    private fun formatNumber(number: Int): String {
        return number.toString().reversed().chunked(3).joinToString(" ").reversed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

