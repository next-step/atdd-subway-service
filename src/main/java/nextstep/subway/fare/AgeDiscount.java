package nextstep.subway.fare;

import java.util.Arrays;

public enum AgeDiscount {

    BABY(1, 5, 0, 1),
    CHILDREN(6, 12, 350, 0.5),
    TEENAGER(13, 18, 350, 0.8),
    NORMAL(19, 200, 0, 1);

    private int startAge;
    private int endAge;
    private int deductionFare;
    private double rate;

    private AgeDiscount(int startAge, int endAge, int deductionFare, double rate) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.deductionFare = deductionFare;
        this.rate = rate;
    }

    public static AgeDiscount create(int age) {
        return Arrays.stream(values())
                .filter(memberGroup -> memberGroup.between(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean between(int age) {
        return this.startAge <= age && age <= this.endAge;
    }

    public int getDeductionFare() {
        return this.deductionFare;
    }

    public double getRate() {
        return this.rate;
    }
}
