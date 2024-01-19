package com.example.tickerapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tickerapp.R
import com.example.tickerapp.databinding.FragmentTickerDetailsBinding
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action.NavigateBack
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action.ShowSomethingWentWrongAlert
import com.example.tickerapp.presentation.details.TickerDetailsContact.State
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.BackClicked
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.ChangeFavouriteState
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.Init
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.android.x.viewmodel.viewModel

class TickerDetailsFragment : Fragment(), DIAware {

    private var _binding: FragmentTickerDetailsBinding? = null
    private val binding get() = _binding!!

    override val di: DI by closestDI()
    private val viewModel: TickerDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTickerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.state.collect(::handleState) }
                launch { viewModel.action.collect(::handleAction) }
            }
        }
        binding.buttonFavAction.setOnClickListener { viewModel.viewEvent(ChangeFavouriteState) }
        binding.toolbar.setNavigationOnClickListener { viewModel.viewEvent(BackClicked) }
    }

    override fun onStart() {
        super.onStart()
        val arguments = arguments
        if (arguments != null) {
            val ticker = TickerDetailsFragmentArgs.fromBundle(arguments).ticker
            viewModel.viewEvent(Init(ticker))
        }
    }

    private fun handleState(state: State) {
        with(binding) {
            state.tickerDetailsUI?.let {
                tickerValue.text = it.ticker
                tickerNameValue.text = it.name
                homepageValue.text = it.homepageUrl
                marketValue.text = it.market
                phoneValue.text = it.phoneNumber
            }
            val buttonTextRes =
                if (state.isFavourite) R.string.remove_from_favourites else R.string.add_to_favourites
            buttonFavAction.text = getString(buttonTextRes)
        }
    }

    private fun handleAction(action: Action) {
        when (action) {
            NavigateBack -> navigateBack()
            is ShowSomethingWentWrongAlert -> showSomethingWentWrongAlert()
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun showSomethingWentWrongAlert() {
        Toast.makeText(
            requireContext(),
            getString(R.string.something_went_wrong),
            Toast.LENGTH_SHORT
        ).show()
    }
}