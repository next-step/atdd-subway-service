package nextstep.subway.path.domain.policy.discount;

public class AdultDiscount implements DiscountPolicy {
    public AdultDiscount() {

    }

    @Override
    public double discount(int fare) {
        return fare;
    }
}
