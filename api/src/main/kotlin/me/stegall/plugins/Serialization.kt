package me.stegall.plugins

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import me.stegall.data.*
import me.stegall.services.Values
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun average(values: ArrayList<Float>): Float {
  var counter = 0.0f
  for (value in values) {
    counter += value
  }
  return counter / values.size
}

fun generate_score(original: Float, current: Float): Float {
  return current / original
}

fun Application.configureSerialization() {
  val values = Values()

  install(ContentNegotiation) {
    json()
  }

  routing {
    get("/values") {
      val valuesMap = HashMap<String, ArrayList<Value>>()
      if (call.request.headers["APCA-API-KEY-ID"] != null && call.request.headers["APCA-API-SECRET-KEY"] != null) {
        val (_request, _response, result) = "https://paper-api.alpaca.markets/v2/positions"
          .httpGet()
          .header("APCA-API-KEY-ID", call.request.headers["APCA-API-KEY-ID"]!!)
          .header("APCA-API-SECRET-KEY", call.request.headers["APCA-API-SECRET-KEY"]!!)
          .responseString()
        when (result) {
          is Result.Failure -> {
            val ex = result.getException()
            call.respondText(ex.toString())
          }
          is Result.Success -> {
            val data = result.get()
            val currentValues = Json.decodeFromString<ArrayList<Position>>(data)
            for (value in currentValues) {
              if (!valuesMap.containsKey(value.symbol)) {
                val arr = ArrayList<Value>()
                arr.add(values.toValue(value, "current"))
                valuesMap.put(value.symbol, arr)
              } else {
                val arr = valuesMap.get(value.symbol)!!
                arr.add(values.toValue(value, "current"))
                valuesMap.put(value.symbol, arr)
              }
            }
            val startDates = arrayOf(
              OffsetDateTime.now().minusMonths(6).format(DateTimeFormatter.ISO_DATE_TIME),
              OffsetDateTime.now().minusMonths(3).format(DateTimeFormatter.ISO_DATE_TIME),
              OffsetDateTime.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE_TIME),
              OffsetDateTime.now().minusWeeks(2).format(DateTimeFormatter.ISO_DATE_TIME),
              OffsetDateTime.now().minusWeeks(1).format(DateTimeFormatter.ISO_DATE_TIME),
              OffsetDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME),
            )
            for (startDate in startDates) {
              valuesMap.forEach {
                val symbol = it.key
                val (_request, _response, result) = "https://data.alpaca.markets/v2/stocks/${symbol}/trades?start=${startDate}&limit=1"
                  .httpGet()
                  .header("APCA-API-KEY-ID", call.request.headers["APCA-API-KEY-ID"]!!)
                  .header("APCA-API-SECRET-KEY", call.request.headers["APCA-API-SECRET-KEY"]!!)
                  .responseString()
                when (result) {
                  is Result.Failure -> {
                    val ex = result.getException()
                    call.respondText(ex.toString())
                  }
                  is Result.Success -> {
                    val historicalData = Json.decodeFromString<StockResponse>(result.get())
                    val historicalDataValues = historicalData.trades;
                    val arr = valuesMap.get(symbol);
                    for (historyPoint in historicalDataValues) {
                      if (arr != null) {
                        arr.add(Value(symbol, historyPoint.s, historyPoint.p, historyPoint.t))
                        valuesMap.put(symbol, arr)
                      }
                    }
                  }
                }
              }
            }
            call.respond(valuesMap)
          }
        }
      } else {
        call.respondText("Missing required headers")
      }
    }
    get("/scores") {
      val scoresMap = HashMap<String, PaddedScore>()
      val valuesMap = HashMap<String, ArrayList<Value>>()
      if (call.request.headers["APCA-API-KEY-ID"] != null && call.request.headers["APCA-API-SECRET-KEY"] != null) {
        val (_request, _response, result) = "https://paper-api.alpaca.markets/v2/positions"
          .httpGet()
          .header("APCA-API-KEY-ID", call.request.headers["APCA-API-KEY-ID"]!!)
          .header("APCA-API-SECRET-KEY", call.request.headers["APCA-API-SECRET-KEY"]!!)
          .responseString()
        when (result) {
          is Result.Failure -> {
            val ex = result.getException()
            call.respondText(ex.toString())
          }
          is Result.Success -> {
            val data = result.get()
            val currentPositions = Json.decodeFromString<ArrayList<Position>>(data)
            val minifiedPositions = ArrayList<MinifiedPosition>()
            for (position in currentPositions) {
              minifiedPositions.add(
                MinifiedPosition(
                  position.symbol,
                  position.avg_entry_price.toFloat(),
                  position.qty.toLong()
                )
              )
            }
            val (_request, _response, result) = "https://paper-api.alpaca.markets/v2/orders?status=closed"
              .httpGet()
              .header("APCA-API-KEY-ID", call.request.headers["APCA-API-KEY-ID"]!!)
              .header("APCA-API-SECRET-KEY", call.request.headers["APCA-API-SECRET-KEY"]!!)
              .responseString()
            when (result) {
              is Result.Failure -> {
                val ex = result.getException()
                call.respondText(ex.toString())
              }
              is Result.Success -> {
                val data = result.get()
                val orders = Json.decodeFromString<ArrayList<Order>>(data).filter {
                  var present = false
                  for (position in minifiedPositions) {
                    if (position.symbol == it.symbol) {
                      present = true
                    }
                  }
                  present
                }
                for (order in orders) {
                  val filled_date = OffsetDateTime.parse(order.filled_at)
                  val queryDateTimes = arrayOf(
                    filled_date.plusMinutes(15).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusMinutes(30).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusHours(4).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusWeeks(1).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusWeeks(2).format(DateTimeFormatter.ISO_DATE_TIME),
                    filled_date.plusMonths(1).format(DateTimeFormatter.ISO_DATE_TIME),
                  )
                  for (queryDateTime in queryDateTimes) {
                    val (_request, _response, result) = "https://data.alpaca.markets/v2/stocks/${order.symbol}/trades?start=${queryDateTime}&limit=1"
                      .httpGet()
                      .header("APCA-API-KEY-ID", call.request.headers["APCA-API-KEY-ID"]!!)
                      .header("APCA-API-SECRET-KEY", call.request.headers["APCA-API-SECRET-KEY"]!!)
                      .responseString()
                    when (result) {
                      is Result.Failure -> {
                        val ex = result.getException()
                        call.respondText(ex.toString())
                      }
                      is Result.Success -> {
                        val historicalData = Json.decodeFromString<StockResponse>(result.get())
                        val trade = historicalData.trades.get(0)
                        if (!valuesMap.containsKey(order.symbol)) {
                          valuesMap.put(order.symbol, arrayListOf(Value(order.symbol, trade.s, trade.p, trade.t)))
                        } else {
                          val valueFromMap = valuesMap.get(order.symbol)
                          if (valueFromMap != null) {
                            valueFromMap.add(Value(order.symbol, trade.s, trade.p, trade.t))
                            valuesMap.put(order.symbol, valueFromMap)
                          }
                        }
                      }
                    }
                    valuesMap.keys.forEach {
                      val original_price = order.filled_avg_price
                      val values = valuesMap.get(it)
                      val scores_for_symbol = ArrayList<Score>()
                      if (values != null) {
                        for (value in values) {
                          if (original_price != null) {
                            scores_for_symbol.add(
                              Score(
                                order.symbol,
                                value.current_price,
                                generate_score(original_price.toFloat(), value.current_price),
                                value.time
                              )
                            )
                          }
                        }
                        if (original_price != null) {
                          val values_for_average = ArrayList<Float>()
                          scores_for_symbol.forEach {
                            values_for_average.add(it.score)
                          }
                          scoresMap.put(
                            order.symbol,
                            PaddedScore(original_price.toFloat(), average(values_for_average), scores_for_symbol)
                          )
                        }
                      }
                    }
                  }
                }
                call.respond(scoresMap)
              }
            }
          }
        }
      } else {
        call.respondText("Missing required headers")
      }
    }
  }
}
