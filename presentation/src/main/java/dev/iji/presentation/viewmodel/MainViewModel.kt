package dev.iji.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.iji.domain.model.MovieModel
import dev.iji.domain.usecases.GetMovies
import dev.iji.presentation.R
import dev.iji.presentation.event.UiEvent
import dev.iji.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(private val getMovies: GetMovies) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val _errorMessage = MutableStateFlow<Int?>(null)
    private val _genreQuery = MutableStateFlow<String?>(null)

    val uiState = combine(
        _movies,
        _errorMessage,
        _genreQuery,
        ::combineState
    ).stateIn(
        scope = viewModelScope,
        initialValue = UiState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    var genres = MutableStateFlow<List<String>>(emptyList())
        private set

    var selectedMovie = MutableStateFlow<MovieModel?>(null)
        private set

    var selectedGenre: String? = null
        private set

    init {
        requestMovies()
    }

    private fun combineState(
        movies: List<MovieModel>,
        errorMessage: Int?,
        genreQuery: String?
    ) = UiState(
        movies = if (genreQuery != null) movies.filter { it.genres.contains(genreQuery) } else movies,
        isLoading = movies.isEmpty(),
        errorMessage = errorMessage
    )

    private fun requestMovies() {
        viewModelScope.launch {
            getMovies()
                .onSuccess { movies ->
                    genres.value = getGenres(movies)
                    _movies.value = movies
                    _errorMessage.value = null
                }

                .onFailure {
                    _errorMessage.value = when (it) {
                        is IOException -> R.string.error_internet_connection
                        else -> R.string.empty
                    }
                }
        }
    }

    private fun showMovie(movie: MovieModel?) {
        selectedMovie.value = movie
    }

    private fun filterByGenre(genre: String?) {
        _genreQuery.value = genre?.replaceFirstChar(Char::lowercase)
        selectedGenre = genre
    }

    private fun getGenres(movies: List<MovieModel>): List<String> {
        return mutableListOf<String>().apply {
            movies.forEach { movie ->
                movie.genres.forEach { genre ->
                    add(genre.replaceFirstChar(Char::uppercase))
                }
            }
        }
    }

    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.ShowMovie -> showMovie(uiEvent.movie)
            is UiEvent.FilterByGenre -> filterByGenre(uiEvent.genre)
            is UiEvent.ReloadMovies -> requestMovies()
        }
    }
}