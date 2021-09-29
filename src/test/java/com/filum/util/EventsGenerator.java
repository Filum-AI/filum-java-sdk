package com.filum.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.filum.Event;

public class EventsGenerator {
  public static List<Event> generateEvents(int eventCount) {
    return generateEvents(eventCount, 1, 1);
  }

  public static List<Event> generateEvents(int eventCount, int userIdCount, int deviceIdCount) {
    List<Event> events = new ArrayList<>();
    Random rand = new Random();
    String[] users = new String[userIdCount];
    String[] devices = new String[deviceIdCount];
    for (int i = 0; i < userIdCount; i++) {
      users[i] = "test-user-id-" + i;
    }
    for (int i = 0; i < deviceIdCount; i++) {
      devices[i] = UUID.randomUUID().toString();
    }
    for (int i = 0; i < eventCount; i++) {
      events.add(
          new Event(
              "sample-type-" + i,
              users[rand.nextInt(userIdCount)],
              devices[rand.nextInt(deviceIdCount)]));
    }
    return events;
  }
}
