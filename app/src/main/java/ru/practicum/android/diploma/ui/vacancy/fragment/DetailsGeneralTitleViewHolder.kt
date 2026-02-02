package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsGeneralTitleItemBinding

class DetailsGeneralTitleViewHolder(private val binding: DetailsGeneralTitleItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsGeneralTitleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsGeneralTitleItemBinding.inflate(inflater, parent, false)
            return DetailsGeneralTitleViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.GeneralHeaderItem) {
        binding.vacancyTitle.text = item.vacancyTitle
        binding.vacancySalary.text = item.vacancySalary
    }
}
