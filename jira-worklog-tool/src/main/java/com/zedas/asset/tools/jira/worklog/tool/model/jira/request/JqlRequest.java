package com.zedas.asset.tools.jira.worklog.tool.model.jira.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class JqlRequest
{

    @SerializedName("fields")
    @Expose
    private final List<String> fields = Arrays.asList(
            "key", "summary", "issuetype", "worklog", "customfield_10454");
    @SerializedName("fieldsByKeys")
    @Expose
    private final Boolean fieldsByKeys = false;
    @SerializedName("maxResults")
    @Expose
    private final Integer maxResults = 100;
    @SerializedName("nextPageToken")
    @Expose
    private final String nextPageToken = null;
    @SerializedName("jql")
    @Expose
    private final String jql;

    public JqlRequest(String jql)
    {
        this.jql = jql;
    }

    public List<String> getFields()
    {
        return fields;
    }

    public Boolean getFieldsByKeys()
    {
        return fieldsByKeys;
    }

    public String getJql()
    {
        return jql;
    }

    public Integer getMaxResults()
    {
        return maxResults;
    }

    public String getNextPageToken()
    {
        return nextPageToken;
    }

}
