package core

/**
 * Created by k33g_org on 31/01/15.
 */
class HttpExchange {
    public Response response
    public Request request

    public HttpExchange response(Response response) {
        this.response = response
        return this
    }

    public HttpExchange request(Request request) {
        this.request = request
        return this
    }

    public HttpExchange all(Closure work) {
        work(this.response, this.request)
        return this
    }

    public HttpExchange route(String method, String routeTemplate, Closure work) {
        def params = new UriTemplate(routeTemplate).matchString(this.request.uri().toString())
        if(this.request.method().equals(method) && params.size() > 0) {
            this.request.parameters = params
            work(this.response, this.request)
        }
        return this
    }
    /*--- REST Glue ---*/
    public HttpExchange $get(String routeTemplate, Closure work) {
       return this.route("GET", routeTemplate, work)
    }

    public HttpExchange get(String routeTemplate, Closure work) {
        return this.route("GET", routeTemplate, work)
    }

    public HttpExchange $post(String routeTemplate, Closure work) {
        return this.route("POST", routeTemplate, work)
    }

    public HttpExchange post(String routeTemplate, Closure work) {
        return this.route("POST", routeTemplate, work)
    }

    public HttpExchange $put(String routeTemplate, Closure work) {
        return this.route("PUT", routeTemplate, work)
    }

    public HttpExchange put(String routeTemplate, Closure work) {
        return this.route("PUT", routeTemplate, work)
    }

    public HttpExchange $delete(String routeTemplate, Closure work) {
        return this.route("DELETE", routeTemplate, work)
    }

    public HttpExchange delete(String routeTemplate, Closure work) {
        return this.route("DELETE", routeTemplate, work)
    }

    /*TODO: template route with "protecting" condition*/

    public HttpExchange method(String method, Closure work) {
        if(this.request.method().equals(method)) {
            work(this.response, this.request)
        }
        return this
    }

    public HttpExchange method(String method, Closure routeCondition, Closure work) {
        if(this.request.method().equals(method) &&  routeCondition(this.request.uri().toString())) {
            work(this.response, this.request)
        }
        return this
    }

    /*TODO: method with "protecting" condition*/

    public String contentTypeOfFile(String path) {
        String fileExtension = path.substring(path.lastIndexOf(".") + 1)
        String ct =  ContentTypes.getContentTypes().get(fileExtension)
        return ct ? ct : "text/plain;charset=UTF-8"
    }

    /* Golo version is called "static()"*/
    public HttpExchange staticContents(String staticAssetsDirectory, String defaultPage) {
        /*--- Home ---*/
        this.method("GET",
            {String route -> route.equals("/")},
            {Response res, Request req ->
                String path = new File(".").getCanonicalPath() + staticAssetsDirectory + req.uri().toString() + defaultPage
                response.contentType("text/html").content(
                        new File(path).getText("UTF-8")
                ).send()
            })

        /*--- Serve assets ---*/
        this.method("GET",
            {String route -> !route.equals("/")},
            {Response res, Request req ->
                String path = new File(".").getCanonicalPath() + staticAssetsDirectory + req.uri().toString()

                if(new File(path).exists()) {
                    String contentTypeOfAsset = this.contentTypeOfFile(path)
                    String content = new File(path).getText("UTF-8")
                    res.contentType(contentTypeOfAsset).content(content).send()
                }
            })

        return this
    }

}
