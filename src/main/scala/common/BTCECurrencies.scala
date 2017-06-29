package common

import org.knowm.xchange.currency.{Currency, CurrencyPair}


object BTCECurrencies {

  val BTC_USD: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.USD)
  val BTC_ETH: CurrencyPair = new CurrencyPair(Currency.ETH, Currency.BTC)

  def btcFirst(currencyPair: String): String = {

    if(currencyPair.startsWith("BTC")) currencyPair
    else {
      val secondCurrency = currencyPair
        .substring(0, currencyPair.lastIndexOf("/"))

      currencyPair
        .substring(currencyPair.lastIndexOf("/") + 1, currencyPair.size)
        .concat("/" + secondCurrency)
    }
  }
}
