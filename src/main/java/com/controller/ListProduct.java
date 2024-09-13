package com.controller;

import com.dto.Response_DTO;
import com.dto.User_DTO;
import com.entity.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.HibernateUtil;
import com.model.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;

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
        } else {
            // Get the relative color from the database
            Color colorId = (Color) session.get(Color.class, Integer.parseInt(colorsId));
            // Get the relative condition from the database
            ProductCondition productConditionId = session.get(ProductCondition.class, Integer.parseInt(conditionId));
            // Get the relative model from the database
            Models models = session.get(Models.class, Integer.parseInt(modelId));
            //Get the relative storage from the database
            Storage storage = session.get(Storage.class, Integer.parseInt(storageId));
            // Get the relative status from the database
            ProductStatus productStatus = session.get(ProductStatus.class, 1);

            // Get the user from the session
            User_DTO user_dto = (User_DTO) req.getSession().getAttribute("user");
            Criteria userCriteria = session.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", user_dto.getEmail()));
            User user = (User) userCriteria.uniqueResult();

            // Save the product to the database
            Product product = new Product();
            product.setColor(colorId);
            product.setProductStatus(productStatus);
            product.setProductCondition(productConditionId);
            product.setStorage(storage);
            product.setModel(models);
            product.setTitle(productTitle);
            product.setDescription(productDescription);
            product.setPrice(Double.parseDouble(price));
            product.setQty(Integer.parseInt(quantity));
            product.setDate_time(Date.valueOf(LocalDate.now()));
            product.setUser(user);

            int productId = (int) session.save(product);
            session.beginTransaction().commit();

            // Save the product images to the database
            String applicationPath = req.getServletContext().getRealPath("");
            File folder = new File(applicationPath + "/product-image");

            // Create the folder if it does not exist
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file1 = new File(folder, productId + "-1.jpg");
            InputStream image1InputStream = image1.getInputStream();
            Files.copy(image1InputStream, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File file2 = new File(folder, productId + "-2.jpg");
            InputStream image2InputStream = image2.getInputStream();
            Files.copy(image2InputStream, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File file3 = new File(folder, productId + "-3.jpg");
            InputStream image3InputStream = image3.getInputStream();
            Files.copy(image3InputStream, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

            responseDto.setSuccess(true);
            responseDto.setMessage("Product listed successfully");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDto));
        session.close();

    }
}
