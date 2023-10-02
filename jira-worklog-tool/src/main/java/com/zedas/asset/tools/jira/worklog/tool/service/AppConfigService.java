package com.zedas.asset.tools.jira.worklog.tool.service;

import com.zedas.asset.tools.jira.worklog.tool.model.JsonConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AppConfigService
{
    public List<AppConfig> prepareAppConfig(JsonConfig jsonConfig) {
        final List<AppConfig> configs = new ArrayList<>();
        final String usersString = jsonConfig.getUsers();
        final String[] users = usersString.split(",");
        for (String user : users) {
            AppConfig appConfig = new AppConfig(user, jsonConfig.getPath(), jsonConfig.getUrl());

//            String safeUsername = user.replace("@", "\\u0040");

            String month = jsonConfig.getMonth() < 10 ? "0" + jsonConfig.getMonth() : String.valueOf(jsonConfig.getMonth());
            String startDate = jsonConfig.getYear() + "-" + month + "-01";
            int lastDayOfMonth = getLastDayOfMonthUsingYearMonth(YearMonth.of(jsonConfig.getYear(), jsonConfig.getMonth()));
            String endDate = jsonConfig.getYear() + "-" + month + "-" + lastDayOfMonth;
            final String jql = "worklogDate >= " + startDate + " AND worklogDate <= " + endDate + " AND worklogAuthor = \"" + appConfig.getUsername() + "\"";

            String data = jsonConfig.getEmail()+":"+jsonConfig.getToken();
            String encoded = Base64.getEncoder().encodeToString(data.getBytes());
            final String authHeader = "Basic " + encoded;

            appConfig.setJql(jql);
            appConfig.setBasicAuthHeader(authHeader);
            configs.add(appConfig);
        }
        return configs;
    }

    private int getLastDayOfMonthUsingYearMonth(YearMonth date) {
        return date.atEndOfMonth()
                .getDayOfMonth();
    }
}
