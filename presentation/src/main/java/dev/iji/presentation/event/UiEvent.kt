package dev.iji.presentation.event

import dev.iji.domain.model.MovieModel

sealed class UiEvent(
    val movie: MovieModel? = null,
    val genre: String? = null
) {

    class ShowMovie(movie: MovieModel) : UiEvent(movie = movie)
    class FilterByGenre(genre: String?) : UiEvent(genre = genre)
    class ReloadMovies : UiEvent()
}