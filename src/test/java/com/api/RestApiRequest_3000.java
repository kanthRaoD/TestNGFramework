package com.api;

import com.macys.pages.page.LoginPage;
import com.macys.pages.page.WomenPage;
import com.macys.utils.TestManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RestApiRequest_3000 extends TestManager {

    @Test(groups={"3000","reg_20"})
    public void restapirequest(){
        setScenarioName("TC3000_api_request");
        setSubScenarioName("TC3001_Validate_api_request");
        Response response = RestAssured.get("https://reqres.in/api/users?page=2");
        System.out.println(response.statusCode());
        System.out.println(response.asString());
        System.out.println(response.getBody().asString());
        System.out.println(response.statusLine());
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals("xyz","wxyz");
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
