package com.macys.utils;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ConcurrentHashMap;

public class TestUtil {
    public static ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap();
    public static ConcurrentHashMap<Long, ExtentTest> extentTestMap = new ConcurrentHashMap();
}
