package com.zedas.asset.tools.jira.worklog.tool.model.jira.request;

import java.util.StringJoiner;

public class WorklogRequest
{
    private String comment;
    private String started;
    private Integer timeSpentSeconds;

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getStarted()
    {
        return started;
    }

    public void setStarted(String started)
    {
        this.started = started;
    }

    public Integer getTimeSpentSeconds()
    {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(Integer timeSpentSeconds)
    {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", WorklogRequest.class.getSimpleName() + "[", "]")
                .add("comment='" + comment + "'")
                .add("started='" + started + "'")
                .add("timeSpentSeconds=" + timeSpentSeconds)
                .toString();
    }
}
