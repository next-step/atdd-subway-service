package nextstep.subway.path.domain;

public enum DiscountPolicy {
    TEENAGER(20),
    KID(50);

    private int rate;

    DiscountPolicy(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }
}
