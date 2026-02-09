package ru.practicum.android.diploma.ui.filtration.models

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaListItemBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class AreaViewHolder(private val binding: AreaListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Area) {
        when (model) {
            is Country -> binding.areaName.text = model.name
            is Region -> return // Димас, всё для тебя :)
        }
    }
}
