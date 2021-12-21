package nextstep.subway.fare.domain;

public enum Discount {

    YOUTH(350, 0.8)
    , CHILD(350, 0.5);

    private final int amount;
    private final double percent;

    Discount(int amount, double percent) {
        this.amount = amount;
        this.percent = percent;
    }

    public int getAmount() {
        return amount;
    }

    public double getPercent() {
        return percent;
    }
}
