package com.filum.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.filum.Event;
import com.filum.EventBuilder;
import com.filum.exception.FilumException;


public class EventsGenerator {
  public static List<Event> generateEvents(int eventCount) throws FilumException {
    return generateEvents(eventCount, 1);
  }

  public static List<Event> generateEvents(int eventCount, int userIdCount) throws FilumException {
    List<Event> events = new ArrayList<>();
    Random rand = new Random();
    String[] users = new String[userIdCount];
    for (int i = 0; i < userIdCount; i++) {
      users[i] = "test-user-id-" + i;
    }
    for (int i = 0; i < eventCount; i++) {
      EventBuilder newEventBuilder = new EventBuilder("track", "sample-type-" + i);
      newEventBuilder.setUserID(users[rand.nextInt(userIdCount)]);
      events.add(newEventBuilder.getEvent());
    }
    return events;
  }
}
