package com.am.SalesConversionAPI.service.metrics;

import com.am.SalesConversionAPI.model.metrics.Metric;
import com.am.SalesConversionAPI.repository.tracker.salesdata.TransactionRepository;
import com.am.SalesConversionAPI.repository.tracker.useraction.ActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Service class for simple metrics
 * Metrics data is cached for 1 minute. Currently through simple Caffeine cache.
 *
 * @TODO This class needs to be refactored as soon as we need more types of metrics.
 * @TODO Need to DRY the code and refactor.
 * @TODO Some bits are hardcoded which will cause issues if Action Types change in the DB.
 */
@Service
public class MetricsService {

    private final TransactionRepository transactionRepository;
    private final ActionRepository actionRepository;

    /**
     * Constructor
     * @param transactionRepository Transaction repository
     * @param actionRepository Action repository
     */
    public MetricsService(TransactionRepository transactionRepository, ActionRepository actionRepository) {
        this.transactionRepository = transactionRepository;
        this.actionRepository = actionRepository;
    }

    /**
     * Get metrics for a given date range, omit range to get all time metrics.
     *
     * Response is cached for 1 minute. Caching done with simple Caffeine cache and not Redis etc, also Postgres works just fine.
     *
     * @param startDate Start date
     * @param endDate End date
     * @return Metric object
     */
    @Cacheable(value = "metricsCache", key = "#startDate + '-' + #endDate", unless = "#ignoreCache")
    public Metric getMetrics(String startDate, String endDate, boolean ignoreCache) {

        DateTimeFormatter dateTimeFormatterPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convert String to LocalDate
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate, dateTimeFormatterPattern) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate, dateTimeFormatterPattern) : null;

        // Convert LocalDate to LocalDateTime (start/end of the day)
        LocalDateTime startDateTime = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endDateTime = (end != null) ? end.atTime(LocalTime.MAX) : null;

        // Validate date range
        if( startDateTime != null && endDateTime != null && startDateTime.isAfter(endDateTime) ) {
            throw new DateTimeParseException("Start date cannot be after end date", "", 0);
        }

        return new Metric(
                getTotalSales(startDateTime, endDateTime),
                getConversionRate(startDateTime, endDateTime)
        );
    }

    /**
     * Get conversion rate in a given date range, omit range to get all time conversion rate.
     * Conversion rate is session aware. Meaning two product view actions in same session is counted as one action.
     * If this needs to be different we can change this.
     *
     * @param startDateTime Start date
     * @param endDateTime  End date
     * @return BigDecimal conversion rate
     */
    private BigDecimal getConversionRate(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        long sessionCountReachedEndAction;
        long sessionWhereAction;

        // @TODO Action ID's should be configurable, also DRY the code!
        // @TODO I see performance issue with large datasets, this needs to be rebuilt.
        if (startDateTime != null && endDateTime != null) {
            sessionCountReachedEndAction = actionRepository.countSessionsWhereActionLeadToActionOnSameSessionInDateRange(1, 4, startDateTime, endDateTime);
            sessionWhereAction = actionRepository.countSessionsWhereActionInDateRange(1, startDateTime, endDateTime);
        } else {
            sessionCountReachedEndAction = actionRepository.countSessionsWhereActionLeadToActionOnSameSession(1, 4);
            sessionWhereAction = actionRepository.countSessionsWhereAction(1);
        }

        // Avoid division by zero
        if(sessionCountReachedEndAction == 0) {
            return BigDecimal.ZERO;
        }

        // Calculate conversion rate
        return (
                BigDecimal.valueOf(sessionCountReachedEndAction)
                .divide(BigDecimal.valueOf(sessionWhereAction), 10, RoundingMode.HALF_UP )
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Get total sales in a given date range, omit range to get all sales.
     *
     * @TODO For consistency with Conversion Rate metric, potentially rebuild to base on Actions repository instead.
     *
     * @param startDate Start date
     * @param endDate End date
     * @return Total sales
     */
    private long getTotalSales(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate != null && endDate != null) {
            return transactionRepository.countInDateRange(startDate, endDate);
        }
        return transactionRepository.count();
    }
}
