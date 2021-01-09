package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Money;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class AgeDiscount implements MemberDiscount {

    private static final Money BASIC_DEDUCTION = Money.valueOf(350);

    @Override
    public Money discount(final LoginMember loginMember, final Money fee) {
        Age age = Age.findByAge(loginMember.getAge());
        return age.discount(fee);
    }

    private enum Age {
        KIDS(6, 12, fee -> fee.subtract(BASIC_DEDUCTION).multiply(0.5)),
        TEENAGER(13, 18, fee -> fee.subtract(BASIC_DEDUCTION).multiply(0.2)),
        ETC(0, 0, fee -> Money.zero());

        private final int minAge;
        private final int maxAge;
        private final Function<Money, Money> function;

        Age(final int minAge, final int maxAge, final Function<Money, Money> function) {
            this.minAge = minAge;
            this.maxAge = maxAge;
            this.function = function;
        }

        public static Age findByAge(final int age) {
            return Arrays.stream(Age.values())
                    .filter(ageDiscount -> ageDiscount.contains(age))
                    .findFirst()
                    .orElse(ETC);
        }

        private boolean contains(final int age) {
            return age >= minAge && age <= maxAge;
        }

        public Money discount(final Money fee) {
            return function.apply(fee);
        }
    }
}
