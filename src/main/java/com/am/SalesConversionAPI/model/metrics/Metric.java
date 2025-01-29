package com.am.SalesConversionAPI.model.metrics;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Metric class to store the total sales and conversion rate
 */
@Data
public class Metric {
    private long TotalSales;
    private BigDecimal ConversionRate;

    public Metric(long TotalSales, BigDecimal ConversionRate) {
        this.TotalSales = TotalSales;
        this.ConversionRate = ConversionRate;
    }

    // Lombok not working :(
    public long getTotalSales() {
        return TotalSales;
    }

    // Lombok not working :(
    public BigDecimal getConversionRate() {
        return ConversionRate;
    }
}
