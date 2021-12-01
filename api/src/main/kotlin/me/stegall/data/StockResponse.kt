package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class StockResponse(
  val trades: List<TradeResponse>,
  val symbol: String,
  val next_page_token: String,
)