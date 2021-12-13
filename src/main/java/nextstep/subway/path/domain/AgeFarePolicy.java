package nextstep.subway.path.domain;

import java.util.Arrays;
import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.line.domain.Fare;

public enum AgeFarePolicy {
    FREE(0, 5, 0, 1),
    CHILDREN(6, 12, 350, 0.5),
    TEENAGER(13, 18, 350, 0.2),
    GENERAL(19, Integer.MAX_VALUE, 0, 0);

    private int min;
    private int max;
    private int deductible;
    private double discountRate;

    AgeFarePolicy(int min, int max, int deductible, double discountRate) {
        this.min = min;
        this.max = max;
        this.deductible = deductible;
        this.discountRate = discountRate;
    }

    public static AgeFarePolicy findByAge(int age) {
        AgeFarePolicy[] policies = values();
        return Arrays.stream(policies)
            .filter(it -> it.min <= age && it.max >= age)
            .findFirst()
            .orElseThrow(() -> new InvalidArgumentException("사용자의 나이 정보를 확인해주세요!!"));
    }

    public int discountedFare(Integer fare) {
        int deductible = fare - this.deductible;
        return Double.valueOf(deductible * (1-this.discountRate)).intValue();
    }
}
