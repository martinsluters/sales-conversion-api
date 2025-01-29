package com.am.SalesConversionAPI.service;

import com.am.SalesConversionAPI.model.tracker.TrackableDataModel;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interface/contract to mark something as trackable.
 */
public interface TrackableData {
    TrackableDataModel trackData(JsonNode jsonNode);
}
