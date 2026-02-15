package ru.practicum.android.diploma.ui.vacancy.fragment

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.DetailsCompanyItemBinding

class DetailsCompanyItemViewHolder(private val binding: DetailsCompanyItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): DetailsCompanyItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsCompanyItemBinding.inflate(inflater, parent, false)
            return DetailsCompanyItemViewHolder(binding)
        }
    }

    fun bind(item: VacancyCastItem.CompanyItem) {
        binding.companyName.text = item.employer
        binding.companyLocation.text = item.area

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
        val cornerRadius = dpToPx(2f, itemView.context)

        Glide.with(binding.logo)
            .load(item.logo)
            .placeholder(R.drawable.placeholder_32px)
            .transform(CenterInside(), RoundedCorners(cornerRadius))
            .into(binding.logo)
    }
}
