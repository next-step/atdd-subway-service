package nextstep.subway.path.domain.policy.fare.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import org.springframework.stereotype.Component;

import java.util.Objects;

public class ChildDiscountByAgeStrategy implements DiscountByAgeStrategy {
    public static final int MIN_AGE =6;
    public static final int MAX_AGE = 12;
    public static final int FIXED_DISCOUNT = 350;
    public static final double DISCOUNT_RETE = 0.5;

    @Override
    public int discountBy(LoginMember member, Fare fare) {
        verifyAvailable(member);
        return FIXED_DISCOUNT + (int)((fare.value() - FIXED_DISCOUNT) * DISCOUNT_RETE);
    }

    @Override
    public boolean isAvailable(LoginMember member) {
        return isChild(member);
    }

    private void verifyAvailable(LoginMember member) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException("로그인 사용자만 연령별 할인액을 계산할 수 있습니다.");
        }
    }

    private boolean isChild(LoginMember member) {
        return MIN_AGE <= member.getAge() && member.getAge() <= MAX_AGE;
    }
}
