package com.zedas.asset.tools.jira.worklog.tool.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
{
  "mode":"read", // read, update, create
  "users": "pmalek, mgellert",
  "email": "mgellert@zedas.com",
  "token": "token-xxxxx",
  "url": "https://jira.zedas.com",
  "year": 2021,
  "month": 8,
  "path": "C:/duaa",

  "worklog-path": "C:/dupa",
}
 */
public class JsonConfig
{

    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("users")
    @Expose
    private String users;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("worklog-path")
    @Expose
    private String worklogPath;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWorklogPath() {
        return worklogPath;
    }

    public void setWorklogPath(String worklogPath) {
        this.worklogPath = worklogPath;
    }

}
