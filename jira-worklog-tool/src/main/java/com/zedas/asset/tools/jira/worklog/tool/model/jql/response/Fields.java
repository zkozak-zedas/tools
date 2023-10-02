
package com.zedas.asset.tools.jira.worklog.tool.model.jql.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fields {

    @SerializedName("worklog")
    @Expose
    private Worklog worklog;

    public Worklog getWorklog() {
        return worklog;
    }

    public void setWorklog(Worklog worklog) {
        this.worklog = worklog;
    }

}
