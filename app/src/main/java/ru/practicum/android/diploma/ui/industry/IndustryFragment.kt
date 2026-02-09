package ru.practicum.android.diploma.ui.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.Sector
import ru.practicum.android.diploma.ui.industry.IndustryViewModel.Companion.API_ERROR_OUTPUT
import ru.practicum.android.diploma.ui.industry.IndustryViewModel.Companion.SP_ERROR_INPUT
import ru.practicum.android.diploma.ui.industry.IndustryViewModel.Companion.SP_ERROR_OUTPUT

class IndustryFragment : Fragment() {
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!
    private val industryList = ArrayList<Sector>()
    private var adapter: IndustryAdapter? = null
    private var recycler: RecyclerView? = null
    private val viewModel by viewModel<IndustryViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = IndustryAdapter(industryList, onIndustryClick = { industry ->
            changeItem(industry)
        })

        recycler = binding.recyclerView
        recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = adapter

        viewModel.fillData()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.industryApply.setOnClickListener {
            viewModel.setIndustry()
            findNavController().popBackStack()
        }

        binding.searchEdit.doOnTextChanged { text, _, _, _ ->
            if (viewModel.observeState().value is IndustryState.Content) {
                viewModel.search(text.toString())
            }
            binding.searchImage.isVisible = text.isNullOrEmpty()
            binding.searchClear.isVisible = !text.isNullOrEmpty()
        }

        binding.searchClear.setOnClickListener {
            binding.searchEdit.text.clear()
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: IndustryState) {
        when (state) {
            is IndustryState.Content -> showContent(state.industries, state.flag)
            is IndustryState.Empty -> showEmpty()
            is IndustryState.Error -> showError(state.message)
            is IndustryState.Loading -> showLoading()
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.isVisible = false
            placeholder.isVisible = false
            recyclerView.isVisible = false
        }
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            progressBar.isVisible = false
            placeholder.isVisible = false
            recyclerView.isVisible = false
        }

        when (errorMessage) {
            SP_ERROR_INPUT ->
                Toast.makeText(
                    requireContext(),
                    SP_ERROR_INPUT,
                    Toast.LENGTH_SHORT
                )
                    .show()

            SP_ERROR_OUTPUT ->
                Toast.makeText(
                    requireContext(),
                    SP_ERROR_OUTPUT,
                    Toast.LENGTH_SHORT
                )
                    .show()

            API_ERROR_OUTPUT ->
                binding.apply {
                    placeholder.isVisible = true
                    placeholderImage.isVisible = true
                    placeholderImage.setImageDrawable(
                        requireContext()
                            .getDrawable(R.drawable.image_wrong_query_placeholder)
                    )
                    placeholderText.isVisible = true
                    placeholderText.text = getString(R.string.cannot_get_industry_list)
                    recyclerView.isVisible = false
                    industryApply.isVisible = false
                }
        }
    }

    private fun showContent(industries: List<Sector>, flag: Boolean) {
        binding.apply {
            progressBar.isVisible = false
            placeholder.isVisible = false
            recyclerView.isVisible = true
        }

        industryList.clear()
        industryList.addAll(industries)
        adapter?.notifyDataSetChanged()
        binding.industryApply.isVisible = flag

    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            placeholder.isVisible = false
            recyclerView.isVisible = false
        }
    }

    private fun changeItem(industry: Sector) {
        viewModel.changeItem(industry)
        binding.industryApply.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        recycler = null
        _binding = null
    }
}
