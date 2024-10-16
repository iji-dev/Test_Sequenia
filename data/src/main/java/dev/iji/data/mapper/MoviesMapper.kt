package dev.iji.data.mapper

import dev.iji.data.dto.MoviesDto
import dev.iji.domain.model.MovieModel

object MoviesMapper {

    fun MoviesDto.MovieDto.toMovieModel() = MovieModel(
        description = description,
        genres = genres,
        id = id,
        imageUrl = imageUrl,
        localizedName = localizedName,
        name = name,
        rating = rating,
        year = year
    )
}