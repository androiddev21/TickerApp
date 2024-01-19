package com.example.tickerapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tickerapp.R
import com.example.tickerapp.databinding.ItemTickerBinding
import com.example.tickerapp.databinding.ItemTickerFavouriteBinding
import com.example.tickerapp.presentation.model.TickerUI

class TickerAdapter(
    private val onItemClick: (item: TickerUI) -> Unit,
    private val onButtonClick: (item: TickerUI) -> Unit
) : ListAdapter<TickerUI, TickerAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder =
            when (viewType) {
                R.layout.item_ticker -> {
                    val binding = ItemTickerBinding.inflate(inflater, parent, false)
                    val viewHolder = ViewHolder.NonFavouriteViewHolder(binding)
                    val onItemClick = { _: View ->
                        viewHolder.onItemClicked {
                            onItemClick(it)
                        }
                    }
                    binding.root.setOnClickListener(onItemClick)
                    val onButtonClick = { _: View ->
                        viewHolder.onItemClicked {
                            onButtonClick(it)
                        }
                    }
                    binding.buttonAdd.setOnClickListener(onButtonClick)
                    viewHolder
                }

                R.layout.item_ticker_favourite -> {
                    val binding = ItemTickerFavouriteBinding.inflate(inflater, parent, false)
                    val viewHolder = ViewHolder.FavouriteViewHolder(binding)
                    val onItemClick = { _: View ->
                        viewHolder.onItemClicked {
                            onItemClick(it)
                        }
                    }
                    binding.root.setOnClickListener(onItemClick)
                    val onButtonClick = { _: View ->
                        viewHolder.onItemClicked {
                            onButtonClick(it)
                        }
                    }
                    binding.buttonRemove.setOnClickListener(onButtonClick)
                    viewHolder
                }

                else -> error(
                    "Unsupported viewType ${
                        parent.context.resources.getResourceName(
                            viewType
                        )
                    }"
                )
            }
        return viewHolder
    }

    private inline fun ViewHolder.onItemClicked(block: (TickerUI) -> Unit) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            val item = getItem(bindingAdapterPosition)
            check(item is TickerUI)
            block(item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            item.isFavourite -> {
                check(holder is ViewHolder.FavouriteViewHolder)
                holder.bindFavouriteHolder(item)
            }

            else -> {
                check(holder is ViewHolder.NonFavouriteViewHolder)
                holder.bindNonFavouriteHolder(item)
            }
        }
    }

    private fun ViewHolder.FavouriteViewHolder.bindFavouriteHolder(
        item: TickerUI
    ) {
        with(binding) {
            val context = binding.root.context
            tickerValue.text = context.getString(R.string.ticker_pattern, item.ticker)
            tickerNameValue.text = context.getString(R.string.ticker_name_pattern, item.name)
        }
    }

    private fun ViewHolder.NonFavouriteViewHolder.bindNonFavouriteHolder(
        item: TickerUI
    ) {
        with(binding) {
            val context = binding.root.context
            tickerValue.text = context.getString(R.string.ticker_pattern, item.ticker)
            tickerNameValue.text = context.getString(R.string.ticker_name_pattern, item.name)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.isFavourite -> R.layout.item_ticker_favourite
            else -> R.layout.item_ticker
        }
    }

    sealed class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        class FavouriteViewHolder(val binding: ItemTickerFavouriteBinding) :
            ViewHolder(binding.root)

        class NonFavouriteViewHolder(val binding: ItemTickerBinding) :
            ViewHolder(binding.root)
    }

    private object DiffUtilCallback : DiffUtil.ItemCallback<TickerUI>() {
        override fun areItemsTheSame(
            oldItem: TickerUI,
            newItem: TickerUI
        ): Boolean {
            return oldItem.ticker == newItem.ticker
        }

        override fun areContentsTheSame(
            oldItem: TickerUI,
            newItem: TickerUI
        ): Boolean {
            return oldItem == newItem
        }
    }
}