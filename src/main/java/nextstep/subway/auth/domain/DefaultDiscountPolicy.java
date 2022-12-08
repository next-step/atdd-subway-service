package nextstep.subway.auth.domain;

public class DefaultDiscountPolicy extends DiscountPolicy {

    @Override
    public Money getCharge() {
        return defaultCharge;
    }
}
