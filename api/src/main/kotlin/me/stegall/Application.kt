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
      method(HttpMethod.Put)
      method(HttpMethod.Patch)
      method(HttpMethod.Delete)
      anyHost()
      header(HttpHeaders.ContentType)
      header(HttpHeaders.Authorization)
      allowSameOrigin = true
    }
    configureSerialization()
  }.start(wait = true)
}
