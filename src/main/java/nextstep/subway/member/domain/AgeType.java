package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeType {
    INFANT(0, 5),
    CHILD(6, 12),
    YOUTH(13, 18),
    ADULT(19, Integer.MAX_VALUE);

    private final int ageMin;
    private final int ageMax;

    AgeType(int ageMin, int ageMax) {
        this.ageMin = ageMin;
        this.ageMax = ageMax;
    }

    public static AgeType checkAgeType(final int age) {
        return Arrays.stream(AgeType.values())
            .filter(ageType -> ageType.ageMin <= age && age <= ageType.ageMax)
            .findFirst()
            .orElse(ADULT);
    }
}
