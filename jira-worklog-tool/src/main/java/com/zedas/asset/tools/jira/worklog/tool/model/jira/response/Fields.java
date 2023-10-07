
package com.zedas.asset.tools.jira.worklog.tool.model.jira.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fields
{

    @SerializedName("worklog")
    @Expose
    private Worklog worklog;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("issuetype")
    @Expose
    private IssueType issueType;

    public Worklog getWorklog()
    {
        return worklog;
    }

    public void setWorklog(Worklog worklog)
    {
        this.worklog = worklog;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public IssueType getIssueType()
    {
        return issueType;
    }

    public void setIssueType(IssueType issueType)
    {
        this.issueType = issueType;
    }
}
