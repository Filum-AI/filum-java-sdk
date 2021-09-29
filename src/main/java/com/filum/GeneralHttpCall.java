package com.filum;

public class GeneralHttpCall extends HttpCall {
  private static String apiUrl = Constants.API_URL;

  protected GeneralHttpCall(String apiKey) {
    super(apiKey);
  }

  @Override
  protected String getApiUrl() {
    return apiUrl;
  }
}
