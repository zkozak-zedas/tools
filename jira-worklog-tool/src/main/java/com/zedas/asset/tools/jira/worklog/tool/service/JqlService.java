package com.zedas.asset.tools.jira.worklog.tool.service;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.request.JqlRequest;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.JqlResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JqlService
{
    private static final Logger logger = LogManager.getLogger(JqlService.class);
    private final Gson gson = new Gson();

    public JqlResponse downloadJqlWorklogData(AppConfig appConfig)
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
                JqlResponse jqlResponse = gson.fromJson(response.body(), JqlResponse.class);
                return jqlResponse;
            }
            else
            {
                logger.error("JqlService error: " + response.statusCode() + " " + response.body());
                return null;
            }
        }
        catch (IOException | InterruptedException e)
        {
            logger.error("JqlService error: " + e.getMessage(), e);
            return null;
        }
    }
}
