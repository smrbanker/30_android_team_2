package ru.practicum.android.diploma.ui.favorites.fragment

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.salaryFormatter

class FavouritesViewHolder(private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) = with(binding) {
        val context = itemView.context

        val title = model.description + ", " + model.area
        ivTitle.text = title
        ivCompany.text = model.employer
        ivSalary.text = salaryFormatter(model, context)

        val logo = model.logo
        val path = binding.ivIcon
        val placeholder = R.drawable.placeholder_32px
        setImage(context, logo, path, placeholder, 12)
        path.clipToOutline = true
    }

    fun setImage(itemView: Context, logo: String, path: ImageView, placeholder: Int, dp: Int) {
        Glide
            .with(itemView)
            .load(logo)
            .transform(CenterInside(), RoundedCorners(dp))
            .placeholder(placeholder)
            .into(path)
    }
}
