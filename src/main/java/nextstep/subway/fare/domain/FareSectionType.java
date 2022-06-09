package nextstep.subway.fare.domain;

public enum FareSectionType {
    BASIC(0, 0),
    SECTION_10_TO_50(10, 5),
    SECTION_OVER_50(50, 8);

    private int minDistance;
    private int additionalFareUnit;

    FareSectionType(int minDistance, int additionalFareUnit) {
        this.minDistance = minDistance;
        this.additionalFareUnit = additionalFareUnit;
    }

    public static FareSectionType findTypeByDistance(int distance) {
        if (SECTION_OVER_50.minDistance < distance) {
            return SECTION_OVER_50;
        }
        if (distance > SECTION_10_TO_50.minDistance) {
            return SECTION_10_TO_50;
        }
        return BASIC;
    }
}
