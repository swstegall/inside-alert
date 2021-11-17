package me.stegall.plugins

import com.github.kittinunf.fuel.httpGet
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import com.github.kittinunf.result.Result

fun Application.configureRouting() {

    routing {
        get("/") {
            if (call.request.headers["APCA-API-KEY-ID"] != null && call.request.headers["APCA-API-SECRET-KEY"] != null) {
                val (request, response, result) = "https://paper-api.alpaca.markets/v2/account/activities"
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
                        println(data)
                        call.respondText(data)
                    }
                }
            } else {
                call.respondText("Missing required headers")
            }
        }
    }
}
