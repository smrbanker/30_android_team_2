package ru.practicum.android.diploma.domain.models

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    companion object {
        const val CONNECTION_PROBLEM = "Нет интернета"
        const val SERVER_ERROR = "Внутренняя ошибка сервера"
    }
}
