package me.stegall.plugins

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.http.*
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
                val allOrders = Json.decodeFromString<ArrayList<Order>>(data).filter {
                  var present = false
                  for (position in minifiedPositions) {
                    if (position.symbol == it.symbol) {
                      present = true
                    }
                  }
                  present
                }
                println(allOrders)
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
