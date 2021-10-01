# Filum Java SDK
The backend Java SDK to send event to Filum Business Data Platform

## Installation and Quick Start
Please visit the [Developer Center](https://docs.filum.ai/sources/java) for instructions on using the SDK.

### Installation

1. Download the [Filum Java SDK JAR](https://filum-assets.sgp1.digitaloceanspaces.com/sdk/filum-java-sdk-0.0.1.jar)
2. Add the JAR file to your project dependencies. Example: I put the JAR inside the `libs` folder:
```gradle
dependencies {
    implementation 'org.json:json:20201115'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation files('libs/filum-java-sdk-0.0.1.jar')
}
```

3. Import and use in code:
- Update `WRITEKEY` to your Filum source's writekey. You can get the writekey of your source in the source's Overview tab.
```java
import com.filum.Filum;
import com.filum.exception.FilumException;
import com.filum.EventBuilder;

...

Filum filum = Filum.getInstance("EXAMPLE_INSTANCE");
filum.init("WRITEKEY");

EventBuilder identifyBuilder = new EventBuilder("identify");
JSONObject eventParams = new JSONObject();
eventParams.put("name", "Harry Potter");
eventParams.put("age", 10);
eventParams.put("float_value", 1111.11);
eventParams.put("date", getCurrentTimeISO());
identifyBuilder.setEventParams(eventParams);
identifyBuilder.setUserID("+84946113111");
Event exampleEvent = identifyBuilder.getEvent();
filum.identify(exampleEvent);

EventBuilder trackBuilder = new EventBuilder("track", "Home Page Visited");
trackBuilder.setUserID("+84946113111");
trackBuilder.setEventParams(eventParams);
Event trackEvent = trackBuilder.getEvent();
filum.track(trackEvent);
```

You can refer to the Demo Project for more details.

### Development Guideline
1. Develop using Docker:
```
docker run --rm -v ${PWD}:/home/gradle -p 8080:8080 -it --entrypoint=/bin/bash gradle:jdk11 -i
```

2. Navigate to `demo` folder:
```
cd src/demo
```

3. Update `WRITEKEY` to your Filum source's WRITEKEY in `DemoController.java` file

4. Build the demo project
```
gradle build
```

5. In your browser go to `localhost:8080` and you will see Identify and Track event appear in your source Debugging view.

6. Update your code and rebuild and refresh browser to check for updates from your code modification.