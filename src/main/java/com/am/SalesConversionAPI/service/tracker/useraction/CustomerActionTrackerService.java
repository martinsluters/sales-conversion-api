package com.am.SalesConversionAPI.service.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.useraction.ActionType;
import com.am.SalesConversionAPI.model.tracker.useraction.Action;
import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import com.am.SalesConversionAPI.repository.tracker.useraction.ActionRepository;
import com.am.SalesConversionAPI.repository.tracker.useraction.ActionTypeRepository;
import com.am.SalesConversionAPI.service.tracker.TrackerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Service class for tracking customer actions
 */
@Service
public class CustomerActionTrackerService extends TrackerService {

    /**
     * Action type repository
     */
    private final ActionTypeRepository actionTypeRepository;

    /**
     * Action repository
     */
    private final ActionRepository actionRepository;

    /**
     * Action type instance
     */
    private ActionType actionType;

    /**
     * Action type slug
     */
    private String actionTypeSlug;

    /**
     * Constructor
     * @param actionTypeRepository The action type repository
     * @param actionRepository The action repository
     */
    public CustomerActionTrackerService(ActionTypeRepository actionTypeRepository, ActionRepository actionRepository) {
        this.actionTypeRepository = actionTypeRepository;
        this.actionRepository = actionRepository;
    }

    /**
     * Track customer action
     *
     * @param jsonNode The JSON node containing the action data
     * @return The saved action
     * @throws RuntimeException if required fields are not present or action type not found
     */
    public TrackableDataModel track(JsonNode jsonNode) {
        data = jsonNode;

        // Check if required fields are present
        validateRequiredBaseFields();

        // Get required base fields
        String customerIdentifier = sanitizeInput(data.get("customer_id").asText());
        actionTypeSlug = sanitizeInput(data.get("action_type_slug").asText());

        // Fetch action type by slug
        actionType = findActionTypeBySlug();

        // Validate case-specific (conditional) required fields
        validateCaseSpecificRequiredFields();

        // Get optional fields
        String productIdentifier = jsonNode.has("product_id") ? sanitizeInput(jsonNode.get("product_id").asText()) : null;
        String sessionIdentifier = jsonNode.has("session_id") ? sanitizeInput(jsonNode.get("session_id").asText()) : null;

        // Save values to DB
        return actionRepository.save(new Action(actionType, customerIdentifier, productIdentifier, sessionIdentifier));
    }

    /**
     * Validate required fields for the specific action types
     * @throws RuntimeException if required fields are not present
     */
    private void validateCaseSpecificRequiredFields() {
        // If action type slug is product specific, product_id is required
        if(actionType.requiresProductId()) {
            // @TODO actionType should not be responsible for this, should be in a separate class responsible for the business logic
            if(! data.has("product_id")) {
                throwException("'product_id' is required field for action type: " + HtmlUtils.htmlEscape(actionTypeSlug));
            }
        }

        // Session ID required if not multistep action
        // @TODO actionType should not be responsible for this, should be in a separate class responsible for the business logic
        if(actionType.requiresSessionId()) {
            if( !data.has("session_id") ) {
                throwException("'session_id' is a required field for non-multistep action type: " + HtmlUtils.htmlEscape(actionTypeSlug));
            }
        }
    }

    /**
     * Validate required fields
     * @throws RuntimeException if required fields are not present
     */
    private void validateRequiredBaseFields() {
        if( areRequiredFieldsPresent() ) {
            return;
        }

        throwException("'action_type_slug' and 'customer_id' are required fields");
    }

    /**
     * Fetch action type by slug
     * @return ActionType instance
     * @throws RuntimeException if action type not found
     */
    private ActionType findActionTypeBySlug() {
        return actionTypeRepository.findBySlug(actionTypeSlug)
                .orElseThrow(() -> getException("Action type not found: " + HtmlUtils.htmlEscape(actionTypeSlug)));
    }

    /**
     * Check if required fields are present
     * @return true if required fields are present, false otherwise
     */
    private boolean areRequiredFieldsPresent() {
        return data.has("action_type_slug") && data.has("customer_id");
    }
}
