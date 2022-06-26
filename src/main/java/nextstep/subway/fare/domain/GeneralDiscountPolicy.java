package nextstep.subway.fare.domain;

public class GeneralDiscountPolicy implements DiscountPolicy {
    @Override
    public int discountedFare(int price) {
        return DiscountType.NONE.calculate(price);
    }
}
