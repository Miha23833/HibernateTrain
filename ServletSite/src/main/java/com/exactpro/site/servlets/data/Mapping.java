package com.exactpro.site.servlets.data;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public interface Mapping {
    default JSONObject requestToJSON(HttpServletRequest req) throws IOException {
        return new JSONObject(req.getReader().lines().collect(Collectors.joining()));
    }
}
