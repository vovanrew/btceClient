package common.marketdata

import org.knowm.xchange.dto.marketdata
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType


case class Trades(trades: List[marketdata.Trade],
                  lastID: Long,
                  tradeSortType: TradeSortType) extends MarketData
