package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.util.Properties;

public class HomePage extends BasePage {
    public final static Logger LOGGER = Logger.getLogger(HomePage.class);
    private final By TITLE_MACYS_MAIN = By.id("logo");
    private final By SIGN_IN = By.xpath("//*[@id=\"myRewardsLabel-status\"]/span");
    private final By SIGN_OUT = By.xpath("//*[@id=\"globalHeaderSignout\"]");
    private static String testdataPropertyfile="src\\test\\resources\\";


    public void ValidateMacysTitle()
    {
        Properties prop =loadProperties(testdataPropertyfile +"testdata.properties");
        getDriver().get(prop.getProperty("macys_url"));
        if(isElementVisible(TITLE_MACYS_MAIN)){
            System.out.println("Element is present.........");
            report("INFO","element present on macys home page");
            LOGGER.info("Element is present ");
            buttonClick(SIGN_IN);

        }else{
            report("ERROR","Element is not present");
        }
    }
    public void click_On_Sign_Out_btn()
    {
        if(isElementPresent(SIGN_IN)){
            System.out.println("SIGN_OUT Element is present.........");
            report("INFO","element present on macys home page");
            LOGGER.info("Element is present");
            mouseOverOperation(SIGN_IN);
            buttonClick(SIGN_OUT);
        }else{
            report("ERROR","Element is not present");
        }

    }

}
