package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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
        if(!item.phone.comment.isNullOrEmpty() || !item.phone.formatted.isNullOrEmpty()) {
            if (item.phone.comment.isNullOrEmpty()) {
                binding.comment.isVisible = false
                binding.itemText.isVisible = true
                binding.itemText.text = item.phone.formatted
            } else if (item.phone.formatted.isNullOrEmpty()) {
                binding.itemText.isVisible = false
                binding.comment.isVisible = true
                binding.comment.text = item.phone.comment
            } else {
                binding.comment.isVisible = true
                binding.itemText.isVisible = true
                binding.itemText.text = item.phone.formatted
                binding.comment.text = item.phone.comment
            }
        }
    }
}
