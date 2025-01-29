package com.am.SalesConversionAPI.controller.tracker;

import com.am.SalesConversionAPI.controller.TrackableRestController;
import com.am.SalesConversionAPI.service.ApiResponse;
import com.am.SalesConversionAPI.service.TrackableData;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Base REST controller class to track various data.
 * Implements the Trackable interface and enforces children to implement.
 * Intended to take off some common boilerplate code from the child classes and keep them lean.
 */
public abstract class TrackBaseController implements TrackableRestController {

    /**
     * Service instance to track customer actions
     */
    protected TrackableData service;

    /**
     * Bucket instance to rate limit the API requests
     */
    protected final Bucket sharedBucket;

    /**
     * Constructor to initialize the shared bucket
     * @param sharedBucket Bucket instance to rate limit the API requests
     */
    public TrackBaseController(Bucket sharedBucket) {
        this.sharedBucket = sharedBucket;
    }

    /**
     * Method to handle action tracking. Calls the trackData method to track the customer action.
     * @param jsonNode JSON node containing the customer action details
     * @return ResponseEntity containing the response of the action tracking
     */
    protected ResponseEntity<ApiResponse> handleResponse(JsonNode jsonNode) {

        // Check request rate limit
        if (!sharedBucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ApiResponse(ApiResponse.ERROR, "API rate limit exceeded"));
        }

        try {
            // Hollywood principle
            trackData(jsonNode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(ApiResponse.ERROR, e.getMessage()));
        }

        return ResponseEntity.ok(new ApiResponse(ApiResponse.SUCCESS, "Tracked successfully"));
    }

    /**
     * Custom Exception handler for invalid JSON body passed.
     * @param e Exception thrown when invalid JSON is passed
     * @return ResponseEntity containing the error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse> handleInvalidJson(HttpMessageNotReadableException e) {
        ApiResponse response = new ApiResponse(ApiResponse.ERROR, "Invalid JSON input: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to track the customer action. To be implemented by the child classes.
     * @param jsonNode JSON node containing the customer action details
     */
    protected abstract void trackData(JsonNode jsonNode);
}

