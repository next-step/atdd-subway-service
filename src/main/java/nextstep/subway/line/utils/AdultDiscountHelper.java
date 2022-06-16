package nextstep.subway.line.utils;

import nextstep.subway.member.domain.Member;

public class AdultDiscountHelper implements DiscountHelper {

    private static final int MIN_AGE = 19;
    private static final int MAX_AGE = Integer.MAX_VALUE;
    private static final double DISCOUNT_RATE = 0;

    @Override
    public boolean canSupport(Member member) {
        return member.getAge() >= MIN_AGE && member.getAge() <= MAX_AGE;
    }

    @Override
    public int discount(int price) {
        return (int) (price - price * DISCOUNT_RATE);
    }

}
