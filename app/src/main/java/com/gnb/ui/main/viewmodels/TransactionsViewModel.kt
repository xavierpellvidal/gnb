package com.gnb.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnb.domain.entity.ui.RateUI
import com.gnb.domain.entity.ui.TransactionUI
import com.gnb.domain.usecase.UseCaseResponse
import com.gnb.domain.usecase.mapListTo
import com.gnb.domain.usecase.rate.GetRatesUseCase
import com.gnb.domain.usecase.rate.SaveRateUseCase
import com.gnb.domain.usecase.transaction.GetTransactionsUseCase
import com.gnb.ui.ViewModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private var ratesUseCase: GetRatesUseCase,
    private var transactionsUseCase: GetTransactionsUseCase,
    private var saveRateUseCase: SaveRateUseCase,
) : ViewModel() {

    // Livedata
    private val _transactions: MutableLiveData<MutableList<TransactionUI>> = MutableLiveData()
    val transactions: LiveData<MutableList<TransactionUI>> = _transactions
    private val _loader: MutableLiveData<Boolean> = MutableLiveData()
    val loader: LiveData<Boolean> = _loader
    private val _transactionsError: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val transactionsError: LiveData<ViewModelResponse> = _transactionsError
    private val _sum: MutableLiveData<String> = MutableLiveData()
    val sum: LiveData<String> = _sum

    fun loadTransactions(sku: String, toCurrency: String) {
        viewModelScope.launch {
            _loader.postValue(true)
            loadData(sku, toCurrency)
            _loader.postValue(false)
        }
    }

    /**
     * Load rates to get transactions by [sku] converted to [toCurrency]
     */
    private suspend fun loadData(sku: String, toCurrency: String) {
        // Load rates to let convert transactions
        when (val response = ratesUseCase.getRates()) {
            is UseCaseResponse.Success -> loadTransactions(sku, response.data, toCurrency)
            is UseCaseResponse.NoData -> _transactionsError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _transactionsError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _transactionsError.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }

    /**
     * Load transactions by [sku] and converted them to [toCurrency] using [rates].
     * New rates saved in datasource.
     */
    private suspend fun loadTransactions(sku: String, rates: MutableList<RateUI>, toCurrency: String) =
        when (val response = transactionsUseCase.getTransactions(sku).mapListTo()) {
            is UseCaseResponse.Success -> {
                response.data.map { transaction ->
                    var rate: RateUI?

                    // Have to convert transaction
                    if(transaction.currency != toCurrency) {
                        // Search for rate or create it to make conversion
                        rate = rates.find { it.to == toCurrency && it.from == transaction.currency }
                            ?: findConversion(RateUI(transaction.currency, toCurrency, ""), rates)
                        when (rate) {
                            null -> _transactionsError.postValue(ViewModelResponse.GENERIC_ERROR)
                            else -> {
                                // Calculate conversion and save it to datasource
                                transaction.convertedAmount = (BigDecimal(transaction.amount) * BigDecimal(rate.rate)).setScale(2, RoundingMode.HALF_EVEN).toPlainString()
                                saveRateUseCase.saveRate(rate)
                            }
                        }
                    } else transaction.convertedAmount = transaction.amount

                    transaction.convertedCurrency = toCurrency
                    transaction
                }

                // Post sum value
                _sum.postValue(response.data.sumOf{ BigDecimal(it.convertedAmount)}.toPlainString())

                // Post value to view
                _transactions.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _transactionsError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _transactionsError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _transactionsError.postValue(ViewModelResponse.GENERIC_ERROR)
        }

    /**
     * Calculate new rate using [_rates] to get [desiredRate] and save it to [_rates]
     */
    private fun findConversion(desiredRate: RateUI, _rates: MutableList<RateUI>): RateUI? {
        var result: RateUI = desiredRate.copy()
        var rates = _rates.toMutableList()

        run breaking@{
            rates.filter { it.to == desiredRate.to }.forEach {
                result.from = it.from
                result.rate = it.rate

                //remove current rate
                rates.remove(it)
                rates.filter { it.from == desiredRate.from }.forEach {
                    //Finish
                    if (it.from == desiredRate.from) {
                        result.from = it.from
                        result.rate = (BigDecimal(result.rate) * BigDecimal(it.rate)).toPlainString()
                        _rates.add(result.copy())
                        return@breaking
                    }
                }
            }
        }

        return result
    }
}