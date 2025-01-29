package com.am.SalesConversionAPI.controller.tracker.salesdata;

import com.am.SalesConversionAPI.controller.tracker.TrackBaseController;
import com.am.SalesConversionAPI.service.ApiResponse;
import com.am.SalesConversionAPI.service.tracker.salesdata.SaleTrackerService;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bucket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller class to track sales data. Extends the TrackBaseController class.
 */
@RestController
@RequestMapping("${api-tracking-path}")
public class SaleTrackController extends TrackBaseController {

    /**
     * Constructor to initialize dependencies.
     * @param saleTrackerService Service instance to track sale
     * @param sharedBucket Bucket instance to limit the number of requests
     */
    public SaleTrackController(SaleTrackerService saleTrackerService, Bucket sharedBucket) {
        super(sharedBucket);
        this.service = saleTrackerService;
    }

    /**
     * Implementation of TrackableRestController interface.
     * Tracks a sale/transaction data.
     * @param jsonNode JSON node containing the customer action details
     * @return ResponseEntity containing the response of the action tracking
     */
    @PostMapping("/sale")
    public ResponseEntity<ApiResponse> track(@RequestBody JsonNode jsonNode) {
        return handleResponse(jsonNode);
    }

    /**
     * Method to track a sale
     * @param jsonNode JSON node containing the sale details
     */
    protected void trackData(@RequestBody JsonNode jsonNode) {
        service.trackData(jsonNode);
    }
}
