package nextstep.subway.fare.domain.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SectionEdge;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OverFarePolicyImpl implements OverFarePolicy {
    private static final int OVER_FARE_MIN_DISTANCE = 10;
    private static final int OVER_FARE_MAX_DISTANCE = 50;

    @Override
    public long calculateOverFare(List<SectionEdge> sectionEdges, Distance distance) {
        long lineOverFare = getLineOverFare(sectionEdges);
        long distanceOverFare = calculateDistanceOverFare(distance.getValue());

        return lineOverFare + distanceOverFare;
    }

    private long getLineOverFare(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .map(Line::getExtraFare)
                .map(ExtraFare::getValue)
                .reduce(Long::max)
                .orElse(0L);
    }

    private long calculateDistanceOverFare(int distance) {
        if (OVER_FARE_MIN_DISTANCE < distance && distance <= OVER_FARE_MAX_DISTANCE) {
            return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        if (OVER_FARE_MAX_DISTANCE < distance) {
            return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }

        return 0;
    }
}
