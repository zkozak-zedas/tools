package com.zedas.asset.tools.smart.translations;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Main
{
		public static void main(String[] args) throws IOException
		{
				fromOriginalFile();
		}


		private static void fromOriginalFile() throws IOException
    {
        String propertiesPath = "c:\\temp\\mobile-plugin-l10n.properties";
        String outPath = "c:\\temp\\translations.json";

        try (FileReader fileReader = new FileReader(propertiesPath);
             FileWriter fileWriter = new FileWriter(outPath))
        {
            StringBuffer outputString = new StringBuffer("export const baseTranslations = [\n");
            Properties properties = new Properties();
            properties.load(fileReader);
            properties.forEach((key, value) -> {

								value = ((String)value).replace("\n", "\\n");
								value = ((String)value).replace("\"", "\\\"");

                outputString.append("{\n");
                outputString.append("\"de\": ");
                outputString.append("\"").append(value).append("\",\n");
                outputString.append("\"key\": ");
                outputString.append("\"").append(key).append("\",\n");
                outputString.append("},\n");
            });
						outputString.deleteCharAt(outputString.lastIndexOf(","));
            outputString.append("];");

            fileWriter.write(outputString.toString());
        }
    }
}
