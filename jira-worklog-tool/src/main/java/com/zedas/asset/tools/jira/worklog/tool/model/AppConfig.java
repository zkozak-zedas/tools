package com.zedas.asset.tools.jira.worklog.tool.model;

public class AppConfig
{
    private final String username;
    private final String path;
    private final String url;
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
}
