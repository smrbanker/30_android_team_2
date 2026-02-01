package ru.practicum.android.diploma.ui.vacancy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import kotlin.getValue

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // НАЧАЛО КОДА ДЛЯ ОТРАБОТКИ НАЖАТИЯ НА КНОПКУ ИЗБРАННОГО
        val id = "" // ТЕСТОВЫЙ ID. ДЛЯ РАБОТЫ БЕРЕМ ID ИЗ ПЕРЕДАННОЙ ВАКАНСИИ
        val favour = binding.likeButton

        val viewModel by viewModel<VacancyViewModel>()

        viewModel.checkFavourite(id) // ПРОВЕРКА НА ИЗБРАННОСТЬ ПРИ ВХОДЕ, ЧТОБЫ УСТАНОВИТЬ СТАТУС

        // ИЗМЕНЕНИЕ СОСТОЯНИЯ КНОПКИ, КАК ОТВЕТ VIEWMODEL
        viewModel.observeFavouriteInfo()
            .observe(viewLifecycleOwner) {
                when (it) {
                    true -> favour.setImageResource(R.drawable.ic_like_full)
                    false -> favour.setImageResource(R.drawable.ic_like_outlined)
                }
            }

        favour.setOnClickListener { // ПРИ НАЖАТИИ НА КНОПКУ МЕНЯЕМ ЕЕ СОСТОЯНИЕ И БД (ДОБАВИТЬ/УДАЛИТЬ)
            // viewModel.changeFavourite(vacancy) // РАЗКОММЕНТИРУЙ, КОГДА БУДЕТ VACANCY ДЛЯ ДОБАВЛЕНИЯ В ИЗБРАННОЕ
        }
        // КОНЕЦ КОДА ДЛЯ ОТРАБОТКИ НАЖАТИЯ НА КНОПКУ ИЗБРАННОГО
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VACANCY_ID = "VACANCY_ID"
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(VACANCY_ID to vacancyId)
    }
}
