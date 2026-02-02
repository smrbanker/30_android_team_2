package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsExperienceItemBinding

class DetailsExperienceViewHolder(private val binding: DetailsExperienceItemBinding) :
    RecyclerView.ViewHolder(
        binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsExperienceViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsExperienceItemBinding.inflate(inflater, parent, false)
            return DetailsExperienceViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.ExperienceItem) {
        binding.requestedExperience.text = item.title
        binding.experience.text = item.experience
        binding.schedule.text = item.schedule
        binding.employment.text = item.employment
    }
}
