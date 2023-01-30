package com.application.zaki.githubuser.utils

data class UiState<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): UiState<T> {
            return UiState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): UiState<T> {
            return UiState(Status.ERROR, null, msg)
        }

        fun <T> loading(): UiState<T> {
            return UiState(Status.LOADING, null, null)
        }
    }
}