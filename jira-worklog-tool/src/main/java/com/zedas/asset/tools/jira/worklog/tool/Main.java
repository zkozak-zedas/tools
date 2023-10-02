package com.zedas.asset.tools.jira.worklog.tool;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.JsonConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.JqlResponse;
import com.zedas.asset.tools.jira.worklog.tool.service.AppConfigService;
import com.zedas.asset.tools.jira.worklog.tool.service.JqlService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args)
    {
        final Gson gson = new Gson();
        final JqlService jqlService = new JqlService();
        final AppConfigService appConfigService = new AppConfigService();
        try
        {
            final Path path = Paths.get("C:\\Development\\projects\\others\\asset.tools\\tools\\jira-worklog-tool\\src\\main\\resources\\config.json");
            final String configurationFile = StringUtils.join(Files.readAllLines(path), " ");
            logger.debug("Configuration read: " + configurationFile.length() + " characters");
            final JsonConfig jsonConfig = gson.fromJson(configurationFile, JsonConfig.class);
            logger.debug("Json configuration parsed.");
            final List<AppConfig> appConfig = appConfigService.prepareAppConfig(jsonConfig);
            logger.debug("App configuration parsed.");

            for(AppConfig config : appConfig) {
                JqlResponse jqlResponse = jqlService.downloadJqlWorklogData(config);
                System.out.println(jqlResponse.getIssues().size());
            }

        } catch (Exception e) {
            logger.error("Main error: " + e.getMessage(), e);
        }

    }
}
