# jira-worklog-tool

Command-line tool for Jira Cloud worklogs. It works in two modes:

- **download** (default) – reads worklogs of selected users for a given month and generates an Excel workbook with a monthly work summary,
- **update** – reads a plain-text worklog file and creates the worklogs in Jira.

Authentication uses a Jira API token (Basic auth with `email:token`). Create a token at
https://id.atlassian.com/manage-profile/security/api-tokens and never commit it – keep the real token only in your local `config.json`.

## Build

Requires JDK 20+.

```shell
mvn clean package -pl jira-worklog-tool
```

The runnable jar is `target/jira-worklog-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`.

## Running

```shell
java -jar worklog.jar [config-path] [month year]
```

| Arguments | Behaviour |
| --- | --- |
| *(none)* | Reads `config.json` from the current directory. |
| `month year` | Reads `config.json` from the current directory, overrides `month` and `year`. |
| `config-path` | Reads the given configuration file. |
| `config-path month year` | Reads the given configuration file, overrides `month` and `year`. |

`month` and `year` are ignored in update mode.

## Configuration

| Property | Description |
| --- | --- |
| `mode` | `update` to upload worklogs; any other value or missing property means download. |
| `users` | Comma-separated Jira user emails whose worklogs are downloaded. In update mode the first user is used. |
| `email` | Email of the account that owns the API token. |
| `token` | Jira API token. |
| `url` | Jira base URL, e.g. `https://zedas.atlassian.net`. |
| `year`, `month` | Period to download. |
| `path` | Output `.xlsx` file (download mode). |
| `worklog-path` | Input worklog text file (update mode). |

## Download data

Example configuration:

```json
{
  "users": "abc@zedas.com, cde@zedas.com",
  "email": "abc@zedas.com",
  "token": "...",
  "url": "https://zedas.atlassian.net",
  "year": 2026,
  "month": 1,
  "path": "C:\\Users\\abc\\Documents\\worklog\\worklog.xlsx"
}
```

The generated workbook contains:

- `sums` – hours per user, split into bugs and non-bugs,
- `<user>-details` – issues of the user (with link and summary) × days of the month, with hours, sum and project,
- `<user>-ts` – monthly timesheet of the user (work hours per day).

Limitation: the Jira search result must fit in a single page, otherwise the tool stops with `JqlResponse has more pages - pagination not implemented`.

Example Windows script:

```shell
@ECHO OFF
ECHO Enter month and click enter:
SET /P month=
C:\Users\abc\.jdks\corretto-20.0.1\bin\java -jar C:\Users\abc\Documents\worklog\download\worklog.jar C:\Users\abc\Documents\worklog\download\config.json %month% 2026
start "" "C:\Users\abc\Documents\worklog\worklog.xlsx"
PAUSE
```

## Upload data

Example configuration:

```json
{
  "mode": "update",
  "users": "abc@zedas.com",
  "email": "abc@zedas.com",
  "token": "...",
  "url": "https://zedas.atlassian.net",
  "year": 2026,
  "month": 1,
  "path": "C:\\Users\\abc\\Desktop\\worklog.xlsx",
  "worklog-path": "C:\\Users\\abc\\Documents\\worklog\\upload\\worklog.txt"
}
```

Example `worklog.txt`:

```
29.09.24 9:00
SMART-123 implementation, documentation 4h

30.09.24 9:30
SMART-124 tests 1h30m
SMART-125 discussion 3h

---------- updated 30.09.24 22:37 / 9h 0m ----------
```

Format rules:

- a line starting with a digit sets the date and start time (`d.M.yy HH:mm`),
- every following line is one worklog: `<ISSUE-KEY> <comment> <time>`, where time is `4h`, `30m` or `1h30m`,
- worklogs of the same day are placed one after another, starting at the given time.

Only lines after the last `----------` separator are sent. After a run the tool appends a new separator with the update timestamp and the total time sent, so the same entries are not uploaded twice.

Example Windows script:

```shell
@ECHO OFF
ECHO Upload workflow
C:\Users\abc\.jdks\corretto-20.0.1\bin\java -jar C:\Users\abc\Documents\worklog\upload\worklog.jar C:\Users\abc\Documents\worklog\upload\config.json
PAUSE
```
