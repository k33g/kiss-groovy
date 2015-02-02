package core
import com.sun.net.httpserver.Headers
import groovy.json.JsonSlurper

/**
 * Created by k33g_org on 31/01/15.
 */
class Request {
  String data
  com.sun.net.httpserver.HttpExchange exchange
  HashMap parameters

  Request() {}

  Request(String data, com.sun.net.httpserver.HttpExchange exchange, HashMap parameters) {
    this.data = data
    this.exchange = exchange
    this.parameters = parameters
  }

  Object params(String key) {
    return this.parameters.get(key)
  }
  String method() {
    return this.exchange.getRequestMethod()
  }
  URI uri() {
    return this.exchange.getRequestURI()
  }
  Headers headers() {
    return this.exchange.getRequestHeaders()
  }

  Object json() {
    JsonSlurper jsonSlurper = new JsonSlurper()
    return jsonSlurper.parseText(this.data ? this.data : "{}")
  }

  String[] splitRoute() {
    return this.uri().toString().split("/").toList().findAll {
      part -> !part.equals("")
    }
  }

  /*TODO: cookies*/
}
