package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

import java.util.List;

public class Fare {
    private static final Long DEFAULT_FARE = 1250L;

    private final Long fare;

    private Fare(Long fare) {
        this.fare = fare;
    }

    public static Fare from(Long distance) {
        long overCharge = DistanceChargeRule.calculateFare(distance);

        return new Fare(DEFAULT_FARE + overCharge);
    }

    public static Fare of(Long distance, List<Section> sections) {
        long chargeByDistance = DistanceChargeRule.calculateFare(distance);
        long chargeByLine = getMaxLineSurCharge(sections);

        return new Fare(DEFAULT_FARE + chargeByDistance + chargeByLine);
    }

    private static Long getMaxLineSurCharge(List<Section> sections) {
        return sections.stream()
                .mapToLong(section -> section.getSurCharge())
                .max()
                .orElse(0L);
    }

    public Long getFare() {
        return fare;
    }

    public Fare calculateByAgeFare(int age) {
        long amount = AgeChargeRule.calculateFare(this.fare, age);
        return new Fare(amount);
    }
}
