package com.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

public class restassured {

    @Test(groups={"20161","reg_2023","smoke_test"})
    public void test() {

        Response response = RestAssured.get("https://reqres.in/api/users?page=2");
        System.out.println(response.statusCode());
        System.out.println(response.asString());
        System.out.println(response.getBody().asString());
        System.out.println(response.statusLine());

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
       //String res = response.getBody().asString();
        //convert JSON to string
        JsonPath json = new JsonPath(response.asString());
        System.out.println(json.getString("page"));
        System.out.println(json.getString("support.url"));

        int data = json.getInt("data.size()");
        for( int i=0;i<=data;i++)
        {
            String id = json.getString("data["+i+"].id");
            String email = json.getString("data["+i+"].email");
            System.out.println(id);
            System.out.println(email);
        }
    }
}
