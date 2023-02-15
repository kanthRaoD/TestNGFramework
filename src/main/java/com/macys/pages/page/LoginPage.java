package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import com.macys.utils.DriverSource;
import org.openqa.selenium.By;
import org.testng.log4testng.Logger;

public class LoginPage extends BasePage {
    public final static org.testng.log4testng.Logger LOGGER = Logger.getLogger(LoginPage.class);
    private final By TITLE_MACYS_MAIN = By.id("logo");
    private final By WOMEN_LINK = By.xpath("//*[@id=\"flexid_118\"]/a/span");
    private final By FLAT_SALE_WITH_DISCOUNT = By.id("//*[@id=\"categoryTree\"]/ul/li[1]/span/a");
    public  String  macysUrl ="https://www.macys.com/";
    public void ValidateMacysTitle()
    {
        getDriver().get(macysUrl);
        if(isElementVisible(TITLE_MACYS_MAIN)){
           System.out.println("Element is present.........");
           report("INFO","element present on macys home page");
           LOGGER.info("Element is present");
           buttonClick(WOMEN_LINK);
        }else{
         report("ERROR","Element is not present");
        }
    }
}
