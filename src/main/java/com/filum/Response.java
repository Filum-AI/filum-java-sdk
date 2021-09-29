package com.filum;

import com.filum.exception.FilumInvalidAPIKeyException;

import org.json.JSONObject;

public class Response {
  protected int code;
  protected Status status;
  protected String error;
  protected JSONObject message;

  private static boolean hasInvalidAPIKey(String errorMsg) {
    String invalidAPIKeyError = "Invalid API key: .*";
    return errorMsg.matches(invalidAPIKeyError);
  }

  protected static Response populateResponse(JSONObject json)
      throws FilumInvalidAPIKeyException {
    Response res = new Response();
    
    if (json.length() == 0) {
      int code = 200;
      res.status = Status.getCodeStatus(code);
      return res;
    }

    int code = json.getInt("statusCode");
    Status status = Status.getCodeStatus(code);
    res.code = code;
    res.status = status;
    if (status == Status.INVALID) {
      res.error = Utils.getStringValueWithKey(json, "error");
      if (hasInvalidAPIKey(res.error)) throw new FilumInvalidAPIKeyException(res.error);
      res.message = new JSONObject();
      res.message.put(
          "message", Utils.getJSONArrayValueWithKey(json, "message"));
    } else if (status == Status.PAYLOAD_TOO_LARGE) {
      res.error = Utils.getStringValueWithKey(json, "error");
    }
    return res;
  }
}
