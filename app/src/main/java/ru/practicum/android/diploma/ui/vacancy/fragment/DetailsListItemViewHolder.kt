package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsListItemBinding

class DetailsListItemViewHolder(private val binding: DetailsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsListItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsListItemBinding.inflate(inflater, parent, false)
            return DetailsListItemViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.Item) {
        binding.itemText.text = item.data
    }
}
