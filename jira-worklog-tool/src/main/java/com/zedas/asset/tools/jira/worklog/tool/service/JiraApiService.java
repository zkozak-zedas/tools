package com.zedas.asset.tools.jira.worklog.tool.service;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.InsertWorklog;
import com.zedas.asset.tools.jira.worklog.tool.model.jira.request.JqlRequest;
import com.zedas.asset.tools.jira.worklog.tool.model.jira.request.WorklogRequest;
import com.zedas.asset.tools.jira.worklog.tool.model.jira.response.JiraResponse;
import com.zedas.asset.tools.jira.worklog.tool.model.jira.response.Worklog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JiraApiService
{
    private static final Logger logger = LogManager.getLogger(JiraApiService.class);
    private final Gson gson = new Gson();

    public JiraResponse downloadJqlWorklogData(AppConfig appConfig)
    {
        try
        {
            String url = appConfig.getUrl() + "/rest/api/3/search/jql";
            HttpClient client = HttpClient.newHttpClient();
            JqlRequest jqlRequest = new JqlRequest(appConfig.getJql());

            HttpRequest request = HttpRequest.newBuilder(
                            URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("authorization", appConfig.getBasicAuthHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(jqlRequest)))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                JiraResponse jiraResponse = gson.fromJson(response.body(), JiraResponse.class);
                if (jiraResponse.getIssues().isEmpty())
                {
                    logger.warn("JiraService issue list is empty: " + response.statusCode() + " " + response.body());
                }
                return jiraResponse;
            }
            else
            {
                logger.error("JiraService error: " + response.statusCode() + " " + response.body());
                return null;
            }
        }
        catch (IOException | InterruptedException e)
        {
            logger.error("JiraService error: " + e.getMessage(), e);
            return null;
        }
    }

    public JiraResponse updateWorklogIfNeeded(AppConfig appConfig, JiraResponse jiraResponse)
    {
        jiraResponse.getIssues().forEach(issue -> {
            if (issue.getFields().getWorklog().getTotal() > issue.getFields().getWorklog().getMaxResults())
            {
                try
                {
                    logger.debug("JiraService updateworklog for: " + issue.getKey());
                    String url = appConfig.getUrl() + "/rest/api/3/issue/" + issue.getKey() + "/worklog";
                    HttpClient client = HttpClient.newHttpClient();

                    HttpRequest request = HttpRequest.newBuilder(
                                    URI.create(url))
                            .header("accept", "application/json")
                            .header("content-type", "application/json")
                            .header("authorization", appConfig.getBasicAuthHeader())
                            .GET()
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200)
                    {
                        Worklog worklogResponse = gson.fromJson(response.body(), Worklog.class);
                        issue.getFields().setWorklog(worklogResponse);
                        logger.debug("JiraService updateworklog for: " + issue.getKey() + " done.");
                    }
                    else
                    {
                        logger.error("JiraService updateworklog issue: " + response.statusCode() + " " + response.body());
                    }
                }
                catch (IOException | InterruptedException e)
                {
                    logger.error("JiraService error: " + e.getMessage(), e);
                }
            }
        });
        return jiraResponse;
    }

    public List<InsertWorklog> prepareWorklogsToSend(List<String> worklogsToSend)
    {
        List<InsertWorklog> insertWorklogs = new ArrayList<>();
        Date date = null;
        try
        {
            for (String line : worklogsToSend)
            {
                if (!line.isBlank())
                {
                    if (Character.isDigit(line.charAt(0)))
                    {
                        date = prepareDateFromLine(line);
                    }
                    else
                    {
                        InsertWorklog insertWorklog = new InsertWorklog();
                        String[] lineFragments = line.split(" ");
                        insertWorklog.setIssueKey(lineFragments[0]);

                        int seconds = convertTimeToSeconds(lineFragments[lineFragments.length - 1]);
                        WorklogRequest worklogRequest = new WorklogRequest();
                        worklogRequest.setComment(prepareComment(lineFragments));
                        worklogRequest.setStarted(prepareStartDate(date));
                        worklogRequest.setTimeSpentSeconds(seconds);
                        date = addSecondsToDate(date, seconds);

                        insertWorklog.setWorklogRequest(worklogRequest);
                        insertWorklogs.add(insertWorklog);
                    }
                }
            }
        }
        catch (ParseException | NullPointerException e)
        {
            logger.error("JiraService prepareWorklogsToSend error: " + e.getMessage(), e);
        }
        return insertWorklogs;
    }

    private Date addSecondsToDate(Date date, int seconds)
    {
        long time = date.getTime();
        time += seconds * 1000L;
        return new Date(time);
    }

    private String prepareStartDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String formattedDate = formatter.format(date);
        formattedDate += ":00.000+0000";
        return formattedDate;
    }

    private int convertTimeToSeconds(String lineFragment)
    {
        if (lineFragment.contains("h"))
        {
            String[] fragments = lineFragment.split("h");
            int hours = Integer.parseInt(fragments[0]);
            int minutes = 0;
            if (fragments.length > 1)
            {
                minutes = Integer.parseInt(fragments[1].replace("m", ""));
            }
            return hours * 3600 + minutes * 60;
        }
        else
        {
            int minutes = Integer.parseInt(lineFragment.replace("m", ""));
            return minutes * 60;
        }
    }

    private String prepareComment(String[] lineFragments)
    {
        String[] fragments = Arrays.copyOfRange(lineFragments, 1, lineFragments.length - 1);
        return String.join(" ", fragments);
    }

    private Date prepareDateFromLine(String line) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("d.M.yy HH:mm");
        return formatter.parse(line);
    }

    public int postWorklog(List<AppConfig> appConfigs, InsertWorklog insertWorklog)
    {
        try
        {
            AppConfig appConfig = appConfigs.get(0);
            String url = appConfig.getUrl() + "/rest/api/3/issue/" + insertWorklog.getIssueKey() + "/worklog";
            String json = gson.toJson(insertWorklog.getWorklogRequest());
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(
                            URI.create(url))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("authorization", appConfig.getBasicAuthHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        }
        catch (IOException | InterruptedException e)
        {
            logger.error("JiraService error: " + e.getMessage(), e);
            return 0;
        }
    }
}
