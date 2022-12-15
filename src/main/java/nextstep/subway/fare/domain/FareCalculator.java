package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.message.FareMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;

import java.util.List;
import java.util.Objects;

public class FareCalculator {

    public static final int DEFAULT_FARE = 1_250;

    private final DistanceFarePolicy distanceFarePolicy;
    private final AgeDiscountFarePolicy ageDiscountFarePolicy;
    private final Fare lineOverFare;
    private final Distance distance;

    public FareCalculator(LoginMember member, Path path) {
        validateMember(member);
        validatePath(path);

        this.distanceFarePolicy = DistanceFarePolicy.of(path.getDistance());
        this.ageDiscountFarePolicy = AgeDiscountFarePolicy.of(member.getAge());
        this.lineOverFare = maxLineOverFare(path.getSectionEdges());
        this.distance = path.getDistance();
    }

    public Fare calculate() {
        Fare fare = Fare.sum(Fare.of(DEFAULT_FARE), lineOverFare, distanceFarePolicy.apply(distance));
        return ageDiscountFarePolicy.apply(fare);
    }

    private void validateMember(LoginMember member) {
        if(Objects.isNull(member)) {
            throw new IllegalArgumentException(FareMessage.MEMBER_SHOULD_BE_NOT_NULL.message());
        }

        if(Objects.isNull(member.getAge())) {
            throw new IllegalArgumentException(FareMessage.MEMBER_AGE_SHOULD_BE_NOT_NULL.message());
        }
    }

    private void validatePath(Path path) {
        if(Objects.isNull(path)) {
            throw new IllegalArgumentException(FareMessage.PATH_SHOULD_BE_NOT_NULL.message());
        }

        if(!path.hasStations()) {
            throw new IllegalArgumentException(FareMessage.STATIONS_SHOULD_BE_NOT_NULL.message());
        }
    }

    private Fare maxLineOverFare(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .distinct()
                .map(Line::getFare)
                .max(Fare::compareTo)
                .orElse(Fare.zero());
    }
}
