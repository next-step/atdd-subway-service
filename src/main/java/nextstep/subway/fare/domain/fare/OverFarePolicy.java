package nextstep.subway.fare.domain.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.SectionEdge;

import java.util.List;

public interface OverFarePolicy {
    long calculateOverFare(List<SectionEdge> sectionEdges, Distance distance);
}
