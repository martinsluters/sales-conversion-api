package com.am.SalesConversionAPI.model.tracker.salesdata;

import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class that represents a sales transaction.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
public class Transaction implements TrackableDataModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("transaction_products")
    private List<TransactionProduct> transactionProducts;

    @NotBlank(message = "Transaction identifier is required")
    @Column(name = "transaction_identifier", nullable = false)
    @JsonProperty("transaction_identifier")
    private String transactionIdentifier;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    @NotNull
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // Lombok not working :(
    public List<TransactionProduct> getTransactionProducts () {
        return transactionProducts;
    }
}
