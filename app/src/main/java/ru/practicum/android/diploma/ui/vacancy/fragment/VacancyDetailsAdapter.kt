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
        R.layout.details_list_skills_item -> DetailsSkillItemViewHolder.Companion.from(parent)
        R.layout.details_phone_item -> DetailsPhoneViewHolder.Companion.from(parent)
        R.layout.details_mail_item -> DetailsMailViewHolder.Companion.from(parent)
        else -> error("Unknown viewType create [$viewType]")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.details_big_title_item -> bindBigTitle(holder, position)
            R.layout.details_company_item -> bindCompany(holder, position)
            R.layout.details_experience_item -> bindExperience(holder, position)
            R.layout.details_general_title_item -> bindGeneralTitle(holder, position)
            R.layout.details_list_item -> bindListItem(holder, position)
            R.layout.details_small_title_item -> bindSmallTitle(holder, position)
            R.layout.details_list_skills_item -> bindSkills(holder, position)
            R.layout.details_phone_item -> bindPhone(holder, position)
            R.layout.details_mail_item -> bindMail(holder, position)
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
            is VacancyCastItem.SkillItem -> R.layout.details_list_skills_item
            is VacancyCastItem.PhoneItem -> R.layout.details_phone_item
            is VacancyCastItem.MailItem -> R.layout.details_mail_item
        }
    }

    fun updateAdapter(vacancy: List<VacancyCastItem>) {
        itemsList.clear()
        itemsList.addAll(vacancy)
    }

    private fun bindBigTitle(holder: RecyclerView.ViewHolder, position: Int) {
        val headerHolder = holder as DetailsBigTitleViewHolder
        headerHolder.bind(itemsList[position] as VacancyCastItem.BigHeaderItem)
    }

    private fun bindCompany(holder: RecyclerView.ViewHolder, position: Int) {
        val companyHolder = holder as DetailsCompanyItemViewHolder
        companyHolder.bind(itemsList[position] as VacancyCastItem.CompanyItem)
    }

    private fun bindExperience(holder: RecyclerView.ViewHolder, position: Int) {
        val experienceHolder = holder as DetailsExperienceViewHolder
        experienceHolder.bind(itemsList[position] as VacancyCastItem.ExperienceItem)
    }

    private fun bindGeneralTitle(holder: RecyclerView.ViewHolder, position: Int) {
        val generalTitleHolder = holder as DetailsGeneralTitleViewHolder
        generalTitleHolder.bind(itemsList[position] as VacancyCastItem.GeneralHeaderItem)
    }

    private fun bindListItem(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as DetailsListItemViewHolder
        itemHolder.bind(itemsList[position] as VacancyCastItem.Item)
    }

    private fun bindSmallTitle(holder: RecyclerView.ViewHolder, position: Int) {
        val smallTitleHolder = holder as DetailsSmallTitleViewHolder
        smallTitleHolder.bind(itemsList[position] as VacancyCastItem.SmallHeaderItem)
    }

    private fun bindSkills(holder: RecyclerView.ViewHolder, position: Int) {
        val skillHolder = holder as DetailsSkillItemViewHolder
        skillHolder.bind(itemsList[position] as VacancyCastItem.SkillItem)
    }

    private fun bindPhone(holder: RecyclerView.ViewHolder, position: Int) {
        val phoneHolder = holder as DetailsPhoneViewHolder
        phoneHolder.bind(itemsList[position] as VacancyCastItem.PhoneItem)
    }

    private fun bindMail(holder: RecyclerView.ViewHolder, position: Int) {
        val mailHolder = holder as DetailsMailViewHolder
        mailHolder.bind(itemsList[position] as VacancyCastItem.MailItem)
    }
}
