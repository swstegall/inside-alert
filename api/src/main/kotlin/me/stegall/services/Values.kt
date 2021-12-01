package me.stegall.services

import me.stegall.data.Position
import me.stegall.data.Value

class Values {
  fun toValue(position: Position, time: String): Value {
    return Value(position.symbol, position.qty.toLong(), position.current_price.toFloat(), time)
  }
  fun toValues(positions: ArrayList<Position>, time: String): ArrayList<Value> {
    return ArrayList<Value>(positions.map { Value(it.symbol, it.qty.toLong(), it.current_price.toFloat(), time) })
  }
}