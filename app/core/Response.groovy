package core

import com.sun.net.httpserver.Headers
import groovy.json.JsonOutput

/**
 * Created by k33g_org on 31/01/15.
 */
class Response {
  com.sun.net.httpserver.HttpExchange exchange
  Integer code
  String content
  String message

  Response() {}

  Response(String content, Integer code, String message, com.sun.net.httpserver.HttpExchange exchange) {
    this.content = content
    this.code = code
    this.message = message
    this.exchange = exchange
  }

  Response message(String message) {
    this.message = message
    return this
  }

  Response content(String content) {
    this.content = content
    return this
  }
  Response code(Integer code) {
    this.code = code
    return this
  }
  void send() {

    this.message = "OK"
    this.exchange.sendResponseHeaders(
      this.code,
      this.content.getBytes("UTF-8").length
    )
    this.exchange.getResponseBody().write(this.content.getBytes())
    this.exchange.close()
  }

  Headers headers() {
    return this.exchange.getResponseHeaders()
  }

  Response contentType(String content_type) {
    this.headers().set("Content-Type", content_type)
    return this
  }

  void json(value) {
    this.contentType("application/json;charset=UTF-8").content(JsonOutput.toJson(value)).send()
  }
  void html(value) {
    this.contentType("text/html;charset=UTF-8").content(value).send()
  }
  void text(value) {
    this.contentType("text/plain;charset=UTF-8").content(value).send()
  }

  /*TODO: allowCORS*/
  /*TODO: cookie*/

  Response redirect(String location) {
    this.code(302).content("Redirecting ...").headers().set("Location", location)
    this.send()
    return this
  }

  Response redirect(String location, Integer code) {
    this.code(code).content("Redirecting ...").headers().set("Location", location)
    this.send()
    return this
  }

  /*TODO: SSE*/
}
