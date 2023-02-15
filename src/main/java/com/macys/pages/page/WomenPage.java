package com.macys.pages.page;

import com.macys.pages.common.BasePage;
import com.macys.utils.DriverSource;
import org.openqa.selenium.By;
import org.testng.log4testng.Logger;

public class WomenPage extends BasePage {
    public final static Logger LOGGER = Logger.getLogger(WomenPage.class);
    private final By TEXT_WOMEN = By.xpath("//*[@id=\"localContentContainer\"]/div[2]/div/h1");
    private final By WOMENS_MOUSEOVER = By.xpath("//*[@id=\"flexid_118\"]/a/span");
    private final By SORT_BY_FEATURED_ITEMS= By.xpath("//*[@id=\"sortBy\"]");

    public void VerifyTextWomen()
    {
     if(isElementVisible(TEXT_WOMEN)){
         System.out.println("Element is present.........");
         report("INFO","element present on macys women page");
         LOGGER.info("Element is present");
         System.out.println("Element is present");
     }else{
         report("ERROR","Element is not present");
     }
    }
    public void MouseOverWomenAndClickOnShoes(String value){
        By WOMENS_SHOES = By.xpath("//div[h5[contains(text(),'Complete Your Look')]]/a[contains(text(),'"+value+"')]");
        if(isElementVisible(WOMENS_MOUSEOVER)){
            System.out.println("Womens mouse over");
            mouseOverOperationAndClick(WOMENS_MOUSEOVER,WOMENS_SHOES);
            report("INFO","Mouse over womens link and click on shoes link");
        }else{
            report("ERROR","Element is not present");
        }
    }
    public void sortByFeaturedItems(String value){
            selectvalue(SORT_BY_FEATURED_ITEMS,value);
            report("INFO","select the featured item");
           // report("ERROR","Not able to select the featured item");
    }

}
