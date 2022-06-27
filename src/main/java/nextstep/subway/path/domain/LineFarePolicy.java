package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class LineFarePolicy {
    public Fare calculate(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .map(SectionEdge::getLineExtraFare)
                .max(Fare::compareTo)
                .orElseThrow(NoSuchElementException::new);
    }
}
