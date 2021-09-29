package com.filum;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Date;

public class Utils {
  public static String getStringValueWithKey(JSONObject json, String key) {
    return json.has(key) && json.getString(key) != null ? json.getString(key) : "";
  }

  public static JSONObject getJSONObjectValueWithKey(JSONObject json, String key) {
    return (json.has(key) && !json.isNull(key)) ? json.getJSONObject(key) : new JSONObject();
  }

  public static JSONArray getJSONArrayValueWithKey(JSONObject json, String key) {
    return (json.has(key) && !json.isNull(key)) ? json.getJSONArray(key) : new JSONArray();
  }

  public static int[] jsonArrayToIntArray(JSONArray jsonArray) {
    int[] intArray = new int[jsonArray.length()];
    for (int i = 0; i < intArray.length; i++) {
      intArray[i] = jsonArray.optInt(i);
    }
    return intArray;
  }

  public static int[] convertJSONArrayToIntArray(JSONObject json, String key) {
    boolean hasKey = json.has(key) && !json.isNull(key);
    if (!hasKey) return new int[] {};
    else {
      JSONArray jsonArray = json.getJSONArray(key);
      return jsonArrayToIntArray(jsonArray);
    }
  }

  public static JSONObject convertToFilumItem(String key, Object value) throws JSONException {
    JSONObject json = new JSONObject();
    if(value instanceof String){
        json.put("key", key);
        JSONObject value_json = new JSONObject();
        value_json.put("string_value", value);
        json.put("value", value_json);
    }
    else if(value instanceof Integer){
        json.put("key", key);
        JSONObject value_json = new JSONObject();
        value_json.put("int_value", value);
        json.put("value", value_json);
    }
    else if(value instanceof Float){
        json.put("key", key);
        JSONObject value_json = new JSONObject();
        value_json.put("float_value", value);
        json.put("value", value_json);
    }
    else if(value instanceof Double){
        json.put("key", key);
        JSONObject value_json = new JSONObject();
        value_json.put("double_value", value);
        json.put("value", value_json);
    }
    else if(value instanceof Date){
        json.put("key", key);
        JSONObject value_json = new JSONObject();
        value_json.put("datetime_value", value);
        json.put("value", value_json);
    }
    return json;
  }
}
