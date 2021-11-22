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
import me.stegall.data.Position
import me.stegall.services.Values
import me.stegall.data.Value
import java.time.LocalDate
import java.util.*
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
        val (request, response, result) = "https://paper-api.alpaca.markets/v2/positions"
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
            valuesMap.forEach({
              val symbol = it.key
              val (request, response, result) = "https://data.alpaca.markets/v2/stocks/${symbol}/trades?start=2020-02-06T13:04:56.334320128Z&limit=1"
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
                  println(result.get())
                  call.respondText(result.get())
                }
              }
            })
          }
        }
      } else {
        call.respondText("Missing required headers")
      }
    }
    get("/scores") {
      if (call.request.headers["APCA-API-KEY-ID"] != null && call.request.headers["APCA-API-SECRET-KEY"] != null) {
        call.respondText("scores")
      }
    }
  }
}
