package com.exactpro.site.querymanager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLQuery {
    private final String query;
    private String parametrizedQuery;

    public SQLQuery(String query) {
        this.query = query;
    }

    public SQLQuery(String query, JSONObject parameters) {
        this(query);
        setParameters(parameters);
    }

    public void setParameters(JSONObject parameters) {
        Pattern parameterPattern = Pattern.compile("(?<!['\\w])(:[\\w\\d]+)", Pattern.MULTILINE);

        StringBuilder builder = new StringBuilder(query);

        Matcher matcher = parameterPattern.matcher(query);

        List<Integer[]> parametersPos = new ArrayList<>();

        while (matcher.find()) {
            parametersPos.add(new Integer[]{matcher.start(), matcher.end()});
        }

        for (int i = parametersPos.size() - 1; i >= 0; i--) {
            String key = builder.substring(parametersPos.get(i)[0] + 1, parametersPos.get(i)[1]);
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
            builder.replace(parametersPos.get(i)[0], parametersPos.get(i)[1], parameterValue);
        }
        parametrizedQuery = builder.toString();

    }

    public String getParametrizedQuery() {
        return parametrizedQuery;
    }

    public String getNotParametrizedQuery() {
        return query;
    }
}
