package com.example.tickerapp.presentation.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tickerapp.R
import com.example.tickerapp.databinding.FragmentFavouriteTickersBinding
import com.example.tickerapp.presentation.adapter.TickerAdapter
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action.NavigateToSearch
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action.NavigateToTickerDetails
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.State
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.AddClicked
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.FavouriteTickerClicked
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.Init
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.RemoveFromFavourites
import com.example.tickerapp.presentation.model.TickerUI
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class FavouriteTickersFragment : Fragment(), DIAware {

    private var _binding: FragmentFavouriteTickersBinding? = null
    private val binding get() = _binding!!

    override val di: DI by closestDI()
    private val viewModel: FavouriteTickersViewModel by viewModel()

    private val adapter by lazy {
        TickerAdapter(
            onButtonClick = { viewModel.viewEvent(RemoveFromFavourites(it)) },
            onItemClick = { viewModel.viewEvent(FavouriteTickerClicked(it)) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteTickersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.state.collect(::handleState) }
                launch { viewModel.action.collect(::handleAction) }
            }
        }
        binding.favouriteTickers.adapter = adapter
        binding.fabAdd.setOnClickListener {
            viewModel.viewEvent(AddClicked)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.viewEvent(Init)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleState(state: State) {
        with(binding) {
            if (state.tickers.isNotEmpty()) {
                favouriteTickers.isVisible = true
                noTickers.isVisible = false
                adapter.submitList(state.tickers)
            } else {
                favouriteTickers.isVisible = false
                noTickers.isVisible = true
            }
        }
    }

    private fun handleAction(action: Action) {
        when (action) {
            NavigateToSearch -> navigateToSearch()
            is NavigateToTickerDetails -> toTickersDetails(action.ticker)
        }
    }

    private fun navigateToSearch() {
        findNavController().navigate(R.id.action_favouriteTickersFragment_to_searchTickerFragment)
    }

    private fun toTickersDetails(ticker: TickerUI) {
        val actionNav =
            FavouriteTickersFragmentDirections.actionFavouriteTickersFragmentToTickerDetailsFragment(
                ticker = ticker
            )
        findNavController().navigate(actionNav)
    }
}