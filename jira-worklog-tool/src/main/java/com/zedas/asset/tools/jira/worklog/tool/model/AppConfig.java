package com.zedas.asset.tools.jira.worklog.tool.model;

public class AppConfig
{
    private final String username;
    private final String path;
    private final String url;
    private int month;
    private int year;
    private int lastDayOfMonth;

    private String startOfMonth;
    private String endOfMonth;
    private String basicAuthHeader;
    private String jql;

    public AppConfig(String username, String path, String url)
    {
        this.username = username;
        this.path = path;
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPath()
    {
        return path;
    }

    public String getUrl()
    {
        return url;
    }

    public String getBasicAuthHeader()
    {
        return basicAuthHeader;
    }

    public void setBasicAuthHeader(String basicAuthHeader)
    {
        this.basicAuthHeader = basicAuthHeader;
    }

    public String getJql()
    {
        return jql;
    }

    public void setJql(String jql)
    {
        this.jql = jql;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getStartOfMonth()
    {
        return startOfMonth;
    }

    public void setStartOfMonth(String startOfMonth)
    {
        this.startOfMonth = startOfMonth;
    }

    public String getEndOfMonth()
    {
        return endOfMonth;
    }

    public void setEndOfMonth(String endOfMonth)
    {
        this.endOfMonth = endOfMonth;
    }

    public int getLastDayOfMonth()
    {
        return lastDayOfMonth;
    }

    public void setLastDayOfMonth(int lastDayOfMonth)
    {
        this.lastDayOfMonth = lastDayOfMonth;
    }
}
