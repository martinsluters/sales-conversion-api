package com.am.SalesConversionAPI.service.tracker;

import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import com.am.SalesConversionAPI.service.TrackableData;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * Abstract class for tracking data
 */
public abstract class TrackerService implements TrackableData {

    /**
     * The data to be tracked
     */
    protected JsonNode data;

    /**
     * Intended for child classes to implement the tracking logic
     * @param jsonNode The JSON node containing the data to be tracked
     * @return TrackableDataModel
     */
    protected abstract TrackableDataModel track(JsonNode jsonNode);

    /**
     * Implement interface method to track data and let child classes to do the actual work.
     * @param jsonNode The JSON node containing the data to be tracked
     * @return TrackableDataModel
     */
    public TrackableDataModel trackData(JsonNode jsonNode) {
       return track(jsonNode);
    }

    /**
     * Throws an exception with the given message
     * @param message The message to be thrown
     */
    protected void throwException(String message) {
        throw getException(message);
    }

    /**
     * Returns a new RuntimeException with the given message
     * @param message The message to be thrown
     * @return A new RuntimeException with the given message
     */
    protected RuntimeException getException(String message) {
        return new RuntimeException(message);
    }

    /**
     * Sanitize input
     * Cleans the input from any malicious code.
     * @param dirtyInput The input to be sanitized
     * @return sanitized input
     */
    protected String sanitizeInput(String dirtyInput) {
        return Jsoup.clean(dirtyInput, Safelist.basic());
    }
}
