package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import org.openqa.selenium.By;
import org.testng.log4testng.Logger;

import java.util.Map;

public class SignInPage extends BasePage {
    public final static Logger LOGGER = Logger.getLogger(SignInPage.class);
    private final By CREATE_ACCOUNT = By.xpath("//*[@id=\"standard-sign-up\"]");
    private final By SIGN_IN_MESSAGE = By.xpath("//*[@id=\"pm-desktop-subheading\"]");
    private final By ENTER_EMAIL = By.id("email");
    private final By ENTER_PASSWORD = By.id("pw-input");
    private final By SIGN_BTN = By.id("sign-in");
    public void clickOnCreateAccount()
    {
        if(isElementVisible(CREATE_ACCOUNT)){
            System.out.println("Create Account Element is present.........");
            report("INFO","element present on macys home page");
            LOGGER.info(".......Create Account Element is present.....");
            buttonClick(CREATE_ACCOUNT);

        }else{
            report("ERROR","Element is not present");
        }
    }
    public void signIn(Map<String,String> data)
    {
        String email_address = "";
        String password="";
        if(isElementVisible(SIGN_IN_MESSAGE)){
            if(data.containsKey("emailAddress")){
               email_address = data.get("emailAddress");
            }
            if(data.containsKey("Password")){
                password = data.get("Password");
            }
            enterText(ENTER_EMAIL,email_address);
            enterText(ENTER_PASSWORD,password);
            report("INFO","element email and password");
            buttonClick(SIGN_BTN);
            System.out.println("Create Account Element is present.........");
            report("INFO","element present on macys home page");
            LOGGER.info(".......Create Account Element is present.....");


        }else{
            report("ERROR","Element is not present");
        }
    }
}
