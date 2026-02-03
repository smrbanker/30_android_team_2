package ru.practicum.android.diploma.ui.vacancy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_CHECK
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_DELETE
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_INSERT
import kotlin.getValue

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()
    private val detailAdapter = VacancyDetailsAdapter()
    private var id: String? = null
    private var vacancyFromDB: Vacancy? = null
    private var vacancyForFavourite: Vacancy? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = getArgsId()
        // vacancyFromDB = getMockVacancy()
        // НАЧАЛО КОДА ДЛЯ ОТРАБОТКИ НАЖАТИЯ НА КНОПКУ ИЗБРАННОГО
        val favour = binding.likeButton

        if (!id.isNullOrEmpty() && vacancyFromDB == null) {
            viewModel.checkFavourite(id!!) // ПРОВЕРКА НА ИЗБРАННОСТЬ ПРИ ВХОДЕ, ЧТОБЫ УСТАНОВИТЬ СТАТУС
            viewModel.searchVacancyId(id!!)
        } else if (id.isNullOrEmpty() && vacancyFromDB != null) {
            viewModel.checkFavourite(vacancyFromDB!!.id) // ПРОВЕРКА НА ИЗБРАННОСТЬ ПРИ ВХОДЕ, ЧТОБЫ УСТАНОВИТЬ СТАТУС
            viewModel.setVacancyFromBase(vacancyFromDB!!)
        }
        // ИЗМЕНЕНИЕ СОСТОЯНИЯ КНОПКИ, КАК ОТВЕТ VIEWMODEL
        viewModel.observeFavouriteInfo()
            .observe(viewLifecycleOwner) {
                when (it) {
                    true -> favour.setImageResource(R.drawable.ic_like_full)
                    false -> favour.setImageResource(R.drawable.ic_like_outlined)
                }
        }

        viewModel.observeState()
            .observe(viewLifecycleOwner) {
                render(it)
            }
        binding.detailRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.detailRecyclerView.adapter = detailAdapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        favour.setOnClickListener {
            viewModel.changeFavourite(vacancyForFavourite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun render(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()
            is VacancyDetailsState.Content -> showContent(state.vacancy, state.vacancyFull)
            is VacancyDetailsState.Error -> showError(state.errorMessage)
            is VacancyDetailsState.Empty -> showEmpty(state.emptyMessage)
            is VacancyDetailsState.ErrorDB -> showErrorDB(state.errorMessageDB)
        }
    }
    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.detailRecyclerView.isVisible = false
        binding.placeholder.isVisible = false
    }

    private fun showContent(vacancy: List<VacancyCastItem>, vacancyFull: Vacancy?) {
        vacancyForFavourite = vacancyFull
        binding.progressBar.isVisible = false
        binding.detailRecyclerView.isVisible = true
        binding.placeholder.isVisible = false
        detailAdapter.updateAdapter(vacancy)
        detailAdapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.isVisible = false
        binding.detailRecyclerView.isVisible = false
        binding.placeholder.isVisible = true
        binding.placeholderText.text = getString(errorMessage.toInt())
    }
    private fun showErrorDB(errorMessageDB: String) {
        when (errorMessageDB) {
            DB_ERROR_INSERT -> Toast.makeText(
                requireContext(),
                DB_ERROR_INSERT, Toast.LENGTH_SHORT
            )
                .show()

            DB_ERROR_DELETE -> Toast.makeText(
                requireContext(),
                DB_ERROR_DELETE, Toast.LENGTH_SHORT
            )
                .show()

            DB_ERROR_CHECK -> Toast.makeText(
                requireContext(),
                DB_ERROR_CHECK, Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    private fun showEmpty(emptyMessage: String) {
        binding.progressBar.isVisible = false
        binding.detailRecyclerView.isVisible = false
        binding.placeholder.isVisible = true
        binding.placeholderText.text = getString(emptyMessage.toInt())
    }
    fun getArgsId(): String? {
        val id = arguments?.getString("VACANCY_ID")
        return id
    }

    companion object {
        private const val VACANCY_ID = "VACANCY_ID"
        private const val VACANCY_OBJECT = "VACANCY_OBJECT"
        fun createArgsId(vacancyId: String): Bundle =
            bundleOf(VACANCY_ID to vacancyId)

        fun createArgsVacancy(vacancyJson: String): Bundle =
            bundleOf(VACANCY_OBJECT to vacancyJson)
    }
}
