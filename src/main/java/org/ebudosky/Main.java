package org.ebudosky;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        AnalyticalTool engine = new AnalyticalTool();

        Scanner sc = new Scanner(System.in);
        int S = sc.nextInt();

        for (int i = 0; i < S; i++) {
            String line = sc.nextLine();
            if (line.startsWith("C")) {
                engine.addRecord(line);
            }
            else if (line.startsWith("D")) {
                engine.executeQuery(line);
            }
            else{
                throw new IllegalArgumentException("Unknown argument: " + line.charAt(0));
            }
        }
    }
}