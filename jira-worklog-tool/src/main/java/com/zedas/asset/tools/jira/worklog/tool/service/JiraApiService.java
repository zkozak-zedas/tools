package com.zedas.asset.tools.jira.worklog.tool.service;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.request.JqlRequest;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.JiraResponse;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.Worklog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JiraApiService
{
    private static final Logger logger = LogManager.getLogger(JiraApiService.class);
    private final Gson gson = new Gson();

    public JiraResponse downloadJqlWorklogData(AppConfig appConfig)
    {
        try
        {
            String url = appConfig.getUrl() + "/rest/api/2/search";
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
                if(jiraResponse.getIssues().isEmpty()) {
                    logger.error("JiraService issue list is empty: " + response.statusCode() + " " + response.body());
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
                    String url = appConfig.getUrl() + "/rest/api/2/issue/" + issue.getKey() + "/worklog";
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
                    } else {
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
}
