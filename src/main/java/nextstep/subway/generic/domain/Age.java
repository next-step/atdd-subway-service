package nextstep.subway.generic.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Age {
    private final int value;

    protected Age(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("나이는 0 보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static Age valueOf(Integer age) {
        return new Age(age);
    }

    public enum AgeType {
        CHILDREN(6, 13, 0.5), TEENAGER(13, 19, 0.3), ADULT(19, Integer.MAX_VALUE, 0);

        private final int min;
        private final int max;

        private final double discountRate;

        AgeType(int min, int max, double discountRate) {
            this.min = min;
            this.max = max;
            this.discountRate = discountRate;
        }

        public static AgeType findAgeType(int age) {
            return Arrays.stream(values())
                    .filter(ageType -> ageType.min <= age && age < ageType.max)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("나이 타입을 찾을 수 없습니다."));
        }

        public double getDiscountRate() {
            return discountRate;
        }
    }

    public int getValue() {
        return value;
    }

    public AgeType ageType() {
        return AgeType.findAgeType(value);
    }
}
