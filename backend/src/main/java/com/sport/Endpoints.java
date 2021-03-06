package com.sport;

public abstract class Endpoints {

    public static final String ROOT = "/";
    public static final String SWAGGER = "/swagger";
    public static final String SWAGGER_API_DOCS = "/v2/api-docs";
    public static final String SWAGGER_WEBJARS = "/webjars/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String SWAGGER_UI = "/swagger-ui.html";

    public static final String DEPARTMENTS = "/departments";
    public static final String FILES_YEAR = "/files/general/{year}";
    public static final String SCRAPINGS_YEAR = "/scrapings/{year}";
    public static final String STADIUM_TOWN = "/stadiums/{regionPostalCode}";
    public static final String EVENT = "/event/data/{eventCode}";
    public static final String EVENT_TOWN = "/event/{regionPostalCode}";
    public static final String REGION_LIST = "/regions";


}
