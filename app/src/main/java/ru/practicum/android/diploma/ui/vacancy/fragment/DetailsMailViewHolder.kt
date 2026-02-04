package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsMailItemBinding

class DetailsMailViewHolder(private val binding: DetailsMailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsMailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsMailItemBinding.inflate(inflater, parent, false)
            return DetailsMailViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.MailItem) {
        binding.name.text = item.name
        binding.mail.text = item.mail
    }
}
