package ru.practicum.android.diploma.ui.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Sector

class IndustryAdapter(private val industries: List<Sector>, private val onIndustryClick: (Sector) -> Unit) :
    RecyclerView.Adapter<IndustryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position])
        holder.itemView.setOnClickListener {
            onIndustryClick.invoke(industries[position])
        }
    }

    override fun getItemCount(): Int {
        return industries.size
    }
}
