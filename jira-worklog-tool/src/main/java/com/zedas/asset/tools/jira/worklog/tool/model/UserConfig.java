package com.zedas.asset.tools.jira.worklog.tool.model;

import com.zedas.asset.tools.jira.worklog.tool.model.jira.response.JiraResponse;

public class UserConfig
{
    private AppConfig appConfig;
    private JiraResponse jiraResponse;

    public UserConfig(AppConfig appConfig, JiraResponse jiraResponse)
    {
        this.appConfig = appConfig;
        this.jiraResponse = jiraResponse;
    }

    public AppConfig getAppConfig()
    {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig)
    {
        this.appConfig = appConfig;
    }

    public JiraResponse getJiraResponse()
    {
        return jiraResponse;
    }

    public void setJiraResponse(JiraResponse jiraResponse)
    {
        this.jiraResponse = jiraResponse;
    }
}
