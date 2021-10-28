package com.filum;

public interface Constants {
  public static final String ANONYMOUS_ID = "anonymous_id";
  public static final String USER_ID = "user_id";
  public static final String CONTEXT = "context";
  public static final String EVENT_ID = "event_id";
  public static final String TIMESTAMP = "timestamp";
  public static final String ORIGINAL_TIMESTAMP = "original_timestamp";
  public static final String SENT_AT = "sent_at";
  public static final String RECEIVED_AT = "received_at";
  public static final String EVENT_NAME = "event_name";
  public static final String EVENT_TYPE = "event_type";
  public static final String EVENT_PARAMS = "event_params";
  public static final String ORIGIN = "origin";
  
  // String API_URL = "https://api2.filum.com/2/httpapi";
  // String BATCH_API_URL = "https://api2.filum.com/batch";
  String API_URL = "https://event.filum.ai/events";
  String BATCH_API_URL = "https://event.filum.ai/events";

  int NETWORK_TIMEOUT_MILLIS = 10000;
  String SDK_LIBRARY = "filum-java-sdk";
  String SDK_VERSION = "0.0.2";

  int MAX_PROPERTY_KEYS = 1024;
  int MAX_STRING_LENGTH = 1000;

  int HTTP_STATUS_BAD_REQ = 400;

  int EVENT_BUF_COUNT = 10;
  int EVENT_BUF_TIME_MILLIS = 10000;

  long[] RETRY_TIMEOUTS = {100, 100, 200, 200, 400, 400, 800, 800, 1600, 1600, 3200, 3200};
  int MAX_CACHED_EVENTS = 16000;
}
