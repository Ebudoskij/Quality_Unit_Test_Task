package org.ebudosky;

import java.time.LocalDate;

public class SupportRecord {
    private final byte serviceTypeId;
    private final byte serviceVariationId;
    private final byte questionTypeId;
    private final byte questionCategoryId;
    private final byte questionSubcategoryId;
    private final boolean firstAnswer;
    private final int date;
    private final short waitingMinutes;

    public SupportRecord(byte serviceTypeId,
                         byte serviceVariationId,
                         byte questionTypeId,
                         byte questionCategoryId,
                         byte questionSubcategoryId,
                         boolean firstAnswer,
                         int date,
                         short waitingMinutes) {
        this.serviceTypeId = serviceTypeId;
        this.serviceVariationId = serviceVariationId;
        this.questionTypeId = questionTypeId;
        this.questionCategoryId = questionCategoryId;
        this.questionSubcategoryId = questionSubcategoryId;
        this.firstAnswer = firstAnswer;
        this.date = date;
        this.waitingMinutes = waitingMinutes;
    }


    public byte getServiceTypeId() {
        return serviceTypeId;
    }

    public byte getServiceVariationId() {
        return serviceVariationId;
    }

    public byte getQuestionTypeId() {
        return questionTypeId;
    }

    public byte getQuestionCategoryId() {
        return questionCategoryId;
    }

    public byte getQuestionSubcategoryId() {
        return questionSubcategoryId;
    }

    public boolean isFirstAnswer() {
        return firstAnswer;
    }

    public int getDate() {
        return date;
    }

    public short getWaitingMinutes() {
        return waitingMinutes;
    }
}
