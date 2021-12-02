package me.stegall

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.stegall.plugins.*
import io.ktor.features.*
import io.ktor.http.*

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
    install(CORS) {
      method(HttpMethod.Options)
      method(HttpMethod.Post)
      method(HttpMethod.Get)
      header(HttpHeaders.ContentType)
      header("APCA-API-KEY-ID")
      header("APCA-API-SECRET-KEY")
      anyHost()
    }
    configureSerialization()
  }.start(wait = true)
}
