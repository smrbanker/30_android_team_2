package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsSmallTitleItemBinding

class DetailsSmallTitleViewHolder (private val binding: DetailsSmallTitleItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from (parent: ViewGroup): DetailsSmallTitleViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsSmallTitleItemBinding.inflate(inflater, parent, false)
            return DetailsSmallTitleViewHolder(binding)
        }
    }

    fun bind (item: VacancyCastItem.SmallHeaderItem) {
        binding.smallTitle.text = item.title
    }
}
