package com.zedas.asset.tools.jira.worklog.tool;

import com.google.gson.Gson;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.InsertWorklog;
import com.zedas.asset.tools.jira.worklog.tool.model.JsonConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.UserConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jira.response.JiraResponse;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
            Path path = Paths.get("config.json");
            Integer month = null;
            Integer year = null;
            if (args != null && args.length > 0)
            {
                try
                {
                    month = Integer.parseInt(args[0]);
                    year = Integer.parseInt(args[1]);
                }
                catch (Exception e)
                {
                    logger.debug("First param is path: " + args[0] + " - " + e.getMessage());
                    path = Paths.get(args[0]);
                    if (args.length > 2)
                    {
                        month = Integer.parseInt(args[1]);
                        year = Integer.parseInt(args[2]);
                    }
                }
            }

            final String configurationFile = StringUtils.join(Files.readAllLines(path), " ");
            logger.debug("Configuration read: " + configurationFile.length() + " characters");
            final JsonConfig jsonConfig = gson.fromJson(configurationFile, JsonConfig.class);
            logger.debug("Json configuration parsed.");
            if (month != null)
            {
                jsonConfig.setMonth(month);
            }
            if (year != null)
            {
                jsonConfig.setYear(year);
            }
            final List<AppConfig> appConfigs = appConfigService.prepareAppConfig(jsonConfig);
            final List<UserConfig> userConfigs = new ArrayList<>();
            logger.debug("App configuration parsed.");
            if (jsonConfig.getMode() == null || !Objects.equals(jsonConfig.getMode(), "update"))
            {
                XSSFWorkbook workbook = xlsxService.createWorkbookWithSums();

                for (AppConfig config : appConfigs)
                {
                    JiraResponse jiraResponse = jiraApiService.downloadJqlWorklogData(config);
                    logger.debug("Worklogs for " + config.getUsername() + " downloaded.");
                    jiraResponse = jiraApiService.updateWorklogIfNeeded(config, jiraResponse);
                    logger.debug("Worklogs for " + config.getUsername() + " updated.");
                    if (jiraResponse.getTotal() > jiraResponse.getMaxResults())
                    {
                        throw new IllegalStateException("JqlResponse total is greater than maxResults");
                    }
                    UserConfig userConfig = new UserConfig(config, jiraResponse);
                    userConfigs.add(userConfig);
                    xlsxService.prepareDetailsPage(workbook, userConfig);
                    logger.debug("Details sheet for " + config.getUsername() + " created.");
                    xlsxService.prepareTimeSheetPage(workbook, userConfig);
                    logger.debug("Timesheet for " + config.getUsername() + " created.");
                }
                xlsxService.updateSumsPage(workbook, userConfigs);
                logger.debug("Sums calculated properly");
                workbook.write(Files.newOutputStream(Paths.get(jsonConfig.getPath())));
                logger.debug("Workbook saved properly.");
            }
            else
            {
                Path worklogPath = Paths.get(jsonConfig.getWorklogPath());
                List<String> lines = Files.readAllLines(worklogPath);
                List<String> worklogsToSend = new ArrayList<>();
                for (String line : lines)
                {
                    if (line.startsWith("---"))
                    {
                        worklogsToSend.clear();
                    }
                    else
                    {
                        worklogsToSend.add(line);
                    }
                }
                logger.debug("Raw worklogs to send: " + worklogsToSend);

                List<InsertWorklog> insertWorklogs = jiraApiService.prepareWorklogsToSend(worklogsToSend);
                for (InsertWorklog insertWorklog : insertWorklogs)
                {
                    int responseCode = jiraApiService.postWorklog(appConfigs, insertWorklog);
                    if (responseCode == 201)
                    {
                        logger.debug("Worklog for " + insertWorklog + " created.");
                    }
                    else
                    {
                        logger.error("Worklog for " + insertWorklog + " not created. Response code: " + responseCode);
                    }
                }

                String updateDateTime = prepareUpdateDateTime();
                String summaryTime = prepareSummaryTime(insertWorklogs);
                lines.add("");
                lines.add("---------- updated " + updateDateTime + " / " + summaryTime + " ----------");
                lines.add("");
                lines.add("");
                Files.writeString(worklogPath, StringUtils.join(lines, "\n"));
                logger.debug("Worklogs updated!");
            }
        }
        catch (Exception e)
        {
            logger.error("Main error: " + e.getMessage(), e);
        }

    }

    private static String prepareSummaryTime(List<InsertWorklog> insertWorklogs)
    {
        int seconds = 0;
        for (InsertWorklog insertWorklog : insertWorklogs)
        {
            seconds += insertWorklog.getWorklogRequest().getTimeSpentSeconds();
        }
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }

    private static String prepareUpdateDateTime()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
        return formatter.format(date);
    }
}
