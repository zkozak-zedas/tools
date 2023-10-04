package com.zedas.asset.tools.jira.worklog.tool.model.jql.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssueType
{
    @SerializedName("name")
    @Expose
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
