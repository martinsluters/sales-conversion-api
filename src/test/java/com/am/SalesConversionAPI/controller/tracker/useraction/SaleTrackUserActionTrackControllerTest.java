package com.am.SalesConversionAPI.controller.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.useraction.Action;
import com.am.SalesConversionAPI.service.tracker.useraction.CustomerActionTrackerService;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@WebMvcTest(UserActionTrackController.class)
class SaleTrackUserActionTrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerActionTrackerService trackService;

    @MockBean
    private Bucket sharedBucket;

    private final String apiEndpoint = "/api/track/action";

    @WithMockUser
    @Test
    void testThatSuccessfulResponseReturnedWhenServiceReturnsActionInstance() throws Exception {

        // Mock the service and Action
        Action mockAction = mock(Action.class);

        // Mock service response
        when(trackService.trackData(any(JsonNode.class))).thenReturn(mockAction);

        // Mock sharedBucket.tryConsume
        when(sharedBucket.tryConsume(1)).thenReturn(true);

        // Json request body, the contents does not matter here as we mock the service anyway.
        String requestBody = """
                {
                    "good": "data"
                }
                """;

        // Perform POST request and verify
        mockMvc.perform(post(apiEndpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Ensure the response is JSON
                .andExpect(jsonPath("$.status").value("success"));
    }

    @WithMockUser
    @Test
    void testThatUnsuccessfulResponseReturnedWhenServiceThrowsException() throws Exception {

        // Mock service throw exception
        doThrow(new RuntimeException("Whatever")).when(trackService).trackData(any(JsonNode.class));

        // Mock sharedBucket.tryConsume
        when(sharedBucket.tryConsume(1)).thenReturn(true);

        // Json request body, the contents does not matter here as we mock the service anyway.
        String requestBody = """
                {
                    "bad": "data"
                }
                """;

        // Perform POST request and verify
        mockMvc.perform(post(apiEndpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Ensure the response is JSON
                .andExpect(jsonPath("$.status").value("error"));
    }

    @WithMockUser
    @Test
    void testThatUnsuccessfulResponseReturnedWhenInvalidJSON() throws Exception {

        // Json request body, the contents does not matter here as we mock the service anyway.
        String requestBody = """
                {
                    "invalid": "json",,
                }
                """;

        // Mock sharedBucket.tryConsume
        when(sharedBucket.tryConsume(1)).thenReturn(true);

        // Perform POST request and verify
        mockMvc.perform(post(apiEndpoint)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // Ensure the response is JSON
                .andExpect(jsonPath("$.status").value("error"));
    }
}