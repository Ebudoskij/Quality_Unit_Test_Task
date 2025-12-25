package org.ebudosky;

import org.ebudosky.Exception.NotEnoughDataException;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalyticalTool {

    List<SupportRecord> waitingRecords = new ArrayList<>();

    public void addRecord(String line) {
        waitingRecords.add(Parser.parseFromLine(line));
    }

    public void executeQuery(String line) {
        String[] parts = line.split(" ");
        String[] dateParts = parts[4].split("-");
        String[] fromDateParts = dateParts[0].split("\\.");

        byte serviceTypeId = -1;
        byte serviceVariationId = -1;
        if (!parts[1].equals("*")) {
            String[] serviceTypeParts = parts[1].split("\\.");
            serviceTypeId = Byte.parseByte(serviceTypeParts[0]);
            if (serviceTypeParts.length > 1) {
                serviceVariationId = Byte.parseByte(serviceTypeParts[1]);
            }
        }

        byte questionTypeId = -1;
        byte questionCategoryId = -1;
        byte questionSubcategoryId = -1;
        if (!parts[2].equals("*")) {
            String[] questionTypeParts = parts[2].split("\\.");
            questionTypeId = Byte.parseByte(questionTypeParts[0]);
            if (questionTypeParts.length > 1) {
                questionCategoryId = Byte.parseByte(questionTypeParts[1]);
                if (questionTypeParts.length > 2) {
                    questionSubcategoryId = Byte.parseByte(questionTypeParts[2]);
                }
            }
        }

        boolean firstAnswer = parts[3].equals("P");


        int dateFrom = Integer.parseInt(fromDateParts[2])*10000 +
                    Integer.parseInt(fromDateParts[1])*100 +
                    Integer.parseInt(fromDateParts[0]);

        int dateTo = -1;
        if (dateParts.length > 1) {
            String[] toDateParts = dateParts[1].split("\\.");
            dateTo = Integer.parseInt(toDateParts[2])*10000 +
                    Integer.parseInt(toDateParts[1])*100 +
                    Integer.parseInt(toDateParts[0]);
        }

        try {
            double averageWaitingTime = findAverageWaitingTime(serviceTypeId,
                    serviceVariationId,
                    questionTypeId,
                    questionCategoryId,
                    questionSubcategoryId,
                    firstAnswer,
                    dateFrom,
                    dateTo);
            System.out.println(Math.round(averageWaitingTime));
        }
        catch (NotEnoughDataException e) {
            System.out.println("-");
        }
    }

    private double findAverageWaitingTime(byte serviceTypeId,
                                          byte serviceVariationId,
                                          byte questionTypeId,
                                          byte questionCategoryId,
                                          byte questionSubcategoryId,
                                          boolean firstAnswer,
                                          int dateFrom,
                                          int dateTo) {
        return waitingRecords.stream()
                .filter(r -> serviceTypeId == -1 || r.getServiceTypeId() == serviceTypeId)
                .filter(r -> serviceVariationId == -1 || r.getServiceVariationId() == serviceVariationId)
                .filter(r -> questionTypeId == -1 || r.getQuestionTypeId() == questionTypeId)
                .filter(r -> questionCategoryId == -1 || r.getQuestionCategoryId() == questionCategoryId)
                .filter(r -> questionSubcategoryId == -1 || r.getQuestionSubcategoryId() == questionSubcategoryId)
                .filter(r -> r.isFirstAnswer() == firstAnswer)
                .filter(r -> r.getDate() >= dateFrom && (dateTo == -1 || r.getDate() <= dateTo))
                .mapToInt(SupportRecord::getWaitingMinutes)
                .average()
                .orElseThrow(() -> new NotEnoughDataException("There is not any support records for this criteria."));
    }
}
