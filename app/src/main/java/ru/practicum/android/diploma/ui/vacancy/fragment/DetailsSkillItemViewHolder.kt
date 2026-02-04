package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsListSkillsItemBinding

class DetailsSkillItemViewHolder(private val binding: DetailsListSkillsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsSkillItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsListSkillsItemBinding.inflate(inflater, parent, false)
            return DetailsSkillItemViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.SkillItem) {
        binding.itemText.text = item.skills
    }
}
