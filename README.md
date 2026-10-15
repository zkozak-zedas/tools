# tools

A small collection of personal command-line utilities used in day-to-day work on the ZEDAS asset project.

| Module | Description |
| --- | --- |
| [jira-worklog-tool](jira-worklog-tool/README.md) | Downloads Jira worklogs into a monthly Excel work summary and uploads worklogs from a plain-text file to Jira. |
| [smart-translations](smart-translations/README.md) | Converts a translations JSON export into the `baseTranslations` JavaScript module used during release preparation. |

## Origin

Both modules were moved here from the internal `com.zedas.asset.tools` repository (`tools/` directory) with their full commit history.
API tokens committed in the past were removed from the history during the migration.

## Requirements

- Maven 3.9+
- JDK 20+ (`jira-worklog-tool` targets Java 20, `smart-translations` targets Java 11)

## Build

Build all modules from the repository root:

```shell
mvn clean package
```

Artifacts:

- `jira-worklog-tool/target/jira-worklog-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`
- `smart-translations/target/smart-translations-1.0-SNAPSHOT.jar`

A single module can be built with `mvn clean package -pl <module>`.
