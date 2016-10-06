package com.foo;

import com.github.tomakehurst.wiremock.http.Request;

/**
 * Created by sdutta on 9/21/16.
 */
public class ResponseGenerator {

    public static String generateResponse(Request request){
        request.getHeader("");
        return request.getBodyAsString();
    }
}
