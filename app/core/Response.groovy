package core

import com.sun.net.httpserver.Headers
import groovy.json.JsonOutput

/**
 * Created by k33g_org on 31/01/15.
 */
class Response {
    public com.sun.net.httpserver.HttpExchange exchange
    public Integer code
    public String content
    public String message

    public Response() {

    }

    public Response(String content, Integer code, String message, com.sun.net.httpserver.HttpExchange exchange) {
        this.content = content
        this.code = code
        this.message = message
        this.exchange = exchange
    }

    public Response message(String message) {
        this.message = message
        return this
    }

    public Response content(String content) {
        this.content = content
        return this
    }
    public Response code(Integer code) {
        this.code = code
        return this
    }
    public void send() {
        /*
        println("----------------")
        println(this.content)
        println(this.code)
        println(this.exchange)
        println("----------------")
        */
        this.message = "OK"
        this.exchange.sendResponseHeaders(
                this.code,
                this.content.getBytes("UTF-8").length
        )
        this.exchange.getResponseBody().write(this.content.getBytes())
        this.exchange.close()
    }

    public Headers headers() {
        return this.exchange.getResponseHeaders()
    }

    public Response contentType(String content_type) {
        this.headers().set("Content-Type", content_type)
        return this
    }

    public void json(value) {
        this.contentType("application/json;charset=UTF-8").content(JsonOutput.toJson(value)).send()
    }
    public void html(value) {
        this.contentType("text/html;charset=UTF-8").content(value).send()
    }
    public void text(value) {
        this.contentType("text/plain;charset=UTF-8").content(value).send()
    }

    /*TODO: allowCORS*/
    /*TODO: cookie*/

    public Response redirect(String location) {
        this.code(302).content("Redirecting ...").headers().set("Location", location)
        this.send()
        return this
    }

    public Response redirect(String location, Integer code) {
        this.code(code).content("Redirecting ...").headers().set("Location", location)
        this.send()
        return this
    }

    /*TODO: SSE*/
}
