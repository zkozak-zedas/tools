package com.zedas.asset.tools.jira.worklog.tool.model;

import com.zedas.asset.tools.jira.worklog.tool.model.jira.request.WorklogRequest;

import java.util.StringJoiner;

public class InsertWorklog
{
    private String issueKey;

    private WorklogRequest worklogRequest;

    public String getIssueKey()
    {
        return issueKey;
    }

    public void setIssueKey(String issueKey)
    {
        this.issueKey = issueKey;
    }

    public WorklogRequest getWorklogRequest()
    {
        return worklogRequest;
    }

    public void setWorklogRequest(WorklogRequest worklogRequest)
    {
        this.worklogRequest = worklogRequest;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", InsertWorklog.class.getSimpleName() + "[", "]")
                .add("issueKey='" + issueKey + "'")
                .add("worklogRequest=" + worklogRequest)
                .toString();
    }
}
