package dev.iji.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.iji.domain.model.MovieModel
import dev.iji.presentation.R
import dev.iji.presentation.databinding.LayoutCardMovieBinding

class MovieListAdapter(
    private val onItemClicked: (MovieModel) -> Unit
) : ListAdapter<MovieModel, MovieListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = LayoutCardMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding) { onItemClicked(it) }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<MovieModel>() {
        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel) =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: LayoutCardMovieBinding,
        onItemClicked: (MovieModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var movie: MovieModel

        init {
            itemView.setOnClickListener {
                onItemClicked(movie)
            }
        }

        fun bind(movie: MovieModel) {

            this.movie = movie

            with(binding) {
                with(moviePreviewImage) {
                    Glide
                        .with(this)
                        .load(movie.imageUrl)
                        .error(R.drawable.movie_preview_error)
                        .into(this)
                }
                movieTitle.text = movie.localizedName
            }
        }
    }
}