package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Sections;

public final class FarePolicy {

    private final Sections sections;
    private final FareDistancePolicy distancePolicy;
    private final FareDiscountPolicy discountPolicy;

    private FarePolicy(FareDistancePolicy distancePolicy, FareDiscountPolicy discountPolicy,
        Sections sections) {
        Assert.notNull(distancePolicy, "거리 요금 정책은 필수입니다.");
        Assert.notNull(discountPolicy, "할인 요금 정책은 필수입니다.");
        Assert.isTrue(isNotEmpty(sections), "구간들은 필수입니다.");
        this.distancePolicy = distancePolicy;
        this.discountPolicy = discountPolicy;
        this.sections = sections;
    }

    public static FarePolicy of(
        FareDistancePolicy distancePolicy, FareDiscountPolicy discountPolicy, Sections sections) {
        return new FarePolicy(distancePolicy, discountPolicy, sections);
    }

    public Fare fare() {
        Fare originalFare = originalFare();
        return originalFare.subtract(discountPolicy.discountFare(originalFare));
    }

    private boolean isNotEmpty(Sections sections) {
        return sections != null && sections.isNotEmpty();
    }

    private Fare originalFare() {
        return distancePolicy.fare()
            .sum(sections.maxExtraFare());
    }

    @Override
    public String toString() {
        return "FarePolicy{" +
            "sections=" + sections +
            ", distancePolicy=" + distancePolicy +
            ", discountPolicy=" + discountPolicy +
            '}';
    }
}
