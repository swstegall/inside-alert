package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class Value(
  val symbol: String,
  val qty: Long,
  val current_price: Float,
  val time: String,
)