package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class TradeActivity(
  val activity_type: String,
  val cum_qty: String,
  val id: String,
  val leaves_qty: String,
  val price: String,
  val qty: String,
  val side: String,
  val symbol: String,
  val transaction_time: String,
  val order_id: String,
  val order_status: String,
  val type: String
)