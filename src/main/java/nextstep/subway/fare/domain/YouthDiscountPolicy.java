package nextstep.subway.fare.domain;

public class YouthDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int price) {
        return (int) ((price - DiscountType.YOUTH.getFare()) * DiscountType.YOUTH.getDiscountPercent());
    }
}
