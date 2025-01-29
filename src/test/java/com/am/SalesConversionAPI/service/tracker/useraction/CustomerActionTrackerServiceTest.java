package com.am.SalesConversionAPI.service.tracker.useraction;

import com.am.SalesConversionAPI.model.tracker.useraction.Action;
import com.am.SalesConversionAPI.model.tracker.useraction.ActionType;
import com.am.SalesConversionAPI.repository.tracker.useraction.ActionRepository;
import com.am.SalesConversionAPI.repository.tracker.useraction.ActionTypeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerActionTrackerServiceTest {

    @Mock
    private ActionTypeRepository actionTypeRepository;

    @Mock
    private ActionRepository actionRepository;

    @InjectMocks
    private CustomerActionTrackerService sutCustomerActionTrackerService;

    @Test
    void testThatExceptionThrownWhenRequiredFieldsNotPassed() throws Exception {

        // When
        JsonNode data = getJsonNodeFromString("""
                {
                    "missing": "required",
                    "fields": "true"
                }
                """);

        // Then expect an exception to be thrown
        assertExceptionThrown(data, RuntimeException.class);
    }

    @Test
    void testThatExceptionThrownWhenActionTypePassedNotFound() throws Exception {

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.empty());

        // When. Does not matter what data, we mock the repository anyway
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "invalid_action_type",
                    "customer_id": 9999,
                    "product_id": 2
                }
                """);

        // Then expect an exception to be thrown
        assertExceptionThrown(data, RuntimeException.class);
    }

    @Test
    void testThatExceptionThrownWhenProductSpecificActionPassedButMissingProductIdentifier() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresProductId()).thenReturn(true);

        // When missing product identifier and has action type slug that requires product id
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "product_specific_action_and_requires_product_id_along",
                    "customer_id": 9999
                }
                """);

        // Then expect an exception to be thrown
        assertExceptionThrown(data, RuntimeException.class);
    }

    @Test
    void testThatExceptionThrownWhenNonMultistepActionPassedButMissingSessionIdentifier() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresSessionId()).thenReturn(true);

        // When missing product identifier and has action type slug that requires product id
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "non_multistep_action_and_requires_session_id_along",
                    "customer_id": 9999
                }
                """);

        // Then expect an exception to be thrown
        assertExceptionThrown(data, RuntimeException.class);
    }

    @Test
    void testActionReturnedWhenAllRequiredFieldsPassedProductSpecific() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Mock Action instance
        Action mockAction = mock(Action.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresProductId()).thenReturn(true);
        when(mockActionType.requiresSessionId()).thenReturn(false);
        when(actionRepository.save(any(Action.class))).thenReturn(mockAction);

        // When all required fields passed
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "product_specific_action_and_requires_product_id_along",
                    "customer_id": 9999,
                    "product_id": 2
                }
                """);

        // Then expect action instance to be returned
        assertNotNull(sutCustomerActionTrackerService.trackData(data));
    }

    @Test
    void testActionReturnedWhenAllRequiredFieldsPassedSessionSpecific() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Mock Action instance
        Action mockAction = mock(Action.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresSessionId()).thenReturn(true);
        when(mockActionType.requiresProductId()).thenReturn(false);
        when(actionRepository.save(any(Action.class))).thenReturn(mockAction);

        // When all required fields passed
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "product_session_specific_and_requires_session_id_along",
                    "customer_id": 9999,
                    "session_id": 2
                }
                """);

        // Then expect action instance to be returned
        assertNotNull(sutCustomerActionTrackerService.trackData(data));
    }

    @Test
    void testActionReturnedWhenAllRequiredFieldsPassedAndNothingSpecificRequired() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Mock Action instance
        Action mockAction = mock(Action.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresSessionId()).thenReturn(false);
        when(mockActionType.requiresProductId()).thenReturn(false);
        when(actionRepository.save(any(Action.class))).thenReturn(mockAction);

        // When all required fields passed
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "action_that_does_not_require_product_id_and_session_id",
                    "customer_id": 9999
                }
                """);

        // Then expect action instance to be returned
        assertNotNull(sutCustomerActionTrackerService.trackData(data));
    }

    @Test
    void testActionReturnedWhenAllRequiredFieldsPassedAndSessionAndProductIdentifiersRequired() throws Exception {

        // Mock the ActionType instance
        ActionType mockActionType = mock(ActionType.class);

        // Mock Action instance
        Action mockAction = mock(Action.class);

        // Arrange
        when(actionTypeRepository.findBySlug(any(String.class))).thenReturn(Optional.ofNullable(mockActionType));
        when(mockActionType.requiresSessionId()).thenReturn(true);
        when(mockActionType.requiresProductId()).thenReturn(true);
        when(actionRepository.save(any(Action.class))).thenReturn(mockAction);

        // When all required fields passed
        JsonNode data = getJsonNodeFromString("""
                {
                    "action_type_slug": "action_that_requires_product_id_and_session_id",
                    "customer_id": 9999,
                    "session_id": "hsabuyshsausu9s",
                    "product_id": 3
                }
                """);

        // Then expect action instance to be returned
        assertNotNull(sutCustomerActionTrackerService.trackData(data));
    }

    protected void assertExceptionThrown(JsonNode data, Class<? extends Exception> exceptionClassName) {
        assertThrows(exceptionClassName, () -> sutCustomerActionTrackerService.trackData(data));
    }

    protected JsonNode getJsonNodeFromString(String json) throws Exception {
        return new ObjectMapper().readTree(json);
    }
}