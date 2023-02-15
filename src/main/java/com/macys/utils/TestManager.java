package com.macys.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Test;
import com.macys.pages.common.BasePage;
import org.apache.commons.io.FileUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.log4testng.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;


public class TestManager extends DriverSource{

    ExtentAppend ext = new ExtentAppend();
    public ExtentReports extent = ext.getExtentInstance();
    public final org.testng.log4testng.Logger LOGGER = Logger.getLogger(TestManager.class);
    public static Properties dataproperties;
    BasePage basepage = new BasePage();
    public static String jbehavePath;
    public static  final String jsonFile ="xref.json";
    public static List<String[]> testResults= new CopyOnWriteArrayList<String[]>(){
        {
            add(new String[]{"TC ID", "TEST NAME","STATUS","TEST TYPE","E2E TCID","FAILURE REASON"});
        }
    };

    public ExtentTest getExtentTest(){
        Long key = Thread.currentThread().getId();
        return TestUtil.extentTestMap.get(key);
    }
    public Long getParentTckey(Long threadID){
        return threadID + 1234567890;
    }
    public void setScenarioName(String scenarioName){
        Long key = Thread.currentThread().getId();
        Long parentTcKey = getParentTckey(key);
        do{
            if (TestUtil.extentTestMap.containsKey(parentTcKey)){
                setSubScenarioName(scenarioName);
            }else {
                ExtentTest extentTest = extent.createTest(scenarioName);
                TestUtil.extentTestMap.put(parentTcKey,extentTest);
                TestUtil.extentTestMap.put(key,extentTest);
                if(!basepage.validateStringStartsWithPattern(scenarioName, "TC[0-9]+_")){
                    basepage.report("error", "Scenario Name '" + scenarioName+ "'not started with pattern TC[0-9]+_ '");
                }
            }
        }while (TestUtil.extentTestMap.get(key) == null);
    }

    public void setSubScenarioName(String subScenarioName){
        if(basepage.validateStringStartsWithPattern(subScenarioName,"TC[0-9]+_") || basepage.validateStringStartsWithPattern(subScenarioName,"Precondition_")){
           Long key = Thread.currentThread().getId();
           Long parentTcKey= getParentTckey(key);
           ExtentTest extentTest = TestUtil.extentTestMap.get(parentTcKey);
           ExtentTest subScenarioTest = extentTest.createNode(subScenarioName);
           TestUtil.extentTestMap.put(key,subScenarioTest);

        }else{
           basepage.report("error","Sub scenario name "+ subScenarioName+"not started with pattern TC[0-9]+_ or precondition");
        }
    }
   @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result){
        System.out.println("TEAR DOWN : :");
        ExtentTest test = getExtentTest();
        String failureReason="";
        String status = null;
        try{
            if(result.getStatus() == ITestResult.FAILURE){
                status ="Failed";
                failureReason = result.getThrowable().getMessage();
                String screenshotPath= ext.takeScreenshot(getDriver(),result.getName());
                test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+"Test case Failed due to below issue:", ExtentColor.RED));
                test.fail(result.getThrowable());
                test.log(Status.FAIL,"Snapshot when exception occor: ",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

            }else if (result.getStatus() == ITestResult.SUCCESS){
                status="Passed";
                test.pass("Passed");
            }else if ( result.getStatus()==ITestResult.SKIP){
                status="Skipped";
                failureReason = result.getThrowable().getMessage();
                test.skip(result.getThrowable());

            }else{
                status = String.valueOf(result.getStatus());
                failureReason= result.getThrowable().getMessage();
                test.error(result.getThrowable());
            }
        }catch (Exception e)
        {
            status="Failed";
            failureReason=e.getMessage();
            test.log(Status.FAIL,"Exception in Tear down :" + e.getMessage());

        }finally {
            captureResult(test,status,failureReason);
            quitDriver();
            Long key = Thread.currentThread().getId();
            Long parentTcKey=getParentTckey(key);
            TestUtil.extentTestMap.keySet().removeIf(k -> k.equals(key));
            TestUtil.extentTestMap.keySet().removeIf(k -> k.equals(parentTcKey));
            TestUtil.driverMap.keySet().removeIf(k -> k.equals(key));
        }
   }
   public void captureResult(ExtentTest test,String testStatus,String failureReason){
        Test testModel = test.getModel();
        if(testModel.isChildNode()){
            Test parentTest = testModel.getParent();
            List<Test> childTestList = parentTest.getNodeContext().getAll();
            String parentTestName = parentTest.getName();
            String parentTestIDName= parentTestName.split("_")[0];
            String parentTestID = parentTestIDName.substring(2);
            testResults.add(new String[]{parentTestID,parentTestName, testStatus,"E2E",parentTestID,failureReason});
            for(Test childTest : childTestList){
                String childTestName = childTest.getName();
                String childTestStatus= childTest.getStatus().toString();
                String ChildTestCaseID = null;
                String childTestIDName = childTestName.split("_")[0];
                if(childTestIDName.toUpperCase().startsWith("TC")){
                    ChildTestCaseID = childTestIDName.substring(2).trim();
                    if(childTestStatus.equalsIgnoreCase("pass")){
                        testResults.add(new String[]{ChildTestCaseID,childTestName,"Passed","Individual",parentTestID,""});

                    }else{
                        testResults.add(new String[]{ChildTestCaseID,childTestName,"Failed","Individual",parentTestID,""});
                    }
                }
            }
        }else{
            String testName = testModel.getName();
            String testIDName = testName.split("_")[0];
            String parentTestID = testIDName.substring(2);
            testResults.add(new String[]{parentTestID,testName,testStatus,"E2E",parentTestID,failureReason});
        }
   }
   public void generateCsvResults() throws Exception
   {

   }
   public void copyResults(){
        String destinationDir = "";
        try{
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            destinationDir = System.getProperty("user.dir")+"/Release1/"+System.getProperty("includedGroups")+"/"+timeStamp+"/extent-reports";
            File logFile = new File(System.getProperty("user.dir")+ "/build/log.out");
            String sourceDir = System.getProperty("user.dir")+"/build/extent-reports";
            File destDir = new File(destinationDir);
            File srcDir = new File(sourceDir);
            FileUtils.copyFileToDirectory(logFile,destDir);
            FileUtils.copyDirectory(srcDir,destDir);
            System.out.println("Results are copied from location" + sourceDir.replace('/','\\'));
            System.out.println("Results are copied from location" + destinationDir.replace('/','\\'));

        }catch (Exception e){
            LOGGER.error("Exception occured while moving results to share drive : " + destinationDir + " with exception "+ e);
            Assert.assertTrue(false,"Exception occured while moving the results to share drive : " + destinationDir);
        }
   }
    @AfterSuite(alwaysRun = true)
    public void ClearExtentTest(){
        extent.flush();
        copyResults();
    }
}
