package com.macys.gcp;

public class SqlQueries {

    final static String topTerms="SELECT * FROM `bigquery-public-data.google_trends.top_terms` WHERE dma_id = #number LIMIT #limitsize";
    final static String scoreData="SELECT * FROM `bigquery-public-data.google_trends.top_terms` WHERE score in (13,9) and dma_id in (552,543) LIMIT 10";


    public static String getScoredata() {
        return scoreData;
    }

    public static String getTopTerms() {
        return topTerms;
    }


}
