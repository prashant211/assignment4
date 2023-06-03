package com.prashant.dummy4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class MainScreen : AppCompatActivity() {
    lateinit var edittext: EditText
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var button: Button
    lateinit var resulttextview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        edittext = findViewById(R.id.edittext)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        button = findViewById(R.id.button)
        resulttextview = findViewById(R.id.resultTextView)


        val currencyList = listOf(
            "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD", "NOK", "MXN", "SGD", "HKD", "KRW", "INR"
        )


        // Create an ArrayAdapter for the sourceCurrencySpinner
        val sourceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = sourceAdapter

        // Create an ArrayAdapter for the targetCurrencySpinner
        val targetAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = targetAdapter

        button.setOnClickListener {
            val amount = edittext.text.toString().toDoubleOrNull()
            val sourceCurrency = spinner1.selectedItem.toString()
            val targetCurrency = spinner2.selectedItem.toString()

            if (amount != null) {
                convertCurrency(amount, sourceCurrency, targetCurrency)
            }else{
                Toast.makeText(this,"Please enter the Amount",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun convertCurrency(amount: Double, sourceCurrency: String, targetCurrency: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getExchangeRates(sourceCurrency)
                }

                val rate = response.rates[targetCurrency]
                if (rate != null) {
                    val convertedAmount = amount * rate
                    resulttextview.text = getString(
                        R.string.converted_amount,
                        convertedAmount,
                        targetCurrency
                    )
                } else {
                    resulttextview.text = getString(R.string.error_conversion)


                }
             } catch (e: IOException) {
            // Handle IO exceptions (e.g., network connection error)
            resulttextview.text = getString(R.string.error_network)
        } catch (e: HttpException) {
            // Handle HTTP exceptions (e.g., server error)
            resulttextview.text = getString(R.string.error_server)
        } catch (e: Exception) {
            // Handle other exceptions
            resulttextview.text = getString(R.string.error_generic)
        }
        }
    }
}

