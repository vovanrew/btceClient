package common.marketdata

import java.util.Date

import org.knowm.xchange.dto.trade.LimitOrder

case class OrderBook(timestamp: Date,
                     asks: List[LimitOrder],
                     bids: List[LimitOrder]) extends MarketData