package com.filum;

import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filum.Filum;
import com.filum.exception.FilumException;
import com.filum.EventBuilder;


@RestController
public class DemoController {
	@RequestMapping("/")
	public String index() throws FilumException {
		Filum filum = Filum.getInstance("INSTANCE_NAME");
		filum.init("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOjM0OH0.eTKQX4vcKFkx2jQs7S1GzDwIb9q7xdaqcQr2Ydhgafw");
		
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

		filum.setLogMode(FilumLog.LogMode.DEBUG);
		return "Filum Java SDK Demo: sending test event.";
	}

	public static String getCurrentTimeISO(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }
}
