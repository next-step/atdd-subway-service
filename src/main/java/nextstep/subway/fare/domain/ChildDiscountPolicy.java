package nextstep.subway.fare.domain;

public class ChildDiscountPolicy implements DiscountPolicy {

    @Override
    public int discountFare(int price) {
        return DiscountType.CHILD.calculate(price);
    }
}
