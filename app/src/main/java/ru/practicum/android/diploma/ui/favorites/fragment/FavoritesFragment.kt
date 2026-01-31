package ru.practicum.android.diploma.ui.favorites.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val vacanciesList = ArrayList<Vacancy>()
    private var adapter: FavouritesAdapter? = null
    private var favouritesList: RecyclerView? = null
    private var imageView: ImageView? = null
    private var textView: TextView? = null
    private var layoutView: LinearLayout? = null
    private var progressBar: ProgressBar? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavouritesAdapter(vacanciesList, onVacancyClick = { vacancy ->
            callDetails(vacancy)
        })

        // favouritesList = binding.recyclerView
        favouritesList?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favouritesList?.adapter = adapter

        imageView = binding.placeholderImage
        textView = binding.placeholderText
        layoutView = binding.placeholder
        progressBar = binding.progressBar

        val viewModel by viewModel<FavouritesViewModel>()

        // viewModel.addSomeVacancies()
        // viewModel.deleteSomeVacancies()
        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    fun callDetails(vacancy: Vacancy) {
        val gson: Gson by inject()
        val vacancyJson: String = gson.toJson(vacancy)
        findNavController().navigate(
            R.id.action_favoritesFragment_to_vacancyFragment,
            // VacancyFragment.createArgs(vacancyJson) - ТРЕБУЕТСЯ СОЗДАТЬ ФУНКЦИЮ В VACANCY FRAGMENT
        )
    }

    private fun render(state: FavouritesState) {
        when (state) {
            is FavouritesState.Content -> showContent(state.vacancies)
            is FavouritesState.Empty -> showEmpty()
            is FavouritesState.Error -> showError()
            is FavouritesState.Loading -> showLoading()
        }
    }

    private fun showEmpty() {
        progressBar?.visibility = View.GONE
        favouritesList?.visibility = View.GONE
        layoutView?.visibility = View.VISIBLE
        imageView?.visibility = View.VISIBLE
        textView?.visibility = View.VISIBLE
        imageView?.setImageResource(R.drawable.image_empty_favorites_placeholder)
        textView?.text = getString(R.string.list_is_empty)
    }

    private fun showError() {
        progressBar?.visibility = View.GONE
        favouritesList?.visibility = View.GONE
        layoutView?.visibility = View.VISIBLE
        imageView?.visibility = View.VISIBLE
        textView?.visibility = View.VISIBLE
        imageView?.setImageResource(R.drawable.image_wrong_query_placeholder)
        textView?.text = getString(R.string.cannot_get_vacancies_list)
    }

    private fun showContent(vacancies: List<Vacancy>) {
        progressBar?.visibility = View.GONE
        favouritesList?.visibility = View.VISIBLE
        layoutView?.visibility = View.GONE
        imageView?.visibility = View.GONE
        textView?.visibility = View.GONE

        vacanciesList.clear()
        vacanciesList.addAll(vacancies)
        adapter?.notifyDataSetChanged()
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        favouritesList = null
        _binding = null
    }
}
