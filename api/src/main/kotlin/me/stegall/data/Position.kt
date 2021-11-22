package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class Position(
  val asset_id: String,
  val symbol: String,
  val exchange: String,
  val asset_class: String,
  val asset_marginable: Boolean,
  val qty: String,
  val avg_entry_price: String,
  val side: String,
  val market_value: String,
  val cost_basis: String,
  val unrealized_pl: String,
  val unrealized_plpc: String,
  val unrealized_intraday_pl: String,
  val unrealized_intraday_plpc: String,
  val current_price: String,
  val lastday_price: String,
  val change_today: String,
)