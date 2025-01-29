package com.am.SalesConversionAPI.controller.metrics;

import com.am.SalesConversionAPI.service.ApiResponse;
import com.am.SalesConversionAPI.service.metrics.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller class to retrieve admin metrics.
 */
@RestController
@RequestMapping("${api-metrics-path}")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * Get metrics for admin.
     *
     * @param startDate Start date for metrics. Format: yyyy-MM-dd
     * @param endDate End date for metrics. Format: yyyy-MM-dd
     * @return ResponseEntity<Object> Response entity with metrics data.
     */
    @GetMapping("/metrics")
    public ResponseEntity<Object> metrics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate

    ) {
        try {
            return ResponseEntity.ok().body(metricsService.getMetrics(startDate, endDate, false));
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.ERROR, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
