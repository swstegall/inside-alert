package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class Order(
  val id: String,
  val client_order_id: String,
  val created_at: String?,
  val updated_at: String?,
  val submitted_at: String?,
  val filled_at: String?,
  val expired_at: String?,
  val canceled_at: String?,
  val failed_at: String?,
  val replaced_at: String?,
  val replaced_by: String?,
  val replaces: String?,
  val asset_id: String,
  val symbol: String,
  val asset_class: String,
  val notional: String?,
  val qty: String,
  val filled_qty: String,
  val filled_avg_price: String?,
  val order_class: String,
  val order_type: String,
  val type: String,
  val side: String,
  val time_in_force: String?,
  val limit_price: String?,
  val stop_price: String?,
  val status: String,
  val extended_hours: Boolean,
  val legs: ArrayList<Order>?,
  val trail_percent: String?,
  val trail_price: String?,
  val hwm: String?,
)