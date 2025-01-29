package com.am.SalesConversionAPI.controller.tracker.useraction;

import com.am.SalesConversionAPI.controller.tracker.TrackBaseController;
import com.am.SalesConversionAPI.service.ApiResponse;
import com.am.SalesConversionAPI.service.tracker.useraction.CustomerActionTrackerService;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller class to track customer actions. Extends the TrackBaseController class.
 */
@RestController
@RequestMapping("${api-tracking-path}")
public class UserActionTrackController extends TrackBaseController {

	/**
	 * Constructor to initialize dependencies.
	 * @param customerActionTrackerService Service instance to track customer actions
	 * @param sharedBucket Bucket instance to limit the number of requests
	 */
	public UserActionTrackController(CustomerActionTrackerService customerActionTrackerService, Bucket sharedBucket) {
		super(sharedBucket);
        this.service = customerActionTrackerService;
	}

	/**
	 * Track method implementation to track customer actions
	 * @param jsonNode JSON node containing the customer action details
	 * @return ResponseEntity containing the response of the action tracking
	 */
	@PostMapping("/action")
	public ResponseEntity<ApiResponse> track(@RequestBody JsonNode jsonNode) {
		return handleResponse(jsonNode);
	}

	/**
	 * Method to track the customer action
	 * @param jsonNode JSON node containing the customer action details
	 */
	protected void trackData(@RequestBody JsonNode jsonNode) {
		service.trackData(jsonNode);
	}
}
