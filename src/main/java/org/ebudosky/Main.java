package org.ebudosky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        AnalyticalTool engine = new AnalyticalTool();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String firstLine = br.readLine();
            if (firstLine == null) return;

            int S = Integer.parseInt(firstLine.trim());

            for (int i = 0; i < S; i++) {
                String line = br.readLine();
                if (line.startsWith("C")) {
                    engine.addRecord(line);
                }
                else if (line.startsWith("D")) {
                    System.out.println(engine.executeQuery(line));
                }
            }
        }
        catch (IOException e){
            System.err.println("An unexpected io exception was encountered: " + e.getMessage());
        }
        catch (NumberFormatException e){
            System.err.println("An unexpected number format exception was encountered: " + e.getMessage());
        }
    }
}