package dev.iji.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesDto(
    @Json(name = "films")
    val movies: List<MovieDto>
) {

    @JsonClass(generateAdapter = true)
    data class MovieDto(
        @Json(name = "description")
        val description: String?,

        @Json(name = "genres")
        val genres: List<String>,

        @Json(name = "id")
        val id: Int?,

        @Json(name = "image_url")
        val imageUrl: String?,

        @Json(name = "localized_name")
        val localizedName: String?,

        @Json(name = "name")
        val name: String?,

        @Json(name = "rating")
        val rating: Double?,

        @Json(name = "year")
        val year: Int?
    )
}