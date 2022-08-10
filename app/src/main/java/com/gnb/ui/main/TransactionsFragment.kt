package com.gnb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnb.R
import com.gnb.databinding.FragmentTransactionsBinding
import com.gnb.domain.entity.ui.TransactionUI
import com.gnb.ui.ViewModelResponse
import com.gnb.ui.main.adapters.LaunchListAdapter
import com.gnb.ui.main.viewmodels.TransactionsViewModel
import com.gnb.util.Constants.SELECTED_CURRENCY
import com.gnb.util.gone
import com.gnb.util.haveConnection
import com.gnb.util.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransactionsFragment : Fragment() {
    // Properties
    private lateinit var transactionsAdapter: LaunchListAdapter
    private lateinit var sku: String

    // View model
    private val viewModel: TransactionsViewModel by viewModels()

    // Content
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveData()
        setListeners()

        // Get bundle sku param and load data
        sku = arguments?.getString("sku")!!
        viewModel.loadTransactions(sku, SELECTED_CURRENCY)

        // Changing toolbar title
        (activity as MainActivity).changeToolbarTitle(sku)
    }

    private fun setListeners() {
        setAdapter()
        binding.btnReload.setOnClickListener { viewModel.loadTransactions(sku, SELECTED_CURRENCY) }
    }

    private fun setAdapter() {
        // Create adapter
        transactionsAdapter = LaunchListAdapter()
        transactionsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Set LayoutManager
        binding.recyclerProducts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerProducts.adapter = transactionsAdapter
        binding.recyclerProducts.isNestedScrollingEnabled = false
    }

    private fun liveData() {
        // Remove if any observer attached
        viewModel.transactions.removeObservers(viewLifecycleOwner)
        viewModel.loader.removeObservers(viewLifecycleOwner)
        viewModel.transactionsError.removeObservers(viewLifecycleOwner)
        viewModel.sum.removeObservers(viewLifecycleOwner)

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            if (loader) binding.linearLoading.show()
            else binding.linearLoading.gone()
        }

        viewModel.transactions.observe(viewLifecycleOwner) { launches ->
            showProducts(launches)
        }

        viewModel.transactionsError.observe(viewLifecycleOwner) { error ->
            showProductsError(error)
        }

        viewModel.sum.observe(viewLifecycleOwner) { sum ->
            binding.txtTotal.text = getString(R.string.transactions_sum, sum, SELECTED_CURRENCY)
        }
    }

    private fun showProductsError(error: ViewModelResponse) {
        when (error) {
            ViewModelResponse.NULL_EMPTY_DATA -> showProductsUIError(getText(R.string.error_no_products))
            ViewModelResponse.NO_NETWORK -> {
                if (!haveConnection(requireContext())) showProductsUIError(getText(R.string.error_no_internet))
                else showProductsUIError(getText(R.string.error_something_wrong))
            }
            ViewModelResponse.GENERIC_ERROR -> showProductsUIError(getText(R.string.error_something_wrong))
        }
    }

    private fun showProductsUIError(text: CharSequence) {
        binding.linearError.show()
        binding.txtError.text = text
        binding.constraintContent.gone()
    }

    private fun showProducts(transactions: MutableList<TransactionUI>) {
        // Update transactions list
        transactions.let { transactionsAdapter.submitList(it) }
        binding.recyclerProducts.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


