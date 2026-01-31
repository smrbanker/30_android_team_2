package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

fun salaryFormatter(vacancy: Vacancy, context: Context): String {
    return when {
        vacancy.salaryFrom != null && vacancy.salaryTo == null -> context.getString(
            R.string.salary_from,
            numberFormatter(vacancy.salaryFrom),
            currencyFormatter(vacancy.currency)
        )

        vacancy.salaryFrom == null && vacancy.salaryTo != null -> context.getString(
            R.string.salary_to,
            numberFormatter(vacancy.salaryTo),
            currencyFormatter(vacancy.currency)
        )

        vacancy.salaryFrom != null && vacancy.salaryTo != null -> context.getString(
            R.string.salary_from_to,
            numberFormatter(vacancy.salaryFrom),
            numberFormatter(vacancy.salaryTo),
            currencyFormatter(vacancy.currency)
        )

        else -> context.getString(R.string.salary_not_specified)
    }
}

fun numberFormatter(number: Int): String {
    return NumberFormat.getInstance(Locale("RU")).format(number)
}

private fun currencyFormatter(currency: String?): String {
    if (currency != null) {
        return when (currency) {
            "RUR", "RUB" -> "₽"
            "BYR" -> "Br"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "Soʻm"
            "GEL" -> "₾"
            "KGT" -> "с"
            else -> {
                currency
            }
        }
    } else {
        return ""
    }
}
