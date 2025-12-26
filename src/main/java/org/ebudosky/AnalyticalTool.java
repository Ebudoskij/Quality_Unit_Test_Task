package org.ebudosky;

import org.ebudosky.Exception.NotEnoughDataException;

import java.util.ArrayList;
import java.util.List;

public class AnalyticalTool {

    List<SupportRecord> waitingRecords = new ArrayList<>();

    public void addRecord(String line) {
        waitingRecords.add(Parser.parseSupportRecord(line));
    }

    public String executeQuery(String line) {
        QueryRecord query = Parser.parseQueryRecord(line);
        try {
            double averageWaitingTime = findAverageWaitingTime(query);
            return String.valueOf(Math.round(averageWaitingTime));
        }
        catch (NotEnoughDataException e) {
            return "-";
        }
    }

    private double findAverageWaitingTime(QueryRecord query) {
        long sum = 0;
        int count = 0;
        for (SupportRecord r : waitingRecords) {
            if (matches(r, query)) {
                sum += r.waitingMinutes();
                count++;
            }
        }
        if (count == 0) throw new NotEnoughDataException("There is not enough data for this query to be displayed!");
        return (double) sum / count;
    }

    private boolean matches(SupportRecord r, QueryRecord query) {
        // Service Hierarchy Match
        boolean serviceMatches = query.serviceTypeId() == Parser.ANY ||
                (r.serviceTypeId() == query.serviceTypeId() &&
                        (query.serviceVariationId() == Parser.ANY || r.serviceVariationId() == query.serviceVariationId()));

        if (!serviceMatches) return false;

        // Question Hierarchy Match
        boolean questionMatches = query.questionTypeId() == Parser.ANY ||
                (r.questionTypeId() == query.questionTypeId() &&
                        (query.questionCategoryId() == Parser.ANY || (r.questionCategoryId() == query.questionCategoryId() &&
                                (query.questionSubcategoryId() == Parser.ANY || r.questionSubcategoryId() == query.questionSubcategoryId()))));

        if (!questionMatches) return false;

        // Response Type and Date Range Match
        boolean typeMatches = r.firstAnswer() == query.firstAnswer();
        boolean dateMatches = r.date() >= query.dateFrom() && (query.dateTo() == Parser.ANY || r.date() <= query.dateTo());

        return typeMatches && dateMatches;
    }
}
