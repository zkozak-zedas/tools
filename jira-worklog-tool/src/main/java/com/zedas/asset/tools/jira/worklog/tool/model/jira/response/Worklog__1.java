
package com.zedas.asset.tools.jira.worklog.tool.model.jira.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Worklog__1 {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("updateAuthor")
    @Expose
    private UpdateAuthor updateAuthor;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("started")
    @Expose
    private String started;
    @SerializedName("timeSpent")
    @Expose
    private String timeSpent;
    @SerializedName("timeSpentSeconds")
    @Expose
    private Integer timeSpentSeconds;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("issueId")
    @Expose
    private String issueId;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public UpdateAuthor getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(UpdateAuthor updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(Integer timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

}
