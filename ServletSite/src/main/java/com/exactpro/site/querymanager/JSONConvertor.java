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
            columns.add(metaData.getColumnName(i));
        }
        result.put("columns", new JSONArray(columns));

        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = null;
        int columnCount = metaData.getColumnCount();
        while (data.next()) {
            jsonObject = new JSONObject();
            for (int index = 1; index <= columnCount; index++) {
                String column = metaData.getColumnName(index);
                Object value = data.getObject(column);
                if (value == null) {
                    jsonObject.put(column, "");
                } else if (value instanceof Date) {
                    jsonObject.put(column, ((Date) value).getTime());
                } else  {
                    jsonObject.put(column, value);
                }
            }
            jArray.put(jsonObject);
        }
        result.put("response", jArray);
        return result;
    }

}
