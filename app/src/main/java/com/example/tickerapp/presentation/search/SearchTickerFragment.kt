package com.example.tickerapp.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tickerapp.R
import com.example.tickerapp.databinding.FragmentSearchTickerBinding
import com.example.tickerapp.presentation.adapter.TickerAdapter
import com.example.tickerapp.presentation.model.TickerUI
import com.example.tickerapp.presentation.search.SearchTickerContract.Action
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateBack
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateToTickerDetails
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.ShowSomethingWentWrongAlert
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.ShowTickerAlreadyAddedAlert
import com.example.tickerapp.presentation.search.SearchTickerContract.State
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.AddToFavourites
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.BackClicked
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.FavouriteTickerClicked
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.SearchTickerByName
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class SearchTickerFragment : Fragment(), DIAware {

    private var _binding: FragmentSearchTickerBinding? = null
    private val binding get() = _binding!!

    override val di: DI by closestDI()
    private val viewModel: SearchTickerViewModel by viewModel()

    private val adapter by lazy {
        TickerAdapter(
            onButtonClick = { viewModel.viewEvent(AddToFavourites(it)) },
            onItemClick = { viewModel.viewEvent(FavouriteTickerClicked(it)) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchTickerBinding.inflate(inflater, container, false)
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
        with(binding) {
            toolbar.setNavigationOnClickListener {
                viewModel.viewEvent(BackClicked)
            }
            tickers.adapter = adapter
            textInput.doOnTextChanged { text, _, _, _ ->
                viewModel.viewEvent(SearchTickerByName(text.toString()))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleState(state: State) {
        with(binding) {
            if (state.tickers.isNotEmpty()) {
                tickers.isVisible = true
                noTickers.isVisible = false
                adapter.submitList(state.tickers)
            } else {
                tickers.isVisible = false
                noTickers.isVisible = true
            }
        }
    }

    private fun handleAction(action: Action) {
        when (action) {
            NavigateBack -> navigateBack()
            is NavigateToTickerDetails -> toTickersDetails(action.ticker)
            is ShowTickerAlreadyAddedAlert -> showTickerAlreadyAddedAlert()
            is ShowSomethingWentWrongAlert -> showSomethingWentWrongAlert()
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun toTickersDetails(ticker: TickerUI) {
        val actionNav =
            SearchTickerFragmentDirections.actionSearchTickerFragmentToTickerDetailsFragment(
                ticker = ticker
            )
        findNavController().navigate(actionNav)
    }

    private fun showTickerAlreadyAddedAlert() {
        Toast.makeText(
            requireContext(),
            getString(R.string.ticker_already_added),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showSomethingWentWrongAlert() {
        Toast.makeText(
            requireContext(),
            getString(R.string.something_went_wrong),
            Toast.LENGTH_SHORT
        ).show()
    }
}