package core
import com.sun.net.httpserver.Headers
import groovy.json.JsonSlurper

/**
 * Created by k33g_org on 31/01/15.
 */
class Request {
    public String data
    public com.sun.net.httpserver.HttpExchange exchange
    public HashMap parameters

    public Request() {

    }
    public Request(String data, com.sun.net.httpserver.HttpExchange exchange, HashMap parameters) {
        this.data = data
        this.exchange = exchange
        this.parameters = parameters
    }

    public params(String key) {
        return this.parameters.get(key)
    }
    public String method() {
        return this.exchange.getRequestMethod()
    }
    public URI uri() {
        return this.exchange.getRequestURI()
    }
    public Headers headers() {
        return this.exchange.getRequestHeaders()
    }

    public json() {
        def jsonSlurper = new JsonSlurper()
        return jsonSlurper.parseText(this.data ? this.data : "{}")
    }


    public String[] splitRoute() {
        return this.uri().toString().split("/").toList().findAll {
            part ->
                !part.equals("")
        }
    }

    /*TODO: cookies*/
}
