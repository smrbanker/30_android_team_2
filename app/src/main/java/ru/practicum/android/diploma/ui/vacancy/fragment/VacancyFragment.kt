package ru.practicum.android.diploma.ui.vacancy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import kotlin.getValue

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()
    private val detailAdapter = VacancyDetailsAdapter()
    private var id: String? = null
    private var vacancyFromDB: Vacancy? = null

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
            // viewModel.checkFavourite(id!!) // ПРОВЕРКА НА ИЗБРАННОСТЬ ПРИ ВХОДЕ, ЧТОБЫ УСТАНОВИТЬ СТАТУС
            viewModel.searchVacancyId(id!!)
        } else if (id.isNullOrEmpty() && vacancyFromDB != null) {
            // viewModel.checkFavourite(vacancyFromDB!!.id) // ПРОВЕРКА НА ИЗБРАННОСТЬ ПРИ ВХОДЕ, ЧТОБЫ УСТАНОВИТЬ СТАТУС
            viewModel.setVacancyFromBase(vacancyFromDB!!)
        }
        // ИЗМЕНЕНИЕ СОСТОЯНИЯ КНОПКИ, КАК ОТВЕТ VIEWMODEL
        /* viewModel.observeFavouriteInfo()
            .observe(viewLifecycleOwner) {
                when (it) {
                    true -> favour.setImageResource(R.drawable.ic_like_full)
                    false -> favour.setImageResource(R.drawable.ic_like_outlined)
                }
            } */

        favour.setOnClickListener { // ПРИ НАЖАТИИ НА КНОПКУ МЕНЯЕМ ЕЕ СОСТОЯНИЕ И БД (ДОБАВИТЬ/УДАЛИТЬ)
            // viewModel.changeFavourite(vacancy) // РАЗКОММЕНТИРУЙ, КОГДА БУДЕТ VACANCY ДЛЯ ДОБАВЛЕНИЯ В ИЗБРАННОЕ
        }
        // КОНЕЦ КОДА ДЛЯ ОТРАБОТКИ НАЖАТИЯ НА КНОПКУ ИЗБРАННОГО

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun render(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()
            is VacancyDetailsState.Content -> showContent(state.vacancy)
            is VacancyDetailsState.Error -> showError(state.errorMessage)
            is VacancyDetailsState.Empty -> showEmpty(state.emptyMessage)
        }
    }
    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.detailRecyclerView.isVisible = false
        binding.placeholder.isVisible = false
    }

    private fun showContent(vacancy: List<VacancyCastItem>) {
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

    /* fun getMockVacancy(): Vacancy {
        return Vacancy(
            "0001b24b-da81-48cd-a420-91e3fbfc5ef0",
            "Backend Developer в Apple",
            "О нас\n\nСтартап. Создаём инновационный маркетплейс в сфере развлечений и туризма. Наша платформа предлагает заведениям полноценную замену веб-сайтов, а пользователям — удобный способ находить и бронировать места и услуги в одном месте, избавляя от необходимости переходить на множество других ресурсов.\nСтремимся создать лучший пользовательский опыт и ищем талантливого Backend-разработчика, чтобы помочь нам в этом! Мы — небольшая, но дружная команда, увлечённая созданием лучшего продукта для наших пользователей. Ценим инициативность, креативность и стремление к постоянному развитию.\n\nЗадача\n\nАктивное развитие маркетплейса, отвечая за серверную логику, API, базу данных и общую архитектуру. Ваши решения напрямую повлияют на производительность, масштабируемость и безопасность платформы.\n\nДля нас крайне важно\n\n- Производительность backend (оптимизация запросов, кэширование);\n- Безопасность (защита от уязвимостей, аутентификация и авторизация);\n- Масштабируемость (архитектура, позволяющая выдерживать растущую нагрузку);\n- Поддержка Frontend (удобный и эффективный API).\n\nСтэк:\n\nОбсуждается с успешным кандидатом.",
            2000,
            30000,
            "USD",
            "Барнаул",
            "Ленина",
            "16",
            "Барнаул, Ленина, 16",
            "Нет опыта",
            "Полный день",
            "Полная занятость",
            "Петров Петр Петрович",
            "",
            "+7 (999) 234-56-78",
            "Apple",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/1200px-Apple_logo_black.svg.png",
            "Лопатино (Пензенская область)",
            "NoSQL,Python",
            "cek6h0n4ffe7ur.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com/vacancies/0001b24b-da81-48cd-a420-91e3fbfc5ef0",
            "Сельское хозяйство"
        )
    } */

    companion object {
        private const val VACANCY_ID = "VACANCY_ID"
        private const val VACANCY_OBJECT = "VACANCY_OBJECT"
        fun createArgsId(vacancyId: String): Bundle =
            bundleOf(VACANCY_ID to vacancyId)

        fun createArgsVacancy(vacancyJson: String): Bundle =
            bundleOf(VACANCY_OBJECT to vacancyJson)
    }
}
