package com.filum;

import java.util.UUID;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


interface ContextBuilderInterface{
    void setActive(Integer isActive);
    void setApp(JSONObject appDict);
    void setCampaign(JSONObject campaignDict);
    void setDevice(JSONObject deviceDict);
    void setIP(String ip);
    void setLibrary(JSONObject libraryDict);
    void setLocale(String locale);
    void setLocation(JSONObject locationDict);
    void setNetwork(JSONObject networkDict);
    void setOS(JSONObject osDict);
    void setPage(JSONObject pageDict);
    void setReferrer(JSONObject referrerDict);
    void setScreen(JSONObject screenDict);
    void setUserAgent(String userAgent);
}

public class ContextBuilder implements ContextBuilderInterface {
    private JSONObject context;

    public ContextBuilder(){
        this.context = new JSONObject();
        this.setActive(1);
        this.setApp(null);
        this.setCampaign(null);
        this.setDevice(null);
        this.setIP(null);
        this.setLibrary(null);
        this.setLocale(null);
        this.setLocation(null);
        this.setNetwork(null);
        this.setOS(null);
        this.setPage(null);
        this.setReferrer(null);
        this.setScreen(null);
        this.setUserAgent(null);
    }

    @Override
    public void setActive(Integer isActive) {
        if(isActive != null) {
            this.context.put("active", isActive);
        }
        else {
            this.context.put("active", 1);
        }
    }

    @Override
    public void setApp(JSONObject appDict) {
        this.context.put("app", replaceWithJSONNull(appDict));
    }

    @Override
    public void setCampaign(JSONObject campaignDict){
        this.context.put("campaign", replaceWithJSONNull(campaignDict));
    }

    @Override
    public void setDevice(JSONObject deviceDict) {
        this.context.put("device", replaceWithJSONNull(deviceDict));
    }

    @Override
    public void setIP(String ip) {
        this.context.put("ip", replaceWithNull(ip));
    }

    @Override
    public void setLibrary(JSONObject libraryDict) {
        if(libraryDict != null){
            this.context.put("library",libraryDict);
        }
        else {
            JSONObject defaultLibrary = new JSONObject();
            defaultLibrary.put("name", Constants.SDK_LIBRARY);
            defaultLibrary.put("version", Constants.SDK_VERSION);
            this.context.put("library", defaultLibrary);
        }
    }

    @Override
    public void setLocale(String locale){
        this.context.put("locale", replaceWithNull(locale));
    }

    @Override
    public void setLocation(JSONObject locationDict){
        this.context.put("location", replaceWithJSONNull(locationDict));
    }

    @Override
    public void setNetwork(JSONObject networkDict) {
        this.context.put("network", replaceWithJSONNull(networkDict));
    }

    @Override
    public void setOS(JSONObject osDict) {
        this.context.put("os", replaceWithJSONNull(osDict));
    }

    @Override
    public void setPage(JSONObject pageDict) {
        this.context.put("page", replaceWithJSONNull(pageDict));
    }

    @Override
    public void setReferrer(JSONObject referrerDict) {
        this.context.put("referrer", replaceWithJSONNull(referrerDict));
    }

    @Override
    public void setScreen(JSONObject screenDict) {
        this.context.put("screen", replaceWithJSONNull(screenDict));
    }

    @Override
    public void setUserAgent(String userAgent) {
        this.context.put("user_agent", replaceWithNull(userAgent));
    }

    /** internal methods */
    protected Object replaceWithJSONNull(Object obj) {
        return obj == null ? JSONObject.NULL : obj;
    }

    protected Object replaceWithNull(Object obj) {
        return obj == null ? null : obj;
    }

    public JSONObject getContext() {
        return this.context;
    }
}