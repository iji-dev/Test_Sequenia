package dev.iji.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.iji.presentation.R
import dev.iji.presentation.adapter.MovieListAdapter
import dev.iji.presentation.databinding.FragmentFeedMoviesBinding
import dev.iji.presentation.event.UiEvent
import dev.iji.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFeedFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private lateinit var snackbar: Snackbar
    private lateinit var movieAdapter: MovieListAdapter
    private lateinit var genresAdapter: ArrayAdapter<String>

    private var _binding: FragmentFeedMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { ui ->
                movieAdapter.submitList(ui.movies.sortedBy { it.localizedName })

                with(binding) {
                    feedGroup.isVisible = !ui.isLoading && ui.errorMessage == null
                    progressBar.isVisible = ui.isLoading && ui.errorMessage == null
                }

                ui.errorMessage?.let { snackbar.show() }
            }
        }

        lifecycleScope.launch {
            viewModel.genres.flowWithLifecycle(lifecycle).collectLatest { genres ->
                genresAdapter = initGenresAdapter(genres)
                binding.genresList.adapter = genresAdapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFeedMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        with(binding) {
            movieCardsList.adapter = movieAdapter

            genresCategory.setOnClickListener {
                movieCardsList.smoothScrollToPosition(0)
                genresList.isGone = !genresList.isGone
            }

            genresList.setOnItemClickListener { parent, _, position, _ ->

                if (parent.getItemAtPosition(position) as String == viewModel.selectedGenre) {
                    viewModel.onEvent(UiEvent.FilterByGenre(null))
                    genresList.adapter = genresAdapter
                } else
                    viewModel.onEvent(UiEvent.FilterByGenre(parent.getItemAtPosition(position) as String))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initGenresAdapter(genres: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            R.layout.layout_list_item,
            genres.distinct().sortedBy { it[0] }
        )
    }

    private fun init() {
        snackbar = Snackbar
            .make(
                requireActivity().window.decorView.rootView,
                resources.getString(R.string.error_internet_connection),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(resources.getString(R.string.repeat)) {
                viewModel.onEvent(UiEvent.ReloadMovies())
            }

        movieAdapter = MovieListAdapter {
            viewModel.onEvent(UiEvent.ShowMovie(it))
            findNavController().navigate(R.id.action_MovieFeedFragment_to_MovieDetailsFragment)
        }
    }
}