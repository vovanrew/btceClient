package common.marketdata

import java.math.BigDecimal
import java.util.Date

import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.Order.OrderType


case class Trade(orderType: OrderType,
                 tradableAmount: BigDecimal,
                 currencyPair: CurrencyPair,
                 price: BigDecimal,
                 timestamp: Date,
                 id: String) extends MarketData