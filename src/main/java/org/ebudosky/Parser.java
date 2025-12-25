package org.ebudosky;

public class Parser {

    public static SupportRecord parseFromLine (String line){
        String[] parts = line.split(" ");
        String[] serviceTypeParts = parts[1].split("\\.");
        String[] questionTypeParts = parts[2].split("\\.");
        String[] dateParts = parts[4].split("\\.");

        byte serviceTypeId = Byte.parseByte(serviceTypeParts[0]);
        byte serviceVariationId = -1;
        if (serviceTypeParts.length > 1){
            serviceVariationId = Byte.parseByte(serviceTypeParts[1]);
        }

        byte questionTypeId = Byte.parseByte(questionTypeParts[0]);
        byte questionCategoryId = -1;
        byte questionSubcategoryId = -1;
        if (questionTypeParts.length > 1){
            questionCategoryId = Byte.parseByte(questionTypeParts[1]);
            if (questionTypeParts.length > 2){
                questionSubcategoryId = Byte.parseByte(questionTypeParts[2]);
            }
        }

        boolean firstAnswer = parts[3].equals("P");

        int date = Integer.parseInt(dateParts[2])*10000 +
                Integer.parseInt(dateParts[1])*100 +
                Integer.parseInt(dateParts[0]);

        short waitingMinutes = Short.parseShort(parts[5]);

        return new SupportRecord(serviceTypeId,
                serviceVariationId,
                questionTypeId,
                questionCategoryId,
                questionSubcategoryId,
                firstAnswer,
                date,
                waitingMinutes);
    }
}
