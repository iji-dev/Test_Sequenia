package dev.iji.presentation.state

import dev.iji.domain.model.MovieModel

data class UiState(
    val movies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: Int? = null
)