package com.filum;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filum.exception.FilumInvalidAPIKeyException;
import com.filum.util.EventsGenerator;
import com.filum.exception.FilumException;


@ExtendWith(MockitoExtension.class)
public class FilumTest {

  private final String apiKey = "test-apiKey";

  @Test
  public void testGetInstance() {
    Filum a = Filum.getInstance();
    Filum b = Filum.getInstance("");
    Filum d = Filum.getInstance("app1");
    Filum e = Filum.getInstance("app2");

    assertSame(a, b);
    assertNotSame(d, e);
    assertNotSame(a, d);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void testTrackEventSuccess(boolean useBatch)
      throws InterruptedException, NoSuchFieldException, IllegalAccessException,
          FilumInvalidAPIKeyException, FilumException {
    Filum filum = Filum.getInstance("test");
    filum.init(apiKey);
    filum.useBatchMode(useBatch);
    filum.setLogMode(FilumLog.LogMode.OFF);
    List<Event> events = EventsGenerator.generateEvents(10, 5);
    HttpCall httpCall = getMockHttpCall(filum, useBatch);
    Response response = new Response();
    response.code = 200;
    response.status = Status.SUCCESS;
    CountDownLatch latch = new CountDownLatch(1);
    when(httpCall.syncHttpCallWithEventsBuffer(anyList()))
        .thenAnswer(
            invocation -> {
              latch.countDown();
              return response;
            });
    for (Event event : events) {
      filum.track(event);
    }
    assertTrue(latch.await(1L, TimeUnit.SECONDS));
    verify(httpCall, times(1)).syncHttpCallWithEventsBuffer(anyList());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void testTrackEventWithInvalidKeyException(boolean useBatch)
      throws InterruptedException, NoSuchFieldException, IllegalAccessException,
          FilumInvalidAPIKeyException, FilumException {
    Filum filum = Filum.getInstance("test");
    filum.init(apiKey);
    filum.useBatchMode(useBatch);
    filum.setLogMode(FilumLog.LogMode.OFF);
    List<Event> events = EventsGenerator.generateEvents(10, 5);
    HttpCall httpCall = getMockHttpCall(filum, useBatch);
    CountDownLatch latch = new CountDownLatch(1);
    when(httpCall.syncHttpCallWithEventsBuffer(anyList()))
        .thenAnswer(
            invocation -> {
              latch.countDown();
              throw new FilumInvalidAPIKeyException("test");
            });
    for (Event event : events) {
      filum.track(event);
    }
    assertTrue(latch.await(1L, TimeUnit.SECONDS));
    verify(httpCall, times(1)).syncHttpCallWithEventsBuffer(anyList());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  public void testTrackEventWithInvalidResponse(boolean useBatch)
      throws InterruptedException, NoSuchFieldException, IllegalAccessException,
          FilumInvalidAPIKeyException, FilumException {
    Filum filum = Filum.getInstance("test");
    filum.init(apiKey);
    filum.useBatchMode(useBatch);
    filum.setLogMode(FilumLog.LogMode.OFF);
    List<Event> events = EventsGenerator.generateEvents(10, 5);
    HttpCall httpCall = getMockHttpCall(filum, useBatch);
    Response response = new Response();
    response.code = 400;
    response.status = Status.INVALID;
    CountDownLatch latch = new CountDownLatch(1);
    when(httpCall.syncHttpCallWithEventsBuffer(anyList()))
        .thenAnswer(
            invocation -> {
              latch.countDown();
              return response;
            });
    for (Event event : events) {
      filum.track(event);
    }
    assertTrue(latch.await(1L, TimeUnit.SECONDS));
    verify(httpCall, atLeast(1)).syncHttpCallWithEventsBuffer(anyList());
  }

  private HttpCall getMockHttpCall(Filum filum, boolean useBatch)
      throws NoSuchFieldException, IllegalAccessException, FilumException {
    HttpCall httpCall;
    if (useBatch) {
      httpCall = mock(BatchHttpCall.class);
    } else {
      httpCall = mock(GeneralHttpCall.class);
    }
    Field httpCallField = filum.getClass().getDeclaredField("httpCall");
    httpCallField.setAccessible(true);
    httpCallField.set(filum, httpCall);
    return httpCall;
  }
}
