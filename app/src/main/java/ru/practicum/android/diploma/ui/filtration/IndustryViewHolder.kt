package ru.practicum.android.diploma.ui.filtration

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(
    private val binding: ItemIndustryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industry) {
        binding.industryName.text = industry.name
    }
}
