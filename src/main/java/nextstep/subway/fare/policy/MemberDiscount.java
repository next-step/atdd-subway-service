package nextstep.subway.fare.policy;

import static nextstep.subway.fare.policy.MemberDiscountPolicy.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Age;

enum MemberDiscount {
    CHILDREN(6, 12, MemberDiscount::calculateChildren),
    TEENAGER(13, 18, MemberDiscount::calculateTeenager);

    private final int start;
    private final int end;
    private final Function<Fare, Integer> calculation;

    MemberDiscount(int start, int end, Function<Fare, Integer> calculation) {
        this.start = start;
        this.end = end;
        this.calculation = calculation;
    }

    private static Optional<MemberDiscount> from(Age age) {
        return Arrays.stream(values())
                .filter(memberDiscount -> age.between(memberDiscount.start, memberDiscount.end))
                .findFirst();
    }

    private static int calculateChildren(Fare fare) {
        BigDecimal deducibleAmount = calculateDeducibleAmount(fare);
        BigDecimal discountAmount = calculateDiscountAmount(deducibleAmount, CHILDREN_DISCOUNT_RATE);
        return deducibleAmount.subtract(discountAmount).intValue();
    }

    private static int calculateTeenager(Fare fare) {
        BigDecimal deducibleAmount = calculateDeducibleAmount(fare);
        BigDecimal discountAmount = calculateDiscountAmount(deducibleAmount, TEENAGER_DISCOUNT_RATE);
        return deducibleAmount.subtract(discountAmount).intValue();
    }

    private static BigDecimal calculateDeducibleAmount(Fare fare) {
        return BigDecimal.valueOf(fare.subtract(DEDUCTIBLE_FARE).value());
    }

    private static BigDecimal calculateDiscountAmount(BigDecimal deducibleAmount, BigDecimal childrenDiscountRate) {
        return deducibleAmount.multiply(childrenDiscountRate);
    }

    public static Fare discountFare(Fare fare, Age age) {
        Optional<MemberDiscount> findMemberDiscount = MemberDiscount.from(age);
        return findMemberDiscount.map(memberDiscount -> Fare.from(memberDiscount.calculation.apply(fare)))
                .orElse(fare);
    }
}
