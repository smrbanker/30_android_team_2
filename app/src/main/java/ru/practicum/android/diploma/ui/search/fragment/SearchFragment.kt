package ru.practicum.android.diploma.ui.search.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Resource
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
    private var filters = false

    private var isLoading = false

    private val viewModel by activityViewModel<SearchViewModel>()

    private var toastJob: Job? = null

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
        setupScrollListener()

        // Эта штука предназначена для показа поисковой выдачи после возвращения на экран поиска
        // с экрана избранного и ему подобных.
        // Так как эта штука стоит первой в обзёрверах, то сначала покажутся все вакансии со "склада"
        // а потом добавятся вакансии из "кусочка".
        // Я не знаю как это обойти. Если передвинуть обзервер в конец всех обзерверов, то
        // тогда мы вернемся к старой реализации, а именно - по возвращении на экран поиска
        // нам будут показан только последний кусочек.
        // То есть, если мы проскроллили 10 страниц, то будет отображаться
        // только 10-я страница, а не все 10 страниц
        viewModel.observeState().observe(viewLifecycleOwner) {
            if (vacancyList.isEmpty()) {
                viewModel.clearLastSearchResult()
                showContent(it.first, it.second)
            }
        }

        // Здесь мы получаем "кусочек" поискового запроса, он всегда состоит из 20 вакансий,
        // несмотря на то, какой это запрос - основной или после скролла
        viewModel.observeVacancy().observe(viewLifecycleOwner) { vacancyState ->
            Log.d("ASD", "vacancy observer, state: ${vacancyState?.javaClass}")
            if (vacancyState != null) render(vacancyState)
        }
        viewModel.observeInput().observe(viewLifecycleOwner) {
            if (binding.editText.text.toString() != it) {
                binding.editText.setText(it)
            }
        }

        if (viewModel.checkFilterButton()) {
            binding.filterButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_filter_on))
        } else {
            binding.filterButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_filter))
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

        setFragmentResultListener(IS_RUN) { _, bundle ->
            filters = bundle.getBoolean(IS_RUN)
            if (filters && binding.editText.text.isNotEmpty()) {
                viewModel.searchAnyway(binding.editText.text.toString())
            }
        }
    }

    private fun configureRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
    }

    private fun setupScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    isLoading = true
                    viewModel.loadMoreVacancies(binding.editText.text.toString())
                }
            }
        })
    }

    private fun render(state: VacancyState) {
        Log.d("ASD", "render block")
        when (state) {
            is VacancyState.Loading -> showLoading(state.flag)
            is VacancyState.Empty -> showEmpty()
            is VacancyState.Content -> showContent(state.vacanciesList, state.itemsFound)
            is VacancyState.Error -> showError(state.errorMessage)
        }
    }

    private fun showLoading(flag: Boolean) {
        if (!flag) {
            isLoading = true
            binding.apply {
                searchResultCount.isVisible = false
                recyclerView.isVisible = false
                placeholder.isVisible = false
                startImage.isVisible = false
                progressBar.isVisible = true
                progressBarAdd.isVisible = false
                placeholderAdd.isVisible = false
                placeholderImageAdd.isVisible = false
                vacancyList.clear()
            }
        } else {
            isLoading = true
            binding.apply {
                progressBar.isVisible = false
                progressBarAdd.isVisible = true
                placeholderAdd.isVisible = true
                placeholderImageAdd.isVisible = true
                placeholderImageAdd.setImageDrawable(requireContext().getDrawable(R.drawable.color_white))
            }
        }
    }

    private fun showEmpty() {
        if (vacancyList.isEmpty()) {
            isLoading = false
            binding.apply {
                placeholderImage.setImageResource(R.drawable.image_wrong_query_placeholder)
                placeholderText.text = requireContext()
                    .resources.getString(R.string.cannot_get_vacancies_list)
                searchResultCount.isVisible = false
                recyclerView.isVisible = false
                placeholder.isVisible = true
                startImage.isVisible = false
                progressBar.isVisible = false
                progressBarAdd.isVisible = false
                placeholderAdd.isVisible = false
                placeholderImageAdd.isVisible = false
            }
        } else {
            binding.apply {
                // recyclerView.isVisible = true
                // adapter.notifyDataSetChanged()
                placeholder.isVisible = false // true
                startImage.isVisible = false
                progressBar.isVisible = false
                progressBarAdd.isVisible = false
                placeholderAdd.isVisible = false
                placeholderImageAdd.isVisible = false
            }
        }
    }

    private fun showContent(vacanciesList: List<Vacancy>, itemsFound: Int) {
        Log.d("ASD", "vacanciesList size: ${vacanciesList.size}")
        isLoading = false
        vacancyList.addAll(vacanciesList)
        adapter.notifyDataSetChanged()
        Log.d("ASD", "fragmentList size: ${vacancyList.size}")
        binding.apply {
            searchResultCount.text = requireContext()
                .resources.getQuantityString(R.plurals.vacancies_found, itemsFound, itemsFound)
            searchResultCount.isVisible = true
            recyclerView.isVisible = true
            placeholder.isVisible = false
            startImage.isVisible = false
            progressBar.isVisible = false
            progressBarAdd.isVisible = false
            placeholderAdd.isVisible = false
            placeholderImageAdd.isVisible = false
        }
    }

    private fun showError(errorMessage: String) {
        if (vacancyList.isEmpty()) {
            isLoading = false
            setPlaceholder(errorMessage)
            binding.apply {
                searchResultCount.isVisible = false
                recyclerView.isVisible = false
                placeholder.isVisible = true
                startImage.isVisible = false
                progressBar.isVisible = false
                progressBarAdd.isVisible = false
                placeholderAdd.isVisible = false
                placeholderImageAdd.isVisible = false
            }
        } else {
            isLoading = false
            binding.apply {
                progressBar.isVisible = false
                progressBarAdd.isVisible = false
                placeholderAdd.isVisible = false
                placeholderImageAdd.isVisible = false
            }
            showToast()
        }
    }

    private fun setPlaceholder(errorMessage: String) {
        when (errorMessage) {
            Resource.CONNECTION_PROBLEM -> {
                if (vacancyList.isEmpty()) {
                    binding.placeholderImage.setImageResource(R.drawable.image_no_internet_placeholder)
                    binding.placeholderText.text = requireContext().resources.getString(R.string.no_internet)
                }
            }

            Resource.SERVER_ERROR -> {
                if (vacancyList.isEmpty()) {
                    binding.placeholderImage.setImageResource(R.drawable.image_server_error_placeholder)
                    binding.placeholderText.text = requireContext().resources.getString(R.string.server_error)
                }
            }
        }
    }

    private fun showToast() {
        toastJob?.cancel()
        toastJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(TOAST_DELAY)
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.check_net),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    companion object {
        const val IS_RUN = "IS_RUN"
        private const val TOAST_DELAY = 1000L
    }
}
