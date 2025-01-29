package com.am.SalesConversionAPI.service.tracker.salesdata;

import com.am.SalesConversionAPI.model.tracker.salesdata.Transaction;
import com.am.SalesConversionAPI.model.tracker.salesdata.TransactionProduct;
import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import com.am.SalesConversionAPI.repository.tracker.salesdata.TransactionRepository;
import com.am.SalesConversionAPI.service.tracker.TrackerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

/**
 * Service class for tracking sales/transaction data.
 */
@Service
public class SaleTrackerService extends TrackerService {

    /**
     * Object mapper
     */
    private final ObjectMapper objectMapper;

    /**
     * Transaction repository
     */
    private final TransactionRepository transactionRepository;

    /**
     * Transaction instance
     */
    private Transaction transaction;

    /**
     * Constructor
     *
     * @param transactionRepository The transaction repository
     * @param objectMapper The object mapper
     */
    public SaleTrackerService(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Track sales data
     *
     * @param jsonNode The JSON node containing the sales data
     * @return The saved transaction
     * @throws RuntimeException If issues arisen during tracking
     */
    public TrackableDataModel track(JsonNode jsonNode) {
        // Sanitize JSON node
        // Sanitization method should be done in a separate class as is on the edge of doing too much.
        jsonNode = sanitizeJsonNode(jsonNode);

        // Set data
        data = jsonNode;

        // Check if required fields are present
        validateRequiredBaseFields();

        try {
            transaction = convertJsonNodeToTransaction(jsonNode);
        } catch (Exception e) {
            throwException("Error converting JSON to Transaction: " + e.getMessage());
        }

        // Validate transaction products is not empty
        validateTransactionProducts();

        // Save values to DB
        return transactionRepository.save(transaction);
    }

    /**
     * Sanitize JSON node.
     *
     * @TODO This method probably should be moved to a separate class
     *
     * @param jsonNode The JSON node to sanitize
     * @return The sanitized JSON node
     */
    public JsonNode sanitizeJsonNode(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            // Iterate through fields and sanitize
            objectNode.fieldNames().forEachRemaining(field -> {
                JsonNode value = objectNode.get(field);

                if (value.isTextual()) {
                    String sanitizedValue = sanitizeInput(value.asText().trim());
                    objectNode.put(field, sanitizedValue);
                } else if (value.isObject() || value.isArray()) {
                    // Recursively sanitize nested objects or arrays
                    sanitizeJsonNode(value);
                }
            });

            return objectNode;
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;

            // Iterate through array elements and sanitize
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode element = arrayNode.get(i);

                if (element.isTextual()) {
                    String sanitizedValue = sanitizeInput(element.asText().trim());
                    arrayNode.set(i, arrayNode.textNode(sanitizedValue));
                } else if (element.isObject() || element.isArray()) {
                    // Recursively sanitize nested objects or arrays
                    sanitizeJsonNode(element);
                }
            }

            return arrayNode;
        }

        // Return the original node if it's not an object or array
        return jsonNode;
    }

    /**
     * Convert JSON node to Transaction
     *
     * @TODO This method probably should be moved to a separate class, some type of factory etc.
     *
     * @param jsonNode The JSON node to convert
     * @return The converted Transaction
     * @throws Exception If issues arise during conversion
     */
    public Transaction convertJsonNodeToTransaction(JsonNode jsonNode) throws Exception {
        // Use ObjectMapper to convert the JsonNode to Order
        Transaction transaction = objectMapper.treeToValue(jsonNode, Transaction.class);

        // Set Transaction reference in TransactionProduct
        if (transaction.getTransactionProducts() != null) {
            for (TransactionProduct transactionProduct : transaction.getTransactionProducts()) {
                transactionProduct.setTransaction(transaction); // Manually set the Transaction reference
            }
        }

        return transaction;
    }

    /**
     * Validate transaction products
     * @throws RuntimeException if transaction products are empty
     */
    private void validateTransactionProducts() {
        if (transaction.getTransactionProducts().isEmpty()) {
            throwException("There must be at least one transaction product");
        }
    }

    /**
     * Validate required fields
     * @throws RuntimeException if required fields are not present
     */
    private void validateRequiredBaseFields() {
        if (areRequiredFieldsPresent()) {
            return;
        }

        throwException("'total_amount', 'transaction_identifier' and 'transaction_products' are required fields");
    }

    /**
     * Check if required fields are present
     * @return true if required fields are present, false otherwise
     */
    private boolean areRequiredFieldsPresent() {
        return data.has("total_amount") && data.has("transaction_identifier") && data.has("transaction_products");
    }
}
