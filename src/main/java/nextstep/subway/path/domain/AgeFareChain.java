package nextstep.subway.path.domain;

public class AgeFareChain {

    protected static final int MIN_FARE = 0;
    protected static final int ADULT_FARE = 1250;
    protected static final int DISCOUNT_FARE = 350;

    protected static final int MIN_CHILD_AGE = 6;
    protected static final int MIN_YOUTH_AGE = 13;
    protected static final int MIN_ADULT_AGE = 19;

    protected static final double CHILD_DISCOUNT_RATE = 0.5;
    protected static final double YOUTH_DISCOUNT_RATE = 0.8;

    private final AgeFareChain chain;

    public AgeFareChain(AgeFareChain chain) {
        this.chain = chain;
    }

    protected int calculate(int age) {
        return chain.calculate(age);
    }
}
