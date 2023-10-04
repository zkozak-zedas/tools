package com.zedas.asset.tools.jira.worklog.tool.service;

import com.zedas.asset.tools.jira.worklog.tool.model.AppConfig;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.Issue;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.JiraResponse;
import com.zedas.asset.tools.jira.worklog.tool.model.jql.response.Worklog__1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class XlsxService
{
    private static final Logger logger = LogManager.getLogger(XlsxService.class);

    public XSSFWorkbook createWorkbookWithSums()
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("sums");
        return workbook;
    }

    public void prepareTimeSheetPage(XSSFWorkbook workbook, String username, JiraResponse data)
    {
        String name = WorkbookUtil.createSafeSheetName(calcSimpleUsername(username) + "-ts");
        Sheet sheet = workbook.createSheet(name);

    }

    public void prepareDetailsPage(XSSFWorkbook workbook, AppConfig config, JiraResponse data)
    {
        String simpleUsername = calcSimpleUsername(config.getUsername());
        String sheetName = WorkbookUtil.createSafeSheetName(simpleUsername + "-details");
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 50 * 256);

        Row row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("Month");
        row1.createCell(1).setCellValue(config.getMonth());

        Row row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("Year");
        row2.createCell(1).setCellValue(config.getYear());

        Row row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("Person");
        row3.createCell(1).setCellValue(simpleUsername);

        Row row4 = sheet.createRow(4);
        List<Integer> days = IntStream.rangeClosed(1, config.getLastDayOfMonth()).boxed().toList();
        days.forEach(day -> row4.createCell(day + 1).setCellValue(day));
        row4.createCell(days.size() + 3).setCellValue("Sum");
        for (int day : days)
        {
            sheet.setColumnWidth(day + 1, 5 * 256);
        }

        AtomicInteger rowCounter = new AtomicInteger(6);
        String sumColumn = getColumnNameByIndex(days.size() + 1);
        String totalSumColumn = getColumnNameByIndex(days.size() + 3);
        data.getIssues().forEach(issue -> {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(issue.getKey());
            row.createCell(1).setCellValue(issue.getFields().getSummary());
            var userWorklogs = issue.getFields().getWorklog().getWorklogs()
                    .stream()
                    .filter(worklog -> worklog.getAuthor().getEmailAddress().equalsIgnoreCase(config.getUsername()))
                    .filter(worklog -> convertToDate(worklog.getStarted()).compareTo(convertToDate(config.getStartOfMonth())) >= 0)
                    .filter(worklog -> convertToDate(worklog.getStarted()).compareTo(convertToDate(config.getEndOfMonth())) <= 0)
                    .toList();

            userWorklogs.forEach(worklog -> {
                int day = calcPositionFromDate(worklog.getStarted());
                row.createCell(day).setCellValue(worklog.getTimeSpentSeconds() / 3600.0);
            });
            String formula = "SUM(C" + rowCounter.get() + ":" + sumColumn + rowCounter.get() + ")";
            row.createCell(days.size() + 3).setCellFormula(formula);
            rowCounter.getAndIncrement();
        });

        if(data.getIssues().size()>0)
        {
            int sumRow = sheet.getLastRowNum() + 1;
            String formula = "SUM(" + totalSumColumn + "6:" + totalSumColumn + sumRow + ")";
            Row row = sheet.createRow(sumRow);
            row.createCell(days.size() + 3).setCellFormula(formula);
        }
    }

    public void updateSumsPage(XSSFWorkbook workbook, List<JiraResponse> data)
    {
        Sheet sheet = workbook.getSheet("sums");
        workbook.getSheet("sums").createRow(0).createCell(0).setCellValue("Sums");
    }

    private String getColumnNameByIndex(int i)
    {
        return switch (i)
        {
            case 26 -> "AA";
            case 27 -> "AB";
            case 28 -> "AC";
            case 29 -> "AD";
            case 30 -> "AE";
            case 31 -> "AF";
            case 32 -> "AG";
            case 33 -> "AH";
            case 34 -> "AI";
            default -> "AJ";
        };
    }

    private static String calcSimpleUsername(String username)
    {
        return username.substring(0, username.indexOf("@"));
    }

    private Date convertToDate(String dateString)
    {
        try
        {
            if (dateString.contains("T"))
            {
                dateString = dateString.substring(0, dateString.indexOf("T"));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateString);
        }
        catch (ParseException e)
        {
            logger.error("Date parse error: " + dateString + " - " + e.getMessage(), e);
            return null;
        }
    }

    private static int calcPositionFromDate(String date)
    {
        String fragment = date.split("-")[2];
        fragment = fragment.substring(0, fragment.indexOf("T"));
        int parsed = Integer.parseInt(fragment);
        parsed++; // it's moved, because of column with issue key and summary
        return parsed;
    }

}
