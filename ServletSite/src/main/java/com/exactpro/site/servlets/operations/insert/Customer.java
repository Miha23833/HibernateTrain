package com.exactpro.site.servlets.operations.insert;

import com.exactpro.connection.DBConnection;
import com.exactpro.site.querymanager.QueryManager;
import com.exactpro.site.querymanager.SQLQuery;
import com.exactpro.site.servlets.operations.Mapping;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "insert-data/Customers")
public class Customer extends HttpServlet implements Mapping {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject reqJSON = requestToJSON(req);
        if (reqJSON.keySet().contains("queryParams")) {
            JSONObject queryParams = reqJSON.getJSONObject("queryParams");
            JSONObject response = new JSONObject();

            SQLQuery query = new SQLQuery(QueryManager.INSERT_QUERIES.customers, queryParams);
            try {
                DBConnection.executeNonResult(query.getParametrizedQuery());
                response.put("response", true);
            } catch (ClassNotFoundException | SQLException e) {
                response.put("response", false);
            }
            resp.getWriter().write(response.toString());
        }
    }
}
