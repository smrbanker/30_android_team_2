package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    items: List<Vacancy> = emptyList()
) : ListAdapter<Vacancy, VacancyAdapter.VacancyViewHolder>(VacancyDiffCallback()) {

    init {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    fun updateItems(newItems: List<Vacancy>) {
        submitList(newItems)
    }

    class VacancyViewHolder(
        private val binding: ItemVacancyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.vacancyNameTextView.text = vacancy.name
            binding.companyNameTextView.text = vacancy.employer?.name ?: ""
            binding.areaTextView.text = vacancy.area?.name ?: ""

            binding.salaryTextView.text = formatSalary(vacancy.salary)
        }

        private fun formatSalary(salary: ru.practicum.android.diploma.domain.models.Salary?): String {
            return when {
                salary == null -> "Зарплата не указана"
                salary.from != null && salary.to != null -> {
                    "от ${formatNumber(salary.from)} до ${formatNumber(salary.to)} ${salary.currency ?: ""}"
                }
                salary.from != null -> "от ${formatNumber(salary.from)} ${salary.currency ?: ""}"
                salary.to != null -> "до ${formatNumber(salary.to)} ${salary.currency ?: ""}"
                else -> "Зарплата не указана"
            }
        }

        private fun formatNumber(number: Int): String {
            return number.toString().reversed().chunked(3).joinToString(" ").reversed()
        }
    }

    class VacancyDiffCallback : DiffUtil.ItemCallback<Vacancy>() {
        override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem == newItem
        }
    }
}

