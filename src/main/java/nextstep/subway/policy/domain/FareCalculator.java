package nextstep.subway.policy.domain;

import java.util.List;
import java.util.NoSuchElementException;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.policy.DiscountPolicyFactory;
import nextstep.subway.policy.price.DistancePricePolicy;
import nextstep.subway.policy.price.LinePricePolicy;
import nextstep.subway.policy.price.PricePolicy;

public class FareCalculator {
    private FareCalculator() {
        throw new IllegalAccessError("이 클래스는 유틸 클래스입니다.");
    }

    public static Price calculate(Distance distance, List<Line> lines, int age) {
        Price totalFare = fareCalculate(distance, lines);

        return DiscountPolicyFactory.generate(age).apply(totalFare);
    }

    public static Price calculate(Distance distance, List<Line> lines) {
        return fareCalculate(distance, lines);
    }

    private static Price fareCalculate(Distance distance, List<Line> lines) {
        PricePolicy linePricePolicy = new LinePricePolicy(lines);
        PricePolicy distancePricePolicy = new DistancePricePolicy(distance);

        List<PricePolicy> pricePolicys = List.of(linePricePolicy, distancePricePolicy);

        Price totalFare = pricePolicys.stream()
                                        .map(PricePolicy::apply)
                                        .reduce((seed, result) -> seed.plus(result))
                                        .orElseThrow(() -> new NoSuchElementException("계산되는 운임이 없습니다."));
        return totalFare;
    }
}
