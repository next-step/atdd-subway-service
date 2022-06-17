package nextstep.subway.line.utils.Discount;

import nextstep.subway.member.domain.Member;

public class StudentDiscountHelper implements DiscountHelper {

    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 18;
    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public boolean canSupport(Member member) {
        return member.getAge() >= MIN_AGE && member.getAge() <= MAX_AGE;
    }

    @Override
    public int discount(int price) {
        return (int) (price - price * DISCOUNT_RATE);
    }
}
