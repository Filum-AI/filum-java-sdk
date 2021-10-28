# Filum Java SDK
The backend Java SDK to send event to Filum Business Data Platform

## Installation and Quick Start
Please visit the [Developer Center](https://docs.filum.ai/sources/java) for instructions on using the SDK.

### Installation

1. Download the latest JAR file [Filum Java SDK Releases](https://github.com/Filum-AI/filum-java-sdk/releases)
2. Add the JAR file to your project dependencies. Example: I put the JAR inside the `libs` folder:
```gradle
dependencies {
    implementation 'org.json:json:20201115'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation files('libs/filum-java-sdk-0.0.2.jar')
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


## Development Guideline
1. Develop using Docker:
```
docker run --rm -v ${PWD}:/home/gradle -p 8080:8080 -it --entrypoint=/bin/bash gradle:jdk11 -i
```

2. Navigate to `demo` folder and update the build.gradle to point to the code instead of the .jar library file:
```
cd src/demo
```
Update the dependencies like this:
```
dependencies {
    implementation 'org.json:json:20201115'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    // implementation files('libs/filum-java-sdk-0.0.2.jar')
    implementation project(':main')
}
```

3. Update `WRITEKEY` to your Filum source's WRITEKEY in `DemoController.java` file

4. Build the demo project then run:
```
gradle build
java -jar build/libs/demo.jar
```

5. In your browser go to `localhost:8080` and you will see Identify and Track event appear in your source Debugging view.

6. Update your code and rebuild and refresh browser to check for updates from your code modification.


## Running Test
You can run the test within Dockerized environment as follow:

1. Start the Docker container:
```
docker run --rm -v ${PWD}:/home/gradle -p 8080:8080 -it --entrypoint=/bin/bash gradle:jdk11 -i
```

2. Navigate to `test` folder:
```
cd src/test
```

3. Run test:
```
gradle test
```

4. You can view the test result in the command like or in the test report at this location in your host machine:
```
./src/test/build/reports/tests/test/index.html
```
