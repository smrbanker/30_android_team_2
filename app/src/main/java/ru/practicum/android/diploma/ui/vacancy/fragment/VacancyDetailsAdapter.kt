package ru.practicum.android.diploma.ui.vacancy.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R

class VacancyDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemsList = ArrayList<VacancyCastItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        R.layout.details_big_title_item -> DetailsBigTitleViewHolder.Companion.from(parent)
        R.layout.details_company_item -> DetailsCompanyItemViewHolder.Companion.from(parent)
        R.layout.details_experience_item -> DetailsExperienceViewHolder.Companion.from(parent)
        R.layout.details_general_title_item -> DetailsGeneralTitleViewHolder.Companion.from(parent)
        R.layout.details_list_item -> DetailsListItemViewHolder.Companion.from(parent)
        R.layout.details_small_title_item -> DetailsSmallTitleViewHolder.Companion.from(parent)
        else -> error("Unknown viewType create [$viewType]")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.details_big_title_item -> {
                val headerHolder = holder as DetailsBigTitleViewHolder
                headerHolder.bind(itemsList[position] as VacancyCastItem.BigHeaderItem)
            }
            R.layout.details_company_item -> {
                val companyHolder = holder as DetailsCompanyItemViewHolder
                companyHolder.bind(itemsList[position] as VacancyCastItem.CompanyItem)
            }
            R.layout.details_experience_item -> {
                val experienceHolder = holder as DetailsExperienceViewHolder
                experienceHolder.bind(itemsList[position] as VacancyCastItem.ExperienceItem)
            }
            R.layout.details_general_title_item -> {
                val generalTitleHolder = holder as DetailsGeneralTitleViewHolder
                generalTitleHolder.bind(itemsList[position] as VacancyCastItem.GeneralHeaderItem)
            }
            R.layout.details_list_item -> {
                val itemHolder = holder as DetailsListItemViewHolder
                itemHolder.bind(itemsList[position] as VacancyCastItem.Item)
            }
            R.layout.details_small_title_item -> {
                val smallTitleHolder = holder as DetailsSmallTitleViewHolder
                smallTitleHolder.bind(itemsList[position] as VacancyCastItem.SmallHeaderItem)
            }
            else -> error("Unknown viewType bind [${holder.itemViewType}]")
        }
    }
    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemsList[position]) {
            is VacancyCastItem.BigHeaderItem -> R.layout.details_big_title_item
            is VacancyCastItem.CompanyItem -> R.layout.details_company_item
            is VacancyCastItem.ExperienceItem -> R.layout.details_experience_item
            is VacancyCastItem.GeneralHeaderItem -> R.layout.details_general_title_item
            is VacancyCastItem.Item -> R.layout.details_list_item
            is VacancyCastItem.SmallHeaderItem -> R.layout.details_small_title_item
        }
    }

    fun updateAdapter(vacancy: List<VacancyCastItem>) {
        itemsList.clear()
        itemsList.addAll(vacancy)
    }
}
