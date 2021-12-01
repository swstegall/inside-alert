package me.stegall.data

import kotlinx.serialization.Serializable

@Serializable
data class TradeResponse(
  val t: String,
  val x: String,
  val p: Float,
  val s: Long,
  val c: List<String>,
  val i: Long,
  val z: String,
)