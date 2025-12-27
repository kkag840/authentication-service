package com.common.oauth.model;


public class UserAgentDetails {
    private final String browserName;
    private final String browserVersion;
    private final String browserComments;

    public UserAgentDetails(final String browserName, final String browserVersion, final String browserComments) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.browserComments = browserComments;
    }

    public String getBrowserComments() {
        return browserComments;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }
}
