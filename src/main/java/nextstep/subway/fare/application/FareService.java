package nextstep.subway.fare.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.AgeDiscountFarePolicy;
import nextstep.subway.fare.domain.DistanceFarePolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.message.FareMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FareService {
    public static final int DEFAULT_FARE = 1_250;

    public Fare calculateFare(LoginMember member, Path path) {
        validatePath(path);

        Fare lineOverFare = maxLineOverFare(path.getSectionEdges());
        Fare distanceOverFare = getDistanceOverFare(path.getDistance());
        Fare fare = Fare.sum(Fare.of(DEFAULT_FARE), lineOverFare, distanceOverFare);

        AgeDiscountFarePolicy ageDiscountFarePolicy = AgeDiscountFarePolicy.of(member.getAge());
        return ageDiscountFarePolicy.apply(fare);
    }

    private void validatePath(Path path) {
        if(Objects.isNull(path)) {
            throw new IllegalArgumentException(FareMessage.PATH_SHOULD_BE_NOT_NULL.message());
        }

        if(!path.hasStations()) {
            throw new IllegalArgumentException(FareMessage.STATIONS_SHOULD_BE_NOT_NULL.message());
        }
    }

    private Fare getDistanceOverFare(Distance distance) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.of(distance);
        return distanceFarePolicy.apply(distance);
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
