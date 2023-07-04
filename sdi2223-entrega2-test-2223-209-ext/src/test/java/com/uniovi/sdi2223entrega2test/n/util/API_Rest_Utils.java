package com.uniovi.sdi2223entrega2test.n.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class API_Rest_Utils {

    static public String login(String email, String password) {
        // Hacemos login
        final String RestAssuredURL = "http://localhost:3000/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "user01@email.com");
        requestParams.put("password", "user01");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Obtenemos el token
        return response.getBody().jsonPath().get("token");
    }
}