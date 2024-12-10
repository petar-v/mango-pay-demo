package org.flutterbit.mangopay.demo.controller;

import io.micronaut.context.annotation.Factory;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.flutterbit.mangopay.demo.model.CoolUser;
import org.flutterbit.mangopay.demo.repository.CoolUserRepository;
import org.flutterbit.mangopay.demo.security.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MicronautTest
public class CoolUserControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoolUserControllerTest.class);

    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_NAME = "TestUser";
    private static final String TEST_USER_PASSWORD = "hashedpassword";
    private static final String WRONG_USER_EMAIL = "wrong@example.com";
    private static final String WRONG_PASSWORD = "wrongpassword";

    private CoolUser mockUser;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    CoolUserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        // Reset the mock repository before each test
        Mockito.reset(userRepository);

        String encodedTestPassword = passwordEncoder.encode(TEST_USER_PASSWORD);
        mockUser = new CoolUser(123L, TEST_USER_NAME, TEST_USER_EMAIL, encodedTestPassword);
    }

    @Test
    void testLoginEndpoint() {
        // Mock repository behavior: user exists
        when(userRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(mockUser));

        // Mock request payload
        String payload = String.format(
                """
                        {
                            "identity": "%s",
                            "secret": "%s"
                        }
                        """,
                TEST_USER_EMAIL, TEST_USER_PASSWORD
        );

        // Perform the POST request
        HttpRequest<String> request = HttpRequest.POST("/users/login", payload)
                .header("Content-Type", "application/json");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        // Assert response contains the JWT token
        assertEquals(200, response.getStatus().getCode());
        assert response.getBody().orElse("").contains("access_token");

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testRegisterEndpoint() {
        // Mock repository behavior: email does not exist
        when(userRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.empty());

        // Mock request payload
        String payload = String.format(
                """
                        {
                            "email": "%s",
                            "name": "%s",
                            "password": "%s"
                        }
                        """,
                TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD
        );

        // Perform the PUT request
        HttpRequest<String> request = HttpRequest.PUT("/users/register", payload)
                .header("Content-Type", "application/json");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        // Assert response indicates success
        assertEquals(200, response.getStatus().getCode());
        assertEquals("User registered successfully", response.getBody().orElse(""));

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(TEST_USER_EMAIL);
        verify(userRepository, times(1)).save(any(CoolUser.class));
    }

    @Test
    void testLoginNoSuchUser() {
        // Mock repository behavior: user does not exist
        when(userRepository.findByEmail(WRONG_USER_EMAIL)).thenReturn(Optional.empty());

        // Mock request payload
        String payload = String.format(
                """
                        {
                            "identity": "%s",
                            "secret": "%s"
                        }
                        """,
                WRONG_USER_EMAIL, TEST_USER_PASSWORD
        );

        // Perform the POST request
        HttpRequest<String> request = HttpRequest.POST("/users/login", payload)
                .header("Content-Type", "application/json");
        try {
            client.toBlocking().exchange(request, String.class);
        } catch (HttpClientResponseException e) {
            // Assert the response status and message
            assertEquals(401, e.getStatus().getCode());
            assertEquals("User not found", e.getMessage());
        }

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(WRONG_USER_EMAIL);
    }

    @Test
    void testLoginWrongPassword() {
        // Mock repository behavior: user exists
        when(userRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(mockUser));

        // Mock request payload
        String payload = String.format(
                """
                        {
                            "identity": "%s",
                            "secret": "%s"
                        }
                        """,
                TEST_USER_EMAIL, WRONG_PASSWORD
        );

        // Perform the POST request
        HttpRequest<String> request = HttpRequest.POST("/users/login", payload)
                .header("Content-Type", "application/json");
        try {
            client.toBlocking().exchange(request, String.class);
        } catch (HttpClientResponseException e) {
            // Assert the response status and message
            assertEquals(401, e.getStatus().getCode());
            assertEquals("Invalid credentials", e.getMessage());
        }

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(TEST_USER_EMAIL);
    }

    @Test
    void testRegisterDuplicateEmail() {
        // Mock repository behavior: email already exists
        when(userRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(mockUser));

        // Mock request payload
        String payload = String.format(
                """
                        {
                            "email": "%s",
                            "name": "%s",
                            "password": "%s"
                        }
                        """,
                TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD
        );

        // Perform the PUT request
        HttpRequest<String> request = HttpRequest.PUT("/users/register", payload)
                .header("Content-Type", "application/json");
        try {
            client.toBlocking().exchange(request, String.class);
        } catch (HttpClientResponseException e) {
            // Assert the response status and message
            assertEquals(400, e.getStatus().getCode());
        }

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(TEST_USER_EMAIL);
        verify(userRepository, never()).save(any(CoolUser.class));
    }

    @Factory
    static class MockBeanFactory {
        @Singleton
        @MockBean(CoolUserRepository.class)
        CoolUserRepository mockCoolUserRepository() {
            return Mockito.mock(CoolUserRepository.class);
        }
    }
}
