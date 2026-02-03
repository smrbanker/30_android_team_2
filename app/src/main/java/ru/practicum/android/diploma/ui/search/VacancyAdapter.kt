package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacancyAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<Vacancy, VacancyAdapter.VacancyViewHolder>(VacancyDiffCallback()) {

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

    inner class VacancyViewHolder(
        private val binding: ItemVacancyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            binding.root.setOnClickListener {
                onItemClick(vacancy.id)
            }

            binding.vacancyName.text = vacancy.name
            binding.companyName.text = vacancy.employer
            binding.vacancyArea.text = vacancy.area

            // Форматирование зарплаты
            binding.vacancySalary.text = formatSalary(
                vacancy.salaryFrom,
                vacancy.salaryTo,
                vacancy.currency
            )

            // Загрузка логотипа компании
            if (vacancy.logo.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(vacancy.logo)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.companyLogo)
            } else {
                binding.companyLogo.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        private fun formatSalary(from: Int?, to: Int?, currency: String?): String {
            val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))
            
            return when {
                from != null && to != null -> {
                    "От ${numberFormat.format(from)} до ${numberFormat.format(to)} ${formatCurrency(currency)}"
                }
                from != null -> {
                    "От ${numberFormat.format(from)} ${formatCurrency(currency)}"
                }
                to != null -> {
                    "До ${numberFormat.format(to)} ${formatCurrency(currency)}"
                }
                else -> {
                    binding.root.context.getString(R.string.salary_not_specified)
                }
            }
        }

        private fun formatCurrency(currency: String?): String {
            return when (currency?.uppercase()) {
                "RUR", "RUB" -> "Р."
                "BYR" -> "Br"
                "USD" -> "$"
                "EUR" -> "€"
                "KZT" -> "₸"
                "UAH" -> "₴"
                "AZN" -> "₼"
                "UZS" -> "so'm"
                "GEL" -> "₾"
                "KGT" -> "сом"
                else -> currency ?: ""
            }
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

