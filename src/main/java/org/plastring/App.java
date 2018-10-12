package org.plastring;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * generate shell script to install local jar package by maven
 * <pre>mvn install:install-file -Dfile=path-to-file -DgroupId=xxx -DartifactId=xxx -Dversion -Dpackaging=jar</pre>
 */
public class App {
    public static void main(String[] args) {
        String pomFilePath =
                System.getProperty("user.dir") + File.separator + args[0];
        System.out.println("xml file: " + pomFilePath);

        StringBuilder xmlString = new StringBuilder();
        File pomFile = new File(pomFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(pomFile))) {
            String line = null;

            while ((line = br.readLine()) != null) {

                xmlString.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject xmlJSONObject = XML.toJSONObject(xmlString.toString());

        List<String> scriptStrings = generateInstallScript(xmlJSONObject);

        String scriptFilename = System.getProperty("user.dir") + File.separator + "install-jar.sh";
        try (BufferedWriter wr = new BufferedWriter(
                new FileWriter(new File(scriptFilename)))) {
            wr.write("#/bin/bash\n");
            for(String str : scriptStrings) {
                System.out.format("writ to file: " + str);
                wr.write(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> generateInstallScript(JSONObject xmlJSONObject) {

        List<String> commandStrings = new ArrayList<>();

        String formatString = "mvn install:install-file -Dfile=%s -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=jar%n";

        JSONArray dependencies = xmlJSONObject.getJSONObject("project")
                .getJSONObject("dependencies").getJSONArray("dependency");

        for (Object obj : dependencies) {
            JSONObject jsonObject = (JSONObject) obj;
            String filename = String
                    .format("%s-%s.jar", jsonObject.get("artifactId"),
                            jsonObject.get("version"));
            String commandString = String
                    .format(formatString, filename, jsonObject.get("groupId"),
                            jsonObject.get("artifactId"),
                            jsonObject.get("version"));
            System.out.format(commandString);
            commandStrings.add(commandString);
        }

        return commandStrings;
    }
}
