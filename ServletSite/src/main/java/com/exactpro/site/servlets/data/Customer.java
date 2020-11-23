package com.exactpro.site.servlets.data;

import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "get-data/Customers")
public class Customer extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getReader().lines().collect(Collectors.joining(", ")));
        Map<String, Map<String, String>> data = new HashMap<>();

        int usersCount = 10;
        String[] fields = new String[5];

        for (int i = 0; i < fields.length; i++) {
            fields[i] = "Field "+i;
        }

        for (int i = 0; i < usersCount; i++) {
            Map<String, String> userData = new HashMap<>();
            for (int j = 0, fieldsLength = fields.length; j < fieldsLength; j++) {
                String field = fields[j];
                if (j == 0){
                    userData.put(field, "User "+i);
                }
                else {
                    userData.put(field, String.valueOf(i));
                }
            }
            data.put("User "+i, userData);
        }

        JSONObject json = new JSONObject();

        json.put("columns", fields);
        json.put("data", data);

        resp.getWriter().write(json.toString());
    }

    @Override
    public void destroy() {

    }
}
