package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.SectionEdge;

import java.util.List;

public interface AdditionalFarePolicy {
    int addFare(List<SectionEdge> paths);
}
