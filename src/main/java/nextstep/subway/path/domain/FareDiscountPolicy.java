package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.domain.Percent;

public final class FareDiscountPolicy {

    private static final Fare CHILD_FLAT_DISCOUNT_FARE = Fare.from(350);
    private static final Percent CHILD_PERCENT_DISCOUNT = Percent.from(50);
    private static final Fare YOUTH_FLAT_DISCOUNT_FARE = Fare.from(350);
    private static final Percent YOUTH_PERCENT_DISCOUNT = Percent.from(20);

    private final LoginMember member;

    private FareDiscountPolicy(LoginMember member) {
        Assert.notNull(member, "로그인 사용자는 필수입니다.");
        this.member = member;
    }

    public static FareDiscountPolicy from(LoginMember member) {
        return new FareDiscountPolicy(member);
    }

    Fare discountFare(Fare fare) {
        if (member.isChild()) {
            return totalDiscountFare(fare, CHILD_FLAT_DISCOUNT_FARE, CHILD_PERCENT_DISCOUNT);
        }
        if (member.isYouth()) {
            return totalDiscountFare(fare, YOUTH_FLAT_DISCOUNT_FARE, YOUTH_PERCENT_DISCOUNT);
        }
        return Fare.zero();
    }

    private Fare totalDiscountFare(Fare fare, Fare flatDiscountFare, Percent percentDiscount) {
        return flatDiscountFare.sum(
            fare.subtract(flatDiscountFare)
                .multiply(percentDiscount));
    }

    @Override
    public String toString() {
        return "FareDiscountPolicy{" +
            "member=" + member +
            '}';
    }
}
