package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeGroup {
    CHILD(6, 13),
    TEEN(13, 19),
    ETC(0, 0);
    private final int minAge;
    private final int maxAge;

    AgeGroup(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeGroup findAgeGroupByAge(Age age) {
        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> age.age() >= ageGroup.minAge && age.age() < ageGroup.maxAge)
                .findFirst()
                .orElse(AgeGroup.ETC);
    }
}
