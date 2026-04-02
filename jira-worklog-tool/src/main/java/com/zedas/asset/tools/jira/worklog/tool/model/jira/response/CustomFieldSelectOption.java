package com.zedas.asset.tools.jira.worklog.tool.model.jira.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomFieldSelectOption
{

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("id")
    @Expose
    private String id;

    public String getSelf()
    {
        return self;
    }

    public void setSelf(String self)
    {
        this.self = self;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
