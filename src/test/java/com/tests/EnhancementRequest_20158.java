package com.tests;

import com.macys.pages.page.*;
import com.macys.utils.TestManager;
import org.testng.annotations.Test;

import java.util.Map;

public class EnhancementRequest_20158 extends TestManager {

    @Test(groups={"20158","reg_2022","Smoke_test2023","regression_test","test"})
    public void enhancementRequest(){
        HomePage homePage = new HomePage();
        SignInPage signInPage= new SignInPage();
        ProfilePage profilePage = new ProfilePage();
        Map<String,String> data;
        CreateAccountPage createAccountPage = new CreateAccountPage();
        setScenarioName("TC20158_CreateAccount_and_Login");
        setSubScenarioName("TC30155_Validate_Macys_Title");
        homePage.ValidateMacysTitle();
        signInPage.clickOnCreateAccount();
        setSubScenarioName("TC30156_Create_Account");
        data=createAccountPage.createAccount();
        profilePage.ValidateSuccessMessage(data);
        profilePage.click_On_My_Account_btn();
        homePage.click_On_Sign_Out_btn();
        homePage.ValidateMacysTitle();
        setSubScenarioName("TC30157_Sign_In");
        signInPage.signIn(data);

    }
}
