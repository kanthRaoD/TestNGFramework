package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import org.openqa.selenium.By;
import org.testng.log4testng.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountPage extends BasePage {
    public final static Logger LOGGER = Logger.getLogger(CreateAccountPage.class);
    private final By SECURE_CREATE_ACCOUNT = By.xpath("//*[@id=\"create-account\"]/div/div[1]/div/h1");
    private final By FIRST_NAME = By.id("ca-profile-firstname");
    private final By LAST_NAME = By.id("ca-profile-lastname");
    private final By EMAIL_ADDRESS = By.id("ca-profile-email");
    private final By PASSWORD = By.id("ca-profile-password");
    private final By SHOW_PASSWORD= By.id("ca-show-pass-btn");
    private final By SELECT_BIRTHDAY_MONTH = By.id("ca-profile-birth-month");
    private final By SELECT_BIRTHDAY_DAY = By.id("ca-profile-birth-day");
    private final By CREATE_ACCOUNT_BTN = By.id("ca-profile-submit");
    Map<String,String> data = new HashMap<>();
    public Map<String,String> createAccount()
    {
        if(isElementVisible(SECURE_CREATE_ACCOUNT)){
            System.out.println("Secure Account Element is present.........");
            String first_Name=randomStringWithCharacters(10);
            data.put("firstName",first_Name);
            String last_Name = randomStringWithCharacters(5);
            data.put("lastName",last_Name);
            String email_Address= randomString(8);
            email_Address = email_Address+"@gmail.com";
            data.put("emailAddress",email_Address);

            String password = randomString(10)+randomNumber(3);
            data.put("Password",password);
            enterText(FIRST_NAME,first_Name);
            enterText(LAST_NAME,last_Name);
            enterText(EMAIL_ADDRESS,email_Address);
            enterText(PASSWORD,password);
            buttonClick(SHOW_PASSWORD);
            selectOption(SELECT_BIRTHDAY_MONTH,"January");
            selectOptionByIndex(SELECT_BIRTHDAY_DAY,12);

            report("INFO","element present on create AccountPage");
            LOGGER.info(".......Create Account Element is present.....");
            buttonClick(CREATE_ACCOUNT_BTN);
            report("INFO","Click on create account Successfully");


        }else{
            report("ERROR","Element is not present");
        }
        return data;
    }
}
