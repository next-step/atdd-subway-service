package nextstep.subway.charge.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AgePolicy {
    private static final List<Integer> YOUTH_AGE_RANGE = IntStream.range(13, 19).boxed().collect(Collectors.toList());
    private static final List<Integer> KID_AGE_RANGE = IntStream.range(6, 13).boxed().collect(Collectors.toList());

    private final AgeType ageType;

    public AgePolicy(Integer age) {
        this.ageType = getAgeType(age);
    }

    private AgeType getAgeType(Integer age) {
        if (YOUTH_AGE_RANGE.contains(age)) {
            return AgeType.YOUTH;
        }

        if (KID_AGE_RANGE.contains(age)) {
            return AgeType.KID;
        }

        return AgeType.NORMAL;
    }

    public void applyPolicy(Charge charge) {
        charge.minus(ageType.getDiscountCharge());
        charge.multiply(ageType.getDiscountRatio());
    }

}
