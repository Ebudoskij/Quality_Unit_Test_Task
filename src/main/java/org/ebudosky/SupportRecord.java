package org.ebudosky;

public record SupportRecord(byte serviceTypeId,
                            byte serviceVariationId,
                            byte questionTypeId,
                            byte questionCategoryId,
                            byte questionSubcategoryId,
                            boolean firstAnswer,
                            int date,
                            short waitingMinutes) {
}
