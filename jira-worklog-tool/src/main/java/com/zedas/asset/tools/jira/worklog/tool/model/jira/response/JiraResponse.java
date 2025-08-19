
package com.zedas.asset.tools.jira.worklog.tool.model.jira.response;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JiraResponse
{

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("isLast")
    @Expose
    private Boolean isLast;
    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("maxResults")
    @Expose
    private Integer maxResults;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

}
