package nextstep.subway.path.infra;

import nextstep.subway.path.domain.RatePolicy;
import java.util.function.Function;

public class RatePolicyByAge implements RatePolicy {
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private final AgeSpecificRule ageSpecificRule;

    public RatePolicyByAge(final int age) {
        this.ageSpecificRule = AgeSpecificRule.valueOf(age);
    }

    @Override
    public double calc(double fee) {
        return ageSpecificRule.calc(fee);
    }

    private enum AgeSpecificRule {
        CHILDREN(fee -> (fee - DEDUCTIBLE_AMOUNT) - ((fee - DEDUCTIBLE_AMOUNT) / 100) * 50),
        TEENAGER(fee -> (fee - DEDUCTIBLE_AMOUNT) - (((fee - DEDUCTIBLE_AMOUNT) / 100) * 20)),
        ADULT(fee -> fee);

        private final Function<Double, Double> discountsFunction;

        AgeSpecificRule(Function<Double, Double> discountsFunction) {
            this.discountsFunction = discountsFunction;
        }

        private double calc(double fee) {
            return discountsFunction.apply(fee);
        }

        private static AgeSpecificRule valueOf(int age) {
            if (age < 13) {
                return CHILDREN;
            }

            if (age > 12 && age < 20) {
                return TEENAGER;
            }

            return ADULT;
        }
    }
}


