package com.alokomkar.starter.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * A State that represents request progress of a single value
 */
sealed class RequestState<out V, out E : Any> {
    object Idle : RequestState<Nothing, Nothing>()
    object Loading : RequestState<Nothing, Nothing>()
    data class Success<out SV>(val value: SV) : RequestState<SV, Nothing>()
    data class Error<out EV : Any>(val value: EV) : RequestState<Nothing, EV>()
}

@Composable
inline fun <T, E : Any> RequestStateRender(
    state: State<RequestState<T, E>>,
    onIdle: @Composable (() -> Unit) = {},
    onSuccess: @Composable (T) -> Unit,
    onError: @Composable ((E) -> Unit) = {},
    onLoading: @Composable (() -> Unit) = {}
) {
    when (val itemValue = state.value) {
        is RequestState.Success -> {
            onSuccess.invoke(
                itemValue.value
            )
        }
        is RequestState.Idle -> {
            onIdle.invoke()
        }
        is RequestState.Error -> onError.invoke(
            itemValue.value
        )
        RequestState.Loading -> onLoading.invoke()
    }
}
