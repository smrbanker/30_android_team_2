package ru.practicum.android.diploma.ui.filtration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private var industries: List<Industry>,
    private val onIndustryClick: (Industry) -> Unit
) : RecyclerView.Adapter<IndustryViewHolder>() {

    fun updateList(newIndustries: List<Industry>) {
        industries = newIndustries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industries[position])
        holder.itemView.setOnClickListener {
            onIndustryClick.invoke(industries[position])
        }
    }

    override fun getItemCount(): Int = industries.size
}
