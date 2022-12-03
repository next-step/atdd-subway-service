package nextstep.subway.auth.domain.discount;

public class NoDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int cost) {
        return cost;
    }
}
