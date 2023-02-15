package com.macys.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.log4testng.Logger;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DriverSource {

    private final int SET_IMPLICIT_TIME_OUT = 500;
    private final int SET_SCRIPT_TIME_OUT =60;
    private final int SET_PAGE_TIME_OUT =600;
    public final static Logger LOGGER = Logger.getLogger(DriverSource.class);
    @BeforeMethod(alwaysRun = true)
    public void newDriver(){
        WebDriver driver = null;
        int retryCounter =0;
        int maxRetryCount=0;
        String browser =System.getProperty("browser");
       // String browser = "chrome";
        System.out.println("testcase id" + System.getProperty("includedGroups"));
        //do{
            try {
                if(browser.equalsIgnoreCase("chrome")){
                    System.out.println("drive source");
                    //System.out.println(System.getProperty("user.dir")+"\\src\\main\\resources\\driver\\chromedriver.exe");
                    System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\main\\resources\\driver\\chromedriver.exe");
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.addArguments("--start-fullscreen");
                    options.addArguments("disable-extentions");
                    options.addArguments("--test-type");
                    options.addArguments("-incognito");
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
                    capabilities.setCapability("chrome.binary",System.getProperty("user.dir")+"\\src\\main\\resources\\driver\\chromedriver.exe");
                    capabilities.setCapability("screen-resolution","1280x1024");
                    capabilities.setCapability(ChromeOptions.CAPABILITY,options);
                    System.out.println("drive source");
                    driver = new ChromeDriver(options);

                }
                driver.manage().timeouts().implicitlyWait(SET_IMPLICIT_TIME_OUT, TimeUnit.MILLISECONDS);
                driver.manage().timeouts().setScriptTimeout(SET_SCRIPT_TIME_OUT,TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(SET_PAGE_TIME_OUT,TimeUnit.SECONDS);
                driver.manage().deleteAllCookies();
                Long threadId = Thread.currentThread().getId();
                TestUtil.driverMap.put(threadId,driver);
                
            }catch (WebDriverException e){
                 LOGGER.error("Webdriver exception : " + e.getMessage());
                 if( driver != null)
                     driver.quit();
                 if(++retryCounter> maxRetryCount){
                     Assert.assertTrue(false,"Exception while creating driver instance:"+e.getMessage());
                    // break;
                 }
            }
        //}while (true);
    }

    public WebDriver getDriver()
    {
        Long key = Thread.currentThread().getId();
        return TestUtil.driverMap.get(key);

    }
    public void quitDriver(){
        WebDriver driver = getDriver();
        if(driver!=null){
            driver.close();
            driver.quit();
        }
    }

}
