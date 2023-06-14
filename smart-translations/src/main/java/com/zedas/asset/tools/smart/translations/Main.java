package com.zedas.asset.tools.smart.translations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String savePath = "C:\\Users\\mgellert\\Desktop\\translations.txt";
        String jsonPath = args[0]; // C:\Users\mgellert\Desktop\translations.json
        List<String> lines = Files.readAllLines(Paths.get(jsonPath), StandardCharsets.UTF_8);
        List<String> result = new ArrayList<>();

        String start = "export const baseTranslations = [\n  {\n";
        result.add(start);

        lines.forEach(line -> {
            if(line.contains("\"key\":") || line.contains("\"de\":") )
            {
                result.add(line+"\n");
            }
            else if(line.contains("  },") || line.contains("\t},"))
            {
                result.add("  }, {"+"\n");
            }
        });

        String end = "  }\n];";
        result.add(end);

        String resultString = String.join("", result);

        Files.writeString(Paths.get(savePath), resultString, StandardCharsets.UTF_8);
    }
}