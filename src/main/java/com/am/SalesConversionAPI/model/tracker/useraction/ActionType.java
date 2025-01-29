package com.am.SalesConversionAPI.model.tracker.useraction;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * The ActionType entity represents the type of an action that is tracked.
 */
@Entity
@Data
public class ActionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "slug", unique = true)
    private String slug;

    @OneToMany(mappedBy = "actionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Action> actions;

    /**
     * Checks if the action type requires a product identifier.
     * @TODO this should be in a separate class responsible for the business logic
     * @return bool true if the action type requires a product identifier
     */
    public boolean requiresProductId() {
        return this.slug.equals("product_viewed") || this.slug.equals("product_added_cart");
    }

    /**
     * Checks if the action type requires a session identifier.
     * @TODO this should be in a separate class responsible for the business logic
     * @return bool true if the action type requires a session identifier
     */
    public boolean requiresSessionId() {
        return this.slug.equals("product_viewed") || this.slug.equals("product_added_cart");
    }
}
