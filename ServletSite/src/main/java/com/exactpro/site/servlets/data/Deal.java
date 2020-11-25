package com.exactpro.site.servlets.data;

import com.exactpro.connection.DBConnection;
import com.exactpro.site.querymanager.JSONConvertor;
import com.exactpro.site.querymanager.QueryManager;
import com.exactpro.site.querymanager.SQLQuery;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "get-data/Deals")
public class Deal extends HttpServlet implements Mapping {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject response = new JSONObject();
        response.put("filterMapping", "/get-data/Deals");
        resp.getWriter().write(response.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject reqJSON = requestToJSON(req);
        JSONObject queryParams = reqJSON.getJSONObject("queryParams");

        SQLQuery query = new SQLQuery(QueryManager.dealsSQL, queryParams);

        try {
            ResultSet respData = DBConnection.executeWithResult(query.getParametrizedQuery());
            resp.getWriter().write(JSONConvertor.toJSON(respData).toString());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
