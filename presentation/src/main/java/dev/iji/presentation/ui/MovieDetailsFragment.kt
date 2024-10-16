package dev.iji.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dev.iji.presentation.R
import dev.iji.presentation.databinding.FragmentDetailsMovieBinding
import dev.iji.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.math.RoundingMode

class MovieDetailsFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.selectedMovie.flowWithLifecycle(lifecycle).collectLatest {
                it?.let { movie ->
                    with(binding) {
                        Glide
                            .with(moviePreviewImage)
                            .load(movie.imageUrl)
                            .error(R.drawable.movie_preview_error)
                            .into(moviePreviewImage)
                        movieTitle.text = movie.localizedName
                        movieGenre.text = combineGenres(movie.genres)
                        movieYear.text = combineYear(movie.year)
                        movieRating.text = calculateRating(movie.rating)
                        movieDescription.text = movie.description
                        (activity as MainActivity).supportActionBar?.title = movie.name
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsMovieBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun calculateRating(rating: Double?): String {
        return when (rating) {
            null -> resources.getString(R.string.unknown_rating)
            else -> "${rating.toBigDecimal().setScale(1, RoundingMode.HALF_UP)}"
        }
    }

    private fun combineGenres(genres: List<String?>): String {
        return if (genres.isNotEmpty()) genres.joinToString(separator = ", ") + ", " else ""
    }

    private fun combineYear(year: Int?): String {
        return "$year " + resources.getString(R.string.year)
    }
}