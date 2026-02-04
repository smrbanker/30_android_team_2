package ru.practicum.android.diploma.ui.vacancy.fragment

import android.content.Intent
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
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_CHECK
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_DELETE
import ru.practicum.android.diploma.ui.vacancy.fragment.VacancyViewModel.Companion.DB_ERROR_INSERT

class VacancyFragment : Fragment() {
    private var id: String? = null
    private var vacancyFromDB: Vacancy? = null
    private var vacancyForFavourite: Vacancy? = null
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()
    private val detailAdapter = VacancyDetailsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // region Получение информции из экрана поиска вакансий
        id = getArgsId()
        // endregion

        // region Получение информции из экрана избранных вакансий
        val vacancyInJson = requireArguments().getString(VACANCY_OBJECT) ?: ""
        val gson: Gson by inject()
        vacancyFromDB = gson.fromJson(vacancyInJson, Vacancy::class.java)
        // endregion

        // region Observers (ViewModel)
        val favour = binding.likeButton
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
        // endregion

        // region Проверка наличия вакансии в списке избранных и вывод на экран результата поиска
        checkSourceData()
        // endregion

        // region Настройка RecyclerView
        binding.detailRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.detailRecyclerView.adapter = detailAdapter
        // endregion

        // region Listeners
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        favour.setOnClickListener {
            viewModel.changeFavourite(vacancyForFavourite)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, vacancyForFavourite?.url)
            startActivity(shareIntent)
        }
        // endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        id = null
        vacancyFromDB = null
        vacancyForFavourite = null
    }

    // region Методы для отображения данных на экране
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
        binding.placeholderImage.setImageResource(R.drawable.server_error)
        binding.placeholderText.text = errorMessage
    }
    private fun showErrorDB(errorMessageDB: String) {
        when (errorMessageDB) {
            DB_ERROR_INSERT -> Toast.makeText(
                requireContext(),
                DB_ERROR_INSERT,
                Toast.LENGTH_SHORT
            )
                .show()

            DB_ERROR_DELETE -> Toast.makeText(
                requireContext(),
                DB_ERROR_DELETE,
                Toast.LENGTH_SHORT
            )
                .show()

            DB_ERROR_CHECK -> Toast.makeText(
                requireContext(),
                DB_ERROR_CHECK,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    private fun showEmpty(emptyMessage: String) {
        binding.progressBar.isVisible = false
        binding.detailRecyclerView.isVisible = false
        binding.placeholder.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.image_vacancy_not_found)
        binding.placeholderText.text = emptyMessage
    }
    // endregion

    private fun getArgsId(): String? {
        val id = arguments?.getString(VACANCY_ID)
        return id
    }

    private fun checkSourceData() {
        if (!id.isNullOrEmpty() && vacancyFromDB == null) {
            viewModel.checkFavourite(id!!)
            viewModel.searchVacancyId(id!!)
        } else if (id.isNullOrEmpty() && vacancyFromDB != null) {
            viewModel.checkFavourite(vacancyFromDB!!.id)
            viewModel.setVacancyFromBase(vacancyFromDB!!)
        }
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
