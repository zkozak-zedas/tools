package com.zedas.asset.tools.smart.translations;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main
{
		public static void main(String[] args) throws IOException
		{
				fromHttpGetReponse(args);
//				fromOriginalFile();
		}


		private static void fromHttpGetReponse(String[] args) throws IOException
		{
				String savePath = args[1];
				String jsonPath = args[0]; // C:\Users\mgellert\Desktop\translations.json
				List<String> lines = Files.readAllLines(Paths.get(jsonPath), StandardCharsets.UTF_8);
				List<String> result = new ArrayList<>();

				String start = "export const baseTranslations = [\n  {\n";
				result.add(start);

				lines.forEach(line -> {
						if(line.contains("\"key\":") || line.contains("\"de\":"))
						{
								result.add(line + "\n");
						}
						else if(line.contains("  },") || line.contains("\t},"))
						{
								result.add("  }, {" + "\n");
						}
				});

				String end = "  }\n];";
				result.add(end);

				String resultString = String.join("", result);

				Files.writeString(Paths.get(savePath), resultString, StandardCharsets.UTF_8);
				System.out.println("Done!");
		}


		private static void fromOriginalFile() throws IOException
		{
				String propertiesPath = "c:\\temp\\mobile-plugin-l10n.properties";
				String outPath = "c:\\temp\\translations.json";

				try (FileReader fileReader = new FileReader(propertiesPath); FileWriter fileWriter = new FileWriter(outPath))
				{
						StringBuffer outputString = new StringBuffer("export const baseTranslations = [\n");
						Properties properties = new Properties();
						properties.load(fileReader);
						properties.forEach((key, value) -> {
								String keyStr = (String) key;
								String valueStr = (String) value;
								if(keyStr.startsWith("mobile."))
								{
										keyStr = keyStr.substring(7);
										valueStr = valueStr.replace("\n", "\\n");
										valueStr = valueStr.replace("\"", "\\\"");

										outputString.append("{\n");
										outputString.append("\"de\": ");
										outputString.append("\"").append(valueStr).append("\",\n");
										outputString.append("\"key\": ");
										outputString.append("\"").append(keyStr).append("\",\n");
										outputString.append("},\n");
								}

						});
						outputString.deleteCharAt(outputString.lastIndexOf(","));
						outputString.append("];");

						fileWriter.write(outputString.toString());
				}
		}
}
