package nextstep.subway.fare.domain;

public class GeneralDiscountPolicy implements DiscountPolicy {
    @Override
    public int discountFare(int price) {
        return DiscountType.NONE.calculate(price);
    }
}
