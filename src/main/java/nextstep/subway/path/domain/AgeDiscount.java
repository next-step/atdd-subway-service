package nextstep.subway.path.domain;

public enum AgeDiscount {
    YOUTH(13, 18, 350, 0.2),
    CHILD(6, 12, 350, 0.5);

    private int startAge;
    private int endAge;
    private final int deductionFare;
    private final double deductionRatio;

    AgeDiscount(int startAge, int endAge, int deductionFare, double deductionRatio) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.deductionFare = deductionFare;
        this.deductionRatio = deductionRatio;
    }

    public int getStartAge() {
        return startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public int getDeductionFare() {
        return deductionFare;
    }

    public double getDeductionRatio() {
        return deductionRatio;
    }
}
