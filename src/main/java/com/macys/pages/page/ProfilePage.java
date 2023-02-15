package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import java.util.Map;

public class ProfilePage extends BasePage {
    public final static Logger LOGGER = Logger.getLogger(ProfilePage.class);
    private final By VALIDATE_SUCCESS_MESSAGE = By.xpath("//*[@id=\"my-account-container\"]/div/div[2]/section[1]/div/div/div/small");
    private final By MY_ACCOUNT = By.xpath("//*[@id=\"my-account-container\"]/div/div[1]/nav/ul/li[1]/a/h2");
    public void ValidateSuccessMessage(Map<String,String>data)
    {
        String first_name="";
        if(isElementVisible(VALIDATE_SUCCESS_MESSAGE)){
            System.out.println("Element is present.........");
            if(data.containsKey("firstName")){
                first_name=data.get("firstName");
            }
            String expected_Message ="Congrats, "+first_name+"! You've successfully created a Macy's account.";
            String actual_Message=getWebElementText(VALIDATE_SUCCESS_MESSAGE);
            Assert.assertEquals(actual_Message,expected_Message);
            report("INFO","Validated success message");
            LOGGER.info("Element is present");
        }else{
            report("ERROR","Element is not present");
        }
    }
    public void click_On_My_Account_btn(){
        if(isElementPresent(MY_ACCOUNT)){
            System.out.println("My Account element is present");
            buttonClick(MY_ACCOUNT);
            report("INFO","Click ON My Account Button");
        }
        else {
            report("ERROR","Element is not present");
        }
    }
}
