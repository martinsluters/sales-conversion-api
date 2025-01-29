package com.am.SalesConversionAPI.service;

import java.time.Instant;

/**
 * Class that represents a general API response.
 *
 * @TODO this needs to be rebuild and its usage made so that it is more descriptive and useful.
 */
public class ApiResponse {

    /**
     * Status of the response.
     */
    private String status;

    /**
     * Message of the response.
     */
    private String message;

    /**
     * Timestamp of the response.
     */
    private String timestamp;

    // Constants
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    /**
     * Constructor
     *
     * @param status status as string.
     * @param message message as string.
     */
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now().toString();
    }

    // Having issues with lombok :(
    public String getStatus() {
        return status;
    }

    // Having issues with lombok :(
    public String getMessage() {
        return message;
    }

    // Having issues with lombok :(
    public String getTimestamp() {
        return timestamp;
    }
}
