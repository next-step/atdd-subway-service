package nextstep.subway.line.utils;

import nextstep.subway.member.domain.Member;

public class ChildDiscountHelper implements DiscountHelper {

    private static final int MIN_AGE = 6;
    private static final int MAX_AGE = 12;
    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public boolean canSupport(Member member) {
        return member.getAge() >= MIN_AGE && member.getAge() <= MAX_AGE;
    }

    @Override
    public int discount(int price) {
        return (int) (price - price * DISCOUNT_RATE);
    }
}
