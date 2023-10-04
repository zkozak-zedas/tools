package com.zedas.asset.tools.jira.worklog.tool;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.JsonConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.JiraResponse;
import com.zedas.asset.tools.jira.worklog.tool.service.AppConfigService;
import com.zedas.asset.tools.jira.worklog.tool.service.JiraApiService;
import com.zedas.asset.tools.jira.worklog.tool.service.XlsxService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args)
    {
        final Gson gson = new Gson();
        final JiraApiService jiraApiService = new JiraApiService();
        final XlsxService xlsxService = new XlsxService();
        final AppConfigService appConfigService = new AppConfigService();
        try
        {
            final Path path = Paths.get("C:\\Development\\projects\\others\\asset.tools\\tools\\jira-worklog-tool\\src\\main\\resources\\config.json");
            final String configurationFile = StringUtils.join(Files.readAllLines(path), " ");
            logger.debug("Configuration read: " + configurationFile.length() + " characters");
            final JsonConfig jsonConfig = gson.fromJson(configurationFile, JsonConfig.class);
            logger.debug("Json configuration parsed.");
            final List<AppConfig> appConfig = appConfigService.prepareAppConfig(jsonConfig);
            final List<JiraResponse> jiraResponses = new ArrayList<>();
            logger.debug("App configuration parsed.");
            XSSFWorkbook workbook = xlsxService.createWorkbookWithSums();

            for (AppConfig config : appConfig)
            {
                JiraResponse jiraResponse = jiraApiService.downloadJqlWorklogData(config);
                logger.debug("Worklogs for " + config.getUsername() + " downloaded.");
                jiraResponse = jiraApiService.updateWorklogIfNeeded(config, jiraResponse);
                logger.debug("Worklogs for " + config.getUsername() + " updated.");
                jiraResponses.add(jiraResponse);
                if (jiraResponse.getTotal() > jiraResponse.getMaxResults())
                {
                    throw new IllegalStateException("JqlResponse total is greater than maxResults");
                }
                xlsxService.prepareDetailsPage(workbook, config, jiraResponse);
                logger.debug("Details sheet for " + config.getUsername() + " created.");
                xlsxService.prepareTimeSheetPage(workbook, config, jiraResponse);
                logger.debug("Timesheet for " + config.getUsername() + " created.");
            }
            xlsxService.updateSumsPage(workbook, jiraResponses);
            logger.debug("Sums calculated properly");
            workbook.write(Files.newOutputStream(Paths.get(jsonConfig.getPath())));
            logger.debug("Workbook saved properly.");
        }
        catch (Exception e)
        {
            logger.error("Main error: " + e.getMessage(), e);
        }

    }
}
