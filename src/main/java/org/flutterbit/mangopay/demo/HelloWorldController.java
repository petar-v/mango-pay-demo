package org.flutterbit.mangopay.demo.mangopay.demo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/hello")
public class HelloWorldController {
    @Get("/")
    public String sayHello() {
        return "{\"message\":\"Hello World\"}";
    }
}
