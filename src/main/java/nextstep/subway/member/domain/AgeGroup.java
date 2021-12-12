package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeGroup {
    CHILD(6, 12),
    YOUTH(13, 18),
    ADULT(19, 150),
    ;

    private final int minRangeAge;
    private final int maxRangeAge;

    AgeGroup(int minRangeAge, int maxRangeAge) {
        this.minRangeAge = minRangeAge;
        this.maxRangeAge = maxRangeAge;
    }

    public static AgeGroup valueOf(int age) {
        return Arrays.stream(AgeGroup.values())
            .filter(ageGroup -> ageGroup.isMatch(age))
            .findFirst()
            .orElse(null);
    }

    public boolean isMatch(int age) {
        return minRangeAge <= age && maxRangeAge >= age;
    }

    public boolean isMatch(Age age) {
        return minRangeAge <= age.getAge() && maxRangeAge >= age.getAge();
    }
}
