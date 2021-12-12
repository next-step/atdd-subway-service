package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

import java.util.List;

public class Fare {
    private final static Long DEFAULT_FARE = 1250L;
    private final Long fare;

    private Fare(Long fare) {
        this.fare = fare;
    }

    public static Fare from(Long distance) {
        long overCharge = OverChargeRule.calculateOverFare(distance);

        return new Fare(DEFAULT_FARE + overCharge);
    }

    public static Fare of(Long distance, List<Section> sections) {
        long chargeByDistance = OverChargeRule.calculateOverFare(distance);
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
}
