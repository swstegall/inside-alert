package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class Score(
  val symbol: String,
  val current_price: Float,
  val score: Float,
  val time: String,
)