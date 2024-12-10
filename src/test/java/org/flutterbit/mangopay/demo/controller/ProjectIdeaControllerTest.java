package org.flutterbit.mangopay.demo.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class ProjectIdeaControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testListIdeas() {
        HttpRequest<?> request = HttpRequest.GET("/ideas");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(200, response.getStatus().getCode());
        assertNotNull(response.getBody());
    }
}
