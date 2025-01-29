package com.am.SalesConversionAPI.repository.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.useraction.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

/**
 * Repository for the Action entity.
 * This repository is used to interact with the database.
 */
public interface ActionRepository extends JpaRepository<Action, Long> {

    /**
     * Count the number of sessions where an action leads to another action on the same session.
     *
     * @TODO This needs to be rebuilt, I can see performance issues with large datasets.
     * @TODO Potentially we need to avoid complex SQL and let Java do the work but for MVP this will do.
     *
     * @param startActionTypeId The id of the start action type.
     * @param endActionTypeId The id of the end action type.
     * @return long
     */
    @Query("SELECT COUNT(DISTINCT a.sessionIdentifier) " +
            "FROM Action a " +
            "WHERE a.actionType.id IN (:startActionTypeId, :endActionTypeId) " +
            "AND a.sessionIdentifier IN (" +
            "  SELECT DISTINCT a2.sessionIdentifier " +
            "  FROM Action a2 " +
            "  WHERE a2.actionType.id IN (:startActionTypeId, :endActionTypeId) " +
            "  GROUP BY a2.sessionIdentifier " +
            "  HAVING COUNT(DISTINCT a2.actionType.id) = 2" +
            ")")
    long countSessionsWhereActionLeadToActionOnSameSession(int startActionTypeId, int endActionTypeId);

    /**
     * Count the number of sessions where an action leads to another action on the same session within a date range.
     *
     * @TODO This needs to be rebuilt, I can see performance issues with large datasets.
     * @TODO Potentially we need to avoid complex SQL and let Java do the work but for MVP this will do.
     *
     * @param startActionTypeId The id of the start action type.
     * @param endActionTypeId The id of the end action type.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return long
     */
    @Query("SELECT COUNT(DISTINCT a.sessionIdentifier) " +
            "FROM Action a " +
            "WHERE a.actionType.id IN (:startActionTypeId, :endActionTypeId) " +
            "AND a.createdAt BETWEEN :startDate AND :endDate " +
            "AND a.sessionIdentifier IN (" +
            "  SELECT DISTINCT a2.sessionIdentifier " +
            "  FROM Action a2 " +
            "  WHERE a2.actionType.id IN (:startActionTypeId, :endActionTypeId) " +
            "  AND a2.createdAt BETWEEN :startDate AND :endDate " +
            "  GROUP BY a2.sessionIdentifier " +
            "  HAVING COUNT(DISTINCT a2.actionType.id) = 2" +
            ")")
    long countSessionsWhereActionLeadToActionOnSameSessionInDateRange(int startActionTypeId, int endActionTypeId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count the number of sessions where an action type is set.
     * @param ActionTypeId The id of the action type.
     * @return long
     */
    @Query("SELECT COUNT(DISTINCT a.sessionIdentifier) " +
            "FROM Action a " +
            "WHERE a.actionType.id = :ActionTypeId")
    long countSessionsWhereAction(int ActionTypeId);

    /**
     * Count the number of sessions where an action type is set within a date range.
     * @param ActionTypeId The id of the action type.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return long
     */
    @Query("SELECT COUNT(DISTINCT a.sessionIdentifier) " +
            "FROM Action a " +
            "WHERE a.actionType.id = :ActionTypeId " +
            "AND a.createdAt BETWEEN :startDate AND :endDate")
    long countSessionsWhereActionInDateRange(int ActionTypeId, LocalDateTime startDate, LocalDateTime endDate);
}
