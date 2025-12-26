package org.ebudosky;

import java.util.Arrays;

public class Parser {

    public static final byte ANY = -1;

    public static SupportRecord parseSupportRecord(String line) {
        String[] parts = line.split(" ");

        // service_id[.variation_id]
        byte[] service = parseHierarchy(parts[1], 2);

        // question_type_id[.category_id.[sub-category_id]]
        byte[] question = parseHierarchy(parts[2], 3);

        boolean firstAnswer = parts[3].equals("P");
        int date = parseDate(parts[4]);
        short time = Short.parseShort(parts[5]);

        return new SupportRecord(
                service[0], service[1],
                question[0], question[1], question[2],
                firstAnswer, date, time
        );
    }

    public static QueryRecord parseQueryRecord(String line) {
        String[] parts = line.split(" ");

        // service_id[.variation_id]
        byte[] service = parts[1].equals("*") ? new byte[]{ANY, ANY} : parseHierarchy(parts[1], 2);

        // question_type_id[.category_id.[sub-category_id]]
        byte[] question = parts[2].equals("*") ? new byte[]{ANY, ANY, ANY} : parseHierarchy(parts[2], 3);

        boolean firstAnswer = parts[3].equals("P");

        String[] dateRange = parts[4].split("-");
        int dateFrom = parseDate(dateRange[0]);
        int dateTo = (dateRange.length > 1) ? parseDate(dateRange[1]) : ANY;

        return new QueryRecord(
                service[0], service[1],
                question[0], question[1], question[2],
                firstAnswer, dateFrom, dateTo
        );
    }

    private static byte[] parseHierarchy(String idString, int maxLevels) {
        byte[] results = new byte[maxLevels];
        Arrays.fill(results, ANY);

        String[] parts = idString.split("\\.");
        for (int i = 0; i < parts.length && i < maxLevels; i++) {
            results[i] = Byte.parseByte(parts[i]);
        }
        return results;
    }

    private static int parseDate(String dateStr) {
        String[] dateParts = dateStr.split("\\.");
        return Integer.parseInt(dateParts[2]) * 10000 +
                Integer.parseInt(dateParts[1]) * 100 +
                Integer.parseInt(dateParts[0]);
    }
}
