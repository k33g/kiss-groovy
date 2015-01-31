import core.HttpExchange
import core.KissHttpServer
import core.Request
import core.Response

HashMap humans = [
        "001": "Bob Morane",
        "002": "John Doe",
        "003": "Jane Doe"
]

def server = new KissHttpServer("localhost", 3008,{ HttpExchange app ->

    app.staticContents("/public", "index.html")

    app.$get("/humans", { Response res, Request req ->
        res.json(humans)
    })

    app.$get("/humans/{id}", { Response res, Request req ->
        res.json(humans.get(req.params("id")))
    })

    app.route("GET", "/users/{first}/{last}", {Response res, Request req ->
        res.json([
                "firstName":req.params("first"),
                "lastName":req.params("last")
        ])
    })

    app.method("GET", {String route -> route.equals("/hello")}, {Response res, Request req ->
        res.html("<h1>Hello World!</h1>")
    })

    app.method("GET", {String route -> route.equals("/hi")}, {Response res, Request req ->
        res.json("message": "hello")
    })

    app.method("GET", {String route -> route.equals("/bob")}, {Response res, Request req ->
        res.html("<h2>Hello Bob Morane</h2>")
    })

})


server.start("Kiss-Groovy is started and listening on (" + server.host + ")" + server.port)
