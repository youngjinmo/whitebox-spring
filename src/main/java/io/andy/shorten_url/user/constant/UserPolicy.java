package io.andy.shorten_url.user.constant;

import io.andy.shorten_url.userLog.constant.UserLogMessage;

public class UserPolicy {
    private UserPolicy() {}
    public static final String[] PRIVACY_FIELDS = {"username", "password"};
    public static final UserLogMessage[] REQUIRED_PUT_LOG_FIELDS = {UserLogMessage.UPDATE_USERNAME, UserLogMessage.UPDATE_STATE};
}
