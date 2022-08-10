package com.gnb.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnb.domain.entity.ui.ProductUI
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.rate.GetAndSaveRatesUseCase
import com.gnb.domain.usecase.transaction.GetProductsUseCase
import com.gnb.ui.ViewModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private var productsUseCase: GetProductsUseCase,
    private var saveRatesUseCase: GetAndSaveRatesUseCase
) : ViewModel() {

    // Livedata
    private val _products: MutableLiveData<MutableList<ProductUI>> = MutableLiveData()
    val products: LiveData<MutableList<ProductUI>> = _products
    private val _loader: MutableLiveData<Boolean> = MutableLiveData()
    val loader: LiveData<Boolean> = _loader
    private val _error: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val error: LiveData<ViewModelResponse> = _error

    fun getProducts() {
        viewModelScope.launch {
            _loader.postValue(true)
            loadData()
            _loader.postValue(false)
        }
    }

    private suspend fun loadData() {
        // Load and save rates to local datasource
        saveRatesUseCase.getAndSaveRates()

        // Load products
        when (val response = productsUseCase.getProducts()) {
            is UseCaseResponse.Success -> {
                _products.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _error.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _error.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _error.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }
}