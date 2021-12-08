package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class PaddedScore(
  val purchase_price: Float,
  val average_score: Float,
  val values: ArrayList<Score>,
)