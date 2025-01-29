package com.am.SalesConversionAPI.controller;

import com.am.SalesConversionAPI.service.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

/**
 * Interface to track customer actions.
 */
public interface TrackableRestController {

    ResponseEntity<ApiResponse> track(JsonNode jsonNode);

}
