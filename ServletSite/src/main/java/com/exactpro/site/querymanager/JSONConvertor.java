package com.exactpro.site.querymanager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONConvertor {

    public static JSONObject toJSON(ResultSet data) throws SQLException {
        ResultSetMetaData metaData = data.getMetaData();
        JSONObject result = new JSONObject();

        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.add(metaData.getColumnLabel(i));
        }
        result.put("columns", new JSONArray(columns));

        List<Object> row = new ArrayList<>();
        JSONObject jsonObject = null;
        while (data.next()) {
            jsonObject = new JSONObject();
            for (String column: columns){
                Object value = data.getObject(column);
                if (value == null) {
                    jsonObject.putOpt(column, "");
                } else if (value instanceof Date) {
                    jsonObject.putOpt(column, ((Date) value).getTime());
                } else  {
                    jsonObject.putOpt(column, value);
                }
            }
            row.add(jsonObject);
        }
        result.put("columns", new JSONArray(columns));
        result.put("response", row);
        return result;
    }

}
