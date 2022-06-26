package nextstep.subway.fare.domain;

public class ChildDiscountPolicy implements DiscountPolicy {

    @Override
    public int discountedFare(int price) {
        return DiscountType.CHILD.calculate(price);
    }
}
