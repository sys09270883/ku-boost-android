package com.konkuk.boost.data

data class UseCase<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status { SUCCESS, ERROR }

    companion object {
        fun <T> success(data: T): UseCase<T> {
            return UseCase(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): UseCase<T> {
            return UseCase(Status.ERROR, data, message)
        }
    }
}