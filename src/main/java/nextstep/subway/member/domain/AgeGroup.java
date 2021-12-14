package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeGroup {
    CHILD(6, 12),
    YOUTH(13, 18),
    DEFAULT(19, 150),
    ;

    private final int minRangeAge;
    private final int maxRangeAge;

    AgeGroup(int minRangeAge, int maxRangeAge) {
        this.minRangeAge = minRangeAge;
        this.maxRangeAge = maxRangeAge;
    }

    public static AgeGroup valueOf(Age age) {
        return Arrays.stream(values())
            .filter(ageGroup -> ageGroup.isMatch(age))
            .findFirst()
            .orElse(DEFAULT);
    }

    public int getMinRangeAge() {
        return minRangeAge;
    }

    public boolean isMatch(int age) {
        return minRangeAge <= age && maxRangeAge >= age;
    }

    public boolean isMatch(Age age) {
        return minRangeAge <= age.getAge() && maxRangeAge >= age.getAge();
    }
}
