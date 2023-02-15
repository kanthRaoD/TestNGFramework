package com.gcp;

import com.macys.gcp.BqQuery;
import com.macys.utils.TestManager;
import org.testng.annotations.Test;

import java.io.IOException;

public class GoogleTrendsData_1000 extends TestManager{

    @Test(groups = {"1000", "reg_2033", "Smoke_test_2033"})
    public void googleTrendsData() throws IOException, InterruptedException {
        setScenarioName("TC1000_Google_Trends");
        setSubScenarioName("TC1001_Validate_Google_Trends_Data");
        BqQuery bqQuery = new BqQuery();
        bqQuery.validateGoogleTrendsData();
    }
}
