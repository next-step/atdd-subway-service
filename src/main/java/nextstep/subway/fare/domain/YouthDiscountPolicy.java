package nextstep.subway.fare.domain;

public class YouthDiscountPolicy implements DiscountPolicy {

    @Override
    public int discountedFare(int price) {
        return DiscountType.YOUTH.calculate(price);
    }
}
