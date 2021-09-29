package com.filum;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.filum.exception.FilumInvalidAPIKeyException;

public class Filum {

  public static final String TAG = Filum.class.getName();

  private static Map<String, Filum> instances = new HashMap<>();
  private String apiKey;

  private FilumLog logger;

  private Queue<Event> eventsToSend;
  private boolean aboutToStartFlushing;

  private HttpCallMode httpCallMode;
  private HttpCall httpCall;

  /**
   * Private internal constructor for Filum. Please use `getInstance(String name)` or
   * `getInstance()` to get a new instance.
   */
  private Filum() {
    logger = new FilumLog();
    eventsToSend = new ConcurrentLinkedQueue<>();
    aboutToStartFlushing = false;
  }

  /**
   * Return the default class instance of Filum that is associated with "" or no string (null).
   *
   * @return the Filum instance that should be used for instrumentation
   */
  public static Filum getInstance() {
    return getInstance("");
  }

  /**
   * Return the class instance of Filum that is associated with this name
   *
   * @param instanceName The key (unique identifier) that matches to the Filum instance
   * @return the Filum instance that should be used for instrumentation
   */
  public static Filum getInstance(String instanceName) {
    if (!instances.containsKey(instanceName)) {
      Filum ampInstance = new Filum();
      instances.put(instanceName, ampInstance);
    }
    return instances.get(instanceName);
  }

  /**
   * Set the API key for this instance of Filum. API key is necessary to authorize and route
   * events to the current Filum project.
   *
   * @param key the API key from Filum website
   */
  public void init(String key) {
    apiKey = key;
    updateHttpCall(HttpCallMode.REGULAR_HTTPCALL);
  }

  /**
   * Set the Event Upload Mode. If isBatchMode is true, the events will log through the Filum
   * HTTP V2 Batch API.
   *
   * @param isBatchMode if using batch upload or not;
   */
  public void useBatchMode(Boolean isBatchMode) {
    updateHttpCall(isBatchMode ? HttpCallMode.BATCH_HTTPCALL : HttpCallMode.REGULAR_HTTPCALL);
  }

  private void updateHttpCall(HttpCallMode updatedHttpCallMode) {
    if (httpCallMode == null || httpCallMode != updatedHttpCallMode) {
      httpCallMode = updatedHttpCallMode;
      httpCall =
          (updatedHttpCallMode == HttpCallMode.BATCH_HTTPCALL)
              ? new BatchHttpCall(apiKey)
              : new GeneralHttpCall(apiKey);
    }
  }

  /**
   * Set the level at which to filter out debug messages from the Java SDK.
   *
   * @param logMode Messages at this level and higher (more urgent) will be logged in the console.
   */
  public void setLogMode(FilumLog.LogMode logMode) {
    this.logger.setLogMode(logMode);
  }

  public void identify(Event event) {
    if(event.getEventType() != "identify") {
      logger.error("Wrong Event Type", "Wrong event type for identify call. You should pass event of type identify to identify() fuction");
    } else {
      eventsToSend.add(event);
      if (eventsToSend.size() >= Constants.EVENT_BUF_COUNT) {
        flushEvents();
      } else {
        tryToFlushEventsIfNotFlushing();
      }
    }
  }

  public void track(Event event) {
    if(event.getEventType() != "track") {
      logger.error("Wrong Event Type", "Wrong event type for track call. You should pass event of type track to track() fuction");
    } else {
      eventsToSend.add(event);
      if (eventsToSend.size() >= Constants.EVENT_BUF_COUNT) {
        flushEvents();
      } else {
        tryToFlushEventsIfNotFlushing();
      }
    }
  }

  private void tryToFlushEventsIfNotFlushing() {
    if (!aboutToStartFlushing) {
      aboutToStartFlushing = true;
      Thread flushThread =
          new Thread(
              () -> {
                try {
                  Thread.sleep(Constants.EVENT_BUF_TIME_MILLIS);
                } catch (InterruptedException e) {

                }
                flushEvents();
                aboutToStartFlushing = false;
              });
      flushThread.start();
    }
  }

  /**
   * Forces events currently in the event buffer to be sent to Filum API endpoint. Only one
   * thread may flush at a time. Next flushes will happen immediately after.
   */
  public synchronized void flushEvents() {
    if (eventsToSend.size() > 0) {
      List<Event> eventsInTransit = new ArrayList<>(eventsToSend);
      eventsToSend.clear();
      CompletableFuture.supplyAsync(
              () -> {
                Response response = null;
                try {
                  response = httpCall.syncHttpCallWithEventsBuffer(eventsInTransit);
                } catch (FilumInvalidAPIKeyException e) {
                  throw new CompletionException(e);
                }
                return response;
              })
          .thenAcceptAsync(
              response -> {
                Status status = response.status;
                // if (Retry.shouldRetryForStatus(status)) {
                //   Retry.sendEventsWithRetry(eventsInTransit, response, httpCall);
                // }
              })
          .exceptionally(
              exception -> {
                logger.error("Invalid API Key", exception.getMessage());
                return null;
              });
    }
  }
}
