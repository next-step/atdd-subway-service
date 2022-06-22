package nextstep.subway.fare.domain;

public class ChildDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int price) {
        return (int) ((price - DiscountType.CHILD.getFare()) * DiscountType.CHILD.getDiscountPercent());
    }
}
