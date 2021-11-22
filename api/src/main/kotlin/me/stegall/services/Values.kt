package me.stegall.services

import me.stegall.data.Position
import me.stegall.data.Value

class Values {
  fun toValue(position: Position, time: String): Value {
    return Value(position.symbol, position.qty, position.current_price, time)
  }
  fun toValues(positions: ArrayList<Position>, time: String): ArrayList<Value> {
    return ArrayList<Value>(positions.map { Value(it.symbol, it.qty, it.current_price, time) })
  }
}