package com.prashant.dummy4

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("v1/latest?apikey=klyF010HJOsNqBEK5qDRrZwegJ0XPkXjyd3bvMP0") // Replace YOUR_API_KEY with your actual API key
    suspend fun getExchangeRates(@Query("base") baseCurrency: String): ExchangeRateResponse
}
