package com.macys.pages.common;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.macys.utils.ExtentAppend;
import com.macys.utils.TestUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePage {
    public final static Logger LOGGER = Logger.getLogger(BasePage.class);
    public ExtentTest extentTest;
    //public ExtentLink extentLink;
    public ExtentAppend extentAppend = new ExtentAppend();
    String parentWindow;
    String parentWindowhandle;

    public WebDriver getDriver()
    {
        Long key = Thread.currentThread().getId();
        return TestUtil.driverMap.get(key);
    }
    public ExtentTest getExtentTest(){
        Long key = Thread.currentThread().getId();
        return TestUtil.extentTestMap.get(key);
    }
    public FluentWait<WebDriver> webDriverFluentWait(){
        return new FluentWait(getDriver()).withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }
    public boolean isElementVisible(By locator)
    {
        try{
            webDriverFluentWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            return  true;
        }catch (Exception e)
        {
            LOGGER.info("Exception isElementPresent::" + locator);
            return false;
        }
    }
    public String randomString(int len)
    {
        LOGGER.info("Before randomString with length::"+len);
        String AB ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0;i<len;i++)
        {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        LOGGER.info("After random string with length"+len);
        return sb.toString();
    }
    public String randomNumber(int len)
    {
        LOGGER.info("Before randomString with length::"+len);
        String AB ="0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0;i<len;i++)
        {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        LOGGER.info("After random string with length"+len);
        return sb.toString();
    }
    public String randomStringWithCharacters(int len)
    {
        LOGGER.info("Before randomString with length::"+len);
        String AB ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0;i<len;i++)
        {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        LOGGER.info("After random string with length"+len);
        return sb.toString();
    }
    public void report(String status, String message){
        SoftAssert softAssert = new SoftAssert();
        boolean retryStatus = false;
        int retryCounter=0;
        int maxRetryCount =3;
        extentTest = getExtentTest();
        do{
            try{
                if(status.equalsIgnoreCase("info")){
                    String screenshotPath = extentAppend.takeScreenshot(getDriver(),"INFO_"+randomString(5)+"_");
                    extentTest.log(Status.INFO,message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }else if (status.equalsIgnoreCase("error"))
                {
                    extentTest.log(Status.ERROR, MarkupHelper.createLabel(message, ExtentColor.AMBER));
                    Assert.fail(message,new Throwable());
                }else{
                    String screenshotPath = extentAppend.takeScreenshot(getDriver(),"PASS"+randomString(5)+"_");
                    extentTest.log(Status.PASS,message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }

            }catch (Exception e){
                e.printStackTrace();
                retryStatus =true;
                LOGGER.error("Report Method Exception :" +e.getMessage());
                if(++retryCounter>maxRetryCount){
                    Assert.assertTrue(false,"Exception while taking screenshot : " +e.getMessage());
                   break;
                }
            }
        }while(retryStatus);

    }
    public boolean validateStringStartsWithPattern(String inputString,String regExPattern){
        Pattern pattern = Pattern.compile(regExPattern);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.lookingAt();
    }
    public boolean switchWindow(String title)
    {
        LOGGER.info("Before switchWindow with Window Title ::"+title);
        parentWindow = getDriver().getTitle();
        LOGGER.info("Parent Window Title ::"+parentWindow);
        parentWindowhandle = getDriver().getWindowHandle();
        String windowTitles = "";
        try{
            webDriverFluentWait().until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver webDriver){return (getDriver().getWindowHandles().size()>1);}
            });
            Set<String> handles = getDriver().getWindowHandles();
            System.out.println("Count=" + handles.size());
            for(String handle : handles){
                getDriver().switchTo().window(handle);
                windowTitles = windowTitles + "|'"+getDriver().getTitle()+"'|";
                System.out.println("getDriver().getTitle()" + getDriver().getTitle());
                if(getDriver().getTitle().equals(title)){
                    LOGGER.info("After switchWindow with Window Title ::"+title);
                    return true;
                }
            }
            getDriver().switchTo().window(parentWindowhandle);
            report("ERROR", "Switching to window with title " + title + "failed. Opened windows are -" + windowTitles);
            return false;
        }catch(Exception e)
        {
            report("ERROR", "Window with title " + title +"not displayed as window count validation failed");
            return false;
        }

    }
    public Properties loadProperties(String path){
        Properties prop = null;
        InputStream input;
        try {
            prop = new Properties();
            input = new FileInputStream(path);
            prop.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            org.testng.Assert.assertTrue(false,"Loading property file failed for path : " + path +".Error Message:"+e.getMessage());
        }
        return prop;
    }
    public  void getUrl(String url){
        LOGGER.info("Before getUrl with url : " + url);
        getDriver().manage().deleteAllCookies();
        getDriver().get(url);
        getDriver().manage().window().maximize();
        LOGGER.info("After geturl with url : " + url);
    }

    public WebElement waitForElement(final By byElement){
        WebElement element;
        try{
            LOGGER.info("BeforeWaitForElement ::" + byElement);
            //waitUntilDomLoad();
            webDriverFluentWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(byElement));
            element = getDriver().findElement(byElement);
        }catch (WebDriverException e){
            LOGGER.info("Exception in waitForElement ::" + byElement);
            throw new WebDriverException(e.getMessage());
        }
        LOGGER.info("AfterWaitForElement::" + byElement);
        return element;
    }
    public List<WebElement> waitForElements(final By byElement){
        List<WebElement> element;
        try{
            LOGGER.info("BeforeWaitForElement ::" + byElement);
           // waitUntilDomLoad();
            element = getDriver().findElements(byElement);
        }catch (WebDriverException e){
            LOGGER.info("Exception in waitForElement ::" + byElement);
            report("error","Exception in waitForElements::" + e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
        LOGGER.info("AfterWaitForElement::" + byElement);
        return element;
    }
    public void scrollToElement(WebElement webElement){
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("arguments[0].scrollIntoView(true);",webElement);
        LOGGER.info("ScrollToElement::" + webElement + "Done");
       // waitUntilDomLoad();
        jse.executeScript("window.scrollBy(0,250)","");
        LOGGER.info("ScrollToElement::" + webElement+ "Done");
    }
    public void scrollUp(WebElement webElement){
        Actions actions = new Actions(getDriver());
        actions.moveToElement(webElement);
        actions.perform();
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("window.scrollBy(0,250)","");
        LOGGER.info("ScrollToElement::" + webElement+ "Done");
    }
    public  WebElement waitForElement(WebElement webElement){
        try{
            LOGGER.info("BeforeWaitForElement::" + webElement);
           // waitUntilDomLoad();
            webDriverFluentWait().until(ExpectedConditions.visibilityOf(webElement));
        }catch (WebDriverException e)
        {
            LOGGER.info("Exception in waitForElement ::" + webElement);
            report("error","Exception in waitForElements::" + e.getMessage());
            throw new WebDriverException(e.getMessage());
        }LOGGER.info("AfterWaitForElement::" + webElement);
        return webElement;
    }
    public void waitByTime(int time){
        try {
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void buttonClick(By locator){
        try{
            LOGGER.info( "BeforeWaitForWebElement in buttonClick ::" + locator);
            webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        }catch (Exception e)
        {
            LOGGER.info("Exception in Buttonclick ::"+ locator);
            e.printStackTrace();
        }
        WebElement ele = waitForElement(locator);
        ele.click();
        LOGGER.info("After buttonClick::" + locator);
    }

    public void waitUntilDomLoad(){
        LOGGER.info("Inside waituntilDomload");
        WebDriver driver = getDriver();
        //FluentWait fluentWait = readyStateWait(driver);
        if(driver.getTitle().contains("/need to be added")) {
            //ExpectedConditions<Boolean> jQueryLoad;
            try {
                LOGGER.info("Page is loaded");
            } catch (Exception e) {
                report("error", "Page is not loaded" + driver.getTitle());
            }
        }
    }
    public void checkBoxClick(By locator, Boolean value)
    {
        try{
            LOGGER.info("BeforewaitForElement in checkBoxClick::" + locator);
            webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("Exception waitForElement in checkBoxClick::" + locator);
        }
        boolean currentValue = waitForElement(locator).isSelected();
        LOGGER.info("After checkboxclick ::" +waitForElement(locator).getTagName()+" : "+waitForElement(locator).isSelected());
        if(currentValue!= value){
            WebElement elm = waitForElement(locator);
            scrollToElement(elm);
            elm.click();
        }
        currentValue = waitForElement(locator).isSelected();
        if(currentValue != value){
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click()",waitForElement(locator));
        }
    }
   public boolean isElementPresent(By locator)
   {
       WebElement element;
       try{
           LOGGER.info("Before isElementPresent::" + locator);
           waitForElement(locator);
           LOGGER.info("After isElementPresent::" + locator);
           return true;
       }catch (Exception e){
           LOGGER.info("Exception isElementPresent::" + locator);
           return false;
       }
   }
   public String getAttributeFromElement(By locator){
        LOGGER.info("Before getAttributeFromElement::" + locator);
        //waitUntilDomLoad();
       if(getDriver().findElement(locator).isDisplayed()){
           LOGGER.info("After getAttributeFromElement::"+ locator);
           return waitForElement(locator).getAttribute("value");
       }else{
           LOGGER.info("Returning null value in getAttributeFromElement::" +locator);
           return null;
       }
   }
   public boolean isElementDisplayed(By locator){
        boolean isElementDisplayed = false;
        LOGGER.info("Before isElementDisplayed::"+ locator+",status::" +isElementDisplayed);
        isElementDisplayed= waitForElement(locator).isDisplayed();
       LOGGER.info("After isElementDisplayed::"+ locator+",status::" +isElementDisplayed);
        return isElementDisplayed;
   }
    public boolean isButtonEnabled(By locator){
        boolean isButtonEnabled = false;
        LOGGER.info("Before isButtonEnabled::"+ locator+",status::" +isButtonEnabled);
        isButtonEnabled= waitForElement(locator).isEnabled();
        LOGGER.info("After isButtonEnabled::"+ locator+",status::" +isButtonEnabled);
        return isButtonEnabled;
    }
    public String getWebElementText(By locator){
        LOGGER.info("Before getWebElementText::"+ locator);
        WebElement webElement= waitForElement(locator);
        LOGGER.info("After getWebElementText::"+ locator);
        return webElement.getText();
    }
    public void enterText(By locator,String text){
        LOGGER.info("Before enterText::" + locator+",with text::" +text);
        WebElement webElementEnter = waitForElement(locator);
        webElementEnter.sendKeys(text);
        /*JavascriptExecutor js =(JavascriptExecutor)getDriver();
        js.executeScript("arguments[0].value'"+ text + "'",webElementEnter);*/
        LOGGER.info("After EnterText::"+ locator+",with text::" +text);
    }
    public void clearAndenterText(By locator,String text){
        LOGGER.info("Before clearAndenterText::" + locator+",with text::" +text);
        WebElement webElementEnter = waitForElement(locator);
        webElementEnter.clear();
        Actions action = new Actions(getDriver());
        action.sendKeys(webElementEnter,text).build().perform();
        LOGGER.info("After clearAndenterText::"+ locator+",with text::" +text);
    }
    public void waitUntilElementVisible(By locator){
        LOGGER.info("Before waitUntilElementVisible::" + locator);
        int sec =180;
        WebDriverWait expwait= new WebDriverWait(getDriver(),Duration.ofSeconds(180));
        expwait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        LOGGER.info("After waitUntilElementVisible::"+ locator);
    }
    public void waitUntilElementClickable(By locator){
        LOGGER.info("Before waitUntilElementClickable::" + locator);
        int sec =180;
        WebDriverWait expwait= new WebDriverWait(getDriver(),Duration.ofSeconds(180));
        expwait.until(ExpectedConditions.elementToBeClickable(locator));
        LOGGER.info("After waitUntilElementClickable::"+ locator);
    }
    public void selectOption(By locator,String opt){
        LOGGER.info("Before selectOption::" + locator+",with select option::" +opt);
        WebElement element = waitForElement(locator);
        webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByVisibleText(opt);
        LOGGER.info("After selectOption::"+ locator+",with select option::" +opt);
    }
    public void selectvalue(By locator,String opt){
        LOGGER.info("Before selectvalue::" + locator+",with select option::" +opt);
        WebElement element = waitForElement(locator);
        webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByValue(opt);
        LOGGER.info("After selectvalue::"+ locator+",with select option::" +opt);
    }
    public void selectOptionByIndex(By locator,int index){
        LOGGER.info("Before selectOptionByIndex::" + locator+",with select option::" +index);
        WebElement element = waitForElement(locator);
        webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        Select select = new Select(element);
        select.selectByIndex(index);
        LOGGER.info("After selectOptionByIndex::"+ locator+",with select option::" +index);
    }
    public void radioBtnClick(By locator){
        LOGGER.info("Before radioBtnClick::" + locator);
        WebElement element = waitForElement(locator);
        webDriverFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
        //waitUntilDomLoad();
        element.click();
        LOGGER.info("After radioBtnClick::"+ locator);
    }
    public void mouseOverOperationAndClick(By locator, By locator1){
        LOGGER.info("Before mouseOverOperationAndClick ::" + locator+"'"+locator1);
        Actions action = new Actions(getDriver());
        WebElement ele = waitForElement(locator);
        scrollToElement(ele);
        action.moveToElement(ele).perform();
        waitForElement(locator1).click();
    }
    public void mouseOverOperation(By locator){
        LOGGER.info("Before mouseOverOperation ::" + locator);
        Actions action = new Actions(getDriver());
        WebElement ele = waitForElement(locator);
        scrollToElement(ele);
        action.moveToElement(ele).perform();
    }
    public boolean switchToChildWindow(String title){
        LOGGER.info("Before switchToChildWindow with child window title:" + title);
        parentWindow = getDriver().getTitle();
        Set<String> handles = getDriver().getWindowHandles();
        for(String handle : handles){
            getDriver().switchTo().window(handle);
            if(getDriver().getTitle().equals(title))
            {
                LOGGER.info("After the switchToChildWindow with chile window title:"+title);
                return true;
            }
        }
        return false;
    }
}
