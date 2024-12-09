package org.flutterbit.mangopay.demo.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/hello")
public class HelloWorldController {
    @Get("/")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public String sayHello() {
        return "{\"message\":\"Hello World\"}";
    }
}
