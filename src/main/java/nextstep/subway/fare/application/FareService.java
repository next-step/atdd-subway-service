package nextstep.subway.fare.application;

import nextstep.subway.fare.domain.DistanceFarePolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.message.FareMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FareService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FareService.class);
    public static final int DEFAULT_FARE = 1_250;

    public Fare calculateFare(Path path) {
        validatePath(path);
        Fare defaultFare = Fare.of(DEFAULT_FARE);
        Fare lineOverFare = maxLineOverFare(path.getSectionEdges());

        Distance distance = path.getDistance();
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.of(distance);
        Fare distanceOverFare = distanceFarePolicy.apply(distance);

        LOGGER.debug("지하철 요금 계산 - 기본료 : {}, 노선 추가 요금 : {}, 거리({}km)별 추가 요금 : {}",
                defaultFare,
                lineOverFare,
                path.getDistance(),
                distanceOverFare);
        return Fare.sum(defaultFare, lineOverFare, distanceOverFare);
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
