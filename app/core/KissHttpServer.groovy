package core

import com.sun.net.httpserver.HttpServer

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by k33g_org on 31/01/15.
 */
class KissHttpServer {

  String host
  Integer port

  HttpServer _serverInstance
  Closure _when404
  Closure _whenError

  //ExecutorService env

  KissHttpServer(String host, Integer port) {
    this.host = host
    this.port = port
  }

  KissHttpServer(String host, Integer port, Closure work) {
    this.host = host
    this.port = port
    this.initialize(work)
  }

  KissHttpServer() {}

  void when404(workWenSomethingWrong) {
    this._when404(workWenSomethingWrong)
  }

  void whenError(workWenSomethingWrong) {
    this._whenError(workWenSomethingWrong)
  }

  KissHttpServer initialize(work) {

    ExecutorService env = Executors.newCachedThreadPool()
    //this.env = env

    def errorReport = { Error error ->
      //TODO: wip
      println("--- this is the error report ---")
      return error.getMessage()
    }

    this._serverInstance =
        HttpServer.create(new InetSocketAddress(this.host, this.port), 0)


    this._serverInstance.createContext("/", { com.sun.net.httpserver.HttpExchange exchange ->

      env.execute((Runnable){
        def inputStream = exchange.getRequestBody()
        def inputStreamReader = new InputStreamReader(inputStream)
        def bufferedReader = new BufferedReader(inputStreamReader)
        def stringRead = bufferedReader.readLine()

        HttpExchange application =
          new HttpExchange()
          .response(new Response("", 200, null, exchange))
          .request(new Request(stringRead, exchange, null))

        application.response.headers().set("Content-Type", "application/json")

          try {
            work(application)
          } catch (Error err) {
            application.response.code(501).message("KO")
            if(this._whenError) {
              this._whenError(application.response, application.request, error)
            } else {
              application.response.headers().set("Content-Type", "text/html")
              application.response.content(errorReport(err)).send()
            }

          } finally {
            if(!application.response.message) {
              application.response.code(404)
              application.response.headers().set("Content-Type", "text/html")

              if(this._when404) {
                this._when404(application.response, application.request)
              } else {
                application.response.content("<h1>Error 404</h1>").send()
              }
            }
          }
      })

    })
    return this
  }

  void start() {
    this._serverInstance.start()
  }
  void start(String message) {
    this._serverInstance.start()
    println(message)
  }

  void stop() {
    this._serverInstance.stop(5)
  }

  void stop(Integer sec) {
    this._serverInstance.stop(sec)
  }


}
