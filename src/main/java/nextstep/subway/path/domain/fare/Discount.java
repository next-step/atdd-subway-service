package nextstep.subway.path.domain.fare;

public enum Discount {
    BASIC(0, 0),
    YOUTH(20, 350),
    CHILD(50, 350),
    BABY(100, 0),
    ELDER(100, 0);

    private final int rate;
    private final int deduction;

    Discount(int rate, int deduction) {
        this.rate = rate;
        this.deduction = deduction;
    }

    public int getRate() {
        return rate;
    }

    public int getDeduction() {
        return deduction;
    }
}
