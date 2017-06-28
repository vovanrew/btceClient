package common

import org.knowm.xchange.currency.{Currency, CurrencyPair}


object BTCECurrencies {

  val BTC_USD: CurrencyPair = new CurrencyPair(Currency.BTC, Currency.USD)
  val BTC_ETH: CurrencyPair = new CurrencyPair(Currency.ETH, Currency.BTC)
}
