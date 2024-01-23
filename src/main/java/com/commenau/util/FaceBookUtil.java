package com.commenau.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class FaceBookUtil {
    private final static String FACEBOOK_APP_ID = "";
    private final static String FACEBOOK_APP_SECRET = "";
    private final static String FACEBOOK_REDIRECT_URL = "http://localhost:8080/login-facebook";
    private final static String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s";

    public static String getToken(final String code) throws IOException {
        String link = String.format(FACEBOOK_LINK_GET_TOKEN, FACEBOOK_APP_ID, FACEBOOK_APP_SECRET,
                FACEBOOK_REDIRECT_URL, code);
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    public static com.restfb.types.User getUserInfo(final String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, FACEBOOK_APP_SECRET, Version.LATEST);
        return facebookClient.fetchObject("me", com.restfb.types.User.class,
                Parameter.with("fields", "id,name,email,last_name,middle_name,first_name"));
    }


}
