package com.controller;

import com.dto.Response_DTO;
import com.model.HibernateUtil;
import com.model.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Session;

import java.io.IOException;

@MultipartConfig
@WebServlet(name = "ListProduct", value = "/list-product")
public class ListProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Response_DTO responseDto = new Response_DTO();

        String categoryId = req.getParameter("categoryId");
        String modelId = req.getParameter("modelId");
        String productTitle = req.getParameter("productTitle");
        String productDescription = req.getParameter("productDescription");
        String storageId = req.getParameter("storageId");
        String price = req.getParameter("price");
        String colorsId = req.getParameter("colorsId");
        String conditionId = req.getParameter("conditionId");
        String quantity = req.getParameter("quantity");

        Part image1 = req.getPart("image1");
        Part image2 = req.getPart("image2");
        Part image3 = req.getPart("image3");

        Session session = HibernateUtil.getSessionFactory().openSession();
        if (productTitle.isEmpty()) {
            responseDto.setMessage("Product title is required");
        } else if (!Validation.validateIntegerValue(categoryId)) {
            responseDto.setMessage("Invalid category");
        } else if (!Validation.validateIntegerValue(modelId)) {
            responseDto.setMessage("Invalid model");
        } else if (!Validation.validateIntegerValue(storageId)) {
            responseDto.setMessage("Invalid storage");
        } else if (!Validation.validateIntegerValue(colorsId)) {
            responseDto.setMessage("Invalid color");
        } else if (!Validation.validateIntegerValue(conditionId)) {
            responseDto.setMessage("Invalid condition");
        } else if (productDescription.isEmpty()) {
            responseDto.setMessage("Product description is required");
        } else if (price.isEmpty()) {
            responseDto.setMessage("Price is required");
        } else if (!Validation.validateDoubleValue(price)) {
            responseDto.setMessage("Invalid price");
        } else if (quantity.isEmpty()) {
            responseDto.setMessage("Quantity is required");
        } else if (!Validation.validateDoubleValue(quantity)) {
            responseDto.setMessage("Invalid quantity");
        } else if (image1.getSubmittedFileName() == null) {
            responseDto.setMessage("Image 1 is required");
        } else if (image2.getSubmittedFileName() == null) {
            responseDto.setMessage("Image 2 is required");
        } else if (image3.getSubmittedFileName() == null) {
            responseDto.setMessage("Image 3 is required");
        }else {
        }

    }
}
