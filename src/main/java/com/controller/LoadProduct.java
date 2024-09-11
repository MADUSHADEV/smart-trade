package com.controller;

import com.dto.Response_DTO;
import com.entity.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import netscape.javascript.JSObject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadProduct", value = "/load-product")
public class LoadProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Response_DTO responseDto = new Response_DTO();
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
//        category criteria
        Criteria criteriaCategory = session.createCriteria(Category.class);
        criteriaCategory.addOrder(Order.asc("name"));
        List<Category> category = criteriaCategory.list();

//        model criteria
        Criteria criteriaModel = session.createCriteria(Models.class);
        criteriaModel.addOrder(Order.asc("name"));
        List<Models> models = criteriaModel.list();

//        color criteria
        Criteria criteriaColor = session.createCriteria(Color.class);
        criteriaColor.addOrder(Order.asc("name"));
        List<Color> colors = criteriaColor.list();

        // conditions criteria
        Criteria criteriaConditions = session.createCriteria(ProductCondition.class);
        criteriaConditions.addOrder(Order.asc("name"));
        List<ProductCondition> condition = criteriaConditions.list();

        // storage criteria
        Criteria criteriaStorage = session.createCriteria(Storage.class);
        criteriaStorage.addOrder(Order.asc("value"));
        List<Storage> storage = criteriaStorage.list();

        JsonObject featureList = new JsonObject();
        featureList.add("category", gson.toJsonTree(category));
        featureList.add("models", gson.toJsonTree(models));
        featureList.add("colors", gson.toJsonTree(colors));
        featureList.add("condition", gson.toJsonTree(condition));
        featureList.add("storage", gson.toJsonTree(storage));

        responseDto.setSuccess(true);
        responseDto.setMessage(featureList);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDto));

    }
}
