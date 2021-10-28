package com.filum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import com.filum.exception.FilumException;

public class EventTest {
  @Test
  public void testCreateEventWithNullUserAndDeviceThrowsException() {
    assertThrows(
        FilumException.class,
        () -> {
          new EventBuilder("test event");
        },
        "Event type must have one defined identify or track");
  }

  @Test
  public void testToJsonObject() throws FilumException {
    String eventType = "identify";
    String userId = "test-user";
    EventBuilder builder = new EventBuilder(eventType);
    builder.setUserID(userId);
    JSONObject eventProperties = new JSONObject();
    eventProperties.put("float_type", 123.456f);
    double doubleValue = 1234.567;
    eventProperties.put("double_type", doubleValue);
    builder.setEventParams(eventProperties);
    Event event = builder.getEvent();

    JSONObject serialized = event.toJsonObject();
    
    assertEquals(eventType, serialized.getString("event_type"));
    assertEquals(userId, serialized.getString("user_id"));
    
    JSONArray kvItems = serialized.getJSONArray("event_params");
    for (int i = 0 ; i < kvItems.length(); i++) {
        JSONObject item = (JSONObject) kvItems.get(i);
        System.out.println(item);
        JSONObject value = item.getJSONObject("value");
        Iterator<String> keys = value.keys();
        while(keys.hasNext()){
            String key = keys.next();
            assertTrue("double_value" == key);
        }
    }

    // assertEquals(
    //     Constants.SDK_LIBRARY + "/" + Constants.SDK_VERSION, serialized.getString("library"));
  }
}
