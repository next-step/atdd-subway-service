package nextstep.subway.amount.domain;

public class Amount {
    private final long amount;

    private Amount(long amount) {
        this.amount = amount;
    }

    public static Amount from(long amount) {
        return new Amount(amount);
    }

    public long value() {
        return amount;
    }
}
