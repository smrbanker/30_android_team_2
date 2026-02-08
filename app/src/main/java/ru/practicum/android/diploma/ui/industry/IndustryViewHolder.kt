package ru.practicum.android.diploma.ui.industry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Sector

class IndustryViewHolder(private val binding: ItemIndustryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Sector) = with(binding) {

        itemIndustryName.text = model.name
        if (model.isChecked) {
            viewIndustryName.setImageResource(R.drawable.ic_radio_button_on)
        } else {
            viewIndustryName.setImageResource(R.drawable.ic_radio_button_off)
        }
    }
}
