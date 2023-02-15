package com.macys.gcp;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableResult;
import com.macys.pages.common.BasePage;

public class BqQuery extends BasePage {
    private static String bqDataPropertyfile="src\\test\\resources\\";
    public void validateGoogleTrendsData() throws JobException, InterruptedException, FileNotFoundException, IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\bqJsonfiles\\master-deck-377013-4978acc07b7d.json"));
        com.google.cloud.bigquery.BigQuery bigquery  = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
        String datasetName = "bigquery-public-data.google_trends";
        String tableName = "top_terms";
        TableId tableId = TableId.of(datasetName, tableName);
        //int number = 552;
        Properties prop=loadProperties(bqDataPropertyfile + "bqdata.properties");
        String updatedQuery = SqlQueries.getTopTerms().replace("#number", prop.getProperty("number")).replace("#limitsize",Integer.toString(10));

        System.out.println(updatedQuery);
        //System.out.println(SqlQueries.getTopTerms().replace("#number", prop.getProperty("number")));
        // Executes the query
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(updatedQuery).build();
        Job queryJob= bigquery.create(JobInfo.newBuilder(queryConfig).build());
        TableResult result = queryJob.getQueryResults();
        // Prints the results
        for (FieldValueList row : result.iterateAll()) {
            String dname=row.get("dma_name").getStringValue();
            String refresh_date= row.get("refresh_date").getStringValue();
            System.out.println("dname"+dname);
            System.out.println("refresh_date"+refresh_date);
        }
    }
}
