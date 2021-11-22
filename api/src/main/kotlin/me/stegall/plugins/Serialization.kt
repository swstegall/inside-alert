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
import me.stegall.data.Position
import me.stegall.data.TradeActivity

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/values") {
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
                        val something = Json.decodeFromString<ArrayList<Position>>(data)
                        call.respond(something)
                    }
                }
            } else {
                call.respondText("Missing required headers")
            }
        }
    }
}
