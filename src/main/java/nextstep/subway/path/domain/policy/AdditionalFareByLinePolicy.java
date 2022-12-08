package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SectionEdge;

import java.util.List;

public class AdditionalFareByLinePolicy implements AdditionalFarePolicy {
    @Override
    public int addFare(List<SectionEdge> edges) {
        return edges.stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElseThrow(IllegalStateException::new);
    }
}
