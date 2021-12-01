package nextstep.subway.fare.domain;

public enum FareSectionType {
    BASIC(0, 0),
    SECTION_10_TO_50(10, 5),
    SECTION_EXCEED_50(50, 8);

    private static final int BASIC_MAX_DISTANCE = 10;

    private final int minDistance;
    private final int additionalDistanceUnit;

    FareSectionType(int minDistance, int additionalDistanceUnit) {
        this.minDistance = minDistance;
        this.additionalDistanceUnit = additionalDistanceUnit;
    }

    public static FareSectionType findByDistance(int distance) {
        if (SECTION_10_TO_50.minDistance < distance && distance <= SECTION_EXCEED_50.minDistance) {
            return SECTION_10_TO_50;
        }

        if (SECTION_EXCEED_50.minDistance < distance) {
            return SECTION_EXCEED_50;
        }

        return BASIC;
    }

    public boolean isBasic() {
        return this.equals(BASIC);
    }

    public boolean isSection10to50() {
        return this.equals(SECTION_10_TO_50);
    }

    public int getAdditionalCount() {
        if (isBasic() || isSection10to50()) {
            return 0;
        }

        return (minDistance - BASIC_MAX_DISTANCE) / SECTION_10_TO_50.getAdditionalDistanceUnit();
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getAdditionalDistanceUnit() {
        return additionalDistanceUnit;
    }
}
