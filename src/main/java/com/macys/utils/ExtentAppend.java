package com.macys.utils;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.FileUtil;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ExtentAppend {

    private static ExtentReports extent;
    private static ExtentHtmlReporter htmlReports;
    private static String extentReportPropertyfile="src\\test\\resources\\";
    public final Logger LOGGER = Logger.getLogger(ExtentAppend.class);

    public static ExtentReports getExtentInstance(){
        if(extent==null){
            try{
                extent = createInstance("extentreport");
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        return extent;
    }
    public static ExtentReports createInstance(String propname) throws Exception{
        Properties prop = new Properties();
        InputStream input =null;
        try{
            input = new FileInputStream(extentReportPropertyfile + propname + ".properties");
           //load a properties file
            prop.load(input);
            // get the property value and print it
            String reportpath = prop.getProperty("reportpath");
            String reporttitle = prop.getProperty("reporttitle");
            String environment = prop.getProperty("environment");
            String reportName = prop.getProperty("reportname");
            copyLogoDirectory();
            String htmlReportName = "<img src ='logos/macyslogo.png' alt='macyslogo' style='height:100%;width=auto;float:left;padding-right:40px;'/>"+
                    "<span class ='label blue darken-3 hspace='35' style='font-size: 130%'>"+reportName+"</span> <span class ='label blue darken-3'style='font-size: 100%'>ENV:"+environment+"</span><img src ='logos/macyslogo.png' alt='macyslogo' style='height:100%;width=auto;float:right;'/>";

            String cssToHideText = "a.brand-logo {display :none;}\n"+
                              "#nav-mobile > li:nt-child(2) > a > span {display : none;}";
            htmlReports =new ExtentHtmlReporter(reportpath);
            extent = new ExtentReports();
            extent.attachReporter(htmlReports);
            extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
            htmlReports.config().setDocumentTitle(reporttitle);
            htmlReports.config().setReportName(htmlReportName);
            htmlReports.config().setTheme(Theme.STANDARD);
            htmlReports.config().setTestViewChartLocation(ChartLocation.TOP);
            htmlReports.config().setChartVisibilityOnOpen(true);
            htmlReports.config().setEncoding("UTF-8");
            htmlReports.config().setProtocol(Protocol.HTTPS);
            htmlReports.config().setCSS(cssToHideText);
            extent.setSystemInfo("Environment", environment);

        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            if(input != null){
                try{
                    input.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return extent;
    }

    public String takeScreenshot(WebDriver driver,String screenshotName)
    {
        String destination = null;
        String imgPath =null;
        int maxRetryCount =5;
        int retryCount=0;
        while (driver instanceof TakesScreenshot){
            String dateName = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
            File source =((TakesScreenshot) driver).getScreenshotAs((OutputType.FILE));
            try{
                imgPath = "TestsScreenshots\\"+ screenshotName + dateName+ ".png";
                destination = System.getProperty("user.dir") + "\\build\\extent-reports\\"+imgPath;
                File finalDestination = new File(destination);
                FileUtils.copyFile(source,finalDestination);
                LOGGER.info("Screen destination :" + destination);
                return imgPath;
            }catch (IOException e){
                LOGGER.error("takeScreenshot Excefption : " + e.getMessage());
                if(++retryCount>maxRetryCount){
                    Assert.assertTrue(false,"Exception while taking screenshot : " + e.getMessage());
                }
            }

        }
        LOGGER.info("Destination after exception : " + destination);
        return imgPath;
    }
    public static void copyLogoDirectory()
    {
     String destinationDir="";
     try{
         String sourceDir= System.getProperty("user.dir")+"/config/logos";
         destinationDir=System.getProperty("user.dir")+"/build/extent-reports/logos";
         File destDir = new File(destinationDir);
         File srcDir = new File(sourceDir);
         FileUtils.copyDirectory(srcDir,destDir);
     }catch (Exception e)
     {
         Assert.assertTrue(false, "Exception occurred while moving logs to driver :"+destinationDir);
     }
    }
}
