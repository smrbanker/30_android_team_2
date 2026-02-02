package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.databinding.DetailsCompanyItemBinding
import ru.practicum.android.diploma.R

class DetailsCompanyItemViewHolder (private val binding: DetailsCompanyItemBinding) :
    RecyclerView.ViewHolder(
        binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsCompanyItemViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsCompanyItemBinding.inflate(inflater, parent, false)
            return DetailsCompanyItemViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.CompanyItem) {
        binding.companyName.text = item.employer
        binding.companyLocation.text = item.area
        Glide.with(binding.logo)
            .load(item.logo)
            .placeholder(R.drawable.placeholder_32px)
            .centerCrop()
            .into(binding.logo)
    }
}
