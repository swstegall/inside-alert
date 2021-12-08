package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class PaddedScore(
  val purchase_price: Float,
  val values: ArrayList<Score>,
)