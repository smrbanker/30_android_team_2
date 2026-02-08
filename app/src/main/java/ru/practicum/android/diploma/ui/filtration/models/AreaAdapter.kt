package ru.practicum.android.diploma.ui.filtration.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.AreaListItemBinding
import ru.practicum.android.diploma.domain.models.Area

class AreaAdapter(private val action: (Area) -> Unit) : RecyclerView.Adapter<AreaViewHolder>() {
    private val areaList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AreaListItemBinding.inflate(inflater, parent, false)
        return AreaViewHolder(binding)
    }

    override fun getItemCount(): Int = areaList.size

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(areaList[position])
        holder.itemView.setOnClickListener {
            action(areaList[position])
        }
    }

    fun updateAreas(areasList: List<Area>) {
        areaList.clear()
        areaList.addAll(areasList)
        notifyDataSetChanged()
    }
}
