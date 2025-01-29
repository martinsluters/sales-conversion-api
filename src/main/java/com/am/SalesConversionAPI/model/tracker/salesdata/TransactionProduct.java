package com.am.SalesConversionAPI.model.tracker.salesdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import java.math.BigDecimal;

/**
 * Class that represents a product in a sales transaction.
 */
@Entity
@Table(name = "transaction_products")
@Data
public class TransactionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotBlank(message = "Product identifier is required")
    @Column(name = "product_identifier", nullable = false)
    @JsonProperty("product_identifier")
    private String productIdentifier;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    // Lombok not working :(
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
