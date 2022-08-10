package com.gnb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnb.R
import com.gnb.databinding.FragmentProductsBinding
import com.gnb.domain.entity.ui.ProductUI
import com.gnb.ui.ViewModelResponse
import com.gnb.ui.main.adapters.ProductsListAdapter
import com.gnb.ui.main.viewmodels.ProductsViewModel
import com.gnb.util.gone
import com.gnb.util.haveConnection
import com.gnb.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(), (ProductUI) -> Unit {
    // Properties
    private lateinit var productsAdapter: ProductsListAdapter
    private var firstLoad = true

    // View model
    private val viewModel: ProductsViewModel by viewModels()

    // Content
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setListeners()
        liveData()

        //Only load data first time fragment created
        if(firstLoad) {
            viewModel.getProducts()
            firstLoad = !firstLoad
        }
    }

    private fun setAdapter() {
        // Create adapter
        productsAdapter = ProductsListAdapter(this)
        productsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Load info
        binding.listProducts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listProducts.adapter = productsAdapter
    }

    private fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            reloadProducts()
        }

        binding.btnReload.setOnClickListener {
            reloadProducts()
        }
    }

    private fun reloadProducts() {
        binding.linearError.gone()
        viewModel.getProducts()
    }

    private fun liveData() {
        // Remove if any observer attached
        viewModel.products.removeObservers(viewLifecycleOwner)
        viewModel.loader.removeObservers(viewLifecycleOwner)
        viewModel.error.removeObservers(viewLifecycleOwner)

        viewModel.products.observe(viewLifecycleOwner) { products ->
            showProducts(products)
        }

        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            binding.swipeRefresh.isRefreshing = loader
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            showError(error)
        }
    }

    private fun showError(error: ViewModelResponse) {
        when (error) {
            ViewModelResponse.NULL_EMPTY_DATA -> showUIError(getText(R.string.error_no_products))
            ViewModelResponse.NO_NETWORK -> {
                if (!haveConnection(requireContext())) showUIError(getText(R.string.error_no_internet))
                else showUIError(getText(R.string.error_something_wrong))
            }
            ViewModelResponse.GENERIC_ERROR -> showUIError(getText(R.string.error_something_wrong))
        }
    }

    private fun showUIError(text: CharSequence) {
        binding.linearError.show()
        binding.txtError.text = text
        binding.principalConstraint.gone()
    }

    private fun showProducts(products: MutableList<ProductUI>) {
        // Update products list
        products.let { productsAdapter.submitList(it) }

        // Show results
        binding.linearError.gone()
        binding.principalConstraint.show()
        binding.swipeRefresh.isRefreshing = false
    }

    /**
     * Adapter item [productClicked] clicked.
     */
    override fun invoke(productClicked: ProductUI) {
        val bundle = bundleOf("sku" to productClicked.sku)
        findNavController(binding.root).navigate(
            R.id.action_ProductsFragment_to_TransactionsFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}