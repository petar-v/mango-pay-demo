package org.flutterbit;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {
    @Get("/")
    public String sayHello() {
        return "Hello World";
    }
}
