package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class Value(
  val symbol: String,
  val qty: String,
  val current_price: String,
  val time: String,
)