package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsBigTitleItemBinding

class DetailsBigTitleViewHolder (private val binding: DetailsBigTitleItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from (parent: ViewGroup): DetailsBigTitleViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsBigTitleItemBinding.inflate(inflater, parent, false)
            return DetailsBigTitleViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.BigHeaderItem) {
        binding.bigTitle.text = item.title
    }
}
