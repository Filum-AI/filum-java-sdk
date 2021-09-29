package com.filum;

import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Event {
  private String anonymousID;
  private String userID;
  private String eventID;
  private JSONObject context = new JSONObject();
  private String timestamp;
  private String originalTimestamp;
  private String sentAt;
  private String receivedAt;
  private String eventName;
  private String eventType;
  private JSONObject eventParams = new JSONObject();
  private String origin;

  public void setAnonymousID(String anonymousID) {
      this.anonymousID = anonymousID;
  }

  public void setUserID(String userID) {
      this.userID = userID;
  }

  public String getUserID() {
      return userID;
  }

  public String getAnonymousID() {
      return anonymousID;
  }
  
  public void setEventID(String eventID) {
      this.eventID = eventID;
  }

  public void setContext(JSONObject context) {
      this.context = context;
  }

  public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
  }

  public void setOriginalTimestamp(String originalTimestamp) {
      this.originalTimestamp = originalTimestamp;
  }

  public void setSentAt(String sentAt) {
      this.sentAt = sentAt;
  }

  public void setReceivedAt(String receivedAt) {
      this.receivedAt = receivedAt;
  }

  public void setEventName(String eventName) {
      this.eventName = eventName;
  }

  public void setEventType(String eventType) {
      this.eventType = eventType;
  }

  public void setEventParams(JSONObject eventParams) {
      this.eventParams = eventParams;
  }

  public void setOrigin(String origin) {
      this.origin = origin;
  }

  public String getEventType() {
    return this.eventType;
  }
  
  /** @return the JSONObject that represents the event data of this event */
  public JSONObject toJsonObject() {
    JSONObject json = new JSONObject();
    try {
      json.put(Constants.ANONYMOUS_ID, this.anonymousID);
      json.put(Constants.USER_ID, this.userID);
      json.put(Constants.EVENT_ID, this.eventID);
      json.put(Constants.TIMESTAMP, this.timestamp);
      json.put(Constants.ORIGINAL_TIMESTAMP, this.originalTimestamp);
      json.put(Constants.SENT_AT, this.sentAt);
      json.put(Constants.RECEIVED_AT, this.receivedAt);
      json.put(Constants.EVENT_NAME, this.eventName);
      json.put(Constants.EVENT_TYPE, this.eventType);
      json.put(Constants.ORIGIN, this.origin);
      json.put(Constants.CONTEXT, this.context);

      JSONArray eventParamsJSONArray = new JSONArray();
      if (eventParams != null) {
          Iterator<String> customEventParamKeys = eventParams.keys();
          while (customEventParamKeys.hasNext()) {
              String key = customEventParamKeys.next();
              Object value = null;
              if (!eventParams.isNull(key)) {
                  value = eventParams.get(key);
                  JSONObject filumValue = Utils.convertToFilumItem(key, value);
                  eventParamsJSONArray.put(filumValue);
              }
          }
      }
      json.put(Constants.EVENT_PARAMS, eventParamsJSONArray);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json;
  }

  /** internal method */
  protected Object replaceWithJSONNull(Object obj) {
    return obj == null ? JSONObject.NULL : obj;
  }

  
}
