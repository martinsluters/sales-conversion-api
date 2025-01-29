package com.am.SalesConversionAPI.repository.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.useraction.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Repository for ActionType.
 */
public interface ActionTypeRepository extends JpaRepository<ActionType, Long> {

    /**
     * Find ActionType by slug
     *
     * @param slug slug of the action
     * @return ActionType
     */
    Optional<ActionType> findBySlug(@Param("slug") String slug);
}
