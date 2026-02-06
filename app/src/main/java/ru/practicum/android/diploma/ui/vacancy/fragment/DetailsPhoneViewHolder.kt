package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.DetailsPhoneItemBinding

class DetailsPhoneViewHolder(private val binding: DetailsPhoneItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsPhoneViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsPhoneItemBinding.inflate(inflater, parent, false)
            return DetailsPhoneViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.PhoneItem) {
        binding.itemText.text = item.phone
    }
}
