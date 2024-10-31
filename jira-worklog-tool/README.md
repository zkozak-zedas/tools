### This app can be used to quickly save and read worklog in jira

This app needs three parameters to start:
- config.json file location
- month (ignored in upload scenario)
- day (ignored in upload scenario)

Also parameters from command line can override parameters from config.json file.

## Download data
Example configuration to download data:
```json
{
    "users": "abc@zedas.com,cde@zedas.com",
    "email": "abc@zedas.com",
    "token": "...",
    "url": "https://zedas.atlassian.net",
    "year": 2000,
    "month": 1,
    "path": "C:\\Users\\abc\\Documents\\worklog\\mgellert.xlsx"
}
```
Example script to run program:
```shell
@ECHO OFF
ECHO Enter month and click enter:
SET /P month=
C:\Users\abc\.jdks\corretto-20.0.1\bin\java -jar C:\Users\abc\Documents\worklog\download\worklog.jar C:\Users\abc\Documents\worklog\download\config.json %month% 2024
start "" "C:\Users\abc\Documents\worklog\mgellert.xlsx"
PAUSE
```
## Upload data
Example configuration to upload data:
```json
{
  "mode": "update",
  "users": "abc@zedas.com",
  "email": "abc@zedas.com",
  "token": "...",
  "url": "https://zedas.atlassian.net",
  "year": 2000,
  "month": 1,
  "path": "C:\\Users\\abc\\Desktop\\worklog.xlsx",
  "worklog-path": "C:\\Users\\abc\\Documents\\worklog\\upload\\worklog.txt"
}

```
Example script to run program:
```shell
@ECHO OFF
ECHO Upload workflow
C:\Users\abc\.jdks\corretto-20.0.1\bin\java -jar C:\Users\abc\Documents\worklog\upload\worklog.jar C:\Users\abc\Documents\worklog\upload\config.json 1 2020
PAUSE
```

Example of worklog.txt file:
```
29.09.24 9:00
SMART-123 implementation, documentation 4h

30.09.24 9:30
SMART-124 tests 1h30m
SMART-125 discussion 3h

---------- updated 30.09.24 22:37 / 9h 0m ----------
```

After running the program, the worklog.txt file will be updated and this `----------` separator will be added. This separator is used to separate already sent worklogs from the new ones. Also there is a timestamp of the last update and the total time spent on the current sent worklog.

