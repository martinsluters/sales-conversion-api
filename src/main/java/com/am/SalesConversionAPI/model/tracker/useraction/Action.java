package com.am.SalesConversionAPI.model.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * The Action entity represents a user action that is tracked.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "action", indexes = {
        @Index(name = "idx_action_customer_identifier", columnList = "customerIdentifier"),
        @Index(name = "idx_action_product_identifier", columnList = "productIdentifier"),
        @Index(name = "idx_action_session_identifier", columnList = "sessionIdentifier"),
        @Index(name = "idx_action_created_at", columnList = "created_at")
})
public class Action implements TrackableDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "action_type_id")
    private ActionType actionType;

    private String customerIdentifier;

    // We need this nullable
    @Column(nullable = true)
    private String productIdentifier;

    @Column(nullable = true)
    private String sessionIdentifier;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    /**
     * Custom constructor to be able to set some required and some nullable fields.
     *
     * @param actionType the action type (required)
     * @param customerIdentifier the customer identifier (required)
     * @param productIdentifier the product identifier (nullable)
     * @param sessionIdentifier the session identifier (nullable)
     */
    public Action(ActionType actionType, String customerIdentifier, String productIdentifier, String sessionIdentifier) {
        this.actionType = actionType;
        this.customerIdentifier = customerIdentifier;
        this.productIdentifier = productIdentifier;
        this.sessionIdentifier = sessionIdentifier;
    }
}
