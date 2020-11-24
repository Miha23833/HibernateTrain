package com.exactpro.site.querymanager;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLQuery {
    private final String query;
    private String parametrizedQuery;

    public SQLQuery(String query) {
        this.query = query;
    }

    public SQLQuery(String query, JSONObject parameters){
        this(query);
        setParameters(parameters);
    }

    public void setParameters(JSONObject parameters) {
        Pattern parameterPattern = Pattern.compile("(?<!['\\w])(:[\\w\\d]+)", Pattern.MULTILINE);

        StringBuilder builder = new StringBuilder(query);

        Matcher matcher = parameterPattern.matcher(query);

        while (matcher.find()) {
            String key = builder.substring(matcher.start(), matcher.end());
            String parameterValue = "null";
            if (parameters.keySet().contains(key)) {
                Object parameter = parameters.get(key);

                if (parameter == null || parameter.toString().equals("null")) {
                    parameterValue = "null";
                } else if (parameter instanceof String || parameter instanceof Character) {
                    parameterValue = String.join("", "'", parameter.toString(), "'");
                } else {
                    parameterValue = parameter.toString();
                }

            }
            builder.replace(matcher.start(), matcher.end(), parameterValue);
        }
        parametrizedQuery = builder.toString();

    }

    public String getParametrizedQuery(){
        return parametrizedQuery;
    }

    public String getNotParametrizedQuery(){
        return query;
    }
}
