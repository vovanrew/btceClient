package common.marketdata

import java.util.Date

import org.knowm.xchange.currency.CurrencyPair


case class Ticker(currencyPair: CurrencyPair,
                  last: BigDecimal,
                  bid: BigDecimal,
                  ask: BigDecimal,
                  high: BigDecimal,
                  low: BigDecimal,
                  vwap: BigDecimal,
                  volume: BigDecimal,
                  timestamp: BigDecimal) extends MarketData