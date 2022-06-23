package nextstep.subway.member.domain;

public class Age {
    private static final int MINIMUM_AGE = 0;
    private final int value;

    public Age(int value) {
        checkAge(value);

        this.value = value;
    }

    public boolean isGreaterThenOrEqual(Age target) {
        return this.value <= target.value;
    }

    public boolean isLessThen(Age target) {
        return this.value > target.value;
    }

    private void checkAge(int age) {
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
    }
}
