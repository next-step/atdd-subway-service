package nextstep.subway.fare.policy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Age;

public enum MemberDiscount {
    CHILDREN(6, 12, 0.5, MemberDiscount::calculateDiscount),
    TEENAGER(13, 18, 0.2, MemberDiscount::calculateDiscount);

    private static final Fare DEDUCTIBLE_FARE = Fare.from(350);

    private final int start;
    private final int end;
    private final BigDecimal discountRate;
    private final BiFunction<Fare, BigDecimal, Integer> calculation;

    MemberDiscount(int start, int end, double discountRate, BiFunction<Fare, BigDecimal, Integer> calculation) {
        this.start = start;
        this.end = end;
        this.discountRate = BigDecimal.valueOf(discountRate);
        this.calculation = calculation;
    }

    private static Optional<MemberDiscount> from(Age age) {
        return Arrays.stream(values())
                .filter(memberDiscount -> age.between(memberDiscount.start, memberDiscount.end))
                .findFirst();
    }

    private static int calculateDiscount(Fare fare, BigDecimal discountRate) {
        BigDecimal deducibleAmount = calculateDeducibleAmount(fare);
        BigDecimal discountAmount = calculateDiscountAmount(deducibleAmount, discountRate);
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
        return findMemberDiscount.map(memberDiscount -> Fare.from(memberDiscount.calculation.apply(fare, memberDiscount.discountRate)))
                .orElse(fare);
    }
}
