# smart-translations

Converts a translations JSON export into the `baseTranslations` JavaScript module. Used to copy translations during release preparation.

## How it works

The tool reads the input JSON file line by line and keeps only:

- lines containing `"key":`
- lines containing `"de":`

Object boundaries (`},`) are rewritten so the result is a valid JavaScript array. The output looks like this:

```js
export const baseTranslations = [
  {
    "key": "some.translation.key",
    "de": "Deutscher Text",
  }, {
    "key": "other.translation.key",
    "de": "Anderer Text",
  }
];
```

The parsing is line based, so the input must be pretty-printed JSON with one property per line (as returned by the translations HTTP GET endpoint).

## Usage

```shell
mvn clean package -pl smart-translations
java -cp smart-translations/target/smart-translations-1.0-SNAPSHOT.jar \
  com.zedas.asset.tools.smart.translations.Main <input-json> <output-file>
```

| Argument | Description |
| --- | --- |
| `input-json` | Path to the translations JSON (response of the translations HTTP GET request saved to a file). |
| `output-file` | Path of the generated JavaScript file. Existing file is overwritten. |

The program prints `Done!` when the output file has been written.

## Legacy mode

`Main.fromOriginalFile()` (not called by default) converts `mobile.*` entries from a `.properties` file (`c:\temp\mobile-plugin-l10n.properties`) to JSON. The paths are hardcoded; it is kept only for reference.
